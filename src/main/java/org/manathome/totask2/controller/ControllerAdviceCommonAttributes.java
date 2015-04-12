package org.manathome.totask2.controller;

import org.manathome.totask2.util.Totask2Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * common environment info for all html templates.
 * 
 * adds some commonly needed info to spring data model usable from all html templates.
 * 
 * <ul>
 *  <li>isEnvironmentHeroku</li>
 *  <li>isEnvironmentLocal</li>
 *  <li>isEnvironmentOpenshift</li>
 *  <li>environmentMode</li>
 * </ul>
 * 
 * @author man-at-home
 * @since  2015-04-12
 */
@ControllerAdvice
public class ControllerAdviceCommonAttributes {
 
    private static final Logger LOG = LoggerFactory.getLogger(ControllerAdviceCommonAttributes.class);
    
    private String envTotask2Mode = "UNKNOWN";
    
    @Autowired 
    public ControllerAdviceCommonAttributes(Environment env) {
        envTotask2Mode = env.getProperty(Totask2Constants.ENV_TOTASK2_MODE, "UNKNOWN");
        LOG.debug("injecting common attributes to models, "
                + " Mode="   + envTotask2Mode 
                + " Heroku=" + "HEROKU".equalsIgnoreCase(this.envTotask2Mode) 
                + " Local="  + "LOCAL".equalsIgnoreCase(this.envTotask2Mode)
              );
    }
    
    /** add common info attributes to all models. */
    @ModelAttribute
    public void populateModel(Model model) {
        
        model.addAttribute("isEnvironmentHeroku",       "HEROKU".equalsIgnoreCase(this.envTotask2Mode));        
        model.addAttribute("isEnvironmentLocal",        "LOCAL".equalsIgnoreCase(this.envTotask2Mode));  
        model.addAttribute("isEnvironmentOpenshift",    "OPENSHIFT".equalsIgnoreCase(this.envTotask2Mode));  
        model.addAttribute("environmentMode",           this.envTotask2Mode);     
    }

}
