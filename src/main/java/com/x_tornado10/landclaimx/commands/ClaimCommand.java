package com.x_tornado10.landclaimx.commands;

import com.x_tornado10.landclaimx.LandClaimX;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ClaimCommand implements CommandExecutor {

    private final LandClaimX plugin;

    public ClaimCommand(LandClaimX plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public  boolean onCommand(CommandSender sender, Command command, String label, String [] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Only Players can run this command!");
            return true;
        }

        if (sender instanceof Player) {

            Player player = (Player) sender;

            if (args.length == 0) {


                Chunk chunk = player.getLocation().getChunk();

                String chunkID = chunk.getX() + "," + chunk.getZ();

                if (plugin.isChunk(chunkID)) {

                    sender.sendMessage("This chunk is already claimed!");

                } else {

                    plugin.addChunk(chunkID, player.getUniqueId());

                }
            } else if (args.length == 1){

                String word = args[0];

                if (word.equals("owner")) {

                    Chunk chunk = player.getLocation().getChunk();

                    String chunkID = chunk.getX() + "," + chunk.getZ();


                   String owneruuid = plugin.getOwner(chunkID).toString();

                   String ownername = Bukkit.getPlayer(plugin.getOwner(chunkID)).getName().toString();


                   player.sendMessage("The owner of this chunk is: " + ownername + "   (" + owneruuid + ")");

                }


            } else {

                player.sendMessage("You provided invalid arguments! Tray again:" + "\n" + "Tray: /claim <owner,overwrite>");

            }

        }


        return true;

    }
}
