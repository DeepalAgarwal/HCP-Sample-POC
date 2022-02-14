package com.adobe.aem.onlineshopping.core.models;


import com.adobe.aem.onlineshopping.core.services.ProductService;
import com.adobe.aem.onlineshopping.core.servlets.GetFilteredPagesServlet;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.RequestAttribute;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.jcr.Session;
import java.util.*;

@Model(adaptables = { SlingHttpServletRequest.class,
        Resource.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class GetProductsModel {
    public static  final Logger LOG= LoggerFactory.getLogger(GetProductsModel.class);

    @RequestAttribute(name = "tag")
    String tag;

    @Inject
    ProductService productService;

    private int noOfCards;

    private ArrayList<String> sample;

    @PostConstruct
    protected void init() {
        try {
            noOfCards = productService.getFilteredCards(tag);
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
}
