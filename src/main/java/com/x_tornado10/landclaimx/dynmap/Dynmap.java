package com.x_tornado10.landclaimx.dynmap;

import org.dynmap.DynmapCommonAPI;
import org.dynmap.DynmapCommonAPIListener;
import org.dynmap.markers.*;

import java.io.InputStream;
import java.util.Set;

public class Dynmap extends DynmapCommonAPIListener {

    @Override
    public void apiEnabled(DynmapCommonAPI dynmapCommonAPI) {

    }

   /* MarkerAPI markerAPI = new MarkerAPI() {
        @Override
        public Set<MarkerSet> getMarkerSets() {
            return null;
        }

        @Override
        public MarkerSet getMarkerSet(String s) {
            return null;
        }

        @Override
        public MarkerSet createMarkerSet(String s, String s1, Set<MarkerIcon> set, boolean b) {
            return null;
        }

        @Override
        public Set<MarkerIcon> getMarkerIcons() {
            return null;
        }

        @Override
        public MarkerIcon getMarkerIcon(String s) {
            return null;
        }

        @Override
        public MarkerIcon createMarkerIcon(String s, String s1, InputStream inputStream) {
            return null;
        }

        @Override
        public Set<PlayerSet> getPlayerSets() {
            return null;
        }

        @Override
        public PlayerSet getPlayerSet(String s) {
            return null;
        }

        @Override
        public PlayerSet createPlayerSet(String s, boolean b, Set<String> set, boolean b1) {
            return null;
        }
    };





    AreaMarker areaMarker = new AreaMarker() {
        @Override
        public double getTopY() {
            return 0;
        }

        @Override
        public double getBottomY() {
            return 0;
        }

        @Override
        public void setRangeY(double v, double v1) {

        }

        @Override
        public int getCornerCount() {
            return 0;
        }

        @Override
        public double getCornerX(int i) {
            return 0;
        }

        @Override
        public double getCornerZ(int i) {
            return 0;
        }

        @Override
        public void setCornerLocation(int i, double v, double v1) {

        }

        @Override
        public void setCornerLocations(double[] doubles, double[] doubles1) {

        }

        @Override
        public void deleteCorner(int i) {

        }

        @Override
        public void setLineStyle(int i, double v, int i1) {

        }

        @Override
        public int getLineWeight() {
            return 0;
        }

        @Override
        public double getLineOpacity() {
            return 0;
        }

        @Override
        public int getLineColor() {
            return 0;
        }

        @Override
        public void setFillStyle(double v, int i) {

        }

        @Override
        public double getFillOpacity() {
            return 0;
        }

        @Override
        public int getFillColor() {
            return 0;
        }

        @Override
        public void setBoostFlag(boolean b) {

        }

        @Override
        public boolean getBoostFlag() {
            return false;
        }

        @Override
        public void setDescription(String s) {

        }

        @Override
        public String getDescription() {
            return null;
        }

        @Override
        public String getMarkerID() {
            return null;
        }

        @Override
        public MarkerSet getMarkerSet() {
            return null;
        }

        @Override
        public void setMarkerSet(MarkerSet markerSet) {

        }

        @Override
        public void deleteMarker() {

        }

        @Override
        public String getWorld() {
            return null;
        }

        @Override
        public String getNormalizedWorld() {
            return null;
        }

        @Override
        public boolean isPersistentMarker() {
            return false;
        }

        @Override
        public String getLabel() {
            return null;
        }

        @Override
        public void setLabel(String s) {

        }

        @Override
        public void setLabel(String s, boolean b) {

        }

        @Override
        public boolean isLabelMarkup() {
            return false;
        }

        @Override
        public int getMinZoom() {
            return 0;
        }

        @Override
        public void setMinZoom(int i) {

        }

        @Override
        public int getMaxZoom() {
            return 0;
        }

        @Override
        public void setMaxZoom(int i) {

        }
    };







    MarkerSet markerSet = new MarkerSet() {
        @Override
        public Set<Marker> getMarkers() {
            return null;
        }

        @Override
        public Set<AreaMarker> getAreaMarkers() {
            return null;
        }

        @Override
        public Set<PolyLineMarker> getPolyLineMarkers() {
            return null;
        }

        @Override
        public Set<CircleMarker> getCircleMarkers() {
            return null;
        }

        @Override
        public Marker createMarker(String s, String s1, String s2, double v, double v1, double v2, MarkerIcon markerIcon, boolean b) {
            return null;
        }

        @Override
        public Marker createMarker(String s, String s1, boolean b, String s2, double v, double v1, double v2, MarkerIcon markerIcon, boolean b1) {
            return null;
        }

        @Override
        public Marker findMarker(String s) {
            return null;
        }

        @Override
        public Marker findMarkerByLabel(String s) {
            return null;
        }

        @Override
        public AreaMarker createAreaMarker(String s, String s1, boolean b, String s2, double[] doubles, double[] doubles1, boolean b1) {
            return null;
        }

        @Override
        public AreaMarker findAreaMarker(String s) {
            return null;
        }

        @Override
        public AreaMarker findAreaMarkerByLabel(String s) {
            return null;
        }

        @Override
        public PolyLineMarker createPolyLineMarker(String s, String s1, boolean b, String s2, double[] doubles, double[] doubles1, double[] doubles2, boolean b1) {
            return null;
        }

        @Override
        public PolyLineMarker findPolyLineMarker(String s) {
            return null;
        }

        @Override
        public PolyLineMarker findPolyLineMarkerByLabel(String s) {
            return null;
        }

        @Override
        public CircleMarker createCircleMarker(String s, String s1, boolean b, String s2, double v, double v1, double v2, double v3, double v4, boolean b1) {
            return null;
        }

        @Override
        public CircleMarker findCircleMarker(String s) {
            return null;
        }

        @Override
        public CircleMarker findCircleMarkerByLabel(String s) {
            return null;
        }

        @Override
        public String getMarkerSetID() {
            return null;
        }

        @Override
        public String getMarkerSetLabel() {
            return null;
        }

        @Override
        public void setMarkerSetLabel(String s) {

        }

        @Override
        public boolean isMarkerSetPersistent() {
            return false;
        }

        @Override
        public Set<MarkerIcon> getAllowedMarkerIcons() {
            return null;
        }

        @Override
        public void addAllowedMarkerIcon(MarkerIcon markerIcon) {

        }

        @Override
        public void removeAllowedMarkerIcon(MarkerIcon markerIcon) {

        }

        @Override
        public boolean isAllowedMarkerIcon(MarkerIcon markerIcon) {
            return false;
        }

        @Override
        public Set<MarkerIcon> getMarkerIconsInUse() {
            return null;
        }

        @Override
        public void deleteMarkerSet() {

        }

        @Override
        public void setHideByDefault(boolean b) {

        }

        @Override
        public boolean getHideByDefault() {
            return false;
        }

        @Override
        public void setLayerPriority(int i) {

        }

        @Override
        public int getLayerPriority() {
            return 0;
        }

        @Override
        public int getMinZoom() {
            return 0;
        }

        @Override
        public void setMinZoom(int i) {

        }

        @Override
        public int getMaxZoom() {
            return 0;
        }

        @Override
        public void setMaxZoom(int i) {

        }

        @Override
        public void setLabelShow(Boolean aBoolean) {

        }

        @Override
        public Boolean getLabelShow() {
            return null;
        }

        @Override
        public void setDefaultMarkerIcon(MarkerIcon markerIcon) {

        }

        @Override
        public MarkerIcon getDefaultMarkerIcon() {
            return null;
        }
    };

    */

}
