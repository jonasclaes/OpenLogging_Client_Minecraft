package be.jonasclaes.openlogging.client.minecraft.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandReload implements CommandExecutor {
    private JavaPlugin plugin;

    public CommandReload(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        this.plugin.reloadConfig();
        this.plugin.getServer().getPluginManager().disablePlugin(this.plugin);
        this.plugin.getServer().getPluginManager().enablePlugin(this.plugin);
        commandSender.sendMessage("OpenLogging Minecraft Client has reloaded.");
        return true;
    }
}
