# Fabric Event Map Generator

This tool generates event classes for [**yarnwrap/Runnable.java**](https://github.com/FabricCore/yarnwrap/blob/1.21.1/src/main/java/yarnwrap/Runnable.java) and **yarnwrap.js** (TODO)

## Usage

1. Have a local copy of [**Fabric**](https://github.com/FabricMC/fabric) (of the correct branch).
2. Install using cargo.
```sh
cargo install --git https://github.com/FabricCore/fabric_event_mappers
```
3. Run the command.
```sh
fabric_event_mappers /path/to/fabric
```

### Identities

When updating to a newer version, you may need to update `identities.json`.

An identity for a handler is the default behaviour of the handler, such as `return true;`.

- Missing identities will be added to the JSON file.
- Only functions that returns non-void value requires an identity.
