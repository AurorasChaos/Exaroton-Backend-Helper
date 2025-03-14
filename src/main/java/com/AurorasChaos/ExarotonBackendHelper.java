package com.AurorasChaos;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * ExarotonBackendHelper is a Spigot plugin that assists with backend operations,
 * including server switching via Velocity.
 * 
 * Author: AurorasChaos
 */
public class ExarotonBackendHelper extends JavaPlugin implements PluginMessageListener, CommandExecutor {
    // Constant for the custom plugin messaging channel
    private static final String CHANNEL = "velocity:custom";

    /**
     * Called when the plugin is enabled.
     * Registers the plugin messaging channels and sets the executor for the command.
     */
    @Override
    public void onEnable() {
        // Register outgoing and incoming plugin channels for communication with Velocity
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, CHANNEL);
        Bukkit.getMessenger().registerIncomingPluginChannel(this, CHANNEL, this);
        
        // Set the command executor for the "switchserver" command
        this.getCommand("switchserver").setExecutor(this);
    }

    /**
     * Called when the plugin is disabled.
     * Unregisters the plugin messaging channels.
     */
    @Override
    public void onDisable() {
        // Unregister the plugin channels to clean up resources
        Bukkit.getMessenger().unregisterOutgoingPluginChannel(this, CHANNEL);
        Bukkit.getMessenger().unregisterIncomingPluginChannel(this, CHANNEL);
    }

    /**
     * Handles incoming plugin messages on the registered channel.
     * Processes only messages from the designated channel.
     *
     * @param channel the channel the message was sent on
     * @param player the player associated with the message
     * @param message the raw message data as a byte array
     */
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        // Verify that the message is coming from our custom channel
        if (!channel.equals(CHANNEL)) return;

        try (DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(message))) {
            // Read the sub-channel to determine the type of message
            String subChannel = inputStream.readUTF();
            // If the sub-channel matches, process the message content
            if (subChannel.equals("exampleMessage")) {
                String receivedData = inputStream.readUTF();
                getLogger().info("Received from Velocity: " + receivedData);
            }
        } catch (Exception e) {
            // Log any exceptions that occur during message processing
            e.printStackTrace();
        }
    }

    /**
     * Sends a message to Velocity to switch a player to a different subserver.
     *
     * @param playerName the name of the player to be switched
     * @param subServer the target subserver name
     */
    public void sendMessageToVelocity(String playerName, String subServer) {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try (DataOutputStream outputStream = new DataOutputStream(byteStream)) {
            // Write the command key and data for switching servers
            outputStream.writeUTF("exarotonSwitch");
            outputStream.writeUTF(playerName);
            outputStream.writeUTF(subServer);
        } catch (Exception e) {
            // Log any errors that occur while writing the data
            e.printStackTrace();
        }

        // Use the first available online player to send the plugin message
        if (!Bukkit.getOnlinePlayers().isEmpty()) {
            Player player = Bukkit.getOnlinePlayers().iterator().next();
            player.sendPluginMessage(this, CHANNEL, byteStream.toByteArray());
        }
    }

    /**
     * Processes the "switchserver" command.
     * Expects two arguments: the player's name and the target subserver.
     *
     * @param sender the source of the command
     * @param command the command that was executed
     * @param label the alias of the command used
     * @param args the command arguments
     * @return true if the command was handled, false otherwise
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Confirm the command is "switchserver"
        if (command.getName().equalsIgnoreCase("switchserver")) {
            // Validate that the required arguments are provided
            if (args.length < 2) {
                sender.sendMessage("Usage: /switchserver <player> <subserver>");
                return true;
            }
            // Extract arguments: player's name and target subserver
            String playerName = args[0];
            String subServer = args[1];
            // Send the server switch request via the messaging system
            sendMessageToVelocity(playerName, subServer);
            sender.sendMessage("Switching " + playerName + " to server: " + subServer);
            return true;
        }
        return false;
    }
}