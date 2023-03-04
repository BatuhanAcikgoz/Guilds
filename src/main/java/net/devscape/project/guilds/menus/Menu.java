package net.devscape.project.guilds.menus;

import net.devscape.project.guilds.Guilds;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.List;
import java.util.Arrays;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.meta.SkullMeta;

import static net.devscape.project.guilds.util.Message.format;

public abstract class Menu implements InventoryHolder {

    protected Inventory inventory;
    protected MenuUtil menuUtil;

    public Menu(final MenuUtil menuUtil) {
        this.menuUtil = menuUtil;
    }
    
    public abstract String getMenuName();
    
    public abstract int getSlots();
    
    public abstract void handleMenu(final InventoryClickEvent p0);
    
    public abstract void setMenuItems();
    
    public void open() {
        this.inventory = Bukkit.createInventory((InventoryHolder)this, this.getSlots(), this.getMenuName());
        this.setMenuItems();
        this.menuUtil.getOwner().openInventory(this.inventory);
    }
    
    public void refresh() {
        this.menuUtil.getOwner().updateInventory();
    }
    
    public Inventory getInventory() {
        return this.inventory;
    }

    public void fillEmpty() {
        for (int slot = 0; slot < this.inventory.getSize(); ++slot) {
            if (this.inventory.getItem(slot) == null) {
                this.inventory.setItem(slot, this.makeItem(Material.GRAY_STAINED_GLASS_PANE, format("&8"), new String[0]));
            }
        }
    }

    public ItemStack makeItem(final Material material, final String displayName, final String... lore) {
        final ItemStack item = new ItemStack(material);
        final ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(displayName);
        itemMeta.setLore((List)Arrays.asList(lore));
        item.setItemMeta(itemMeta);
        return item;
    }

    public ItemStack makeSkull(final String displayName, final String... lore) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta itemMeta = (SkullMeta) item.getItemMeta();
        assert itemMeta != null;
        itemMeta.setOwningPlayer(Bukkit.getPlayer(displayName));
        itemMeta.setDisplayName(displayName);
        itemMeta.setLore((List)Arrays.asList(lore));
        item.setItemMeta(itemMeta);
        return item;
    }
    
    public ItemStack makeItem(final Material material, final String displayName, final List<String> lore) {
        final ItemStack item = new ItemStack(material);
        final ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(displayName);
        itemMeta.setLore((List)lore);
        item.setItemMeta(itemMeta);
        return item;
    }
}
