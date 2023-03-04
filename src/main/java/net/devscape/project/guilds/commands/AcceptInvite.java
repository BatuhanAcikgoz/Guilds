package net.devscape.project.guilds.commands;

import java.util.Optional;
import java.util.Objects;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import net.devscape.project.guilds.handlers.Guild;
import net.devscape.project.guilds.handlers.GPlayer;
import net.devscape.project.guilds.handlers.Role;
import net.devscape.project.guilds.util.Message;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;
import net.devscape.project.guilds.Guilds;
import net.devscape.project.guilds.SubCommand;

public class AcceptInvite extends SubCommand {

    public AcceptInvite(final Guilds plugin, final CommandSender sender, final String[] args) {
        super(plugin, sender, args);
        this.execute();
    }
    
    private boolean execute() {
        final Player player2 = (Player)this.getSender();
        final String guildName = this.getArgs()[1];
        final boolean noGuild = !this.getPlugin().getData().getGuild(player2.getUniqueId()).isPresent();
        final boolean invited = this.getPlugin().getData().hasInvite(player2.getUniqueId(), guildName);
        if (!invited) {
            Message.sendPlaceholder(this.getPlugin(), this.getSender(), "no-invite", guildName);
            return false;
        }
        final Optional<Guild> guild = this.getPlugin().getData().getGuild(guildName);
        final boolean guildExists = guild.isPresent();
        if (guildExists && noGuild) {
            final Integer guildmembers = guild.get().getMembers().size();
            final int guildmaxmember = guild.get().getMaxMembers();
            if (guildmaxmember > guildmembers) {
                this.getPlugin().getData().savePlayer(new GPlayer(player2.getUniqueId(), guild.get().getName(), Role.MEMBER));
                Message.sendPlaceholder(this.getPlugin(), this.getSender(), "invite-accepted", guild.get().getName());
                this.getPlugin().getData().deleteInvite(player2.getUniqueId(), guild.get().getName());
                if (Bukkit.getPlayer(guild.get().getOwner()) != null) {
                    Message.sendPlaceholder(this.getPlugin(), Objects.requireNonNull(Bukkit.getPlayer(guild.get().getOwner())), "player-joined-guild", player2.getName());
                }
            } else {
                Message.send(this.getPlugin(), this.getSender(), "guild-slot-max");
            }
        }
        else if (!guildExists) {
            player2.sendMessage(guildName + " does not exist anymore!");
        }
        else {
            Message.send(this.getPlugin(), this.getSender(), "already-in-guild");
        }
        return true;
    }
}
