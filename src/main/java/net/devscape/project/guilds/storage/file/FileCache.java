package net.devscape.project.guilds.storage.file;

import java.util.Optional;
import net.devscape.project.guilds.handlers.Role;
import java.util.HashMap;
import net.devscape.project.guilds.handlers.Guild;
import net.devscape.project.guilds.handlers.GPlayer;
import java.util.UUID;
import java.util.Map;

public class FileCache {

    private final Map<UUID, GPlayer> players;
    private final Map<String, Guild> guilds;
    private final Map<UUID, Double> transactionConfirmations;
    
    public FileCache() {
        this.players = new HashMap<UUID, GPlayer>();
        this.guilds = new HashMap<String, Guild>();
        this.transactionConfirmations = new HashMap<UUID, Double>();
    }
    
    public void addPlayer(final UUID uuid, final String guildId, final Role role) {
        this.players.put(uuid, new GPlayer(uuid, guildId, role));
    }
    
    public Optional<GPlayer> getPlayer(final UUID uuid) {
        if (this.players.containsKey(uuid)) {
            return Optional.of(this.players.get(uuid));
        }
        return Optional.empty();
    }
    
    public void removePlayer(final UUID uuid) {
        this.players.remove(uuid);
    }
    
    public boolean playerExists(final UUID uuid) {
        return this.players.containsKey(uuid);
    }
    
    public boolean guildExists(final String guildId) {
        return this.guilds.containsKey(guildId);
    }
    
    public void addGuild(final Guild guild) {
        this.guilds.put(guild.getName(), guild);
    }
    
    public void removeGuild(final String id) {
        this.guilds.remove(id);
    }
    
    public Optional<Guild> getGuild(final String id) {
        if (this.guilds.containsKey(id)) {
            return Optional.of(this.guilds.get(id));
        }
        return Optional.empty();
    }
    
    public Map<String, Guild> getGuilds() {
        return this.guilds;
    }
    
    public Map<UUID, GPlayer> getPlayers() {
        return this.players;
    }
    
    public void addTransactionConfirmation(final UUID uuid, final double value) {
        this.transactionConfirmations.put(uuid, value);
    }
    
    public double getTransactionAmount(final UUID uuid) {
        return this.transactionConfirmations.get(uuid);
    }
    
    public boolean hasTransactionConfirmation(final UUID uuid) {
        return this.transactionConfirmations.containsKey(uuid);
    }
}
