package net.devscape.project.guilds.commands;

import java.util.List;
import net.devscape.project.guilds.handlers.Guild;
import net.devscape.project.guilds.util.Message;
import java.util.Collections;
import org.bukkit.command.CommandSender;
import net.devscape.project.guilds.Guilds;
import net.devscape.project.guilds.SubCommand;

public class ListGuilds extends SubCommand {
    public ListGuilds(final Guilds plugin, final CommandSender sender, final String[] args) {
        super(plugin, sender, args);
        this.execute();
    }
    
    private boolean execute() {
        final List<Guild> guildList = this.getPlugin().getData().getAllGuilds();
        Collections.sort(guildList);
        if (guildList.size() == 0) {
            Message.send(this.getPlugin(), this.getSender(), "no-guilds");
            return false;
        }
        Message.send(this.getPlugin(), this.getSender(), "guild-list-header");
        for (int listSize = Math.min(guildList.size(), 10), i = 0; i < listSize; ++i) {
            final Guild guild = guildList.get(i);
            final String[] placeholders = { String.valueOf(i + 1), guild.getName(), String.valueOf(guild.getMembers().size()) };
            Message.sendGuildList(this.getPlugin(), this.getSender(), "guild-list-line", placeholders);
        }
        return true;
    }
}
