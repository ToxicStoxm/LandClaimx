package com.x_tornado10.landclaimx;

import com.x_tornado10.landclaimx.commands.ClaimCommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public final class LandClaimX extends JavaPlugin {

    private HashMap<String, UUID> chunks;


    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getLogger().info("starting up...");

        saveDefaultConfig();

        this.chunks = new HashMap<>();

        getCommand("claim").setExecutor(new ClaimCommand(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic



        Bukkit.getLogger().info("shutting down...");
    }

    public void addChunk(String chunk, UUID owner) {

        chunks.put(chunk, owner);

    }

    public boolean isChunk(String chunk) {

        return chunks.containsKey(chunk);
    }

    public UUID getOwner(String chunk) {

        return chunks.get(chunk);

    }

    public void removeChunk(String chunk, UUID owner) {

        chunks.remove(chunk, owner);

    }
}
