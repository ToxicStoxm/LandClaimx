package com.x_tornado10.landclaimx.handlers;

import com.x_tornado10.landclaimx.LandClaimX;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigHandler {

    private LandClaimX plugin = LandClaimX.getInstance();
    private FileConfiguration config;

    public ConfigHandler (FileConfiguration config) {

        this.config = config;

    }

    public boolean getDoClaimsReadPrint() {

        return  config.getBoolean("Plugin.doclaimsreadprint");


    }

    public String getWorldname() {

        return  config.getString("Plugin.worldname");

    }

}
