package edu.skku.MAP.teamprojectmanager;

public class FriendsItem {
    private String ID;
    private String TMPID;
    private String NAME;
    public FriendsItem () { }

    public FriendsItem(String ID, String TMPID, String NAME) {
        this.ID = ID;
        this.TMPID = TMPID;
        this.NAME = NAME;
    }

    public String getID() {
        return ID;
    }
    public String getTMPID(){return TMPID;}
    public String getNAME(){return NAME;}
}