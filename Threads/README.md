## SER321-Module4 Activity 1
1. This program uses either single threaded server or multi-threaded server to perform string operations as requested by a client.  The client may request the following operations:
	a. add a string to a list
	b. remove a string from a list by its list index
	c. display the list of strings
	d. display the size of each string in the list
	e. reverse a string in the list by its list index
	f. enter a '.' to quit
2. Further, there are three gradle tasks that can be called, each with a differnt function.
	a. Task 1. This task spins up an unthreaded server to perform client requests. It can be called with the command:
		```
		gradle Task1 -Pport=<port>
		```
		A client can connect from a Windows Command Prompt (requires Telnet installed) with the command:
		```
		telnet <host> <port>
		```
	b. Task 2. This task spins up an unbounded server to perform client requests.  Theoretically, any number of clients may connect.  It can be called with the command:
		```
		gradle Task2 -Pport=<port>
		```
		A client can connect from a Windows Command Prompt (requires Telnet installed) with the command:
		```
		telnet <host> <port>
		```
	c. Task 3. This task spins up a bounded server to perform client requests. It will allow up the given number of clients to connect. Clients attempting to connect beyond this limit will be placed in a wait status.  When a space in the pool opens a waiting client will automatically be connected.  It can be called with the command:
		```
		gradle Task3 -Pport=<port> -Pthread=<pool size>
		```
		A client can connect from a Windows Command Prompt (requires Telnet installed) with the command:
		```
		telnet <host> <port>
		```
3. This program utilizes TCP and Telnet protocols. 