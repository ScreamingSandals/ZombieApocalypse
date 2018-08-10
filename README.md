# Zombie Apocalypse
Simple Spigot minigame with zombies.

#### More info is on our spigotmc.org site: https://www.spigotmc.org/resources/zombieapocalypse.34779/

#### Actual stable version: 2.0.0

#### Recommended Minecraft version: 1.13

ZA is a minigame for Spigot. This minigame can run on more worlds with more arenas and can run on normal world, where players playing for example survival.

## Tutorial

For editing game you need a permission: misat11.za.admin
### Create game
> 1) Build some arena!
>
> 2) Create new arena with /za admin <new arena name> add
>
> 3) Set first position of arena /za admin <new arena name> pos1
>
> 4) Set second position of arena /za admin <new arena name> pos2
>
> 5) Set position of player spawn /za admin <new arena name> spawn
>
> 6) Set pause countdown /za admin <new arena name> pausecountdown <countdown (in seconds)>
>
> 7) After all settings (see below) will be setted run: /za admin <new arena name> save

### Adding phases
Before running you must set phases!
> 1) Create phase with: /za admin <new arena name> phase add <phase countdown (in seconds)>
>
> 2) Previous command will get you a phase number, for example 0 or 1 or 2
>
> 3) Now you must add monsters /za admin <new arena name> monster add <phase number> <entity type: see below> <how often will be spawn (in seconds)>

### Set giant game
Giant game is a type of Zombie Apocalypse with giant. Giant game will start after all phases will be done!
> 1) Stand to place, where you want giant spawn and run command: /za admin <new arena name> bossgame set
>
> 2) To disable giant game run: /za admin <new arena name> bossgame reset

### Small Arenas
Small arenas are areas with custom position and custom monsters spawning!
> 1) Build some arena!
>
> 2) Create new arena with /za admin <arena name> small add <small arena>
>
> 3) Set first position of arena /za admin <arena name> small pos1 <small arena>
>
> 4) Set second position of arena /za admin <arena name> small pos2 <small arena>
>
> 5) Now you can customize monsters spawning with: /za admin <new arena name> small monsteradd <small arena> <phase number> <entity type: see below> <how often will be spawn (in seconds)>

### Villager Stores
> 1) To add trading villager to your position run: /za admin <arena name> store add
>
> 2) To remove trading villager on your position run: /za admin <arena name> store remove


## Features
> - Multi phases and giant game
>
> - Teleporting players after end and start of phases
>
> - Small arenas
>
> - Coins /za coins
>
> - Shop
>
> - Vault (gets you 1$ for kill)
>
> - Sound effects
>
> - Title on start
>
> - Customizing monster spawns
>
> - Enable farming in arena
>
> - Signs for join and leave