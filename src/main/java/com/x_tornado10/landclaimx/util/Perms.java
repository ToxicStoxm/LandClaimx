package com.x_tornado10.landclaimx.util;

import com.x_tornado10.landclaimx.LandClaimX;

public class Perms {

    private LandClaimX plugin = LandClaimX.getInstance();

    private String perms_claim = "landclaimx.claim";
    private String perms_remove = "landclaimx.claim.remove";
    private String perms_remove_other = "landclaimx.claim.remove.other";
    private String perms_clear = "landclaimx.claim.clear";
    private String perms_radius = "landclaimx.claim.radius";
    private String perms_owner = "landclaimx.claim.owner";
    private String perms_overwrite = "landclaimx.claim.overwrite";


    public String getPerms_claim() {
        return perms_claim;
    }

    public void setPerms_claim(String perms_claim) {
        this.perms_claim = perms_claim;
    }

    public String getPerms_remove() {
        return perms_remove;
    }

    public void setPerms_remove(String perms_remove) {
        this.perms_remove = perms_remove;
    }

    public String getPerms_remove_other() {
        return perms_remove_other;
    }

    public void setPerms_remove_other(String perms_remove_other) {
        this.perms_remove_other = perms_remove_other;
    }

    public String getPerms_clear() {
        return perms_clear;
    }

    public void setPerms_clear(String perms_clear) {
        this.perms_clear = perms_clear;
    }

    public String getPerms_radius() {
        return perms_radius;
    }

    public void setPerms_radius(String perms_radius) {
        this.perms_radius = perms_radius;
    }

    public String getPerms_owner() {
        return perms_owner;
    }

    public void setPerms_owner(String perms_owner) {
        this.perms_owner = perms_owner;
    }

    public String getPerms_overwrite() {
        return perms_overwrite;
    }

    public void setPerms_overwrite(String perms_overwrite) {
        this.perms_overwrite = perms_overwrite;
    }
}
