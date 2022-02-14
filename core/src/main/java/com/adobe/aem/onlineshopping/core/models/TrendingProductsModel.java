package com.adobe.aem.onlineshopping.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;

@Model(adaptables = Resource.class)
public class TrendingProductsModel {

    @Inject
    String title;

    public String getTitle(){
        return title;
    };




}
