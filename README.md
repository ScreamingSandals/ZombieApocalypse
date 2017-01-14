# Zombie Apocalypse
Simple bukkit/spigot minigame with zombies.

####Actual stable version: 1.0.10

It's for minecraft version 1.8 - 1.11.2

ZA is a minigame for bukkit and spigot from version 1.8. To run this minigame need separate world, where is nothing, only ZA. ZA have only 1 arena. The game is divided into the phases after 5 minutes. Between waves is always a minute of the day.



## Tutorial

For editing game you need a permission: misat11.za.admin
### Create game
> 1) Create and build a world that can spawn monsters. 
>
> 2) Go to spawnpoint of players and run command: /za setspawnloc 
>
> 3) Run a command /za enablegame to enable game. 

### Zombie spawns set
Think twice before setting where you zombies spawn. It must be square or rectangular area. These areas may overlap. Each such area must have your name or number. 
> 1) Stand to one place where you want to spawn zombies and run this command: /za arena pos1 (number or name of your area)
>
> 2) Stand to two place where you want to spawn zombies and run this command: /za arena pos2 (number or name of your area)
>
> 3) Now you need to set the time between spawnutím zombies: /za arena countdown (number or name of your area) (time in seconds)

### Set giant game
Giant game is a type of Zombie Apocalypse with giant. 5 phase lasts 30 minutes. In the last phase spawn Giant, which should be killed.
> 1) Stand to place, where you want giant spawn and run command: /za setgiantloc
>
> 2) Run a command /za enablegiantgame to enable giant.



## Features
> - Multi phases and giant game
>
> - Teleporting players after end and start of phases
>
> - Set of zombie spawns
>
> - Points for killing - /za points
>
> - Daily gift - /za gift

### Planned features
> - Shop
>
> - Vault hook
