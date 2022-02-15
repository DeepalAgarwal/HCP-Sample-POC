package com.adobe.aem.onlineshopping.core.services;

import com.google.gson.JsonObject;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public interface ProductService {

    public JSONObject getProductsData();

    public JsonObject getProductDetailsByProductId(String productId);

    public ArrayList<HashMap<String, String>> getFilteredCards(String tag);
    public ArrayList<HashMap<String, String>> getFilteredCardsFromPDP(String tag);

}
