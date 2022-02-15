package com.adobe.aem.onlineshopping.core.services.impl;
import com.adobe.aem.onlineshopping.core.config.CommonConfiguration;
import com.adobe.aem.onlineshopping.core.services.ProductService;
import com.adobe.aem.onlineshopping.core.services.UtilityService;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
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
import javax.jcr.Value;
import java.util.*;

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


    // get product details from PDP
    @Override
    public ArrayList<HashMap<String, String>> getFilteredCardsFromPDP(String tag) {
        int noOfCards = 0;
        ArrayList<HashMap<String, String>> productDetailList = new ArrayList<>();
        try {
            final Map<String, Object > param = Collections.singletonMap(ResourceResolverFactory.SUBSERVICE, (Object) "getResourceResolver");
            resourceResolver = resourceResolverFactory.getServiceResourceResolver(param);
            Session session = resourceResolver.adaptTo(Session.class);
            LOG.info("Got Session ---" );

            Map predicateMap = new HashMap();
            predicateMap.put("path", "/content/onlineShopping/us/en/products");
            predicateMap.put("type", "cq:Page");
            predicateMap.put("property", "jcr:content/@cq:tags");
            predicateMap.put("property.value", tag);
            Query query = queryBuilder.createQuery(PredicateGroup.create(predicateMap), session);
            SearchResult result = query.getResult();
            noOfCards = result.getHits().size();
            LOG.info("No of product cards :----" + noOfCards);

            for (Hit currentPageResult : result.getHits()) {
                String path = currentPageResult.getPath();
                LOG.info("Path: " + path);
                Resource resource = resourceResolver.getResource(path);
                LOG.info("Got Resource : ");
                //Node node = resource.adaptTo(Node.class);
                Resource contentNode = resource.getChild("jcr:content/root/container");
                Iterator<Resource> childlist = contentNode.listChildren();
                   while(childlist.hasNext())
                    {
                        Resource res3 = childlist.next();
                        String str = res3.getName().toString();
                        LOG.info("Name is ::" + str);
                        if(str.equals("productdetails"))
                        {
                            LOG.info("Inside If:");
                            Node productDetailNode = res3.adaptTo(Node.class);
                            //Node productDetailNode= node.getNode("productdetails");
                            LOG.info("Got Node");
                            HashMap<String, String> productDetailMap = new HashMap<>();
                            productDetailMap.put("title", productDetailNode.getProperty("productTitle").getString());
                            productDetailMap.put("description", productDetailNode.getProperty("productDescription").getString());
                            productDetailMap.put("image", productDetailNode.getProperty("fileReference").getString());
                            productDetailList.add(productDetailMap);

                             LOG.info("product Map-------" + productDetailMap);
                        }
                    }
            }
            LOG.info("Product Detail List : --------------" + productDetailList);

        } catch (Exception  e)  {
            e.printStackTrace();
        }
        return productDetailList;
    }

    // get product details from Page Properties
    @Override
    public ArrayList<HashMap<String, String>> getFilteredCards(String tag) {
        int noOfCards = 0;
        ArrayList<HashMap<String, String>> productDetailList = new ArrayList<>();
        try {
            final Map<String, Object > param = Collections.singletonMap(ResourceResolverFactory.SUBSERVICE, (Object) "getResourceResolver");
            resourceResolver = resourceResolverFactory.getServiceResourceResolver(param);
            Session session = resourceResolver.adaptTo(Session.class);
            LOG.info("Got Session ---" );

            Map predicateMap = new HashMap();
            predicateMap.put("path", "/content/onlineShopping/us/en/products");
            predicateMap.put("type", "cq:Page");
            predicateMap.put("property", "jcr:content/@cq:tags");
            predicateMap.put("property.value", tag);
            Query query = queryBuilder.createQuery(PredicateGroup.create(predicateMap), session);
            SearchResult result = query.getResult();
            noOfCards = result.getHits().size();
            LOG.info("No of product cards :----" + noOfCards);

            for (Hit currentPageResult : result.getHits()) {
                String path = currentPageResult.getPath();
                LOG.info("Path: " + path);
                Resource resource = resourceResolver.getResource(path);
                LOG.info("Got Resource : ");
                Node node = resource.adaptTo(Node.class);
                Node contentNode = node.getNode("jcr:content");
                Node thumbnailNode = contentNode.getNode("image");
                LOG.info("got details from JCR : ");
                //String imageThumbnailPath = thumbnailNode.getProperty("fileReference").getString();
                //String title = contentNode.getProperty("jcr:title").getString();
                //String description = contentNode.getProperty("jcr:description").getString();
                //Value[] tags = contentNode.getProperty("cq:tags").getValues();
                HashMap<String, String> productDetailMap = new HashMap<>();
                productDetailMap.put("title", contentNode.getProperty("jcr:title").getString());
                productDetailMap.put("description", contentNode.getProperty("jcr:description").getString());
                productDetailMap.put("image", thumbnailNode.getProperty("fileReference").getString());
                productDetailList.add(productDetailMap);
                LOG.info("MAP created :  ");
                //PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
                //Page page = pageManager.getContainingPage(resource);
                LOG.info("product Map-------" + productDetailMap);
            }
            LOG.info("Product Detail List : --------------" + productDetailList);

        } catch (Exception  e)  {
            e.printStackTrace();
        }
        return productDetailList;
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