package com.adobe.aem.onlineshopping.core.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.osgi.service.metatype.annotations.Option;

@ObjectClassDefinition(name = "Online Shopping JSON API Configuration")
public @interface CommonConfiguration {

    @AttributeDefinition( name = "Protocol",description = "Choose Protocol",
            options = {
                    @Option(label = "HTTP", value = "http"), @Option(label = "HTTPS", value = "https") })
    public String getProtocol();

    @AttributeDefinition(
            name = "Server",
            description = "Enter the server name")
    public String getServer();

    @AttributeDefinition(
            name = "Endpoint",
            description = "Enter the endpoint")
    public String getEndpoint();
}

