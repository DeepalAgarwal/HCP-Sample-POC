package com.adobe.aem.onlineshopping.core.services;

import com.google.gson.JsonObject;
import org.json.JSONObject;

public interface UtilityService {

    public JSONObject getJsonObject(String StringData);

    public String getAPIResponse(String url);
}
