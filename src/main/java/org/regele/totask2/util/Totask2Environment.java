package org.regele.totask2.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/** Naming and Environment Information (loaded from application.*.yml files). */
@Component
public class Totask2Environment {

    @Value("${app.name}")
    private String applicationName = null;

    @Value("${app.description}")
    private String applicationDescription = null;

    @Value("${app.version}")
    private String applicationVersion = null;

    /** application name. */
    public String getName() {
        return this.applicationName;
    }

    /** header description. */
    public String getDescription() {
        return this.applicationDescription;
    }

    /** version information. */
    public String getVersion() {
        return this.applicationVersion;
    }

    @Override
    public String toString() {
        return "Environment [applicationName=" + applicationName
                + ", applicationDescription=" + applicationDescription
                + ", applicationVersion=" + applicationVersion + "]";
    }

}
