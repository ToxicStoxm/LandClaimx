package com.x_tornado10.landclaimx;

import com.x_tornado10.landclaimx.commands.ClaimCommand;
import com.x_tornado10.landclaimx.commands.ClaimCommandTabCompletion;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

//MAIN CLASS
public final class LandClaimX extends JavaPlugin {

    private File claimsFile;
    private FileConfiguration claims;

   private static HashMap<String, UUID> chunks;

   final static String FilePath = "plugins/LandClaimX/data/claims.yml";
   public String getOperatingSystem() {

       String os = System.getProperty("os.name");
       return os;

   }


    @Override
    public void onEnable() {



        // Plugin startup logic
        Bukkit.getLogger().info(consoleprefix + "Welcome back!");
        Bukkit.getLogger().info(consoleprefix + "Starting up...");
        Bukkit.getLogger().info(consoleprefix + "Checking for dependencies...");

        if (Bukkit.getPluginManager().getPlugin("dynmap") == null) {

            Bukkit.getLogger().warning(consoleprefix + "Dependencies: Plugin 'dynmap' wasn't found!");
            Bukkit.getLogger().warning(consoleprefix + "Disabling plugin...");
            Bukkit.getPluginManager().disablePlugin(this);

        } else {

            Bukkit.getLogger().warning(consoleprefix + "Dependencies: Plugin 'dynmap' was found and validated!");

        }

        //writing default config or reloading it if it exists
        saveDefaultConfig();
        reloadConfig();

        //console log
        Bukkit.getLogger().info(consoleprefix + "Loading config.yml...");
        Bukkit.getLogger().info(consoleprefix + "--Prefix: " + prefix);
        Bukkit.getLogger().info(consoleprefix + "--Console Prefix: " + consoleprefix);
        Bukkit.getLogger().info(consoleprefix + "--Version: " + version);
        Bukkit.getLogger().info(consoleprefix + "--Author: " + author);
        Bukkit.getLogger().info(consoleprefix + "Successfully loaded config.yml");

        Bukkit.getLogger().info(consoleprefix + "Loading claims.yml...");

        Bukkit.getLogger().info(consoleprefix);
        Bukkit.getLogger().info(consoleprefix + "---------------------------------------------------------------------------------------------");


        //triggering  the 'getHashMapFromTextFile' that reads the claims from the claims.yml file and puts them in to the HashMap 'chunks'
        this.chunks = new HashMap<>();

        Map<String, UUID> mapFromFile = getHashMapFromTextFile();

        for(Map.Entry<String, UUID> entry : mapFromFile.entrySet()){

            try {

                Bukkit.getLogger().info(consoleprefix + "Chunk: "+ entry.getKey() + " <==> " + "Owner: " + Bukkit.getPlayer(entry.getValue()).getName() + " <==> " + "UUID: " + entry.getValue());

            } catch (Exception e){

                Bukkit.getLogger().info(consoleprefix + "Chunk: " + entry.getKey() + " <==> " + "Owner: " + Bukkit.getOfflinePlayer(entry.getValue()).getName() + " <==> " + "UUID: " + entry.getValue());

            }





        }
        //console log
        Bukkit.getLogger().info(consoleprefix + "---------------------------------------------------------------------------------------------");
        Bukkit.getLogger().info(consoleprefix);
        Bukkit.getLogger().info(consoleprefix + "Loaded claims.yml");

        Bukkit.getLogger().info(consoleprefix + "Enabled!");



        //registering command '/claim' and TabCompletion for the '/claim' command
        getCommand("claim").setExecutor(new ClaimCommand(this));
        getCommand("claim").setTabCompleter(new ClaimCommandTabCompletion(this));

    }



    @Override
    public void onDisable() {
        // Plugin shutdown logic

        Bukkit.getLogger().info(consoleprefix + "Saving files...");


        //writing HashMap 'chunks' to claims.yml
        if (chunks == null) {

        } else {

            File data = new File(getDataFolder(), "data");
            if (data.mkdirs()) {

                Bukkit.getLogger().info(consoleprefix + "Successfully created Directory LandClaimX/data/");

            }

            File file = new File(FilePath);
            if (file.mkdirs()) {

                Bukkit.getLogger().info(consoleprefix + "Successfully created File LandClaimX/data/claims.yml");

            }

            BufferedWriter bf = null;

            try {
                bf = new BufferedWriter(new FileWriter(file));

                for (Map.Entry<String, UUID> entry : chunks.entrySet()) {

                    bf.write(entry.getKey() + ":" + entry.getValue());

                    bf.newLine();

                }

                bf.flush();
            } catch (IOException e) {

                e.printStackTrace();

            } finally {

                try {

                    bf.close();

                } catch (Exception e) {
                }

            }
        }

        //triggering save function to save everything before the plugin gets disabled
        save();

        //console log
        Bukkit.getLogger().info(consoleprefix + "Everthing saved!");

        Bukkit.getLogger().info(consoleprefix + "Good Bye!");

        Bukkit.getLogger().info(consoleprefix + "shutting down...");
    }

    //Function for getting claims from claims.yml and putting them into the HashMap 'chunks'
    public static Map<String, UUID> getHashMapFromTextFile() {

        Map<String, UUID> mapFileContents = new HashMap<String, UUID>();

        BufferedReader br = null;
        try {

            File file = new File(FilePath);


            br = new BufferedReader(new FileReader(file));

            String line = null;

            while ((line = br.readLine()) != null) {

                String[] parts = line.split(":");

                String chunkID = parts[0].trim();
                UUID UUID = java.util.UUID.fromString(parts[1].trim());

                if (!chunkID.equals("") && !UUID.equals("")) {
                    chunks.put(chunkID, UUID);

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            errormessage();

        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                }
            }
        }

        return chunks;

    }


    //plugin information getting stored into strings to be used in messages
    public String prefix = getConfig().getString("Plugin.prefix").toString();
    public String consoleprefix = getConfig().getString("Plugin.consoleprefix").toString();

    public String version = getConfig().getString("Version").toString();
    public String author = "x_Tornado10";



    //function for adding a new chunk to the HashMap chunks. Is triggered by '/claim'
    public void addChunk(String chunk, UUID owner) {

        chunks.put(chunk, owner);

    }

    //function for checking if the current chunk is already claimed (returns true/false). Also triggered by 'claim'
    public boolean isChunk(String chunk) {

        return chunks.containsKey(chunk);
    }

    //function for getting the Owner of the current chunk (returns UUID). Triggered by '/claim owner'
    public UUID getOwner(String chunk) {

        return chunks.get(chunk);

    }

    //function for removing a Chunk from the HashMap 'chunk' (returns nothing). Triggered by '/claim remove'
    public void removeChunk(String chunk, UUID owner) {

        chunks.remove(chunk, owner);

    }

    //function for saving the configuration file
    public void save() {

            saveConfig();

    }

    //function for clearing all entries from the claims.yml file. Triggered by '/claim clear' & '/claim clear confirm'
    public void clearFile() {

        try {

            PrintWriter pw = new PrintWriter(FilePath);
            pw.print("");
            pw.close();

        } catch (IOException ex) {

            ex.printStackTrace();

        }

    }

    //function for clearing the HashMap 'chunks'. Triggered by '/claim clear' & '/claim clear confirm'
    public void clearChunks() {

        chunks.clear();

    }

    public String perms_claim = "landclaimx.claim";
    public String perms_remove = "landclaimx.claim.remove";
    public String perms_remove_other = "landclaimx.claim.remove.other";
    public String perms_clear = "landclaimx.claim.clear";
    public String perms_radius = "landclaimx.claim.radius";
    public String perms_owner = "landclaimx.claim.owner";
    public String perms_overwrite = "landclaimx.claim.overwrite";

    public static String errorprefix = "LandClaimXERROR";

    public static void errormessage() {



        Bukkit.getLogger().warning(errorprefix + "An ERROR occurred while loading values from 'claims.yml'");
        Bukkit.getLogger().warning(errorprefix + "This is normal if claims.yml is empty!");
        Bukkit.getLogger().warning(errorprefix + "Please restart the server or open an Issue on GitHub: https://github.com/ToxicStoxm/LandClaimx/issues");

    }



}
