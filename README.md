# Balloon Blitz

Balloon Blitz is a Java-based, Battleship-inspired game developed for a Software Architecture project. It implements an MVC structure, LAN multiplayer capabilities, and interactive Java Swing gameplay.

## Project Structure

The project is divided into three main modules:

1. **Servidor**: This module is responsible for the server-side logic and includes the following sub-modules:
    - `main-servidor`: Contains the main class to start the server.
    - `modelo-servidor`: Contains the server logic and dependencies.
    - `conexion-servidor`: Manages server connections and observers.

2. **Cliente**: This module handles the client-side logic and includes the following sub-modules:
    - `main-cliente`: Contains the main class to start the client.
    - `vista-cliente`: Manages the graphical user interface using Java Swing.
    - `conexion-cliente`: Manages client connections.

3. **Entidades**: This module is a standalone project that contains the shared entities used by both the server and client.

## Getting Started

### Prerequisites

- Java 21
- Maven 3.8.1 or higher

### Building the Project

To build the project, first navigate to **entidades** and run:

```sh
mvn clean install
```

Then, navigate to **servidor** and **cliente** directories and run in each:

```sh
mvn clean install
```

### Running the Server

To start the server, navigate to the `main-servidor` directory and run:

```sh
mvn exec:java -Dexec.mainClass="org.itson.edu.balloonblitz.main.servidor.MainServidor"
```

### Running the Client

To start the client, navigate to the `main-cliente` directory and run:

```sh
mvn exec:java -Dexec.mainClass="org.itson.edu.balloonblitz.main.cliente.MainCliente"
```

## License

This project is licensed under the MIT License.