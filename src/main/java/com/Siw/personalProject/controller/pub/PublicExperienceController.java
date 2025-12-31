package com.Siw.personalProject.controller.pub;

import com.Siw.personalProject.service.ExperienceService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PublicExperienceController {

    public PublicExperienceController(ExperienceService service) {
    }

    @GetMapping("/experience")
    public String showExperience() {
        // Redirect to the home page anchor so HomeController loads the model and the browser scrolls to the experience section
        return "redirect:/#experience";
    }


}