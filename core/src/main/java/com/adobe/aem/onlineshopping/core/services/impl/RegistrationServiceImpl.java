package com.adobe.aem.onlineshopping.core.services.impl;

import com.adobe.aem.onlineshopping.core.services.RegistrationService;
import org.apache.sling.api.resource.*;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.*;
import java.text.ParseException;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

@Component(service = RegistrationService.class, immediate = true)
public class RegistrationServiceImpl implements RegistrationService {

    public static  final Logger LOG= LoggerFactory.getLogger(RegistrationService.class);

    @Reference
    ResourceResolverFactory resourceResolverFactory;

    ResourceResolver resourceResolver = null;
    Session session;

    @Override
    public Boolean checkAge(String dobString) {
        LOG.info("Hello from Service  " + dobString);
        Boolean validAge= false;
        try {
            Date dob=new SimpleDateFormat("yyyy-MM-dd").parse(dobString);
            Date currentDate = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(LocalDateTime.now()));
            LOG.info("input date = " +dob + " current Date = " +currentDate );
            //Period.between(date1, currentDate).getYears();
            LocalDate dob1 = dob.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate currentDate1 = currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            int age = Period.between(dob1, currentDate1).getYears();
            LOG.info("User's age is : " +age );
            if (age>=15)
                validAge = true;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return validAge;
    }


    @Override
    public JSONObject storeData(JSONObject inputData){
        LOG.info("Store Data Starts");
        JSONObject jsonResponse = new JSONObject();
        try {
            final Map<String, Object > param = Collections.singletonMap(ResourceResolverFactory.SUBSERVICE, (Object) "getResourceResolver");
            resourceResolver = resourceResolverFactory.getServiceResourceResolver(param);
            Resource resource = resourceResolver.getResource("/content/onlineShopping");
            Node resourceNode = null;
            if(resource !=null) {
                LOG.info(" Resource Found !");
                resourceNode = resource.adaptTo(Node.class);
            }
            else{
                jsonResponse.put("500", "No resource Found");
                return  jsonResponse;
            }
            if(resourceNode.hasNode("registrationData"))
            {
                LOG.info("Node Already present");
                Node regNode =resourceNode.getNode("registrationData");
                regNode.remove();
                LOG.info("Node Removed");
            }

            Node registrationNode = resourceNode.addNode("registrationData", "nt:unstructured");
            LOG.info("Added new Node Now");
            Iterator<String> itr = inputData.keys();
            while(itr.hasNext())
            {
                String key = itr.next();
                LOG.info("---proprties : " + key, (String) inputData.get(key));
                registrationNode.setProperty(key, (String) inputData.get(key));
            }
            resourceResolver.commit();
            LOG.info("Data saved succeefully");
            jsonResponse.put("200", "You have Registered Successfully");
        } catch (LoginException | RepositoryException e)  {
            e.printStackTrace();
        } catch (PersistenceException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonResponse;
    }


}
