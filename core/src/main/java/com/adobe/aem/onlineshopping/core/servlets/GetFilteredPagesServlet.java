package com.adobe.aem.onlineshopping.core.servlets;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestParameter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component(service = { Servlet.class },
        property = { Constants.SERVICE_DESCRIPTION + "= Get Filtered Content Servlet",
                "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                "sling.servlet.paths=" + "/bin/filterContent" })
public class GetFilteredPagesServlet extends SlingAllMethodsServlet {
    private static final long serialVersionUID = 1L;
    public static  final Logger LOG= LoggerFactory.getLogger(GetFilteredPagesServlet.class);

    @Reference
    QueryBuilder queryBuilder;

    @Override
    protected void doGet(final SlingHttpServletRequest req,
                         final SlingHttpServletResponse resp) throws ServletException, IOException {
        List<RequestParameter> inputParams = req.getRequestParameterList();
        JSONObject jsonRequestParams = getRequestAsJsonObject(inputParams, req);
        ResourceResolver resourceResolver = req.getResourceResolver();
        //String namespace = "hcp-site";
        //String category = req.getParameter("category");
        Session session = req.getResourceResolver().adaptTo(Session.class);
        int hitsPerPage=0;
        try {
            Map predicateMap = new HashMap();
            predicateMap.put("path", "/content/onlineShopping/us/en/products");
            predicateMap.put("type", "cq:Page");
            predicateMap.put("property", "jcr:content/@cq:tags");

            predicateMap.put("property.value", jsonRequestParams.getString("tag"));

            Query query = queryBuilder.createQuery(PredicateGroup.create(predicateMap), session);
            SearchResult result = query.getResult();
            hitsPerPage = result.getHits().size();
            LOG.info("Search Result pages in new Project: --------------");

            for (Hit currentPageResult : result.getHits()) {
                String path = currentPageResult.getPath();
                //ValueMap currentPageValues = currentPageResult.getProperties();
                //String pageTitle = currentPageValues.get("jcr:content/jcr:title", " ");
                Resource resource = resourceResolver.getResource(path);
                Node node = resource.adaptTo(Node.class);
                Node contentNode = node.getNode("jcr:content");

                String title = contentNode.getProperty("jcr:title").getString();
                String description = contentNode.getProperty("jcr:description").getString();
                Value[] tags = contentNode.getProperty("cq:tags").getValues();
                String parentTag = "";
                String childTags = "";
                int count = 0;
                for (Value value : tags) {
                    if(count ==0)
                        parentTag = value.getString();
                    else
                        childTags += value.getString()+ ",";
                    count++;
                }
                Node thumbnailNode = contentNode.getNode("image");
                String imageThumbnailPath = thumbnailNode.getProperty("fileReference").getString();

                ValueMap properties = resource.getValueMap();
                //PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
                //Page page = pageManager.getContainingPage(resource);
                LOG.info("path: "+path + "--parentTag: " +parentTag + "-- childTag" +childTags);
                LOG.info("title: "+title + " ---description: "+description +"-- image: "+imageThumbnailPath);


            }
        }
        catch (RepositoryException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        resp.setContentType("text/plain");
        resp.getWriter().write(String.valueOf(hitsPerPage));
    }

    public JSONObject getRequestAsJsonObject(List<RequestParameter> requestParameterList, SlingHttpServletRequest request)
    {
        JSONObject jsonRequestParams = new JSONObject();
        try {
            for ( RequestParameter param: requestParameterList) {
                jsonRequestParams.put(param.getName(), request.getParameter(param.getName()));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  jsonRequestParams;
    }
}
