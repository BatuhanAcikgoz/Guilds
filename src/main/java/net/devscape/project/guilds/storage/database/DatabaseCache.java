package net.devscape.project.guilds.storage.database;

import java.util.HashMap;
import java.util.UUID;
import java.util.Map;

public class DatabaseCache
{
    private final Map<UUID, Double> transactionConfirmations;
    
    public DatabaseCache() {
        this.transactionConfirmations = new HashMap<UUID, Double>();
    }
    
    public void addTransactionConfirmation(final UUID uuid, final double value) {
        this.transactionConfirmations.put(uuid, value);
    }
    
    public void removeTransactionConfirmation(final UUID uuid) {
        this.transactionConfirmations.remove(uuid);
    }
    
    public boolean hasPendingTransaction(final UUID uuid) {
        return this.transactionConfirmations.containsKey(uuid);
    }
}
