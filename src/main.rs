#![allow(static_mut_refs)]

use std::{
    collections::{HashMap, HashSet},
    ffi::OsStr,
    fs,
    io::Write,
    path::{Path, PathBuf},
    sync::{Mutex, OnceLock},
};

use json::JsonValue;

fn main() {
    static IDENTITIES: OnceLock<Mutex<JsonValue>> = OnceLock::new();
    IDENTITIES
        .set(Mutex::new(
            json::parse(fs::read_to_string("identities.json").unwrap().as_str()).unwrap(),
        ))
        .unwrap();
    let root = PathBuf::from(std::env::args().nth(1).expect("no path specified"));

    let mut results = Vec::new();

    fn search_match(path: &Path, results: &mut Vec<String>) {
        let mut dir = fs::read_dir(path).unwrap();

        while let Some(entry) = dir.next() {
            let entry = entry.unwrap();

            if entry.path().is_file() && entry.path().extension() != Some(OsStr::new("java")) {
                continue;
            }

            if entry.path().is_dir() {
                search_match(&entry.path(), results);
            } else {
                let content = fs::read_to_string(entry.path()).unwrap();
                if content.contains("Event<") {
                    fn escape(s: &str) -> String {
                        let mut out = String::new();

                        enum Escape {
                            Normal,
                            OpenSlash,
                            Escaped,
                            InMultiLineComment,
                            InSingleLineComment,
                            MultiLineClosing,
                        }

                        let mut escape = Escape::Normal;

                        for c in s.chars() {
                            match escape {
                                Escape::Normal if c == '\\' => escape = Escape::Escaped,
                                Escape::Normal if c == '/' => escape = Escape::OpenSlash,
                                Escape::Normal => {}
                                Escape::Escaped => escape = Escape::Normal,
                                Escape::OpenSlash if c == '/' => {
                                    out.pop();
                                    escape = Escape::InSingleLineComment;
                                    continue;
                                }
                                Escape::OpenSlash if c == '*' => {
                                    out.pop();
                                    escape = Escape::InMultiLineComment;
                                    continue;
                                }
                                Escape::OpenSlash => escape = Escape::Normal,
                                Escape::InSingleLineComment if c == '\n' => escape = Escape::Normal,
                                Escape::InSingleLineComment => continue,
                                Escape::InMultiLineComment if c == '*' => {
                                    escape = Escape::MultiLineClosing;
                                    continue;
                                }
                                Escape::InMultiLineComment => continue,
                                Escape::MultiLineClosing if c == '/' => {
                                    escape = Escape::Normal;
                                    continue;
                                }
                                Escape::MultiLineClosing => {
                                    escape = Escape::InMultiLineComment;
                                    continue;
                                }
                            }

                            out.push(c);
                        }

                        out
                    }

                    results.push(escape(&content));
                }
            }
        }
    }

    search_match(&root, &mut results);

    static mut IMPORT_INDEX: OnceLock<HashMap<String, String>> = OnceLock::new();
    unsafe { IMPORT_INDEX.set(HashMap::new()) }.unwrap();

    let mut imports: HashSet<String> = HashSet::from_iter(results.iter().flat_map(|file| {
        file.lines()
            .filter(|line| line.starts_with("import "))
            .map(|line| {
                let chunks = line
                    .split_once(" ")
                    .unwrap()
                    .1
                    .split_once(";")
                    .unwrap()
                    .0
                    .split(".")
                    .collect::<Vec<_>>();
                unsafe { IMPORT_INDEX.get_mut() }
                    .unwrap()
                    .insert(chunks.last().unwrap().to_string(), chunks.join("."));
                // format!("{}.*", chunks[..chunks.len() - 1].join("."))
                chunks.join(".")
            })
    }));

    #[derive(Debug)]
    struct FunctionalInterface {
        pub qualifier: String,
        pub result: String,
        pub name: String,
        pub arguments: Vec<(String, String)>,
    }

    impl FunctionalInterface {
        pub fn to_string(&self) -> String {
            let body = format!(
                "runF({})",
                self.arguments
                    .iter()
                    .map(|(t, v)| {
                        if let Some(qualifier) = unsafe { IMPORT_INDEX.get() }.unwrap().get(t) {
                            if qualifier.starts_with("net.minecraft") {
                                format!(
                                    "new {}({v})",
                                    qualifier.replace("net.minecraft", "yarnwrap")
                                )
                            } else {
                                v.to_string()
                            }
                        } else {
                            v.to_string()
                        }
                    })
                    .collect::<Vec<_>>()
                    .join(", ")
            );
            format!(
                "@Override\npublic {} {}({}) {{ {} }}",
                self.result,
                self.name,
                self.arguments
                    .iter()
                    .map(|(t, v)| format!("{t} {v}"))
                    .collect::<Vec<_>>()
                    .join(", "),
                if self.result.as_str() == "void" {
                    format!("{body};")
                } else {
                    let mut identities = IDENTITIES.get().unwrap().lock().unwrap();
                    let identity = if identities[&self.name]
                        .as_str()
                        .is_some_and(|s| !s.is_empty())
                    {
                        identities[&self.name].as_str().unwrap().to_string()
                    } else {
                        let _ = identities.insert(self.name.as_str(), "");
                        println!(
                            "missing identity `{}`, using default, entry added to identities.json",
                            self.name
                        );
                        format!(
                            "return {};",
                            match self.result.as_str() {
                                "int" | "long" | "float" | "double" | "short" | "byte" => "0",
                                "boolean" => "true",
                                _ => "null",
                            }
                        )
                    };

                    format!(
                        r#"Object res = {body}; if (Undefined.isUndefined(res)) {{ {identity} }} else try {{ return ({}) res; }} catch (Exception e) {{ try {{ Object step = ((org.mozilla.javascript.NativeJavaObject) res).unwrap(); return ({}) step.getClass().getField("wrapperContained").get(step); }} catch (Exception _e) {{}} ws.siri.jscore.Core.log("\u00A77[\u00A7cCastError ({})\u00A77] \u00A7c" + e.toString()); {identity} }}"#,
                        self.result, self.result, self.name,
                    )
                },
            )
        }
    }

    // static mut IMPORT_INDEX: OnceLock<HashMap<String, String>> = OnceLock::new();
    // unsafe { IMPORT_INDEX.set(HashMap::new()) }.unwrap();

    let (mut functional_interfaces, mut event_classes): (Vec<_>, Vec<_>) = results
        .iter()
        .filter_map(|file| {
            let package = file
                .trim()
                .lines()
                .next()
                .unwrap()
                .split_once(" ")
                .unwrap()
                .1
                .split_once(";")
                .unwrap()
                .0;
            let class =
                if let Some(classline) = file.lines().find(|line| line.starts_with("public ")) {
                    classline
                        .trim()
                        .split(" ")
                        .find(|s| s.chars().next().unwrap().is_uppercase())
                        .unwrap()
                        .to_string()
                } else {
                    return None;
                };

            if class.contains("<") {
                return None;
            }

            imports.insert(format!("{package}.{}.*", class.split("<").next().unwrap()));
            imports.insert(format!("{package}.*"));

            Some(
                file.split("@FunctionalInterface")
                    .skip(1)
                    .filter_map(|src| {
                        let interface_name = src
                            .split_once("interface ")
                            .unwrap()
                            .1
                            .split_once(" ")
                            .unwrap()
                            .0;

                        if interface_name.contains("<") {
                            return None;
                        }
                        let result = src
                            .split_once('{')
                            .unwrap()
                            .1
                            .trim()
                            .split_once(' ')
                            .unwrap()
                            .0
                            .split("\n")
                            .last()
                            .unwrap()
                            .to_string();
                        if result.as_str() == "static" {
                            return None;
                        }
                        let function_name = src
                            .split_once('{')
                            .unwrap()
                            .1
                            .trim()
                            .split_once(' ')
                            .unwrap()
                            .1
                            .split_once('(')
                            .unwrap()
                            .0
                            .to_string();
                        let mut argument_failed = false;
                        let arguments = src
                            .split_once('{')
                            .unwrap()
                            .1
                            .trim()
                            .split_once(' ')
                            .unwrap()
                            .1
                            .split_once('(')
                            .unwrap()
                            .1
                            .split_once(')')
                            .unwrap()
                            .0
                            .split(',')
                            .filter(|s| !s.is_empty())
                            .map(|pair| {
                                if pair.contains("@") {
                                    let mut skipped = pair.trim().split(" ").skip(1);
                                    (skipped.next().unwrap(), skipped.next().unwrap())
                                } else if let Some(pair) = pair.trim().split_once(' ') {
                                    pair
                                } else {
                                    argument_failed = true;
                                    ("", "")
                                }
                            })
                            .map(|(a, b)| (a.to_string(), b.to_string()))
                            .collect();

                        if argument_failed {
                            return None;
                        }

                        Some((
                            FunctionalInterface {
                                qualifier: format!("{package}.{class}.{interface_name}"),
                                result,
                                name: function_name,
                                arguments,
                            },
                            (format!("Packages.{package}.{class}"), class.clone()),
                        ))
                    })
                    .collect::<Vec<_>>(),
            )
        })
        .flatten()
        .unzip();

    // dbg!(&event_classes);

    event_classes = HashSet::<(String, String)>::from_iter(event_classes.into_iter())
        .into_iter()
        .collect::<Vec<_>>();
    event_classes.sort();

    functional_interfaces.sort_by_key(|item| item.qualifier.clone());

    let mut import_sorted = imports
        .iter()
        .map(|im| format!("import {im};"))
        .collect::<Vec<_>>();
    import_sorted.sort();

    let runnable = format!(
        r#"package yarnwrap;

import org.mozilla.javascript.Undefined;

{}

public class RunnableGenerated extends ws.siri.jscore.wraps.IRunnable implements
{} {{

public RunnableGenerated(String ident, String function) {{
    super(ident, function);
}}

{}
}}"#,
        import_sorted.join("\n"),
        functional_interfaces
            .iter()
            .map(|inter| inter.qualifier.clone())
            .collect::<Vec<_>>()
            .join(",\n"),
        functional_interfaces
            .iter()
            .map(FunctionalInterface::to_string)
            .collect::<Vec<_>>()
            .join("\n\n")
    );

    fs::OpenOptions::new()
        .write(true)
        .create(true)
        .truncate(true)
        .open("RunnableGenerated.java")
        .unwrap()
        .write_all(runnable.as_bytes())
        .unwrap();

    fs::OpenOptions::new()
        .write(true)
        .create(true)
        .truncate(true)
        .open("events.js")
        .unwrap()
        .write_all(
            event_classes
                .into_iter()
                .map(|(package, class)| {
                    format!(
                        "let {} = {};",
                        class.split("<").next().unwrap(),
                        package.split("<").next().unwrap()
                    )
                })
                .collect::<Vec<_>>()
                .join("\n")
                .as_bytes(),
        )
        .unwrap();

    let file = &mut fs::OpenOptions::new()
        .write(true)
        .truncate(true)
        .open("identities.json")
        .unwrap();

    let s = json::stringify_pretty(IDENTITIES.get().unwrap().lock().unwrap().clone(), 4);
    file.write_all(s.as_bytes()).unwrap();

    // dbg!(functional_interfaces);
    // println!("{}", imports.iter().cloned().collect::<Vec<_>>().join("\n"))
}
