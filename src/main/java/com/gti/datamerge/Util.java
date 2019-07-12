package com.gti.datamerge;

import com.gti.datamerge.database.Row;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Util {
    public static List<Action> parseJsonActions(String json) {
        JSONParser parser = new org.json.simple.parser.JSONParser();
        List<Action> actions = new ArrayList<>();
        try {
            JSONArray array = (JSONArray) parser.parse(json);

            Iterator<JSONObject> iterator = array.iterator();
            while(iterator.hasNext()) {
                JSONObject obj = iterator.next();
                int actionType = Action.INSERT;
                if(obj.get("type").equals("insert")) {
                    actionType = Action.INSERT;
                }
                if(obj.get("type").equals("update")) {
                    actionType = Action.UPDATE;
                }
                Row row = new Row();

                JSONObject rowData = (JSONObject) obj.get("row");

                Iterator rowIter = rowData.keySet().iterator();
                while(rowIter.hasNext()) {
                    String key = (String) rowIter.next();
                    row.put(key, (String) rowData.get(key));
                }
                actions.add(new Action(actionType, row, (String) obj.get("table")));
            }
            return actions;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }
}
