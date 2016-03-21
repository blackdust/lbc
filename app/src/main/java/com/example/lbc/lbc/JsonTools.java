package com.example.lbc.lbc;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lbc on 2016/3/21.
 */
public class JsonTools {
    public static List<Map<String,String>>parseJsonMaps(String jsonString) throws JSONException {
        List<Map<String, String>> list = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray jsonArray = jsonObject.getJSONArray("subject");
        for(int i =0; i<jsonArray.length();i++){
            JSONObject ele = jsonArray.getJSONObject(i);
            Map<String,String>map = new HashMap<String,String>();
            map.put("introduction",ele.getString("introduction"));
            map.put("url",ele.getString("url"));
            map.put("name",ele.getString("name"));
            list.add(map);
        }
        return  list;

    }
}
