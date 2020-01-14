# Protocol group other3
This is version `other3V1` of the protocol for the programming project of module 05 of TCS.
Any remarks or questions can be asked to Daan Pluister on the Software Systems slack.

## Protocol format
- Every command or response is indicated in a `code block`. 
- `<argument>` required argument.
- `[argument]` optional input or argument.
- `[argument]*` optional input or argument 0 or multiple times.
- `<option1|option2>` required argument could be option1 or option2.

## General message format
- Delimiter is a semi colon. You do not enter a semi colon after the last argument or command.
- There should not be spaces right before or after a semi colon. These will be treated as part of the argument or command.
- Every command is a single letter. There is always at least a command, and zeroor more arguments may be added.

### Example
`<command>;<argument>;<argument>;[optional argument]`

## Client -> Server

### Connect
The client says hello to the server, sends what protocol they will use, and sendwhich extensions they have enabled (if any).

#### Syntax
`H;<protocol version>[;extension name]*`

#### Examples
`h;other3V1`
`h;other3V1;chat`
`h;other3V1;chat;security`
`h;other3V1;security;chat`

### Lobby request
When a client sends a lobby request it is expected that the server returns with lobbies.
#### Syntax
`l`

### Join lobby
The client can join an existing or new lobby with the join command and indicate theirname and team name.

#### Syntax
`j;<lobby name>;<player name>;<team name>`
#### Examples
`j;4w3s0m3sw4g lobby;John;team Cool`
`j;NewLobby;Alice;myTeam`

### Start game
When a client sends a game request it is expected that the server will send a game request back to all clients.

#### Syntax
`s`

### Game move
If the turn indicates your color, you indicate your move. The client indicates which marble is initiating the move (marble 1), which marble is the last in the row of marbles they want to move (marble 2), and the destination of marble 1. If only one marble is moved, the first and second argument should be the same. For the coordinate system we use the Wikipedia notation: https://en.wikipedia.org/wiki/Abalone_(board_game)#Move_notation (Capital letter A-I followed by a number 1-9)
#### Syntax
m;<coordinates of marble 1>;<coordinates of marble 2>;<destination of marble 1>
#### Examples
m;A3;A5;B3

### Disconnect from lobby/game (intentional)
If the client wants to disconnect from the lobby or while a game is not running, they send an exit command. After sending an exit the client stays connected to the server but has to join a new lobby to play a game.

#### Syntax
`x`

### Close connection (intentional)
A client can only close the connection when he is not in a lobby or game. The client sends an exit command.
#### Syntax
`x`

## Server -> Client
### Confirm connect
After receiving a hello request the server responds with hello and then lists all available lobbies and the amount of people in that lobby.
#### Syntax
`h`
for each lobby: `l;<lobby name>;<nr of players in lobby>`
`---EOT---`
#### Example
`h`
`l;Jelles Lobby;1`
`l;4w3s0m3sw4g lobby;3`
`---EOT---` OR (if no lobbies available)

`h`
`---EOT---`

### Provide lobbies
After receiving a lobby request the server lists all available lobbies and the amount of people in that lobby.

*(suggestion)* lobby request will also send the players in the lobby.
#### Syntax
for each lobby: `l;<lobby name>;<nr of players in lobby>`
`---EOT---`

*(suggestion)* for each lobby: `l;<lobby name>;p;<player name>;<team name>[;p;<player name>;<team name>]*`
`---EOT---`
#### Example
`l;Jelles Lobby;1`
`l;4w3s0m3sw4g lobby;4`
`---EOT---` OR (if no lobbies available)

`---EOT---`

*(suggestion)* `l;Jelles Lobby;p;John;team Cool`
`l;4w3s0m3sw4g lobby;p:Alice;team Cool;black;p;Bob;team Hot;blue;p;Chris;team Cool;white;p;Daan;team Hot;red`
`---EOT---`

### Join game
After receiving a join request the server can respond in a number of ways. 

#### successfull join
If the join request or lobby create request is successful, it will confirm the join by listing each player in the lobby with their name, team name and assigned color (including the newly connected client). The server will send the same list to each client in the lobby.
The color can be 
##### Syntax
(for each player in the lobby): `j;<player name>;<team name>;<black|white|blue|red>`
`---EOT---`

##### Example
`j;John;team Cool;black`
`j;John;team Hot;white`
`---EOT---` OR

`j;Alice;team Cool;black`
`j;Bob;team Hot;white`
`j;Chris;team neither Cool or Hot;blue`
`---EOT---` OR

`j;Alice;team Cool;black`
`j;Bob;team Hot;blue`
`j;Chris;team Cool;white`
`j;Daan;team Hot;red`
`---EOT---`

#### unsuccessfull join 
If the server found the joined lobby is full, there is another player in the lobby with the same player name AND team name, or the client tried to add a third team name to a four player game, the server will send an exception type 3 and tell the client what is wrong. It will then list all lobbies again. Available messages are (`player with that name exists`, `lobby full` and `there are already two teams in four player lobby`)
##### Syntax
`e;3;<Message>`

##### Examples
`e;3;player with that name exists` OR
`e;3;lobby full` OR
`e;3;there are already two teams in four player lobby`

### Game start
After a game start request the server sends a conformation to all clients in the lobby or sends a error back to the client requesting the start.

#### accepted Game start
After a game start request the game is started with the amount of people currently in the lobby
#### Syntax
`s`

#### unaccepted Game start 
If there is only one client in the lobby, the server will send an exception type 3 (The message can be: `only one player in lobby`).

##### Syntax
`e;3;<Message>`
##### Example
`e;3;only one player in lobby`

### Turn
Every turn, the server indicates who can make a move.
#### Syntax
`t; <black|white|blue|red>`
#### Example
`t; black`

### Move
After a move request the server checks the validity of the move.

#### Accepted move
If the move is accepted, the server confirms the move by forwarding the move to all clients. The new turn can be send after this.
##### Syntax
`m;<coordinates of marble 1>;<coordinates of marble 2>;<destinationof marble 1>`

#### Unaccepted move
If the move is deemed illegal by the server, the server responds only to the client whose turn it is. The client can then try another move.
##### Syntax
`u`

### Game end
When a game ends the server sends the server sends the result of the game to all clients. (Possible results can be: `game won`, `draw`, `disconnection`). If the game is won, the server also sends the colors of the one or two winner(s). If the game has ended in a draw, nothing happens. If the game has ended with a disconnection, the server also sends the color of the disconnected client.

#### Syntax
`g;<result>[;color[;color]]`

#### Examples
`g;game won; black; white` OR
`g;game won; black` OR
`g;draw` OR
`g;disconnection; black`

## Commands
### Commands that can be send by the client
| char | message | description                                                |
|------|---------|------------------------------------------------------------|
| h    | HELLO   | Indicate a new connection to the server                    |
| l    | LOBBY   | Request all lobbies                                        |
| j    | JOIN    | Indicate a new connection to a lobby or create a new lobby |
| s    | START   | Start the game in the current lobby                        |
| m    | MOVE    | Make a move                                                |
| x    | EXIT    | Exit the current lobby or game or close connection         |

### List of server responses
| char | message | description                                                |
|------|---------|------------------------------------------------------------|
| h    | HELLO   | Recognise connection to the server                         |
| l    | LOBBY   | List all lobbies                                           |
| j    | JOIN    | Indicate a client has joined a lobby or created a new lobby|
| s    | START   | Indicate the game has started                              |
| t    | TURN    | Undicate whose turn it is                                  |
| m    | MOVE    | Approve a move and distribute it to other clients          |
| g    | GAME_OVER   | Indicate the current game is finished                  |

### Improper messages
There are a number of improper messages a server can receive. We have divided them into 4 categories
| char        | message             | description                                                                                            |
|-------------|---------------------|--------------------------------------------------------------------------------------------------------|
| e;1         | Unexpected command  | The command letter is not expected in this context. A list of possible commands is sent by the server. |
| e;2         | Unexpected argument | One or more arguments is invalid. The format of the expected arguments with this command is sent by the server. |
| e;3         | Illegal argument    | Performing this action is expected but not allowed. The server will indicate what is wrong. Examples: not your turn, name already exists, lobby is full, etc. |
| u           | Unaccepted move     | Special case of e;3, the performed move is not allowed by the server. The client can try again.   |

## Extensions
Extensions are not decided upon yet and will be discussed on the next meeting on Friday the 17th.
