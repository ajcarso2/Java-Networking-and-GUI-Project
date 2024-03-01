# README for Java Networking and GUI Project

## Project Overview
This project is a Java application that demonstrates networking and graphical user interface (GUI) capabilities in Java. It consists of two main components: a client and a server. The client can connect to the server using sockets to send and receive messages, including JSON-formatted data. Additionally, the client includes a GUI component for interactive user engagement. The server component processes requests from the client, including echoing messages and handling other predefined actions.

## Prerequisites
- Java Development Kit (JDK) 8 or above
- Gradle (optional) for building and running the project

## How to Build and Run
1. **Using Gradle (Recommended):**
   - To build the project, navigate to the project root directory and run:
     ```
     gradle build
     ```
   - To run the server component:
     ```
     gradle runServer
     ```
   - To run the client component:
     ```
     gradle runClient
     ```

2. **Without Gradle:**
   - Compile the Java files manually using `javac` command. Make sure to include the `org.json` library in the classpath during compilation.
   - Run the server:
     ```
     java Server
     ```
   - Run the client:
     ```
     java Client
     ```

## Client
The client can be started with optional command-line arguments to specify the host and port of the server:
- `-h [host]`: Specify the server's hostname (default is `localhost`).
- `-p [port]`: Specify the server's port number (default is `8080`).

The client sends a JSON message to the server and receives a response. The GUI part of the client allows users to interact with the application through a graphical interface, showing images, accepting text inputs, and displaying messages.

## Server
The server listens for connections on a specified port. It processes incoming requests from clients, performs actions based on the request content (e.g., echoing messages, handling game logic), and sends responses back to the clients.

## Networking Utilities
The project includes utility classes for networking operations, such as sending and receiving messages over sockets (`NetworkUtils`) and converting JSON objects to and from byte arrays (`JsonUtils`).

## GUI Components
- **ClientGui:** Provides a graphical interface for user interaction, including displaying images, entering text, and showing messages.
- **OutputPanel:** A panel within the GUI for text output and input.
- **PicturePanel:** Displays a grid of images as part of the GUI.

## Additional Features
- The application demonstrates the use of JSON for data interchange between client and server.
- The GUI part uses Swing components to create an interactive user experience.

## Contributing
Contributions to the project are welcome. Please adhere to the project's coding standards and submit pull requests for any enhancements.
