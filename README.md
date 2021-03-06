# Dark Souls III - Save Backup Tool

Automated save backup tool for Dark Souls III utilizing rolling backups.  
Based on [EntityZero backup tool](http://www.nexusmods.com/darksouls2/mods/26) with some changes and customizations.

## Configuration
This tool requires the configuration file *config.properties* located in the same directory as the executable, containing the following information:
```ini
#_____Dark Souls III Backup Configuration_____

# Location of save file that will be backed up
# C\:\\Users\\Alex\\AppData\\Roaming\\DarkSoulsIII\\01100001040592c2\\DS30000.sl2
save_file=C:\\Users\\<USER_NAME>\\AppData\\Roaming\\DarkSoulsIII\\<STEAM_ID>\\DS30000.sl2

# Location of the directory that will contain the backed up saves
# C\:\\Users\\Alex\\AppData\\Roaming\\DarkSoulsIII\\BACKUPS
backup_path=C:\\Users\\<USER_NAME>\\AppData\\Roaming\\DarkSoulsIII\\BACKUPS

# Prefix and extension of backups created
# If backup_prefix=AAA and backup_extension=zzz then the backup will be AAA_001.zzz
backup_prefix=DS30000
backup_extension=.sl2

# Maximum number of backups to create before rolling over
max_saves=10

# Time between backups in minuets
delay=10

# DO NOT CHANGE
# Tells the program the last backup that was created
# If last_backup=6 then AAA_006.zzz was the most recent backup
last_backup=1
```

## Compiling
```sh
cd src
javac save/RollingBackup.java
jar cvfe DarkSoulsIII.jar RollingBackup save/*.class
```

## Running
```sh
java -cp DarkSoulsIII.jar save.RollingBackup
```
