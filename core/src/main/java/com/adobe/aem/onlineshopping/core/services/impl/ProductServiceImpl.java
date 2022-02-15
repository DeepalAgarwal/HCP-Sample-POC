package com.adobe.aem.onlineshopping.core.services.impl;
import com.adobe.aem.onlineshopping.core.config.CommonConfiguration;
import com.adobe.aem.onlineshopping.core.services.ProductService;
import com.adobe.aem.onlineshopping.core.services.UtilityService;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;
import com.google.gson.JsonObject;
import org.apache.sling.api.resource.*;
import org.apache.sling.commons.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component(service = ProductService.class)
@Designate(ocd= CommonConfiguration.class)
public class ProductServiceImpl implements ProductService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductService.class);
    private CommonConfiguration config;

    @Reference
    UtilityService utilityService;

    @Reference
    ResourceResolverFactory resourceResolverFactory;

    @Reference
    QueryBuilder queryBuilder;

    ResourceResolver resourceResolver = null;
    Session session;

    @Activate
    protected void activate(CommonConfiguration config) {
        this.config = config;
    }


    @Override
    public int getFilteredCards(String tag) {
        int noOfCards = 0;
        try {
            final Map<String, Object > param = Collections.singletonMap(ResourceResolverFactory.SUBSERVICE, (Object) "getResourceResolver");
            resourceResolver = resourceResolverFactory.getServiceResourceResolver(param);
            Session session = resourceResolver.adaptTo(Session.class);
            LOG.info("Got Session ---" );

            Map predicateMap = new HashMap();
            predicateMap.put("path", "/content/onlineShopping/us/en/products");
            predicateMap.put("type", "cq:Page");
            predicateMap.put("property", "jcr:content/@cq:tags");
            predicateMap.put("property.value", "hcp-site:pediatric");
            Query query = queryBuilder.createQuery(PredicateGroup.create(predicateMap), session);
            SearchResult result = query.getResult();
            LOG.info("Query Executed");
            noOfCards = result.getHits().size();
            LOG.info("No of Product Cards: --------------" + noOfCards);

        } catch (Exception  e)  {
            e.printStackTrace();
        }
        return noOfCards;
    }


    @Override
    public JSONObject getProductsData() {
        LOG.info("----------< Reading the config values >----------");
        JSONObject jsonResponse = null;
            String protocol = config.getProtocol();
            String server = config.getServer();
            String endpoint = config.getEndpoint();
            String url = protocol + "://" + server + "/" + endpoint;
            LOG.info("URL is ; "+url);
            String response = utilityService.getAPIResponse(url);
            JSONObject jsonResp = utilityService.getJsonObject(response);
            LOG.info(" --- JSON Response --- " + jsonResp);
        return jsonResponse;
    }

    @Override
    public JsonObject getProductDetailsByProductId(String productId) {
        String protocol = config.getProtocol();
        String server = config.getServer();
        String endpoint = config.getEndpoint();
        String url = protocol + "://" + server + "/" + endpoint;
        LOG.info("URL is ; "+url);
        return null;
    }




}