# dntp

Downright Neighborly Transfer Protocol
Course project, to develop a a protocol over UDP to make it reliable.

# Running notes
created using java sdk 1.8.0_152
 

## From Command Line
### To test running as a package use the helloworld
1. go to /src in terminal
1. `javac hello/HelloWorld.java`    to compile the .java file
1. `java -cp . hello/HelloWorld`    to run the file. This should output `Hello World!` in the terminal.

### To test the socketpractice application
1. open 2 terminal windows, you'll start the server, then the client
1. go to /dntp/src in terminal
1. `javac socketpractice/ServerSideSocket.java` to compile the server class
1.  `java -cp . socketpractice/ServerSideSocket.java` to run the server. You should see `The server's main is running` in the terminal window.
1. Go to /dntp/src in the other terminal window.
1. `javac socketpractice/ClientSideSocket.java` to compile the .java file
1. `java -cp . socketpractice/ClientSideSocket` to run the client. You should see `here is the string written out/r/n` in the terminal if it connected to the server correctly. Note that string does not exist on the client, so it must get it from the server.

relevant stackoverflow:   https://stackoverflow.com/questions/16137713/how-do-i-run-a-java-program-from-the-command-line-on-windows


