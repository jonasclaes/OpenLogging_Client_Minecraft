package be.jonasclaes.openlogging.client.minecraft;

import be.jonasclaes.openlogging.client.minecraft.commands.CommandReload;
import be.jonasclaes.openlogging.client.minecraft.communication.HTTP;
import be.jonasclaes.openlogging.client.minecraft.events.Events;
import be.jonasclaes.openlogging.client.minecraft.logging.Handler;
import be.jonasclaes.openlogging.client.minecraft.reporting.PlayerReporter;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class OpenLoggingClientMinecraft extends JavaPlugin {
    private Boolean loggingEnabled;
    private Boolean loggingEventsEnabled;
    private String openLoggingServerEndpoint;
    private String openLoggingId;
    private Handler logHandler;
    private Listener eventListener;

    private Boolean playerReporterEnabled;
    private int playerReporterWait;
    private PlayerReporter playerReporter;
    private Thread playerReporterThread;

    @Override
    public void onEnable() {
        super.onEnable();
        this.saveDefaultConfig();

        this.loggingEnabled = this.getConfig().getBoolean("main.enable-logging");
        this.loggingEventsEnabled = this.getConfig().getBoolean("main.enable-event-logging");
        this.openLoggingServerEndpoint = this.getConfig().getString("main.endpoint");
        this.openLoggingId = this.getConfig().getString("main.id");

        this.playerReporterEnabled = this.getConfig().getBoolean("reporting.player.enable");
        this.playerReporterWait = this.getConfig().getInt("reporting.player.wait");

        HTTP.setEndpoint(this.openLoggingServerEndpoint);
        HTTP.setId(this.openLoggingId);

        // Handle logs.
        if (this.loggingEnabled) {
            this.logHandler = new Handler(this.getServer());
            Logger.getLogger("").addHandler(this.logHandler);
        }

        // Register event handler.
        if (this.loggingEnabled && this.loggingEventsEnabled) {
            this.eventListener = new Events(this);
            this.getServer().getPluginManager().registerEvents(this.eventListener, this);
        }

        // Create new player reporter and thread.
        if (this.loggingEnabled && this.playerReporterEnabled) {
            this.playerReporter = new PlayerReporter(this.getServer(), this.playerReporterWait);
            this.playerReporterThread = new Thread(this.playerReporter);
            this.playerReporterThread.start();
        }

        // Register commands;
        this.getCommand("reload-olcm").setExecutor(new CommandReload(this));

        this.getLogger().info("OpenLogging Minecraft Client has enabled.");
    }

    @Override
    public void onLoad() {
        super.onLoad();

        this.getLogger().info("OpenLogging Minecraft Client has loaded.");
    }

    @Override
    public void onDisable() {
        super.onDisable();

        // Stop data reporter and thread.
        if (this.loggingEnabled && this.playerReporterEnabled) {
            this.playerReporter.stop();
            while (this.playerReporterThread.isAlive()) {}
        }

        // Remove log handler.
        if (this.loggingEnabled) {
            Logger.getLogger("").removeHandler(this.logHandler);
        }

        // Unregister all events.
        if (this.loggingEnabled && this.loggingEventsEnabled) {
            HandlerList.unregisterAll(this.eventListener);
        }

        this.getLogger().info("OpenLogging Minecraft Client has disabled.");
    }
}
