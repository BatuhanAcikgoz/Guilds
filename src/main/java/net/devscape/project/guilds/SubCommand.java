package net.devscape.project.guilds;

import org.bukkit.command.CommandSender;

public class SubCommand
{
    private final Guilds plugin;
    private final CommandSender sender;
    private final String[] args;
    
    public SubCommand(final Guilds plugin, final CommandSender sender, final String[] args) {
        this.plugin = plugin;
        this.sender = sender;
        this.args = args;
    }
    
    public Guilds getPlugin() {
        return this.plugin;
    }
    
    public CommandSender getSender() {
        return this.sender;
    }
    
    public String[] getArgs() {
        return this.args;
    }
}
