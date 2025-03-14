# ExarotonBackendHelper

ExarotonBackendHelper is a Spigot plugin designed to streamline backend operations for Exaroton-hosted servers. It enables server administrators to switch players between subservers using Velocity's custom messaging channel. This tool is ideal for networks that require seamless player transfers across different server instances.

## Features

- **Server Switching**: Use the `/switchserver` command to move a player to a specified subserver.
- **Velocity Integration**: Communicates via the custom messaging channel `velocity:custom` with a subchannel key `exarotonSwitch` for backend operations.
- **Simple Configuration**: All essential settings—such as commands and permissions—are defined in the `plugin.yml` file.
- **Spigot API Compatibility**: Developed for Spigot API 1.20 and compiled against Spigot 1.21.4, requiring Java 17.

## Installation

### Requirements:

- A Spigot server (version 1.21.4 recommended).
- Java 17.
- Maven (for building the plugin).

### Building the Plugin:

1. Clone or download the project repository.
2. Open a terminal in the project directory.
3. Run the following command to build the plugin using Maven:

```bash
mvn clean package
```

4. After a successful build, locate the generated JAR file in the `target` directory.

### Deploying the Plugin:

1. Move the `ExarotonBackendHelper.jar` file into your server’s `plugins` folder.
2. Restart or reload your server to activate the plugin.

## Configuration

The plugin is primarily configured via the `plugin.yml` file. Below is an excerpt demonstrating the key configurations:

```yaml
name: ExarotonBackendHelper
main: com.AurorasChaos.ExarotonBackendHelper
version: 1.0
api-version: 1.20
description: A Spigot plugin for handling server switching via Exaroton.
author: AurorasChaos
commands:
  switchserver:
    description: Switch a player to another Exaroton server.
    usage: /switchserver <player> <subserver>
    permission: spigotplugin.switchserver
permissions:
  spigotplugin.switchserver:
    description: Allows a player to use the switchserver command.
    default: op
```

Customize these settings as needed to adjust command usage or permission nodes.

## Usage

Once installed, you can switch a player to a different subserver by executing:

```php
/switchserver <player> <subserver>
```

- **player**: The name of the player you wish to switch.
- **subserver**: The target subserver identifier.

When executed, the plugin sends a message through the `velocity:custom` channel using the `exarotonSwitch` subchannel key to initiate the server switch. All message handling is performed in the main class (`ExarotonBackendHelper.java`).

## Development

- **Author**: AurorasChaos
- **Main Class**: The core functionality is implemented in `ExarotonBackendHelper.java` (located under `com.AurorasChaos`).
- **Maven Build**: The project uses Maven for dependency management and compilation. Refer to the `pom.xml` for details on the Spigot version and Java compatibility.
- **Extensibility**: Developers can extend the messaging functionality or adjust the command behavior by modifying the `sendMessageToVelocity` method and other related sections in the source code.

## License

This project is licensed under the **GNU General Public License v3.0 (GPL-3.0)**. See the `LICENSE` file for details.
