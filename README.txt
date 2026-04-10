simple server utils

current features:
- /modlist (/ml) command - lists mods on the server
- 2 gamerules - "ssu:timesync_enabled"* and "ssu:timesync_offset"**
- /thor <target> - strikes the selected entity by thor
- text formatting - using &<colorcode> you can format text in color
- more to come



*timesync_enabled - if true, the server time will be synced to real UTC time
**timesync_offset - time offset - set this to your timezone of choice in hours (e.g. +2 for CEST, -5 for EST and so on)

Note: timesync_offset does NOT adjust for daylight savings time!