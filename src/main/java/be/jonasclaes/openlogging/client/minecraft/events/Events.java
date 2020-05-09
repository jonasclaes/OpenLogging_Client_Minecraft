package be.jonasclaes.openlogging.client.minecraft.events;

import be.jonasclaes.openlogging.client.minecraft.communication.HTTP;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Events implements Listener {
    private JavaPlugin plugin;
    private final Boolean pluginEnableEvent;
    private final Boolean pluginDisableEvent;
    private final Boolean playerJoinEvent;
    private final Boolean playerQuitEvent;
    private final Boolean playerChatEvent;
    private final Boolean blockBreakEvent;
    private final Boolean blockPlaceEvent;

    public Events(JavaPlugin plugin) {
        this.plugin = plugin;

        this.pluginEnableEvent = this.plugin.getConfig().getBoolean("events.PLUGIN_ENABLE");
        this.pluginDisableEvent = this.plugin.getConfig().getBoolean("events.PLUGIN_DISABLE");
        this.playerJoinEvent = this.plugin.getConfig().getBoolean("events.PLAYER_JOIN");
        this.playerQuitEvent = this.plugin.getConfig().getBoolean("events.PLAYER_QUIT");
        this.playerChatEvent = this.plugin.getConfig().getBoolean("events.PLAYER_CHAT");
        this.blockBreakEvent = this.plugin.getConfig().getBoolean("events.BLOCK_BREAK");
        this.blockPlaceEvent = this.plugin.getConfig().getBoolean("events.BLOCK_PLACE");
    }

    @EventHandler
    public void onPluginEnable(PluginEnableEvent event) {
        if (this.pluginEnableEvent) {
            String logLine = "OpenLogging Minecraft Client has been enabled.";

            HTTP.sendLog("PLUGIN_ENABLE", logLine);
        }
    }

    @EventHandler
    public void onPluginDisable(PluginDisableEvent event) {
        if (this.pluginDisableEvent) {
            String logLine = "OpenLogging Minecraft Client has been disabled.";

            HTTP.sendLog("PLUGIN_DISABLE", logLine);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (this.playerJoinEvent) {
            Player player = event.getPlayer();
            String logLine = String.format("%s[%s] joined the server.", player.getDisplayName(), player.getUniqueId());
            player.sendMessage(String.format(ChatColor.AQUA + "Welcome to the server " + ChatColor.GREEN + "%s" + ChatColor.AQUA + ".", player.getDisplayName()));
            player.sendMessage(ChatColor.BLUE + "This server is using the OpenLogging plugin by Jonas Claes.");

            HTTP.sendLog("PLAYER_JOIN", logLine);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (this.playerQuitEvent) {
            Player player = event.getPlayer();
            String logLine = String.format("%s[%s] quit the server.", player.getDisplayName(), player.getUniqueId());

            HTTP.sendLog("PLAYER_QUIT", logLine);
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (this.playerChatEvent) {
            Player player = event.getPlayer();
            String logLine = String.format("%s[%s]: %s", player.getDisplayName(), player.getUniqueId(), event.getMessage());

            HTTP.sendLog("PLAYER_CHAT", logLine);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (this.blockBreakEvent) {
            Player player = event.getPlayer();
            Block block = event.getBlock();
            String logLine = String.format("%s[%s] broke block %s at X%d Y%d Z%d", player.getDisplayName(), player.getUniqueId(), block.getType().getKey(), block.getX(), block.getY(), block.getZ());

            HTTP.sendLog("BLOCK_BREAK", logLine);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (this.blockPlaceEvent) {
            Player player = event.getPlayer();
            Block block = event.getBlock();
            String logLine = String.format("%s[%s] placed block %s at X%d Y%d Z%d", player.getDisplayName(), player.getUniqueId(), block.getType().getKey(), block.getX(), block.getY(), block.getZ());

            HTTP.sendLog("BLOCK_PLACE", logLine);
        }
    }
}
