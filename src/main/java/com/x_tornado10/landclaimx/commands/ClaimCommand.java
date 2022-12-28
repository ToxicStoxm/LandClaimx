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


        //NoPremissionMessage String for later use
        String nopermsmessage = plugin.prefix + "You do not have the permissions to execute that command!";

        //if the command isn't executed by a player
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.prefix + "Only Players can run this command!");
            return true;
        }

        //if the command is executed by a player
        if (sender instanceof Player) {



            //defining player
            Player player = (Player) sender;
            //general plugin information. Displayed when using '/claim info'
            String plinfo = plugin.prefix + "§bby §3x_Tornado10 " + "§bVersion: §3" + plugin.version + "§r";



            //list of all online players for later use
            List<String> onlineplayers = new ArrayList<>();
            Player[] players = new Player[Bukkit.getOnlinePlayers().size()];
            Bukkit.getServer().getOnlinePlayers().toArray(players);
            for (int i = 0; i < players.length; i++) {

                onlineplayers.add(players[i].getName());

            }


            //permission check
            if (player.hasPermission("landclaimx.claim")) {

                //if the player didn't provide any arguments
                if (args.length == 0) {


                    //current chunk gets defined
                    Chunk chunk = player.getLocation().getChunk();

                    //defining a chunkID for every chunk
                    String chunkID = chunk.getX() + "," + chunk.getZ();

                    //if the chunk is already claimed (isChunk function from main class)
                    if (plugin.isChunk(chunkID)) {

                        //permission check
                        if (player.hasPermission("landclaimx.claim.overwrite")) {

                            //player feedback
                            sender.sendMessage(plugin.prefix + "This chunk is already claimed!" + "\n" + "Type '/claim overwrite' to overwrite the previous claim");

                            //permissio ncheck
                        } else if (player.hasPermission("landclaimx.claim.owner")){

                            //player feedback
                            sender.sendMessage(plugin.prefix + "This chunk is already claimed!" + "\n" + "Type '/claim owner' to display the chunk's Owner");

                        }else {

                            //player feedback
                            sender.sendMessage(plugin.prefix + "This chunk is already claimed!");

                        }

                    } else {

                        //adds the current chunk to the HashMap 'chunks' (chunkID, UUID)
                        plugin.addChunk(chunkID, player.getUniqueId());
                        //player feedback
                        player.sendMessage(plugin.prefix + "Successfully claimed chunk!");


                    }
                    //if the player provided 1 argument
                } else if (args.length == 1) {

                    //putting the first argument in a string for later use
                    String word = args[0];

                    //argument check for the first argument, if the player provided wrong arguments a help message is send
                    if (word.equals("owner") || word.equals("overwrite") || word.equals("remove") || word.equals("info") || word.equals("help") || word.equals("clear")) {

                        //if the player provided 2 arguments
                        if (args.length == 2) {

                            //putting the second argument in a string for later use
                            String word2 = args[1];

                            //argument check for the second argument, if the player provided wrong arguments a help message is send
                            if (word2.equals("confirm")) {

                            } else {

                                //player feedback
                                player.sendMessage(plugin.prefix + "You provided invalid arguments! " + "\n" + "Type '/claim help' to display the Help Menu");

                            }

                        }

                    } else {
                        //player feedback
                        player.sendMessage(plugin.prefix + "You provided invalid arguments! " + "\n" + "Type '/claim help' to display the Help Menu");
                    }


                    //if the first argument equals to 'owner'
                    if (word.equals("owner")) {


                        //permission check
                        if (player.hasPermission("landclaimx.claim.owner")) {

                            Chunk chunk = player.getLocation().getChunk();

                            String chunkID = chunk.getX() + "," + chunk.getZ();

                            if (plugin.getOwner(chunkID) == null) {

                                //player feedback
                                player.sendMessage(plugin.prefix + "This Chunk isn't claimed yet!" + "\n" + "Type '/claim' to claim it");
                                return false;

                            } else {

                                //Putting information about the owner into strings
                                String owneruuid = plugin.getOwner(chunkID).toString();

                                String ownername = Bukkit.getPlayer(plugin.getOwner(chunkID)).getName().toString();

                                //layer feedback
                                player.sendMessage(plugin.prefix + "The owner of this chunk is: " + ownername + "   (" + owneruuid + ")");
                            }

                        } else {

                            //player feedback
                            player.sendMessage(nopermsmessage);

                        }


                    }





                    //if the first argument equals to 'overwrite'
                    if (word.equals("overwrite")) {

                        //permission check
                        if (player.hasPermission("landclaimx.claim.overwrite")) {

                            Chunk chunk = player.getLocation().getChunk();

                            String chunkID = chunk.getX() + "," + chunk.getZ();

                            plugin.addChunk(chunkID, player.getUniqueId());

                            //player feedback
                            player.sendMessage(plugin.prefix + "Successfully claimed chunk!");

                        } else {

                            //player feedback
                            player.sendMessage(nopermsmessage);

                        }

                    }






                    //if the first argument equals to 'remove'
                    if (word.equals("remove")) {

                        //permission check
                        if (player.hasPermission("landclaimx.claim.remove")) {

                            Chunk chunk = player.getLocation().getChunk();

                            String chunkID = chunk.getX() + "," + chunk.getZ();

                            //check if the chunk isn't claimed yet to prevent error
                            if (plugin.getOwner(chunkID) == null) {

                                //player feedback
                                player.sendMessage(plugin.prefix + "This Chunk isn't claimed yet!" + "\n" + "Type '/claim' to claim it");
                                return false;

                            } else {

                                //permission check
                                if (player.hasPermission("landclaimx.claim.remove.other")) {

                                    //removing chunk from HashMap 'chunks'
                                    plugin.removeChunk(chunkID, player.getUniqueId());
                                    player.sendMessage(plugin.prefix + "Successfully removed claim!");

                                    //check if the player tries to remove his own chunk claim
                                } else if (((Player) sender).getUniqueId().equals(plugin.getOwner(chunkID))) {

                                    //removing chunk from HashMap 'chunks'
                                    plugin.removeChunk(chunkID, player.getUniqueId());
                                    player.sendMessage(plugin.prefix + "Successfully removed claim!");

                                    //check if the player tries to remove other players chunk claim without permission
                                } else {

                                    //player feedback
                                    player.sendMessage("You can not unclaim chunks, owned by other players!");

                                }
                            }


                        }else {

                            //player feedback
                            player.sendMessage(nopermsmessage);

                        }

                    }




                    //if first argument equals to 'info'
                    if (word.equals("info")) {

                        //send info message
                        player.sendMessage(plinfo);

                    }



                    //if first argument equals to 'help'
                    if (word.equals("help")) {

                        //defining the help menu
                        String[] helpmenu = new String[9];

                        //defining the lines of the help menu
                        helpmenu[0] = "§b -- /claim --               \n §7Claim the current chunk if it's not been claimed yet§r";
                        helpmenu[1] = "§b -- /claim remove --        \n §7Remove the current chunk from your/any claimed chunks list§r";
                        helpmenu[2] = "§b -- /claim owner --         \n §7Displays the Name and UUID of the current chunks owner§r";
                        helpmenu[3] = "§b -- /claim overwrite --     \n §7Claim the current chunk and overwrite its previous ownership§r";
                        helpmenu[4] = "§b -- /claim info --          \n §7Displays Author and Version of this Plugin§r";
                        helpmenu[5] = "§b -- /claim help --          \n §7Displays this menu§r";
                        helpmenu[6] = "§7━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━";
                        helpmenu[7] = "§b -- /claim clear --         \n §7Clear all claimed chunks§r";
                        helpmenu[8] = "§b -- /claim clear confirm -- \n §7Confirmation to clear all claimed chunks (/claim clear)§r";

                        //defining the structure of the help menu
                        player.sendMessage(helpmenu[6] + "\n" +
                                plinfo.replace("["," ").replace("]"," ") + "\n\n" +

                                helpmenu[0] + "\n\n" +
                                helpmenu[1] + "\n\n" +
                                helpmenu[2] + "\n\n" +
                                helpmenu[3] + "\n\n" +
                                helpmenu[4] + "\n\n" +
                                helpmenu[7] + "\n\n" +
                                helpmenu[8] + "\n\n" +

                                helpmenu[5] + "\n" +
                                helpmenu[6]);


                    }



                    //if the first argument equals 'clear'
                    if (word.equals("clear")) {

                        //permission check
                        if (player.hasPermission("landclaimx.claim.clearall")) {

                            //player feedback
                            player.sendMessage("Are you sure that you want to delete all claimed chunks?\n" + "Type '/claim clearall confirm' to confirm");

                        } else {

                            //player feedback
                            player.sendMessage(nopermsmessage);

                        }


                    }



                    //if the player provided more than 1 argument
                } else {

                    //if the player provided 2 arguments
                    if (args.length == 2) {

                        //putting the arguments into strings for later use
                        String word = args[0];
                        String word2 = args[1];

                        //permission check
                        if (player.hasPermission("landclaimx.claim.clear")) {

                            //if the first argument equals 'clear'
                            if (word.equals("clear")) {

                                //if the second argument equals 'confirm'
                                if (word2.equals("confirm")) {

                                    //player feedback
                                    player.sendMessage(plugin.prefix + "Starting process...");

                                    //clearing the HashMap 'chunks'
                                    plugin.clearChunks();
                                    //clearing the save file claims.yml
                                    plugin.clearFile();

                                    //player feedback
                                    player.sendMessage(plugin.prefix + "Process complete!");
                                    player.sendMessage(plugin.prefix + "Successfully deleted all cleimed chunks");

                                }

                            }
                        } else {

                            //player feedback
                            player.sendMessage(nopermsmessage);

                        }


                    } else {

                        //player feedback
                        player.sendMessage(plugin.prefix + "You provided too many arguments!");

                    }

                }



            } else {

                //player feedback
                player.sendMessage(nopermsmessage);

            }

        }

        return true;

    }
}
