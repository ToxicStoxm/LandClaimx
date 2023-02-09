package com.x_tornado10.landclaimx;

import com.x_tornado10.landclaimx.commands.ClaimCommandTabCompletion;
import com.x_tornado10.landclaimx.commands.ClaimCommand;
import org.bukkit.Bukkit;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

//MAIN CLASS
public final class LandClaimX extends JavaPlugin {

   private static HashMap<String, UUID> chunks;


   final static String FilePath = "plugins/LandClaimX/data/claims.yml";



    //plugin information getting stored into strings to be used in messages
    public String prefix = getConfig().getString("Plugin.prefix");
    public String consoleprefix = getConfig().getString("Plugin.consoleprefix");

    public String version = this.getDescription().getVersion();
    public String author = this.getDescription().getAuthors().get(0);



    public String perms_claim = "landclaimx.claim";
    public String perms_remove = "landclaimx.claim.remove";
    public String perms_remove_other = "landclaimx.claim.remove.other";
    public String perms_clear = "landclaimx.claim.clear";
    public String perms_radius = "landclaimx.claim.radius";
    public String perms_owner = "landclaimx.claim.owner";
    public String perms_overwrite = "landclaimx.claim.overwrite";

    public static String errorprefix = "[LandClaimXERROR] ";



   @Override
   public void onLoad() {



   }


    @Override
    public void onEnable() {

        // Plugin startup logic
        Bukkit.getLogger().info(consoleprefix + "Welcome back!");
        Bukkit.getLogger().info(consoleprefix + "Starting up...");
        Bukkit.getLogger().info(consoleprefix + "Checking for dependencies...");

        if (Bukkit.getPluginManager().getPlugin("dynmap") == null) {

            onError("Dependencies: Plugin 'dynmap' wasn't found!");

        } else {

            Bukkit.getLogger().info(consoleprefix + "Dependencies: Dynmap version " + Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("dynmap")).getDescription().getVersion() + " was found and validated!");

        }


        //writing default config or reloading it if it exists
        saveDefaultConfig();
        reloadConfig();

        createclaims_yml();


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

        boolean doclaimsreadprint = true;

        try {

            doclaimsreadprint = getConfig().getBoolean("Plugin.doclaimsreadprint");

        } catch (Exception e) {

            getConfig().set("Plugin.doclaimsreadprint", true);

        }

        if (!doclaimsreadprint) {

            Bukkit.getLogger().info(consoleprefix + "ReadLog from file 'claims.yml' is disabled");
            Bukkit.getLogger().info(consoleprefix + "To enable, it change 'doclaimsreadprint' to 'true' in 'config.yml'");

        }

        //triggering  the 'getHashMapFromTextFile' that reads the claims from the claims.yml file and puts them in to the HashMap 'chunks'
        chunks = new HashMap<>();

        Map<String, UUID> mapFromFile = getHashMapFromTextFile();

        for (Map.Entry<String, UUID> entry : mapFromFile.entrySet()) {


            if (doclaimsreadprint) {

                try {

                    Bukkit.getLogger().info(consoleprefix + "Chunk: " + entry.getKey() + " <==> " + "Owner: " + Bukkit.getPlayer(entry.getValue()).getName() + " <==> " + "UUID: " + entry.getValue());

                } catch (Exception e) {

                    Bukkit.getLogger().info(consoleprefix + "Chunk: " + entry.getKey() + " <==> " + "Owner: " + Bukkit.getOfflinePlayer(entry.getValue()).getName() + " <==> " + "UUID: " + entry.getValue());
                }
            }


        }
        //console log
        Bukkit.getLogger().info(consoleprefix + "---------------------------------------------------------------------------------------------");
        Bukkit.getLogger().info(consoleprefix);
        Bukkit.getLogger().info(consoleprefix + "Loaded claims.yml");

        getCommand("claim").setExecutor(new ClaimCommand(this));
        getCommand("claim").setTabCompleter(new ClaimCommandTabCompletion());

        Bukkit.getLogger().info(consoleprefix + "Enabled!");

    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic

        Bukkit.getLogger().info(consoleprefix + "Saving files...");


        //writing HashMap 'chunks' to claims.yml
        if (chunks != null) {

            createclaims_yml();

            File file = new File(FilePath);

            try (BufferedWriter bf = new BufferedWriter(new FileWriter(file))) {

                for (Map.Entry<String, UUID> entry : chunks.entrySet()) {

                    bf.write(entry.getKey() + ":" + entry.getValue());

                    bf.newLine();

                }

                bf.flush();

            } catch (IOException e) {

                e.printStackTrace();

            }
        }

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

                    e.printStackTrace();

                }
            }
        }

        return chunks;

    }

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

    public void replaceChunk(String chunk, UUID owner) {

       if (chunks.get(chunk) != null) {

           chunks.replace(chunk, owner);

       } else {

           chunks.put(chunk, owner);

       }

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

    public static void errormessage() {



        Bukkit.getLogger().severe(errorprefix + "An ERROR occurred while loading values from 'claims.yml'");
        Bukkit.getLogger().severe(errorprefix + "This is normal if claims.yml is empty!");
        Bukkit.getLogger().severe(errorprefix + "Please restart the server or open an Issue on GitHub: https://github.com/ToxicStoxm/LandClaimx/issues");

    }


    public void onError(String cause) {

        Bukkit.getLogger().severe(consoleprefix + "AN ERROR OCCURRED!");
        Bukkit.getLogger().severe(consoleprefix + "Detecting possible cause...");
        Bukkit.getLogger().severe(consoleprefix + "Cause: " + cause);
        Bukkit.getLogger().severe(consoleprefix + "DISABLING PLUGIN...");
        Bukkit.getPluginManager().disablePlugin(this);

    }

    public boolean claimsexist(File file) {

        return file.exists();

    }

    public void createclaims_yml() {

        File data = new File(getDataFolder(), "data");
        File file = new File(getDataFolder() + "/data/claims.yml");
        if (!data.exists()) {

            data.mkdirs();
            Bukkit.getLogger().info(consoleprefix + "Successfully created LandClaimX/data/");

        }

       if (!file.exists()) {

           FileConfiguration filecfg = YamlConfiguration.loadConfiguration(file);
           Bukkit.getLogger().info(consoleprefix + "Successfully created LandClaimX/data/claims.yml");

       }

    }


}
