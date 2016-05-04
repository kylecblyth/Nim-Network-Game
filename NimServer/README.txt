===============================================================================
||                             NIM SERVER README                             ||
===============================================================================
||                                                                           ||
||    Nim Server is the server-side implementation of the game Nim. It hosts ||
||  any number of 2-player sessions created when Nim clients connect to it.  ||
||                                                                           ||
||  HOW DO SESSIONS WORK?                                                    ||
||    * Upon connecting to the server, a Nim client will either prompt a new ||
||      session creation or join an existing session.                        ||
||    * If all sessions are full or no sessions yet exist, Nim Server will   ||
||      create a new one and connect the client to it. If any session has an ||
||      open spot, the server will connect the new client to an existing se- ||
||      ssion and begin that session's game.                                 ||
||    * Any player may end a session by closing their Nim window. This will  ||
||      disconnect any player connected to that session.                     ||
||  STARTING THE GAME                                                        ||
||    * When joining a new session, the Nim client's board will initally be  ||
||      grayed out and unresponsive. It is waiting for an opponent to conne- ||
||      ct to the server. Once an opponent's client connects, the game will  ||
||      begin, indicated by the first client's board pieces turning red.     ||
||    * The game can be played for any number of rounds. Score is shown in   ||
||      the fields at the right. The New Game button can start a new game at ||
||      any time, even if the current game has not been completed. Any play- ||
||      er may use this button at any time, provided a session has begun.    ||
||   HOW TO PLAY NIM                                                         ||
||    * The objective of Nim is to be the last person to take markers off    ||
||      the board (the red pieces).                                          ||
||    * You may take any of the markers off of the board when it is your     ||
||      turn, and all of the markers above the one you've clicked will also  ||
||      be removed. Once you remove a marker, your pieces will gray out      ||
||      until your opponent makes his move.                                  ||
||                                                                           ||
||   RUNNING THE PROGRAM                                                     ||
||    * You must start the server first. Run NimServer by entering:          ||
||              java NimServer <host> <port>                                 ||
||      An example host is localhost. This will run NimServer right on your  ||
||      machine without further configuration. Any unused port will do, ex   ||
||      6055.                                                                ||
||    * Now run a client. Do this with the following:                        ||
||              java Nim <serverhost> <serverport> <clienthost>              ||
||                  <clientport> <playername>                                ||
||      For our localhost example, our parameters would be "localhost 6055   ||
||      localhost 6056 Player1". You can run more clients as desired. Keep   ||
||      in mind each client must connect on a different client port. The     ||
||      server port, however, remains the same. Enjoy :)                     ||
||                                                                           ||
|| ------------------------------------------------------------------------- ||
||    ABOUT THIS PROGRAM                                                     ||
||        The Nim program was an assignment done for Concepts of Parallel    ||
||    and Distributed Systems at RIT. It is authored by Kyle Blyth, with the ||
||    UI designed by Alan Kaminsky, professor. Author credit is given in     ||
||    each source file as appropriate.                                       ||
||                                                                           ||   
===============================================================================