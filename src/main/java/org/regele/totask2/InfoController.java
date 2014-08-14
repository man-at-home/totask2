package org.regele.totask2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/** web controller. */
@Controller
public class InfoController {
    
    private static final Logger LOG = LoggerFactory.getLogger(InfoController.class);

    @RequestMapping("/greeting")
    public String greeting(
            @RequestParam(value = "name", required = false, defaultValue = "World") String name,
            Model model) {
        
        LOG.debug("info(" + name + ")");
        model.addAttribute("name", name);
        return "greeting";
    }

}// class
