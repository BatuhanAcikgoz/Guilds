package net.devscape.project.guilds.commands;

import java.util.Objects;
import java.util.Optional;
import net.devscape.project.guilds.handlers.Role;
import net.devscape.project.guilds.handlers.Guild;
import net.devscape.project.guilds.util.Message;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;
import net.devscape.project.guilds.Guilds;
import net.devscape.project.guilds.SubCommand;

public class InvitePlayer extends SubCommand {

    public InvitePlayer(final Guilds plugin, final CommandSender sender, final String[] args) {
        super(plugin, sender, args);
        this.execute();
    }
    
    private boolean execute() {
        final Player player = (Player)this.getSender();
        final Player player2 = Bukkit.getPlayer(this.getArgs()[1]);
        final Optional<Guild> guild = this.getPlugin().getData().getGuild(player.getUniqueId());
        final Integer guildmembers = guild.get().getMembers().size();
        final int guildmaxmember = guild.get().getMaxMembers();
        if (!guild.isPresent()) {
            Message.send(this.getPlugin(), this.getSender(), "must-be-owner");
            return false;
        }
        if (player2 == null) {
            Message.sendPlaceholder(this.getPlugin(), this.getSender(), "player-not-found", this.getArgs()[1]);
            return false;
        }
        if (player.getName().equalsIgnoreCase(this.getArgs()[1])) {
            Message.send(this.getPlugin(), this.getSender(), "self-invite-error");
            return false;
        }
        if (!(guild.get().getMembers().get(player.getUniqueId()) == Role.LEADER || guild.get().getMembers().get(player.getUniqueId()) == Role.MOD)) {
            Message.send(this.getPlugin(), this.getSender(), "must-be-owner");
            return false;
        }
        if (guild.get().getMembers().containsKey(player2.getUniqueId())) {
            Message.sendPlaceholder(this.getPlugin(), this.getSender(), "player-already-in-guild", player2.getName());
            return false;
        }
        if (guildmembers >= guildmaxmember){
            Message.send(this.getPlugin(), Objects.requireNonNull(Bukkit.getPlayer(guild.get().getOwner())), "guild-slot-max");
            return false;
        }
        this.getPlugin().getData().saveInvite(player2.getUniqueId(), guild.get().getName());
        Message.sendPlaceholder(this.getPlugin(), this.getSender(), "invite-send", player2.getName());
        Message.sendPlaceholder(this.getPlugin(), player2, "invite-receive", guild.get().getName());
        return true;
    }
}
