# SocketGroupChat

This repository contains a simple client-server chat application written in Java, facilitating group communication. Multiple clients can connect to a server, enabling interaction in a group chat setting. Messages are broadcasted to all connected clients, and a log of previous messages is stored in a file (`messages.txt`).

## Usage

### Server

The server functionality is implemented in the `ClientHandler` class.

1. Create a `ClientHandler` instance for each connected client.
2. Handles the exchange of messages between clients.
3. Broadcasts messages to all connected clients.
4. Persists messages in `messages.txt`.

### Client

The client functionality is implemented in the `Client` class.

1. Connects to the server using a socket.
2. Displays messages from the group chat.
3. Enables users to send messages to the group chat.

## How to Run

1. Compile the Java files.
2. Execute the `Server` class to initiate the server on localhost (port 1234).
3. Run the `Client` class to establish a connection to the server and participate in the chat.

## Notes

- Messages are logged in `messages.txt` for future reference.
- Robust exception handling ensures a graceful closure of connections.
- Simultaneous connections from multiple clients are supported.

Feel free to extend and customize the code to suit your requirements.
