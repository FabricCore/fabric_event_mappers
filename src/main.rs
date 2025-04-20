#![allow(static_mut_refs)]

use std::{
    collections::{HashMap, HashSet},
    ffi::OsStr,
    fs,
    path::{Path, PathBuf},
    sync::OnceLock,
};

fn main() {
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
                if content.contains("public static final Event<") {
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
            format!(
                "@Override\npublic {} {}({}) {{ runF({}); {} }}",
                self.result,
                self.name,
                self.arguments
                    .iter()
                    .map(|(t, v)| format!("{t} {v}"))
                    .collect::<Vec<_>>()
                    .join(", "),
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
                    .join(", "),
                if self.result.as_str() == "void" {
                    String::new()
                } else {
                    format!(
                        "return {}; ",
                        match self.result.as_str() {
                            "int" | "long" | "float" | "double" | "short" | "byte" => "1",
                            "boolean" => "true",
                            _ => "null",
                        }
                    )
                },
            )
        }
    }

    let functional_interfaces = results
        .iter()
        .flat_map(|file| {
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
            let class = file
                .lines()
                .find(|line| line.contains(" class "))
                .unwrap()
                .split_once(" class ")
                .unwrap()
                .1
                .chars()
                .filter(char::is_ascii_alphanumeric)
                .collect::<String>();
            imports.insert(format!("{package}.{class}.*"));
            imports.insert(format!("{package}.*"));

            file.split("@FunctionalInterface")
                .skip(1)
                .map(|src| {
                    let interface_name = src
                        .split_once("public interface ")
                        .unwrap()
                        .1
                        .split_once(" ")
                        .unwrap()
                        .0;
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
                        .map(|pair| {
                            if pair.contains("@") {
                                let mut skipped = pair.trim().split(" ").skip(1);
                                (skipped.next().unwrap(), skipped.next().unwrap())
                            } else {
                                pair.trim().split_once(' ').unwrap()
                            }
                        })
                        .map(|(a, b)| (a.to_string(), b.to_string()))
                        .collect();

                    FunctionalInterface {
                        qualifier: format!("{package}.{class}.{interface_name}"),
                        result,
                        name: function_name,
                        arguments,
                    }
                })
                .collect::<Vec<_>>()
        })
        .collect::<Vec<_>>();

    println!(
        r#"package yarnwrap;

import java.util.HashMap;

{}

public class Core extends ws.siri.jscore.wraps.Runnable implements
{} {{

public static HashMap<String, Core> runnables = new HashMap<>();


public Core(String ident, String function) {{
    super(ident, function);
    f = ws.siri.jscore.Core.rhino.compileFunction(ws.siri.jscore.Core.rhinoScope, function, ident, 1, null);
    runnables.put(ident, this);
}}

public static Core runnable(String ident, String function) {{
    return Core.create(ident, function);
}}

public static Core create(String ident, String function) {{
    if (runnables.containsKey(ident)) {{
        org.mozilla.javascript.Function f = ws.siri.jscore.Core.rhino.compileFunction(ws.siri.jscore.Core.rhinoScope, function, ident, 1, null);
        runnables.get(ident).f = f;
        return runnables.get(ident);
    }} else {{
        return new Core(ident, function);
    }}
}}

public static Core getRunnable(String ident) {{
    return Core.runnables.get(ident);
}}

{}
}}"#,
        imports
            .iter()
            .map(|im| format!("import {im};"))
            .collect::<Vec<_>>()
            .join("\n"),
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
    )

    // dbg!(functional_interfaces);
    // println!("{}", imports.iter().cloned().collect::<Vec<_>>().join("\n"))
}
