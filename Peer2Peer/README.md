## SER321-Module4 Activity 2
1. This program demonstrates a simple peer to peer question and answer game. Each player acts as both a server and a client. The program begins by allowing free text chat amongst players.  During this time, players should agree on who the first host will be.  When all players indicate they are ready and the host is selected, the host, on command, sends a question to the other players. The player who first answers correctly will be awarded one point and become the host asking the next question. This continues until a player accumulates 3 points winning the game.  The players may choose to play another game with the winner becoming initial host of that game.

2. There is one gradle task each player will call to connect to the game.It can be called with the command:
		```
		gradle runPeer -Pname=<user name> -Pport=<port>
		```

3. The games begins asking which other players the player should be connected to.  The player may connect to other players by entering a 'space' separated list of ':' separated host/port pairs.  The following is a template demonstrating connection to another player:
```
<ip address of player>:<port number of player> -q --console=plain
```
The following is example of one player connecting to two other local players:
```
localhost:<port of first player> localhost:<port of second player>
```
	a. This begins free text amongst players. When the first host is agreed upon, each player may enter "start" to begin or "exit" at anytime to quit the game.
	b. The host will enter '1' to indicate host status.  All other players enter '0' to indicate "Pawn" status.
	c. When ready, the host may enter "yes" to send the first question to the other players.
	d. Any Pawn may answer.  The first to answer correctly becomes the next host and may send the next question.
	e. The new host will enter 'yes' to start the new round.
	f. Every other player mush press enter to begin the next round.
	g. At the end of each game, players will be given the option to start a new game or quit.

4. This program utilizes TCP and a custom JSON protocol.
	a. General chat is sent amongst peer using a protocol of form:
		{'username': '<username>','message': '<message>'}
		If a message of this form contains the value "exit" for the key "message", they will exit the game.
		In certain contexts, a message of "start" will begin a game, and "yes" will send a question.
		A message of "I WON I WON I WON I WON" will end the game with sender of this message being the winner.
	b. As the host, asking a question uses a protocol of form:
		{'username': '<username>','question': '<question>','answer':'<answer>'}
		Each client receives both the question and answer against which there guesses are tested.
	c. As a pawn, answering a question uses a protocol of form:
		{'username': '<username>','guess': '<guess>'}
		If <guess> matches <answer>, the pawn is correct and becomes the next host.