package net.devscape.project.guilds.storage;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import net.devscape.project.guilds.handlers.GPlayer;
import net.devscape.project.guilds.handlers.Guild;

public interface ManageData
{
    boolean saveGuild(final Guild p0);

    boolean removePlayer(UUID uuid);

    boolean savePlayer(final GPlayer p0);
    
    boolean saveInvite(final UUID p0, final String p1);
    
    boolean deleteGuild(final String p0);
    
    boolean deleteInvite(final UUID p0, final String p1);
    
    Optional<Guild> getGuild(final String p0);
    
    Optional<Guild> getGuild(final UUID p0);
    
    Optional<GPlayer> getPlayer(final UUID p0);
    
    boolean hasInvite(final UUID p0, final String p1);
    
    List<Guild> getAllGuilds();
    
    boolean saveAllData();
    
    boolean loadAllData();

    boolean GuildSetNameSQL(final UUID p0, final String p1);

    String getGuildDBId(final String p0);

    boolean updatePlayer(final UUID p0, final String p1, final String p2) throws SQLException;
}
