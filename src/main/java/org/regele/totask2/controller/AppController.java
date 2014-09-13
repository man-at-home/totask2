package org.regele.totask2.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


/** main menu. */
@Controller
public class AppController {

    private static final Logger LOG = LoggerFactory.getLogger(AppController.class);
 
    /** landing page. */
    @RequestMapping("/")
    public String index() {        
        LOG.debug("index");
        return "index";
    }
        
}