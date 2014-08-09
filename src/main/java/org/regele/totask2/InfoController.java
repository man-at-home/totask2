package org.regele.totask2;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/** web controller. */
@Controller
public class InfoController {

    @RequestMapping("/greeting")
    public String greeting(
            @RequestParam(value = "name", required = false, defaultValue = "World") String name,
            Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }

}// class
