package be.jonasclaes.openlogging.client.minecraft.logging;

import be.jonasclaes.openlogging.client.minecraft.communication.HTTP;
import be.jonasclaes.openlogging.client.minecraft.communication.HTTPResponseException;
import org.bukkit.Server;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class Handler extends java.util.logging.Handler {
    private Server server;

    public Handler(Server server) {
        this.server = server;
    }

    @Override
    public void publish(LogRecord record) {
        Level level = record.getLevel();
        String category = "";

        if (level.equals(Level.INFO)) {
            category = "INFO";
        } else if (level.equals(Level.WARNING)) {
            category = "WARNING";
        } else if (level.equals(Level.SEVERE)) {
            category = "CRITICAL";
        } else {
            category = "INFO";
        }

        HTTP.sendLog(category, record.getMessage());
    }

    @Override
    public void flush() {

    }

    @Override
    public void close() throws SecurityException {

    }
}
