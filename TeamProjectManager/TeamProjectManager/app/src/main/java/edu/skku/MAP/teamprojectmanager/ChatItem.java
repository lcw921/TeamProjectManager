package edu.skku.MAP.teamprojectmanager;

public class ChatItem {
    private String FROM;
    private String CONTENTS;
    private String TIME;

    public ChatItem () { }

    public ChatItem(String FROM, String CONTENTS, String TIME) {
        this.FROM = FROM;
        this.CONTENTS = CONTENTS;
        this.TIME = TIME;
    }

    public String getFROM() {
        return FROM;
    }

    public String getCONTENTS() {
        return CONTENTS;
    }

    public String getTIME() {
        return TIME;
    }
}
