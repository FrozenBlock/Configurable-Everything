Hello
Put changelog in plain text please
Make sure to clear after release

Put changelog here:

-----------------
- Updated FrozenLib compatibility to 2.2.4+
  - Includes updates to config parsing for Configurable Everything
- Updated minimum Fabric Language Kotlin version to 1.13.8
- Updated minimum FKE version to 1.1.9
  - 2.0 on 26.1-snapshot-1
- Added a wrapper for adding blocks via scripts
  - The wiki has been updated to show an example
- Added wrappers to allow all types of blocks to be created via scripts
- Fixed rare crash with entity attribute amplifiers
- Fixed an issue where the default config causes a "No alpha allowed" error in the config GUI
- 1.21.x: Removed json5 datapack support
  - 26.1: Fixed json5 datapack support
  - 26.1: Added support for djs, jsonc, hjson, txt, and ubjson files
  - 26.1: Renamed config option from json5Support to moreJsonSuppport
- 26.1: Removed script remapping
  - The unobfuscation update makes script remapping obsolete
