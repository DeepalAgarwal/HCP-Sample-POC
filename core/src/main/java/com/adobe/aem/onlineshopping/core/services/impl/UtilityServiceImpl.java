package com.adobe.aem.onlineshopping.core.services.impl;

import com.adobe.aem.onlineshopping.core.services.RegistrationService;
import com.adobe.aem.onlineshopping.core.services.UtilityService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.time.LocalDate;

@Component(service = UtilityService.class, immediate = true)
public class UtilityServiceImpl implements UtilityService {

    public static  final Logger LOG= LoggerFactory.getLogger(UtilityService.class);

    @Override
    public JSONObject getJsonObject(String StringData) {
        LOG.info("Input String Json : " + StringData);
        JSONObject jsonResponse = null;

        try {
            jsonResponse = new JSONObject(StringData);
            LOG.info("Converted Json Object ---- " + jsonResponse);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonResponse;
    }

    @Override
    public String getAPIResponse(String url) {
        String apiResponse = null;
        try {
            URL requestURL = new URL(url);
            HttpsURLConnection connection = (HttpsURLConnection) requestURL.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            LOG.info("Response Code : " + responseCode);
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                apiResponse = response.toString();
            }
            else
                apiResponse = "No Data Found";
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
