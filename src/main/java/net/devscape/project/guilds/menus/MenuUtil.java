package net.devscape.project.guilds.menus;

import net.devscape.project.guilds.handlers.Guild;
import org.bukkit.entity.Player;

public class MenuUtil {

    private Player owner;
    private Guild guild;
    
    public MenuUtil(final Player owner, final Guild guild) {
        this.owner = owner;
        this.guild = guild;
    }
    
    public Player getOwner() {
        return this.owner;
    }
    
    public void setOwner(final Player owner) {
        this.owner = owner;
    }
    
    public Guild getGuild() {
        return this.guild;
    }
    
    public void setGuild(final Guild guild) {
        this.guild = guild;
    }
}
