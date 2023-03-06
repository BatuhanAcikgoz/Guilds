package net.devscape.project.guilds.handlers;

import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Guild implements Comparable<Guild> {

    private String name;
    private UUID owner;
    private String description;
    private Map<UUID, Role> members;
    private int level;
    private int levelexp;

    public Guild(final UUID owner, final String name) {
        this.owner = owner;
        this.name = name;
        (this.members = new HashMap<UUID, Role>()).put(owner, Role.LEADER);
        this.description = "A new guild!";
        this.level = 0;
        this.levelexp = 0;
    }

    public Guild(final String name) {
        this.owner = null;
        this.name = name;
        this.members = new HashMap<UUID, Role>();
        this.description = "A new guild!";
        this.level = 0;
        this.levelexp = 0;
    }

    public Guild(final String name, final UUID ownerId, final String description, final List<Object> memberObjects, final int level, final int levelexp) {
        this.members = new HashMap<UUID, Role>();
        this.name = name;
        this.owner = ownerId;
        this.description = description;
        memberObjects.forEach(member -> this._addMember((JSONObject) member));
        this.level = level;
        this.levelexp = levelexp;
    }

    private void _addMember(final JSONObject member) {
        final String id = String.valueOf(member.get("player"));
        final String role = String.valueOf(member.get("role"));
        this.members.put(UUID.fromString(id), Role.valueOf(role));
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public Map<UUID, Role> getMembers() {
        return this.members;
    }

    public boolean isOwner(final UUID playerUuid) {
        return playerUuid.equals(this.owner);
    }

    public boolean containsMember(final UUID playerUuid) {
        return this.members.containsKey(playerUuid);
    }

    public UUID getOwner() {
        return this.owner;
    }

    public void setOwner(final UUID owner) {
        this.owner = owner;
    }

    public int getLevelExp() {
        return this.levelexp;
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevelExp(final int levelexp) {
        if  (levelexp >= 0 && levelexp <= 352) {
                this.levelexp = levelexp;
                this.level = (int) (Math.sqrt(levelexp+9)-3);
        }
        if  (levelexp >= 353 && levelexp <= 1507) {
                this.levelexp = levelexp;
                this.level = (int) (8.1+Math.sqrt(0.4*(levelexp-(195.975))));
        }
        if  (levelexp >= 1508) {
                this.levelexp = levelexp;
                this.level = (int) (18.055+Math.sqrt(0.222*(levelexp-752.9861)));
        }
    }
    
    public void setLevel(final int level) {
        if (level >= 0 && level <= 300) {
        if  (level >= 0 && level <= 16) {
            this.level = level;
            this.levelexp = (level*level)+(6*level);
        }
        if  (level >= 17 && level <= 31) {
                this.level = level;
                this.levelexp = (int) ((2.5*level*level)-(40.5*level)+360);
            }
        if  (level >= 32) {
                this.level = level;
                this.levelexp = (int) ((4.5*level*level)-(162.5*level)+2220);
            }
        }
        else {
            if (level < 0) {
                this.level = 0;
            }
            if (level > 300) {
                this.level = 300;
            }
        }
    }
    public int getMaxMembers() {
        return (int)(this.level * 1.2);
    }
    
    public int getMaxLevel() {
        return 300;
    }
    
    @Override
    public String toString() {
        final JSONObject json = new JSONObject();
        json.put("name", this.name);
        if (this.owner != null) {
            json.put("owner", this.owner.toString());
        }
        json.put("description", this.description);
        final JSONArray jsonArray = new JSONArray();
        if (this.members != null) {
            for (final UUID member : this.members.keySet()) {
                final JSONObject jsonObject = new JSONObject();
                jsonObject.put("player", member.toString());
                jsonObject.put("role", this.members.get(member).toString());
                jsonArray.add(jsonObject);
            }
            json.put("members", jsonArray);
        }
        return json.toString();
    }
    
    @Override
    public int compareTo(final Guild o) {
        if (this.members.size() > o.getMembers().size()) {
            return 1;
        }
        return -1;
    }
    
    public void setMembers(final Map<UUID, Role> members) {
        this.members = members;
    }
    
    public void addMember(final UUID uuid) {
        this.members.put(uuid, Role.MEMBER);
    }
    
    public void removeMember(final UUID uuid) {
        this.members.remove(uuid);
    }

}
