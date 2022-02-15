package com.adobe.aem.onlineshopping.core.models;


import com.adobe.aem.onlineshopping.core.services.ProductService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.RequestAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;

@Model(adaptables = { SlingHttpServletRequest.class,
        Resource.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class GetProductDetailModel {
    public static  final Logger LOG= LoggerFactory.getLogger(GetProductDetailModel.class);

    @RequestAttribute(name = "tag")
    String tag;

    @Inject
    ProductService productService;

    private int noOfCards;

    private ArrayList<String> sample;
    private ArrayList<HashMap<String, String>> productDetailList;

    @PostConstruct
    protected void init() {
        try {
            productDetailList = productService.getFilteredCardsFromPDP(tag);
            sample = new ArrayList<>();
            sample.add("Deepali");
            sample.add("Radhika");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getSample() {
        return sample;
    }

    public int getNoOfCards() {
        return noOfCards;
    }

    public ArrayList<HashMap<String, String>> getProductDetailList() {
        return productDetailList;
    }
}
