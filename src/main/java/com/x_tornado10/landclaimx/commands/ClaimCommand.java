package com.x_tornado10.landclaimx.commands;

import com.x_tornado10.landclaimx.LandClaimX;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;


public class ClaimCommand implements CommandExecutor {

    private final LandClaimX plugin;

    public ClaimCommand(LandClaimX plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public  boolean onCommand(CommandSender sender, Command command, String label, String [] args) {

        String nopermsmessage = plugin.prefix + "You do not have the permissions to execute that command!";

        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.prefix + "Only Players can run this command!");
            return true;
        }

        if (sender instanceof Player) {

            Player player = (Player) sender;
            String plinfo = plugin.prefix + "§bby §3x_Tornado10 " + "§bVersion: §3" + plugin.version + "§r";

            List<String> onlineplayers = new ArrayList<>();
            Player[] players = new Player[Bukkit.getOnlinePlayers().size()];
            Bukkit.getServer().getOnlinePlayers().toArray(players);
            for (int i = 0; i < players.length; i++) {

                onlineplayers.add(players[i].getName());

            }

            if (player.hasPermission("landclaimx.claim")) {

                if (args.length == 0) {


                    Chunk chunk = player.getLocation().getChunk();

                    String chunkID = chunk.getX() + "," + chunk.getZ();

                    if (plugin.isChunk(chunkID)) {

                        if (player.hasPermission("landclaimx.claim.overwrite")) {

                            sender.sendMessage(plugin.prefix + "This chunk is already claimed!" + "\n" + "Type '/claim overwrite' to overwrite the previous claim");

                        } else if (player.hasPermission("landclaimx.claim.owner")){

                            sender.sendMessage(plugin.prefix + "This chunk is already claimed!" + "\n" + "Type '/claim owner' to display the chunk's Owner");

                        }else {

                            sender.sendMessage(plugin.prefix + "This chunk is already claimed!");

                        }

                    } else {

                        plugin.addChunk(chunkID, player.getUniqueId());
                        player.sendMessage(plugin.prefix + "Successfully claimed chunk!");

                    }
                } else if (args.length == 1) {

                    String word = args[0];

                    if (word.equals("owner") || word.equals("overwrite") || word.equals("remove") || word.equals("info") || word.equals("help")) {

                    } else {
                        player.sendMessage(plugin.prefix + "You provided invalid arguments! " + "\n" + "Try: /claim <owner,overwrite,remove,help,info>");
                    }


                    if (word.equals("owner")) {


                        if (player.hasPermission("landclaimx.claim.owner")) {

                            Chunk chunk = player.getLocation().getChunk();

                            String chunkID = chunk.getX() + "," + chunk.getZ();

                            if (plugin.getOwner(chunkID) == null) {

                                player.sendMessage(plugin.prefix + "This Chunk isn't claimed yet!" + "\n" + "Type '/claim' to claim it");
                                return false;

                            } else {


                                String owneruuid = plugin.getOwner(chunkID).toString();


                                String ownername = Bukkit.getPlayer(plugin.getOwner(chunkID)).getName().toString();


                                player.sendMessage(plugin.prefix + "The owner of this chunk is: " + ownername + "   (" + owneruuid + ")");
                            }

                        } else {

                            player.sendMessage(nopermsmessage);

                        }


                    }







                    if (word.equals("overwrite")) {

                        if (player.hasPermission("landclaimx.claim.overwrite")) {

                            Chunk chunk = player.getLocation().getChunk();

                            String chunkID = chunk.getX() + "," + chunk.getZ();

                            plugin.addChunk(chunkID, player.getUniqueId());

                            player.sendMessage(plugin.prefix + "Successfully claimed chunk!");

                        } else {

                            player.sendMessage(nopermsmessage);

                        }

                    }







                    if (word.equals("remove")) {

                        if (player.hasPermission("landclaimx.claim.remove")) {

                            Chunk chunk = player.getLocation().getChunk();

                            String chunkID = chunk.getX() + "," + chunk.getZ();

                            if (plugin.getOwner(chunkID) == null) {

                                player.sendMessage(plugin.prefix + "This Chunk isn't claimed yet!" + "\n" + "Type '/claim' to claim it");
                                return false;

                            } else {


                                if (player.hasPermission("landclaimx.claim.remove.other")) {

                                    plugin.removeChunk(chunkID, player.getUniqueId());
                                    player.sendMessage(plugin.prefix + "Successfully removed claim!");

                                } else if (((Player) sender).getUniqueId().equals(plugin.getOwner(chunkID))) {

                                    plugin.removeChunk(chunkID, player.getUniqueId());
                                    player.sendMessage(plugin.prefix + "Successfully removed claim!");

                                } else {

                                    player.sendMessage("You can not unclaim chunks, owned by other players!");

                                }
                            }


                        }else {

                            player.sendMessage(nopermsmessage);

                        }

                    }




                    if (word.equals("info")) {

                        player.sendMessage(plinfo);

                    }



                    if (word.equals("help")) {

                        String[] helpmenu = new String[7];

                        helpmenu[0] = "§b -- /claim --               \n §7Claim the current chunk if it's not been claimed yet§r";
                        helpmenu[1] = "§b -- /claim remove --        \n §7Remove the current chunk from your/any claimed chunks list§r";
                        helpmenu[2] = "§b -- /claim owner --         \n §7Displays the Name and UUID of the current chunks owner§r";
                        helpmenu[3] = "§b -- /claim overwrite --     \n §7Claim the current chunk and overwrite its previous ownership§r";
                        helpmenu[4] = "§b -- /claim info --          \n §7Displays Author and Version of this Plugin§r";
                        helpmenu[5] = "§b -- /claim help --          \n §7Displays this menu§r";
                        helpmenu[6] = "§7━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━";

                        player.sendMessage(helpmenu[6] + "\n" + plinfo.replace("["," ").replace("]"," ") + "\n\n" + helpmenu[0] + "\n\n" + helpmenu[1] + "\n\n" + helpmenu[2] + "\n\n" + helpmenu[3] + "\n\n" + helpmenu[4] + "\n\n" + helpmenu[5] + "\n" + helpmenu[6]);


                    }

                } else {

                    player.sendMessage(plugin.prefix + "You provided too many arguments!");

                }



            } else {

                player.sendMessage(nopermsmessage);

            }

        }

        return true;

    }
}
