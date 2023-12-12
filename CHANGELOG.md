Hello
Put changelog in plain text please
Make sure to clear after release

Future changelog items:
- Added Gravity Config
    - Contains gravity belts
- Added Item Config
    - Contains item reach overrides
- Added Structure Config
    - Contains structure and structure set removals
- Partially locked config GUIs when configs are synced from the server
    - Config syncing introduced in FrozenLib 1.5
- Added Kotlin script remapping
    - Enables accessing obfuscated Minecraft classes
        - Also (optionally) includes mod classes!
- Added config options to configure script remapping
- Added config interactions for Kotlin Scripts
    - Check the wiki for more information
- Added external dependency support for Kotlin Scripts
    - Check the wiki for more information

Put changelog here:

-----------------
- Fixed critical mod compatibility issue
- Updated embedded FrozenLib to 1.5.1

1.0.6 Changelog:
- Updated Kotlin to 1.9.21
  - Scripts will be compiled against 1.9.21
- Updated minimum FLK version to 1.10.16
- Added Mod Protocol Config
  - Enabled by default
  - Main toggle found within the config rather than Main Config
  - No GUI at the moment
- Added requirements to lock config GUIs if their main toggle is disabled
- Added a GUI for Fluid Config
- Added missing `registry` option to Main Config GUI
- Fixed some options' functionality
- Fixed compatibility with Bedrockify
- Implemented slight optimizations
- Updated embedded FrozenLib to 1.5
