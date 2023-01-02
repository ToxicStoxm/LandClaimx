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
import java.util.UUID;


public class ClaimCommand implements CommandExecutor {


    private final LandClaimX plugin;

    public ClaimCommand(LandClaimX plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public  boolean onCommand(CommandSender sender, Command command, String label, String [] args) {

        //Player Feedback messages
        String noperms = plugin.prefix + "You do not have the permissions to execute that command!";
        String invalargs = plugin.prefix + "You provided invalid arguments! " + "\n" + "Type '/claim help' to display the Help Menu";
        String plinfo = plugin.prefix + "§bby §3x_Tornado10 " + "§bVersion: §3" + plugin.version + "§r";
        String alredyclaimed = plugin.prefix + "This chunk is already claimed!" + "\n";
        String isnotclaimed = plugin.prefix + "This Chunk isn't claimed yet!" + "\n" + "Type '/claim' to claim it";
        String successclaim = plugin.prefix + "Successfully claimed chunk!";
        String successclaims = plugin.prefix + "Successfully claimed chunks!";

        //used for /claim radius
        String radius1 = "1";
        String radius2 = "2";
        String radius3 = "3";
        String radius4 = "4";
        String radius5 = "5";
        String radius6 = "6";
        String radius7 = "7";
        String radius8 = "8";


        //if the command is executed by a player
        if (!(sender instanceof Player)) {

            //if the command isn't executed by a player
            sender.sendMessage(plugin.prefix + "Only Players can run this command!");
            return true;

        } else {

            //defining player
            Player player = (Player) sender;
            //general plugin information. Displayed when using '/claim info'

            //permission check
            if (player.hasPermission(plugin.perms_claim)) {

                //if the player didn't provide any arguments
                if (args.length == 0) {

                    //current chunk gets defined
                    Chunk chunk = player.getLocation().getChunk();

                    //defining a chunkID for every chunk
                    String chunkID = chunk.getX() + "," + chunk.getZ();

                    //if the chunk is already claimed (isChunk function from main class)
                    if (plugin.isChunk(chunkID)) {

                        //permission check
                        if (player.hasPermission(plugin.perms_overwrite)) {

                            //player feedback
                            sender.sendMessage(alredyclaimed + "Type '/claim overwrite' to overwrite the previous claim");

                            //permissio ncheck
                        } else if (player.hasPermission(plugin.perms_owner)){

                            //player feedback
                            sender.sendMessage(alredyclaimed + "Type '/claim owner' to display the chunk's Owner");

                        }else {

                            //player feedback
                            sender.sendMessage(alredyclaimed + "You don't have the Permissions to overwrite the claim!");

                        }

                    } else {

                        //adds the current chunk to the HashMap 'chunks' (chunkID, UUID)
                        plugin.addChunk(chunkID, player.getUniqueId());
                        //player feedback
                        player.sendMessage(successclaim);


                    }
                    //if the player provided 1 argument
                } else if (args.length == 1) {

                    String word = args[0];

                    //argument check for the first argument, if the player provided wrong arguments a help message is send
                    if (!word.equals("owner") && !word.equals("overwrite") && !word.equals("info") && !word.equals("help") && !word.equals("clear") && !word.equals("radius") && !word.equals("remove")) {

                        //player feedback
                        player.sendMessage(invalargs);

                    }

                    if (word.equals("radius")) {

                        player.sendMessage(plugin.prefix + "Please enter a number between 1 and 8!");

                    }


                    //if the first argument equals to 'owner'
                    if (word.equals("owner")) {


                        //permission check
                        if (player.hasPermission(plugin.perms_owner)) {

                            Chunk chunk = player.getLocation().getChunk();

                            String chunkID = chunk.getX() + "," + chunk.getZ();

                            if (plugin.getOwner(chunkID) == null) {

                                //player feedback
                                player.sendMessage(isnotclaimed);
                                return false;

                            } else {

                                //Putting information about the owner into strings
                                String owneruuid = plugin.getOwner(chunkID).toString();

                                try {

                                    String ownername = Bukkit.getPlayer(plugin.getOwner(chunkID)).getName();
                                    //player feedback
                                    player.sendMessage(plugin.prefix + "The owner of this chunk is: \n" + plugin.prefix + ownername);


                                } catch (Exception e) {

                                    String ownername = Bukkit.getOfflinePlayer(plugin.getOwner(chunkID)).getName();
                                    //player feedback
                                    player.sendMessage(plugin.prefix + "The owner of this chunk is: \n" + plugin.prefix + ownername);
                                }

                            }

                        } else {

                            //player feedback
                            player.sendMessage(noperms);

                        }


                    }





                    //if the first argument equals to 'overwrite'
                    if (word.equals("overwrite")) {

                        //permission check
                        if (player.hasPermission(plugin.perms_overwrite)) {

                            Chunk chunk = player.getLocation().getChunk();

                            String chunkID = chunk.getX() + "," + chunk.getZ();

                            plugin.addChunk(chunkID, player.getUniqueId());

                            //player feedback
                            player.sendMessage(successclaim);

                        } else {

                            //player feedback
                            player.sendMessage(noperms);

                        }

                    }






                    //if the first argument equals to 'remove'
                    if (word.equals("remove")) {

                        //permission check
                        if (player.hasPermission(plugin.perms_remove)) {

                            Chunk chunk = player.getLocation().getChunk();

                            String chunkID = chunk.getX() + "," + chunk.getZ();

                            //check if the chunk isn't claimed yet to prevent error
                            if (plugin.getOwner(chunkID) == null) {

                                //player feedback
                                player.sendMessage(isnotclaimed);
                                return false;

                            } else {

                                //permission check
                                if (player.hasPermission(plugin.perms_remove_other)) {

                                    //removing chunk from HashMap 'chunks'
                                    plugin.removeChunk(chunkID, player.getUniqueId());
                                    player.sendMessage(successclaim);

                                    //check if the player tries to remove his own chunk claim
                                } else if (((Player) sender).getUniqueId().equals(plugin.getOwner(chunkID))) {

                                    //removing chunk from HashMap 'chunks'
                                    plugin.removeChunk(chunkID, player.getUniqueId());
                                    player.sendMessage(successclaim);

                                    //check if the player tries to remove other players chunk claim without permission
                                } else {

                                    //player feedback
                                    player.sendMessage("You can not unclaim chunks, owned by other players!");

                                }
                            }


                        }else {

                            //player feedback
                            player.sendMessage(noperms);

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
                        String[] helpmenu = new String[10];

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
                        helpmenu[9] = "§b -- /claim radius <1-8> --  \n §7Claim all chunks in a radius of 1-8. USE CAREFULLY!§r";

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
                                helpmenu[9] + "\n\n" +

                                helpmenu[5] + "\n" +
                                helpmenu[6]);


                    }

                    //if the first argument equals 'clear'
                    if (word.equals("clear")) {

                        //permission check
                        if (player.hasPermission(plugin.perms_clear)) {

                            //player feedback
                            player.sendMessage("Are you sure that you want to delete all claimed chunks?\n" + "Type '/claim clear confirm' to confirm");

                        } else {

                            //player feedback
                            player.sendMessage(noperms);

                        }


                    }

                    //if the player provided more than 1 argument
                } else {

                    String word = args[0];

                    //if the player provided 2 arguments
                    if (args.length == 2) {

                        String word2 = args[1];

                        //argument check for the first and second argument, if the player provided wrong arguments a help message is send
                        if (word.equals("clear"))  {
                            if (player.hasPermission(plugin.perms_clear)) {
                                if (!word2.equals("confirm")) {

                                    //player feedback
                                    player.sendMessage(invalargs);

                                }
                            }

                        }


                        if (word.equals("radius")) {

                            if (player.hasPermission(plugin.perms_radius)) {


                                if (word2.equals(radius1) || word2.equals(radius2) || word2.equals(radius3) || word2.equals(radius4) || word2.equals(radius5) || word2.equals(radius6) || word2.equals(radius7) || word2.equals(radius8)) {

                                   int chunkZ = player.getLocation().getChunk().getZ();
                                   int chunkX = player.getLocation().getChunk().getX();


                                       Integer chunkXm1 = chunkX-1;
                                       Integer chunkXp1 = chunkX+1;

                                       int chunkZm1 = chunkZ-1;
                                       int chunkZp1 = chunkZ+1;

                                       List<String> chunkID = new ArrayList<>();

                                       chunkID.add(chunkX + "," + chunkZ);
                                       chunkID.add(chunkXm1 + "," + chunkZ);
                                       chunkID.add(chunkXm1 + "," + chunkZp1);
                                       chunkID.add(chunkX + "," + chunkZp1);
                                       chunkID.add(chunkXp1 + "," + chunkZp1);
                                       chunkID.add(chunkXp1 + "," + chunkZ);
                                       chunkID.add(chunkXp1 + "," + chunkZm1);
                                       chunkID.add(chunkX + "," + chunkZm1);
                                       chunkID.add(chunkXm1 + "," + chunkZm1);

                                       UUID owner = player.getUniqueId();

                                       int i = chunkID.size()-1;

                                       while (i > -1) {

                                           if (!plugin.isChunk(chunkID.get(i))) {

                                               plugin.addChunk(chunkID.get(i), owner);

                                           } else {

                                               if (player.hasPermission(plugin.perms_overwrite) && !plugin.getOwner(chunkID.get(i)).equals(player.getUniqueId())) {

                                                   plugin.addChunk(chunkID.get(i), owner);

                                               }

                                               List<UUID> u = new ArrayList<>();

                                               u.add(plugin.getOwner(chunkID.get(i)));

                                               List<String> owners = new ArrayList<>();

                                               int in = u.size() - 1;


                                               while (in > -1) {

                                                   try {

                                                       owners.add(Bukkit.getPlayer(u.get(in)).getName());

                                                   } catch (Exception e) {

                                                       owners.add(Bukkit.getOfflinePlayer(u.get(in)).getName());

                                                   }

                                                   in = in-1;

                                               }

                                               if (i == 0) {
                                                   if (!plugin.getOwner(chunkID.get(i)).equals(player.getUniqueId())) {


                                                       if (player.hasPermission(plugin.perms_overwrite)) {

                                                           player.sendMessage(plugin.prefix + "Your selected area overlaps with chunks claimed by other players " + owners + "\n" + plugin.prefix + "Overwriting because needed permissions are given...");
                                                           player.sendMessage(successclaims);

                                                       } else {

                                                           player.sendMessage(plugin.prefix + "Your selected area overlaps with chunks claimed by other players " + owners + "\n" + plugin.prefix + "Reduce the radius of the command or type '/claim help' for more info!");

                                                       }

                                                   } else {

                                                       player.sendMessage(successclaims);

                                                   }
                                               }

                                       }


                                           i = i - 1;

                                       }



                                   } else {

                                    player.sendMessage(invalargs);

                                }


                            } else {

                                player.sendMessage(noperms);


                            }

                        }


                        //if the first argument equals 'clear'
                        if (word.equals("clear")) {

                        //permission check
                        if (player.hasPermission(plugin.perms_clear)) {

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
                                    player.sendMessage(plugin.prefix + "Successfully deleted all claimed chunks");

                                } else {

                                    player.sendMessage(invalargs);

                                }

                            }else {

                                //player feedback
                                player.sendMessage(noperms);

                            }
                        }


                    } else {

                        //player feedback
                        player.sendMessage(plugin.prefix + "You provided too many arguments!");

                    }

                }



            } else {

                //player feedback
                player.sendMessage(noperms);

            }

        }

        return true;

    }

}
