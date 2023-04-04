package com.example.courseapi.config;

import ch.qos.logback.core.PropertyDefinerBase;
import ch.qos.logback.core.spi.PropertyDefiner;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;

@Log4j2
public class ServerPathPropertyDefiner extends PropertyDefinerBase implements PropertyDefiner {

    @Override
    public String getPropertyValue() {

        final String property = StringUtils.trim(System.getProperty("server.path"));

        if (StringUtils.isEmpty(property)) {
            log.warn("The property server.path is not defined");
            return ".";
        }

        return property;
    }
}