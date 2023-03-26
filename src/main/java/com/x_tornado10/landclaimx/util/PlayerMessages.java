package com.x_tornado10.landclaimx.util;

import org.bukkit.entity.Player;

import java.util.ArrayList;

public class PlayerMessages {

    private String prefix;

    public PlayerMessages (String prefix) {

        this.prefix = prefix;

    }

    public void msg(Player player, String message) {

        player.sendMessage(prefix + "§7" + message + "§r");

    }

    public void msg(ArrayList<Player> players, String message) {

        for (Player player : players) {

            player.sendMessage(prefix + "§7" + message + "§r");

        }

    }

}
