package net.devscape.project.guilds;

import ga.strikepractice.StrikePractice;
import ga.strikepractice.api.StrikePracticeAPI;
import ga.strikepractice.events.DuelEndEvent;
import net.devscape.project.guilds.handlers.RadomeAPI;
import net.devscape.project.guilds.storage.file.FileManager;
import net.devscape.project.guilds.handlers.Guild;
import java.sql.SQLException;
import net.devscape.project.guilds.storage.database.Database;

import java.util.HashMap;
import java.util.Optional;
import java.util.logging.Level;
import net.devscape.project.guilds.handlers.hooks.PAPI;
import net.devscape.project.guilds.menus.MenuListener;
import java.util.Objects;
import net.devscape.project.guilds.menus.MenuUtil;
import net.devscape.project.guilds.storage.database.DatabaseCache;
import net.devscape.project.guilds.storage.ManageData;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class Guilds extends JavaPlugin implements Listener {

    private static Guilds instance;
    private ManageData data;
    private static Economy econ;
    private DatabaseCache cache;
    private static final HashMap<Player, MenuUtil> menuUtilMap;
    private Guilds plugin;

    public void onEnable() {

        init();

    }
    
    public void onDisable() {
        this.data.saveAllData();
        this.getLogger().log(Level.INFO, "Plugin disabled!");
    }

    public void init() {
        (Guilds.instance = this).saveDefaultConfig();
        StrikePracticeAPI api = StrikePractice.getAPI();
        boolean dbEnabled;
        if (Objects.requireNonNull(this.getConfig().getString("storage-type")).equalsIgnoreCase("db")) {
            this.setupDatabase();
            dbEnabled = true;
        } else {
            this.setupFilesystem();
            dbEnabled = false;
        }

        if (!dbEnabled) {
            this.data.loadAllData();
            this.autoSaveTask();
        }
        Objects.requireNonNull(this.getCommand("guilds")).setExecutor(new CommandHandler(this));
        this.getServer().getPluginManager().registerEvents(new MenuListener(), this);
        this.setupEconomy();
        if (this.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PAPI().register();
            this.getLogger().log(Level.INFO, "PlaceholderAPI found Guilds!");
        }
        new RadomeAPI(this);
        this.cache = new DatabaseCache();
        this.getLogger().log(Level.INFO, "Plugin enabled!");
    }
    
    @EventHandler
    public void onLeave(final PlayerQuitEvent e) {
        Guilds.menuUtilMap.remove(e.getPlayer());
    }

    private void autoSaveTask() {
        new BukkitRunnable() {
            public void run() {
                Guilds.this.data.saveAllData();
                Guilds.this.getLogger().log(Level.INFO, "Auto-saved data.");
            }
        }.runTaskTimer(this, 6000L, 6000L);
    }
    
    private void setupEconomy() {
        if (this.getServer().getPluginManager().getPlugin("Vault") == null) {
            this.getLogger().log(Level.SEVERE, "Economy not setup!");
            return;
        }
        final RegisteredServiceProvider<Economy> rsp = (RegisteredServiceProvider<Economy>)this.getServer().getServicesManager().getRegistration((Class)Economy.class);
        if (rsp == null) {
            return;
        }
        Guilds.econ = rsp.getProvider();
    }
    
    private boolean setupDatabase() {
        try {
            this.data = new Database(this.getConfig().getString("database-info.address"), this.getConfig().getInt("database-info.port"), this.getConfig().getString("database-info.username"), this.getConfig().getString("database-info.password"), this.getConfig().getString("database-info.table"), this.getConfig().getString("database-info.database"));
            return true;
        }
        catch (SQLException e) {
            this.getLogger().log(Level.SEVERE, e.getMessage());
            this.getLogger().log(Level.SEVERE, "Plugin shutting down due to database error. Check your database settings.");
            this.getPluginLoader().disablePlugin(this);
            return false;
        }
    }
    
    public static MenuUtil getMenuUtil(final Player player, final Guild guild) {
        if (Guilds.menuUtilMap.containsKey(player)) {
            return Guilds.menuUtilMap.get(player);
        }
        final MenuUtil menuUtil = new MenuUtil(player, guild);
        Guilds.menuUtilMap.put(player, menuUtil);
        return menuUtil;
    }
    
    public static HashMap<Player, MenuUtil> getMenuUtilMap() {
        return Guilds.menuUtilMap;
    }
    
    public static Guilds getInstance() {
        return Guilds.instance;
    }
    
    private boolean setupFilesystem() {
        try {
            this.data = new FileManager(this);
            return true;
        }
        catch (Exception e) {
            this.getLogger().log(Level.SEVERE, "File system could not be initialized..");
            this.getPluginLoader().disablePlugin(this);
            return false;
        }
    }
    
    public static Economy getEcon() {
        return Guilds.econ;
    }
    
    public ManageData getData() {
        return this.data;
    }
    
    public DatabaseCache getCache() {
        return this.cache;
    }
    
    static {
        Guilds.econ = null;
        menuUtilMap = new HashMap<Player, MenuUtil>();
    }
}
