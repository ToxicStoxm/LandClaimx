package com.x_tornado10.landclaimx.util;

import com.x_tornado10.landclaimx.LandClaimX;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.PluginLogger;

import java.awt.*;

public class Logger {

    private String prefix;

    private LandClaimX plugin;
    private ConsoleCommandSender commandSender = Bukkit.getConsoleSender();
    private Color darkred;
    private PluginLogger logger;

    public Logger(String prefix) {

        this.prefix = prefix;
        plugin = LandClaimX.getInstance();
        logger = new PluginLogger(plugin);

    }

    public void info(String message) {

        //logger.info(prefix + message);
        commandSender.sendMessage(prefix + message);

    }

    public void warning(String message) {

        //logger.warning(prefix + "§6" + message + "§r");
        commandSender.sendMessage(prefix + "§6" + message + "§r");

    }

    public void severe(String message) {

        //logger.severe(prefix + "§4" + message + "§r");
        commandSender.sendMessage(prefix + "§4" + message + "§r");


    }

}
