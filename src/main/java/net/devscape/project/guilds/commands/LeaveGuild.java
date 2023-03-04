package net.devscape.project.guilds.commands;

import java.util.Optional;
import net.devscape.project.guilds.handlers.Guild;
import net.devscape.project.guilds.util.Message;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;
import net.devscape.project.guilds.Guilds;
import net.devscape.project.guilds.SubCommand;

public class LeaveGuild extends SubCommand {

    public LeaveGuild(final Guilds plugin, final CommandSender sender, final String[] args) {
        super(plugin, sender, args);
        this.execute();
    }
    
    private boolean execute() {
        if (!(this.getSender() instanceof Player)) {
            return false;
        }
        final Player player = (Player)this.getSender();
        final Optional<Guild> guild = this.getPlugin().getData().getGuild(player.getUniqueId());
        if (!guild.isPresent()) {
            Message.send(this.getPlugin(), this.getSender(), "not-in-guild");
            return false;
        }
        if (guild.get().isOwner(player.getUniqueId())) {
            Message.send(this.getPlugin(), this.getSender(), "leave-guild-owner");
            return false;
        }
        guild.get().removeMember(player.getUniqueId());
        this.getPlugin().getData().removePlayer(player.getUniqueId());
        Message.sendPlaceholder(this.getPlugin(), this.getSender(), "leave-guild", guild.get().getName());
        return true;
    }
}
