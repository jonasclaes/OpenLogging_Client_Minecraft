package be.jonasclaes.openlogging.client.minecraft.reporting;

import be.jonasclaes.openlogging.client.minecraft.communication.HTTP;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Date;

public class PlayerReporter implements Runnable {
    private Server server;
    private int waitTime;
    private long lastRunTime;
    private Boolean stop;

    public PlayerReporter(Server server, int waitTime) {
        this.server = server;
        this.waitTime = waitTime;
        this.stop = false;
    }

    @Override
    public void run() {
        while (!stop) {
            long currentTime = new Date().getTime();
            long timeDiff = currentTime - this.lastRunTime;
            if (timeDiff > waitTime) {
                int maxPlayers = this.server.getMaxPlayers();
                Collection<? extends Player> onlinePlayers = this.server.getOnlinePlayers();
                HTTP.sendLog("COUNT_PLAYERS", String.format("%d/%d", onlinePlayers.size(), maxPlayers));
                lastRunTime = new Date().getTime();
            }

            try {
                // Don't stress the CPU too much.
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        this.stop = true;
    }
}
