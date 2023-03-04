package net.devscape.project.guilds.storage.database;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;

import net.devscape.project.guilds.Guilds;
import net.devscape.project.guilds.handlers.Role;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import net.devscape.project.guilds.handlers.GPlayer;
import java.sql.ResultSet;
import net.devscape.project.guilds.handlers.Guild;
import java.sql.PreparedStatement;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

import net.devscape.project.guilds.storage.ManageData;

public class Database implements ManageData {
    private final String host;
    private final int port;
    private final String username;
    private final String password;
    private final String playerTable;
    private final String guildTable;
    private final String inviteTable;
    private final String database;

    public Database(final String host, final int port, final String username, final String password, final String table, final String database) throws SQLException {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.playerTable = table + "_players";
        this.guildTable = table + "_guilds";
        this.inviteTable = table + "_invites";
        this.database = database;
        this.connect();
        this.createAllTables();
    }
    
    public Connection connect() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password);
        }
        catch (SQLException | ClassNotFoundException ex2) {
            throw new SQLException("Could not connect");
        }
    }
    
    public boolean createAllTables() throws SQLException {
        this.createGuildTable();
        this.createPlayerTable();
        this.createInvitesTable();
        return true;
    }
    
    private boolean createGuildTable() throws SQLException {
        final String sql = "CREATE TABLE IF NOT EXISTS " + this.guildTable + " (id VARCHAR(64) NOT NULL, description VARCHAR(128) NOT NULL, level INT NOT NULL DEFAULT 0, levelexp INT NOT NULL DEFAULT 0, PRIMARY KEY (id))";
        try (final Connection conn = this.connect();
             final PreparedStatement stat = conn.prepareStatement(sql)) {
            stat.execute();
            return true;
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Could not create guild table");
        }
    }
    
    private boolean createPlayerTable() throws SQLException {
        final String sql = "CREATE TABLE IF NOT EXISTS " + this.playerTable + " (id VARCHAR(64) PRIMARY KEY, role VARCHAR(12) NOT NULL, guild_id VARCHAR(64) NOT NULL, INDEX guild_ind (guild_id), FOREIGN KEY (guild_id) REFERENCES " + this.guildTable + "(id) ON DELETE CASCADE ON UPDATE CASCADE)";
        try (final Connection conn = this.connect();
             final PreparedStatement stat = conn.prepareStatement(sql)) {
            stat.execute();
            return true;
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Could not create player table");
        }
    }
    
    @Override
    public boolean saveGuild(final Guild guild) {
        try {
            if (this.rowExists(this.guildTable, guild.getName())) {
                return this.updateGuild(guild);
            }
            return this.insertGuild(guild);
        }
        catch (SQLException e) {
            return false;
        }
    }
    
    private boolean updateGuild(final Guild guild) throws SQLException {
        final String sql = "UPDATE " + this.guildTable + " SET id = ?, description = ?, level = ?, levelexp = ? WHERE id = ?";
        try (final Connection conn = this.connect();
             final PreparedStatement stat = conn.prepareStatement(sql)) {
            stat.setString(1, guild.getName());
            stat.setString(2, guild.getDescription());
            stat.setInt(3, guild.getLevel());
            stat.setInt(4, guild.getLevelExp());
            stat.setString(5, guild.getName());
            stat.execute();
            return true;
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Could not update guild: " + guild.getName());
        }
    }
    
    private boolean rowExists(final String table, final String id) {
        final String sql = "SELECT EXISTS (SELECT * FROM " + table + " WHERE id = ?)";
        try (final Connection conn = this.connect();
             final PreparedStatement stat = conn.prepareStatement(sql)) {
            stat.setString(1, id);
            stat.execute();
            final ResultSet rs = stat.getResultSet();
            if (rs.next()) {
                return rs.getBoolean(1);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    private boolean rowExists(final String table, final String playerId, final String guildId) {
        final String sql = "SELECT EXISTS (SELECT * FROM " + table + " WHERE player_id = ? AND guild_id = ?)";
        try (final Connection conn = this.connect();
             final PreparedStatement stat = conn.prepareStatement(sql)) {
            stat.setString(1, playerId);
            stat.setString(2, guildId);
            stat.execute();
            final ResultSet rs = stat.getResultSet();
            if (rs.next()) {
                return rs.getBoolean(1);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    private boolean insertGuild(final Guild guild) {
        final String sql = "INSERT INTO " + this.guildTable + " VALUES (?, ?, ?, ?)";
        try (final Connection conn = this.connect();
             final PreparedStatement stat = conn.prepareStatement(sql)) {
            stat.setString(1, guild.getName());
            stat.setString(2, guild.getDescription());
            stat.setInt(3, guild.getLevel());
            stat.setInt(4, guild.getLevelExp());
            stat.execute();
            return true;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean deleteGuild(final String id) {
        final String sql = "DELETE FROM " + this.guildTable + " WHERE id = ?";
        try (final Connection conn = this.connect();
             final PreparedStatement stat = conn.prepareStatement(sql)) {
            stat.setString(1, id);
            stat.execute();
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean removePlayer(final UUID uuid) {
        final String sql = "DELETE FROM " + this.playerTable + " WHERE id = ? AND role = ?";
        try (final Connection conn = this.connect();
             final PreparedStatement stat = conn.prepareStatement(sql)) {
            stat.setString(1, uuid.toString());
            stat.setString(2, "MEMBER");
            stat.execute();
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean savePlayer(final GPlayer player) {
        try {
            if (this.rowExists(this.playerTable, player.getGuildId())) {
                return this.updatePlayer(player.getUuid(), player.getRole().toString(), player.getGuildId());
            }
            return this.insertPlayer(player.getUuid(), player.getRole().toString(), player.getGuildId());
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private boolean insertPlayer(final UUID id, final String role, final String guildId) throws SQLException {
        final String sql = "INSERT INTO " + this.playerTable + " VALUES (?, ?, ?)";
        try (final Connection conn = this.connect();
             final PreparedStatement stat = conn.prepareStatement(sql)) {
            stat.setString(1, id.toString());
            stat.setString(2, role);
            stat.setString(3, guildId);
            stat.execute();
            return true;
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Could not add player to the database");
        }
    }
    
    private boolean updatePlayer(final UUID id, final String role, final String guildId) throws SQLException {
        final String sql = "UPDATE " + this.playerTable + "SET role = ?, guild_id = ? WHERE id = ?";
        try (final Connection conn = this.connect();
             final PreparedStatement stat = conn.prepareStatement(sql)) {
            stat.setString(1, role);
            stat.setString(2, guildId);
            stat.setString(3, id.toString());
            stat.execute();
            return true;
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Could not add player to the database");
        }
    }

    @Override
    public Optional<Guild> getGuild(final String id) {
        final Guild guild = new Guild(id);
        final Optional<Guild> guildData = this.loadGuildData(id);
        if (!guildData.isPresent()) {
            return Optional.empty();
        }
        guild.setLevelExp(guildData.get().getLevelExp());
        guild.setDescription(guildData.get().getDescription());
        final Optional<Map<UUID, Role>> members = this.loadGuildMembers(id);
        if (members.isPresent()) {
            guild.setMembers(members.get());
            for (final UUID uuid : members.get().keySet()) {
                if (members.get().get(uuid).equals(Role.LEADER)) {
                    guild.setOwner(uuid);
                }
            }
            return Optional.of(guild);
        }
        return Optional.empty();
    }
    
    @Override
    public Optional<Guild> getGuild(final UUID uuid) {
        final Optional<GPlayer> player = this.getPlayer(uuid);
        if (player.isPresent()) {
            final String guildId = player.get().getGuildId();
            return this.getGuild(guildId);
        }
        return Optional.empty();
    }
    
    @Override
    public Optional<GPlayer> getPlayer(final UUID id) {
        final String sql = "SELECT role, guild_id FROM " + this.playerTable + " WHERE id = ?";
        try (final Connection conn = this.connect();
             final PreparedStatement stat = this.connect().prepareStatement(sql)) {
            stat.setString(1, id.toString());
            stat.execute();
            final ResultSet rs = stat.getResultSet();
            if (rs.next()) {
                final String role = rs.getString(1);
                final String guildId = rs.getString(2);
                return Optional.of(new GPlayer(id, guildId, Role.valueOf(role)));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Guild> getAllGuilds() {
        final Set<String> ids = this.getGuildIds();
        final List<Guild> guilds = new ArrayList<Guild>();
        for (final String id : ids) {
            final Optional<Guild> guild = this.getGuild(id);
            guild.ifPresent(guilds::add);
        }
        return guilds;
    }
    
    @Override
    public boolean saveAllData() {
        return false;
    }
    
    @Override
    public boolean loadAllData() {
        return false;
    }
    
    @Override
    public boolean hasInvite(final UUID playerId, final String guildId) {
        final String sql = "SELECT * FROM " + this.inviteTable + " WHERE player_id = ? AND guild_id = ?";
        try (final Connection conn = this.connect();
             final PreparedStatement stat = conn.prepareStatement(sql)) {
            stat.setString(1, playerId.toString());
            stat.setString(2, guildId);
            stat.execute();
            final ResultSet rs = stat.getResultSet();
            if (rs.next()) {
                return true;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Set<String> getGuildIds() {
        final String sql = "SELECT id FROM " + this.guildTable;
        final Set<String> ids = new HashSet<String>();
        try (final Connection conn = this.connect();
             final PreparedStatement stat = conn.prepareStatement(sql)) {
            stat.execute();
            final ResultSet rs = stat.getResultSet();
            while (rs.next()) {
                ids.add(rs.getString(1));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            return new HashSet<String>();
        }
        return ids;
    }
    
    private Optional<Guild> loadGuildData(final String id) {
        final Guild guild = new Guild(id);
        final String sql = "SELECT description, levelexp FROM " + this.guildTable + " WHERE id = ?";
        try (final Connection conn = this.connect();
             final PreparedStatement stat = conn.prepareStatement(sql)) {
            stat.setString(1, id);
            stat.execute();
            final ResultSet rs = stat.getResultSet();
            if (rs.next()) {
                final String desc = rs.getString(1);
                final int levelexp = rs.getInt(2);
                guild.setDescription(desc);
                guild.setLevelExp(levelexp);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
        return Optional.of(guild);
    }
    
    private Optional<Map<UUID, Role>> loadGuildMembers(final String id) {
        final String sql = "SELECT * FROM " + this.playerTable + " WHERE guild_id = ?";
        final Map<UUID, Role> members = new HashMap<UUID, Role>();
        try (final Connection conn = this.connect();
             final PreparedStatement stat = conn.prepareStatement(sql)) {
            stat.setString(1, id);
            stat.execute();
            final ResultSet rs = stat.getResultSet();
            while (rs.next()) {
                final UUID uuid = UUID.fromString(rs.getString(1));
                final Role role = Role.valueOf(rs.getString(2));
                members.put(uuid, role);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
        if (members.size() == 0) {
            return Optional.empty();
        }
        return Optional.of(members);
    }
    
    private void createInvitesTable() throws SQLException {
        final String sql = "CREATE TABLE IF NOT EXISTS " + this.inviteTable + "(player_id VARCHAR(64) NOT NULL, guild_id VARCHAR(64) NOT NULL, INDEX guild_ind (guild_id), FOREIGN KEY (guild_id) REFERENCES " + this.guildTable + "(id) ON DELETE CASCADE ON UPDATE CASCADE)";
        try (final Connection conn = this.connect();
             final PreparedStatement stat = conn.prepareStatement(sql)) {
            stat.execute();
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Could not create invites table");
        }
    }
    
    @Override
    public boolean saveInvite(final UUID playerId, final String guildId) {
        if (this.rowExists(this.inviteTable, playerId.toString(), guildId)) {
            return false;
        }
        return this.insertInvite(playerId.toString(), guildId);
    }
    
    private boolean insertInvite(final String playerId, final String guildId) {
        final String sql = "INSERT INTO " + this.inviteTable + " VALUES (?, ?)";
        try (final Connection conn = this.connect();
             final PreparedStatement stat = conn.prepareStatement(sql)) {
            stat.setString(1, playerId);
            stat.setString(2, guildId);
            stat.execute();
            return true;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean GuildSetNameSQL(final UUID playerId, final String guildId) {
        final Optional<Guild> oldguildname = Guilds.getInstance().getData().getGuild(playerId);
        final String sql = "UPDATE " + this.guildTable + " SET id = ? WHERE id = ?";
        try (final Connection conn = this.connect();
             final PreparedStatement stat = conn.prepareStatement(sql)) {
            stat.setString(1, guildId);
            stat.setString(2, oldguildname.get().getName());
            stat.execute();
            return true;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean deleteInvite(final UUID playerId, final String guildId) {
        final String sql = "DELETE FROM " + this.inviteTable + " WHERE player_id = ? AND guild_id = ?";
        try (final Connection conn = this.connect();
             final PreparedStatement stat = this.connect().prepareStatement(sql)) {
            stat.setString(1, playerId.toString());
            stat.setString(2, guildId);
            stat.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
