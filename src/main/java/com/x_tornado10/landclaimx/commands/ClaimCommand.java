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

            if (player.hasPermission("landclaimx.claim")) {

                if (args.length == 0) {


                    Chunk chunk = player.getLocation().getChunk();

                    String chunkID = chunk.getX() + "," + chunk.getZ();

                    if (plugin.isChunk(chunkID)) {

                        if (player.hasPermission("landclaimx.claim.overwrite")) {

                            sender.sendMessage("This chunk is already claimed!" + "\n" + "Type '/claim overwrite' to overwrite the previous claim");

                        } else if (player.hasPermission("landclaimx.claim.owner")){

                            sender.sendMessage("This chunk is already claimed!" + "\n" + "Type '/claim owner' to display the chunk's Owner");

                        }else {

                            sender.sendMessage("This chunk is already claimed!");

                        }

                    } else {

                        plugin.addChunk(chunkID, player.getUniqueId());

                    }
                } else if (args.length == 1) {

                    String word = args[0];

                    if (word.equals("owner") || word.equals("overwrite") || word.equals("remove")) {

                    } else {
                        player.sendMessage("You provided invalid arguments! " + "\n" + "Try: /claim <owner,overwrite,remove>");
                    }


                    if (word.equals("owner")) {


                        if (player.hasPermission("landclaimx.claim.owner")) {

                            Chunk chunk = player.getLocation().getChunk();

                            String chunkID = chunk.getX() + "," + chunk.getZ();

                            if (plugin.getOwner(chunkID) == null) {

                                player.sendMessage("This Chunk isn't claimed yet!" + "\n" + "Type '/claim' to claim it");
                                return false;

                            } else {


                                String owneruuid = plugin.getOwner(chunkID).toString();


                                String ownername = Bukkit.getPlayer(plugin.getOwner(chunkID)).getName().toString();


                                player.sendMessage("The owner of this chunk is: " + ownername + "   (" + owneruuid + ")");
                            }

                        } else {

                            player.sendMessage("You do not have the permissions to execute that command!");

                        }


                    }







                    if (word.equals("overwrite")) {

                        if (player.hasPermission("landclaimx.claim.overwrite")) {

                            Chunk chunk = player.getLocation().getChunk();

                            String chunkID = chunk.getX() + "," + chunk.getZ();

                            plugin.addChunk(chunkID, player.getUniqueId());

                            player.sendMessage("overwriting...");

                        } else {

                            player.sendMessage("You do not have the permissions to execute that command!");

                        }

                    }







                    if (word.equals("remove")) {

                        if (player.hasPermission("landclaimx.claim.remove")) {

                            Chunk chunk = player.getLocation().getChunk();

                            String chunkID = chunk.getX() + "," + chunk.getZ();

                            if (plugin.getOwner(chunkID) == null) {

                                player.sendMessage("This Chunk isn't claimed yet!" + "\n" + "Type '/claim' to claim it");
                                return false;

                            } else {


                                if (player.hasPermission("landclaimx.claim.remove.other")) {

                                    plugin.removeChunk(chunkID, player.getUniqueId());

                                } else if (((Player) sender).getUniqueId().equals(plugin.getOwner(chunkID))) {

                                    plugin.removeChunk(chunkID, player.getUniqueId());

                                } else {

                                    player.sendMessage("You can not unclaim chunks, owned by other players!");

                                }
                            }


                        }else {

                            player.sendMessage("You do not have the permissions to execute that command!");

                        }

                    }






                } else {

                    player.sendMessage("You provided too many arguments!");

                }



            } else {

                player.sendMessage("You do not have the permissions to execute that command!");

            }

        }

        return true;

    }
}
