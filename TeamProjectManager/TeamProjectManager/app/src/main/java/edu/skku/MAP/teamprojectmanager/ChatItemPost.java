package edu.skku.MAP.teamprojectmanager;

import java.util.HashMap;
import java.util.Map;

public class ChatItemPost {
    public String FROM;
    public String CONTENTS;
    public String TIME;

    public ChatItemPost(){
        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost.class)
    }

    public ChatItemPost(String FROM, String CONTENTS, String TIME) {
        this.FROM = FROM;
        this.CONTENTS = CONTENTS;
        this.TIME = TIME;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("FROM", FROM);
        result.put("CONTENTS", CONTENTS);
        result.put("TIME", TIME);
        return result;
    }
}
