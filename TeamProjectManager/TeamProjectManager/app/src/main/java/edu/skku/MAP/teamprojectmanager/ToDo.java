package edu.skku.MAP.teamprojectmanager;

import java.util.HashMap;
import java.util.Map;

public class ToDo {
    public String todo_id;
    public String day;
    public String start_time;
    public String end_time;

    public ToDo () { }

    public ToDo(String todo_id, String day, String start_time, String end_time) {
        this.todo_id = todo_id;
        this.day = day;
        this.start_time = start_time;
        this.end_time = end_time;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("todo_id", todo_id);
        result.put("day", day);
        result.put("start_time", start_time);
        result.put("end_time", end_time);
        return result;
    }
}
