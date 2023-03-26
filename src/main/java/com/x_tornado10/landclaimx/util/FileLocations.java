package com.x_tornado10.landclaimx.util;

import com.x_tornado10.landclaimx.LandClaimX;

public class FileLocations {

    private LandClaimX plugin = LandClaimX.getInstance();

    private String plpath = plugin.getDataFolder().getAbsolutePath();

    private String claims_yml = plpath + "/data/claims.yml";


    private String datadir = plpath + "/data/";

    public String getPlpath() {
        return plpath;
    }

    public void setPlpath(String plpath) {
        this.plpath = plpath;
    }

    public String getClaims_yml() {
        return claims_yml;
    }

    public void setClaims_yml(String claims_yml) {
        this.claims_yml = claims_yml;
    }

    public String getDatadir() {
        return datadir;
    }

    public void setDatadir(String datadir) {
        this.datadir = datadir;
    }
}
