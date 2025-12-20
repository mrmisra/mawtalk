package org.eu.mawtalk;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MawTalk extends JavaPlugin {
    private final Map<UUID, UUID> lastMessager = new ConcurrentHashMap<>();
    private final Map<UUID, Set<UUID>> ignored = new ConcurrentHashMap<>();
    private PartyManager partyManager = new PartyManager();

    @Override
    public void onEnable() {
        this.getCommand("msg").setExecutor(new MsgCommand(this));
        this.getCommand("reply").setExecutor(new ReplyCommand(this));
        this.getCommand("ignore").setExecutor(new IgnoreCommand(this));
        this.getCommand("unignore").setExecutor(new UnignoreCommand(this));
        this.getCommand("ignorelist").setExecutor(new IgnoreListCommand(this));
        this.getCommand("mawtalk").setExecutor(new HelpCommand(this));
        this.getCommand("party").setExecutor(new PartyCommand(this));

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new ChatListener(this), this);

        getLogger().info("MawTalk enabled");
    }

    @Override
    public void onDisable() {
        getLogger().info("MawTalk disabled");
    }

    public Map<UUID, UUID> getLastMessager() {
        return lastMessager;
    }

    public Map<UUID, Set<UUID>> getIgnored() {
        return ignored;
    }

    public PartyManager getPartyManager() {
        return partyManager;
    }
}
