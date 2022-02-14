package com.adobe.aem.onlineshopping.core.servlets;

import com.adobe.aem.onlineshopping.core.services.ProductService;
import com.adobe.aem.onlineshopping.core.services.UtilityService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.json.JSONObject;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

@Component(service = { Servlet.class },
        property = { Constants.SERVICE_DESCRIPTION + "= Product Servlet",
                "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                "sling.servlet.paths=" + "/bin/getProducts" })
public class ProductsServlet extends SlingSafeMethodsServlet {
    private static final long serialVersionUID = 1L;
    public static  final Logger LOG= LoggerFactory.getLogger(ProductsServlet.class);

    @Reference
    ProductService productService;

    @Override
    protected void doGet(final SlingHttpServletRequest req,
                         final SlingHttpServletResponse resp) throws ServletException, IOException {

        try {
            LOG.info("Calling getProducts Service ------");

            /*
            String stringtoCOnvert = "{\"name\":\"John\", \"age\":21 , \"place\":\"Nevada\"}";
            LOG.info("---------------> String to convert ----------  " + stringtoCOnvert);
            JSONObject json = utilityService.getJsonObject(stringtoCOnvert);
             */

            JSONObject json = productService.getProductsData();
            resp.setContentType("text/plain");
            resp.getWriter().write(String.valueOf(json));

        }
        catch (Exception e)
        {

        }
    }
}
