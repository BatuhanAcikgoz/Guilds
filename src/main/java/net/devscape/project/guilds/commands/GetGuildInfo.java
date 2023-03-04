package net.devscape.project.guilds.commands;

import java.util.Optional;
import net.devscape.project.guilds.util.Message;
import net.devscape.project.guilds.handlers.Guild;
import org.bukkit.command.CommandSender;
import net.devscape.project.guilds.Guilds;
import net.devscape.project.guilds.SubCommand;

public class GetGuildInfo extends SubCommand {

    public GetGuildInfo(final Guilds plugin, final CommandSender sender, final String[] args) {
        super(plugin, sender, args);
        this.execute();
    }
    
    private void execute() {
        final String guildName = this.getArgs()[0];
        final Optional<Guild> guild = this.getPlugin().getData().getGuild(guildName);
        if (!guild.isPresent() || guild.get().getMembers().size() == 0) {
            Message.sendPlaceholder(this.getPlugin(), this.getSender(), "guild-not-found", this.getArgs()[0]);
        }
        else {
            Message.sendGuildInfo(this.getPlugin(), this.getSender(), "messages.guild-info", guild.get());
        }
    }
}
