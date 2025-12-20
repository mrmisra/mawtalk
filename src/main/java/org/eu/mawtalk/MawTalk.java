package org.eu.mawtalk;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MawTalk extends JavaPlugin {
    private final Map<UUID, UUID> lastMessager = new ConcurrentHashMap<>();

    @Override
    public void onEnable() {
        this.getCommand("msg").setExecutor(new MsgCommand(this));
        this.getCommand("reply").setExecutor(new ReplyCommand(this));
        getLogger().info("MawTalk enabled");
    }

    @Override
    public void onDisable() {
        getLogger().info("MawTalk disabled");
    }

    public Map<UUID, UUID> getLastMessager() {
        return lastMessager;
    }
}
