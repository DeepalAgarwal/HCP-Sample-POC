package com.adobe.aem.onlineshopping.core.services;


import org.apache.sling.commons.json.JSONObject;

public interface RegistrationService {

    public Boolean checkAge(String age);

    public JSONObject storeData(JSONObject inputData);

}
