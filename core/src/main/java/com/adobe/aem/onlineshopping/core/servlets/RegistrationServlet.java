package com.adobe.aem.onlineshopping.core.servlets;

import com.adobe.aem.onlineshopping.core.services.RegistrationService;
import com.day.cq.commons.jcr.JcrConstants;
import com.google.gson.JsonObject;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestParameter;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.commons.json.jcr.JsonJcrNode;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;

@Component(service = { Servlet.class },
        property = { Constants.SERVICE_DESCRIPTION + "=Registration Servlet",
                "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                "sling.servlet.paths=" + "/bin/registerdata" })
public class RegistrationServlet extends SlingAllMethodsServlet {
    private static final long serialVersionUID = 1L;
    public static  final Logger LOG= LoggerFactory.getLogger(RegistrationServlet.class);

    @Reference
    RegistrationService registrationService;

    @Override
    protected void doGet(final SlingHttpServletRequest req,
                          final SlingHttpServletResponse resp) throws ServletException, IOException {
        LOG.info("-----LOGGER Started----------------");
        List<RequestParameter> inputParams = req.getRequestParameterList();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", "Deepali");
        LOG.info("JSON Object : ");
        String abc = jsonObject.getAsString();
        LOG.info("abc is : " + abc);
        JSONObject responseMsg = new JSONObject();
        try {
            JSONObject jsonRequestParams = getRequestAsJsonObject(inputParams, req);
            LOG.info("Print JSON Request" + jsonRequestParams);
            Boolean isValidAge = registrationService.checkAge(jsonRequestParams.getString("dob"));

            if(isValidAge) {
                responseMsg = registrationService.storeData(jsonRequestParams);
                LOG.info("Data saved successfully and response received !");
            }
            else
                resp.getWriter().write(responseMsg.toString());

            resp.setContentType("text/plain");
            resp.getWriter().write(responseMsg.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
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




