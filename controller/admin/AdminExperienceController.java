package com.Siw.personalProject.controller.admin;

import com.Siw.personalProject.model.ExperienceEntity;
import com.Siw.personalProject.service.ExperienceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/experience")
public class AdminExperienceController {

    private final ExperienceService service;

    public AdminExperienceController(ExperienceService service) {
        this.service = service;
    }

   @GetMapping
public String list(Model model) {
    model.addAttribute("experiences", service.findAll());
    return "admin/list";  // punta al template list.html
}


    @GetMapping("/new")
public String createForm(Model model) {
    model.addAttribute("experience", new ExperienceEntity());
    return "admin/form";  // punta al template form.html
}


    @PostMapping
public String save(@ModelAttribute ExperienceEntity experience) {
    service.save(experience);
    return "redirect:/admin/experience"; // dopo il salvataggio torna alla lista
}


   @GetMapping("/edit/{id}")
public String editForm(@PathVariable Long id, Model model) {
    model.addAttribute("experience", service.findById(id));
    return "admin/form";  // usa lo stesso form.html
}


   @GetMapping("/delete/{id}")
public String delete(@PathVariable Long id) {
    service.delete(id);
    return "redirect:/admin/experience";
}

}
