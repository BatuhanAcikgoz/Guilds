package net.devscape.project.guilds.menus.pages;

import java.util.*;

import net.devscape.project.guilds.Guilds;
import net.devscape.project.guilds.handlers.Role;
import net.devscape.project.guilds.util.Message;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import net.devscape.project.guilds.menus.MenuUtil;
import net.devscape.project.guilds.menus.Menu;

import static net.devscape.project.guilds.util.Message.format;

public class GuildMenu extends Menu {

    private Guilds plugin;
    public GuildMenu(final MenuUtil menuUtil) {
        super(menuUtil);
    }
    
    @Override
    public String getMenuName() {
        return "[lang]guilds.menu.info[/lang]";
    }
    
    @Override
    public int getSlots() {
        return 45;
    }
    
    @Override
    public void handleMenu(final InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        e.setCancelled(true);

        if (e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getType().equals(Material.PLAYER_HEAD)
                && e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(format("[lang]guilds.menu.members[/lang]"))) {
            Role role = menuUtil.getGuild().getMembers().get(player.getUniqueId());

            if (role == Role.LEADER) {
                e.setCancelled(true);
                e.getWhoClicked().closeInventory();
                new MembersMenu(Guilds.getMenuUtil((Player) e.getWhoClicked(), menuUtil.getGuild())).open();
            } else {
            }
        }
    }
    
    @Override
    public void setMenuItems() {
        this.inventory.setItem(10, this.makeItem(Material.NETHER_STAR, format("[lang]guilds.menu.leader[/lang]" + Bukkit.getOfflinePlayer(this.menuUtil.getGuild().getOwner()).getName())));
        final StringBuilder string = new StringBuilder();
        for (final UUID member : this.menuUtil.getGuild().getMembers().keySet()) {
            string.append(Bukkit.getOfflinePlayer(member).getName()).append(" ");
        }
        this.inventory.setItem(12, this.makeItem(Material.PLAYER_HEAD, format("[lang]guilds.menu.members[/lang]")));
        this.inventory.setItem(14, this.makeItem(Material.PLAYER_HEAD, format("[lang]guilds.menu.size[/lang]" + this.menuUtil.getGuild().getMembers().size() + "/" + this.menuUtil.getGuild().getMaxMembers())));
        final List<String> whatAreGuilds = new ArrayList<String>();
        whatAreGuilds.add("[lang]guilds.menu.info1[/lang]");
        whatAreGuilds.add("[lang]guilds.menu.info2[/lang]");
        whatAreGuilds.add("[lang]guilds.menu.info3[/lang]");
        this.inventory.setItem(17, this.makeItem(Material.BOOK, format("[lang]guilds.menu.whatsguild[/lang]"), format(whatAreGuilds)));

        final List<String> guildCommands = new ArrayList<>();
        guildCommands.add("[lang]guilds.menu.help1[/lang]");
        guildCommands.add("[lang]guilds.menu.help2[/lang]");
        guildCommands.add("[lang]guilds.menu.help3[/lang]");
        guildCommands.add("[lang]guilds.menu.help4[/lang]");
        guildCommands.add("[lang]guilds.menu.help5[/lang]");
        guildCommands.add("[lang]guilds.menu.help6[/lang]");
        this.inventory.setItem(35, this.makeItem(Material.OAK_SIGN, format("[lang]guilds.menu.commands[/lang]"), format(guildCommands)));

        this.inventory.setItem(16, this.makeItem(Material.WHITE_STAINED_GLASS_PANE, format("&f")));
        this.inventory.setItem(25, this.makeItem(Material.WHITE_STAINED_GLASS_PANE, format("&f")));
        this.inventory.setItem(34, this.makeItem(Material.WHITE_STAINED_GLASS_PANE, format("&f")));

        int online = 0;
        for (final UUID member2 : this.menuUtil.getGuild().getMembers().keySet()) {
            if (Bukkit.getPlayer(member2) != null) {
                ++online;
            }
        }

        this.inventory.setItem(28, this.makeItem(Material.GREEN_DYE, format("[lang]guilds.menu.online[/lang]" + online + "/" + this.menuUtil.getGuild().getMembers().size())));
        this.inventory.setItem(30, this.makeItem(Material.FEATHER, format("[lang]guilds.menu.description[/lang]" + this.menuUtil.getGuild().getDescription())));
        this.inventory.setItem(32, this.makeItem(Material.EXPERIENCE_BOTTLE, format("[lang]guilds.menu.level[/lang]" + this.menuUtil.getGuild().getLevel() + "/" + this.menuUtil.getGuild().getMaxLevel())));

        fillEmpty();
    }
}
