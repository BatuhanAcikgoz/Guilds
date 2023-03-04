package net.devscape.project.guilds.handlers;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class GPlayer {

    private final UUID uuid;
    private String guildId;

    private Role role;
    private final Set<String> invites;
    
    public GPlayer(final UUID uuid, final String guildId, final Role role) {
        this.uuid = uuid;
        this.guildId = guildId;
        this.role = role;
        this.invites = new HashSet<String>();
    }
    
    public UUID getUuid() {
        return this.uuid;
    }
    
    public String getGuildId() {
        return this.guildId;
    }
    
    public void setGuildId(final String newGuildId) {
        this.guildId = newGuildId;
    }
    
    public Role getRole() {
        return this.role;
    }
    
    public void setRole(final Role role) {
        this.role = role;
    }
    public boolean hasInvite(final String guildId) {
        return this.invites.contains(guildId);
    }
    public void addInvite(final String guildId) {
        this.invites.add(guildId);
    }
    
    public void removeInvite(final String guildId) {
        this.invites.remove(guildId);
    }
}
