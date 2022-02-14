package com.adobe.aem.onlineshopping.core.services;

import com.google.gson.JsonObject;
import org.json.JSONObject;

public interface ProductService {

    public JSONObject getProductsData();

    public JsonObject getProductDetailsByProductId(String productId);

    public int getFilteredCards(String tag);
}
