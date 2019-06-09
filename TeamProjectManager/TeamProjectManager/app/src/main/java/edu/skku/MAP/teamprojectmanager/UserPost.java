package edu.skku.MAP.teamprojectmanager;

import java.util.HashMap;
import java.util.Map;

public class UserPost {
    public String EMAIL;
    public String ID;
    public String NAME;

    public UserPost(){
        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost.class)
    }

    public UserPost(String EMAIL, String ID, String NAME) {
        this.EMAIL = EMAIL;
        this.ID = ID;
        this.NAME = NAME;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("EMAIL", EMAIL);
        result.put("ID", ID);
        result.put("NAME", NAME);
        return result;
    }
}
