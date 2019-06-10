package edu.skku.MAP.teamprojectmanager;

public class LocationItem {
    private String ADDRESS;
    private String LATITUDE;
    private String LONGITUDE;

    public LocationItem () { }

    public LocationItem(String ADDRESS, String LATITUDE, String LONGITUDE) {
        this.ADDRESS = ADDRESS;
        this.LATITUDE = LATITUDE;
        this.LONGITUDE = LONGITUDE;
    }

    public String getADDRESS() {
        return ADDRESS;
    }

    public String getLATITUDE() {
        return LATITUDE;
    }

    public String getLONGITUDE() {
        return LONGITUDE;
    }

}
