package edu.skku.MAP.teamprojectmanager;

import java.util.HashMap;
import java.util.Map;

public class ChatListItemPost {
    public String PRJECTNAME;
    public String FROM;
    public String TO;

    public ChatListItemPost(){
        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost.class)
    }

    public ChatListItemPost(String PRJECTNAME, String FROM, String TO) {
        this.PRJECTNAME = PRJECTNAME;
        this.FROM = FROM;
        this.TO = TO;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("PRJECTNAME", PRJECTNAME);
        result.put("FROM", FROM);
        result.put("TO", TO);
        return result;
    }
}
