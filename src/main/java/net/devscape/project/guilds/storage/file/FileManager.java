package net.devscape.project.guilds.storage.file;

import net.devscape.project.guilds.handlers.Role;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;
import java.io.FileReader;
import java.util.Set;
import java.io.IOException;
import java.util.Objects;
import java.io.FileWriter;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

import java.util.Map;
import java.util.ArrayList;
import java.util.List;

import net.devscape.project.guilds.handlers.GPlayer;
import java.util.UUID;
import net.devscape.project.guilds.handlers.Guild;
import java.util.Optional;
import net.devscape.project.guilds.Guilds;
import net.devscape.project.guilds.storage.ManageData;

public class FileManager implements ManageData {

    private final Guilds plugin;
    private final FileCache cache;
    
    public FileManager(final Guilds plugin) {
        this.plugin = plugin;
        this.cache = new FileCache();
    }
    
    public FileCache getCache() {
        return this.cache;
    }
    
    @Override
    public Optional<Guild> getGuild(final String name) {
        return this.getCache().getGuild(name);
    }
    
    @Override
    public Optional<Guild> getGuild(final UUID playerUuid) {
        if (!this.getCache().getPlayer(playerUuid).isPresent()) {
            return Optional.empty();
        }
        final String guildId = this.getCache().getPlayer(playerUuid).get().getGuildId();
        return this.getCache().getGuild(guildId);
    }
    
    @Override
    public Optional<GPlayer> getPlayer(final UUID id) {
        return this.getCache().getPlayer(id);
    }
    
    @Override
    public boolean hasInvite(final UUID uuid, final String guildId) {
        return this.getCache().getPlayer(uuid).isPresent() && this.getCache().getPlayer(uuid).get().hasInvite(guildId);
    }

    @Override
    public List<Guild> getAllGuilds() {
        final List<Guild> guildList = new ArrayList<Guild>();
        final Map<String, Guild> guilds = this.getCache().getGuilds();
        for (final String id : guilds.keySet()) {
            guildList.add(guilds.get(id));
        }
        return guildList;
    }
    
    @Override
    public boolean saveAllData() {
        try {
            this.saveAllGuilds();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
    
    @Override
    public boolean loadAllData() {
        this.loadAllGuilds();
        return true;
    }

    @Override
    public boolean GuildSetNameSQL(UUID p0, String p1) {
        return false;
    }

    @Override
    public String getGuildDBId(String p0) {
        return null;
    }

    @Override
    public boolean updatePlayer(UUID p0, String p1, String p2) {
        return false;
    }

    @Override
    public boolean saveGuild(final Guild guild) {
        if (this.getCache().getGuilds().containsKey(guild.getName())) {
            this.getCache().getGuilds().replace(guild.getName(), guild);
        }
        else {
            this.getCache().addGuild(guild);
        }
        return true;
    }

    @Override
    public boolean removePlayer(UUID uuid) {
        this.getCache().removePlayer(uuid);
        return true;
    }

    @Override
    public boolean savePlayer(final GPlayer player) {
        if (this.getCache().playerExists(player.getUuid())) {
            this.getCache().getPlayers().replace(player.getUuid(), new GPlayer(player.getUuid(), player.getGuildId(), player.getRole()));
        }
        else {
            this.getCache().addPlayer(player.getUuid(), player.getGuildId(), player.getRole());
        }
        return true;
    }
    
    public void saveAllGuilds() throws IOException {
        FileWriter file = null;
        final Set<String> guildIds = this.getCache().getGuilds().keySet();
        final ArrayList<Guild> guildList = new ArrayList<Guild>();
        for (final String guild : guildIds) {
            if (!this.getCache().getGuild(guild).isPresent()) {
                continue;
            }
            guildList.add(this.getCache().getGuild(guild).get());
        }
        final JSONArray guildsArray = new JSONArray();
        for (final Guild guild2 : guildList) {
            final JSONObject guildJson = new JSONObject();
            guildJson.put("guild", guild2);
            guildsArray.add(guildJson);
        }
        try {
            file = new FileWriter(this.plugin.getDataFolder().getAbsolutePath() + "//guilds.json");
            file.write(guildsArray.toJSONString());
        }
        catch (IOException e) {
            e.printStackTrace();
            Objects.requireNonNull(file).flush();
            file.close();
        }
        finally {
            try {
                Objects.requireNonNull(file).flush();
                file.close();
            }
            catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }
    
    public void loadAllGuilds() {
        try {
            final FileReader reader = new FileReader(this.plugin.getDataFolder().getAbsolutePath() + "//guilds.json");
            final Object object = new JSONParser().parse(reader);
            ((JSONArray)object).forEach(guild -> this._addGuild((JSONObject) guild));
        }
        catch (IOException | ParseException ex2) {
            final Exception ex = null;
            final Exception e = ex;
            e.printStackTrace();
        }
    }
    
    private void _addGuild(final JSONObject guilds) {
        final JSONObject guild = (JSONObject) guilds.getOrDefault("guild", null);
        final String name = String.valueOf(guild.getOrDefault("name", null));
        final String ownerId = String.valueOf(guild.getOrDefault("owner", null));
        final String description = String.valueOf(guild.getOrDefault("description", "No description set!"));
        final ArrayList<Object> members = (ArrayList<Object>) guild.getOrDefault("members", new ArrayList<Object>());
        final long levelexp = (long) guild.getOrDefault("levelexp", 0);
        final long level = (long) guild.getOrDefault("level", 0);
        final Guild guildObject = new Guild(name, UUID.fromString(ownerId), description, members, (int)levelexp, (int)level);
        this._addMembers(members, name);
        this.getCache().addGuild(guildObject);
    }
    
    private void _addMembers(final ArrayList<Object> memberObjects, final String name) {
        memberObjects.forEach(member -> this._addMember((JSONObject) member, name));
    }
    
    private void _addMember(final JSONObject member, final String name) {
        final String id = String.valueOf(member.get("player"));
        final String role = String.valueOf(member.get("role"));
        this.getCache().addPlayer(UUID.fromString(id), name, Role.valueOf(role));
    }
    
    @Override
    public boolean deleteGuild(final String guildName) {
        this.getCache().removeGuild(guildName);
        return true;
    }
    
    @Override
    public boolean saveInvite(final UUID receiver, final String guildName) {
        if (!this.getCache().getPlayer(receiver).isPresent()) {
            return false;
        }
        this.getCache().getPlayer(receiver).get().addInvite(guildName);
        return true;
    }
    
    @Override
    public boolean deleteInvite(final UUID receiver, final String name) {
        if (!this.getCache().getPlayer(receiver).isPresent()) {
            return false;
        }
        this.getCache().getPlayer(receiver).get().removeInvite(name);
        return true;
    }
}
