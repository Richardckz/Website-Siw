package com.Siw.personalProject.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributes {

    @Value("${plausible.enabled:false}")
    private boolean plausibleEnabled;

    @Value("${plausible.domain:}")
    private String plausibleDomain;

    @ModelAttribute("plausibleEnabled")
    public boolean plausibleEnabled() {
        return plausibleEnabled;
    }

    @ModelAttribute("plausibleDomain")
    public String plausibleDomain() {
        return plausibleDomain;
    }
}
