# BomberGame
Intro: 
------------
A project for a small network game, containing a server and a client along with nessasery game files.

Server and Client: 
------------
The game is run from the 2 files from the folder: `src/game/`

`Server.java`

`Client.java`

The Server needs to be run first, it will control the map and informations about the game, like the score and player placement. When it is run, it will first let your choose the map/level you wish to play on, then it will give you the IP address and the lokal address that is used by the client to find the correct server.

The client can then be run multiple times, or from multiple computers. You will need to type in either the IP address or choose use localhost. Then choose your Player name and the player will enter the game.

Score: 
------------
* Moving gives 1 point 
* Shooting cost 10 points 
* Killing a player steals 33% of their points 
