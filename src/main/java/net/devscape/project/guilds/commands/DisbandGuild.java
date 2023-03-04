package net.devscape.project.guilds.commands;

import java.util.Optional;

import net.devscape.project.guilds.handlers.Guild;
import net.devscape.project.guilds.util.Message;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;
import net.devscape.project.guilds.Guilds;
import net.devscape.project.guilds.SubCommand;

public class DisbandGuild extends SubCommand {

    public DisbandGuild(final Guilds plugin, final CommandSender sender, final String[] args) {
        super(plugin, sender, args);
        this.execute();
    }

    public boolean execute() {
        final Player player = (Player)this.getSender();
        try {
            final Optional<Guild> guild = this.getPlugin().getData().getGuild(player.getUniqueId());
            if (!guild.isPresent()) {
                Message.send(this.getPlugin(), this.getSender(), "must-be-owner");
                return true;
            }
            if (guild.get().getOwner().equals(player.getUniqueId())) {

                TextComponent mainComponent = new TextComponent( "Bu komut loncanızı silecek! Bunu yapmak istediğinize emin misiniz?" );
                mainComponent.setColor( ChatColor.BLUE  );
                TextComponent lastComponent = new TextComponent( "Evet derseniz loncanız geri döndürülemez!" );
                lastComponent.setColor( ChatColor.BLUE  );
                TextComponent subComponent = new TextComponent( "[Evet Loncayı Sil!!!]" );
                subComponent.setColor( ChatColor.RED );
                subComponent.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder( "Loncanızı silmek istediğinize emin misiniz?" ).create() ) );
                subComponent.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/g disbandacceptconfirmradome" ) );
                player.spigot().sendMessage( mainComponent );
                player.spigot().sendMessage( subComponent );
                player.spigot().sendMessage( lastComponent );
            }
            else {
                Message.send(this.getPlugin(), this.getSender(), "must-be-owner");
            }
        }
        catch (NullPointerException e) {
            return false;
        }
        return true;
    }
}
