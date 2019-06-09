package edu.skku.MAP.teamprojectmanager;

public class ChatListItem {
    private String PROJECTNAME;
    private String FROM;
    private String TO;

    public ChatListItem () { }

    public ChatListItem(String PROJECTNAME, String FROM, String TO) {
        this.PROJECTNAME = PROJECTNAME;
        this.FROM = FROM;
        this.TO = TO;
    }

    public String getPROJECTNAME() {
        return PROJECTNAME;
    }

    public String getFROM() {
        return FROM;
    }

    public String getTO() {
        return TO;
    }
}
