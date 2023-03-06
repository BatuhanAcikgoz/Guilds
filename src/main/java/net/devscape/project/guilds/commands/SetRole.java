package net.devscape.project.guilds.commands;

import net.devscape.project.guilds.Guilds;
import net.devscape.project.guilds.SubCommand;
import net.devscape.project.guilds.handlers.Guild;
import net.devscape.project.guilds.handlers.Role;
import net.devscape.project.guilds.util.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;

public class SetRole extends SubCommand {

    public SetRole(final Guilds plugin, final CommandSender sender, final String[] args) {
        super(plugin, sender, args);
        this.execute();
    }

    public boolean execute() {
        final Player player = (Player)this.getSender();
        try {
            final Optional<Guild> guild1 = this.getPlugin().getData().getGuild(player.getUniqueId());
            if (!guild1.isPresent()) {
                Message.send(this.getPlugin(), this.getSender(), "must-be-owner");
                return true;
            }
            final Optional<Guild> guild2 = this.getPlugin().getData().getGuild(Objects.requireNonNull(Bukkit.getPlayer(this.getArgs()[1])).getUniqueId());
            if (!guild2.isPresent()) {
                Message.send(this.getPlugin(), this.getSender(), "must-be-same-guild");
                return true;
            }
            if (guild1.get().getMembers().get(player.getUniqueId()) == Role.LEADER && Objects.equals(guild1.get().getName(), guild2.get().getName())) {
                if (Objects.equals(this.getArgs()[2], "MOD")) {
                    this.getPlugin().getData().updatePlayer(Bukkit.getPlayer(this.getArgs()[1]).getUniqueId(), "MOD", guild1.get().getName());
                    Message.sendPlaceholder(this.getPlugin(), this.getSender(), "setrole-mod", this.getArgs()[1]);
                } else if (Objects.equals(this.getArgs()[2], "MEMBER")) {
                    this.getPlugin().getData().updatePlayer(Bukkit.getPlayer(this.getArgs()[1]).getUniqueId(), "MEMBER", guild1.get().getName());
                    Message.sendPlaceholder(this.getPlugin(), this.getSender(), "setrole-member", this.getArgs()[1]);
                } else {
                    Message.send(this.getPlugin(), this.getSender(), "syntax.setrole");
                }
            }
            else {
                Message.send(this.getPlugin(), this.getSender(), "must-be-owner");
            }
        }
        catch (NullPointerException e) {
            return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}
