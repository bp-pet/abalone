# Abalone
Welcome to the abalone game of Project Group Other3.4. A report about this project can be found in [Software_Systems_Programming_Project.pdf](Software_Systems_Programming_Project.pdf).

## Run local game
To start a local abalone game one can run the class [Abalone.java](src/abalone/Abalone.java). This class call the main method that will process arguments or userinput to create players in a local Abalone game. One can run the program in Eclipse with arguments `<player name>`. The number of arguments will create that number of HumanPlayers except if the player name contains one of the available stratagy names, then the local game will create a computer player with that strategy. Available strategies are `RandomStrategy` and `ItsOverAnakinIHaveTheHighGroundStrategy` (the smart strategy). The HumanPlayer also has a hint functionality where moves are found from the `ItsOverAnakinIHaveTheHighGroundStrategy`.

## Run server
A abalone server is implemented which communicates with abalone clients according to [Protocol of group Other3](https://git.snt.utwente.nl/s1959190/protocol-other3v1/blob/master/protocol_other3VCurrent.md) (supported version `other3V1.3`). To start a server where clients are able to connect to one must run the class [AbaloneServer.java](src/abalone/server/AbaloneServer.java). Once the port is filled in a server is started.

## Run client
A abalone client is implemented which communicates with a abalone server according to [Protocol of group Other3](https://git.snt.utwente.nl/s1959190/protocol-other3v1/blob/master/protocol_other3VCurrent.md) (supported version `other3V1.3`). To play a game online one can start a client who can connect to a server. To do this run the class [AbaloneClient.java](src/abalone/client/AbaloneClient.java) a ip and port have to be given. Once connected to a server one can request the help command `h` for more information.