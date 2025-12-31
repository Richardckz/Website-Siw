package com.Siw.personalProject;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class SecurityIntegrationTest {
        

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private InMemoryUserDetailsManager userDetailsManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void anonymousIndexShouldReturn200() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Chukwu Richard - Portfolio")));
    }

    @Test
    public void adminLoginWithRedirectShouldGoToAdminDashboard() throws Exception {
        // admin user is configured in UserConfig with username "admin" and password "admin123"
        mockMvc.perform(post("/login")
                .param("username", "admin")
                .param("password", "admin123")
                .param("redirect", "/admin/dashboard"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/dashboard"));
    }

    @Test
    public void nonAdminCannotRedirectToAdmin() throws Exception {
        // Create a simple non-admin user for this test
        if (!userDetailsManager.userExists("user")) {
            userDetailsManager.createUser(
                    User.withUsername("user")
                            .password(passwordEncoder.encode("userpass"))
                            .roles("USER")
                            .build()
            );
        }

        mockMvc.perform(post("/login")
                .param("username", "user")
                .param("password", "userpass")
                .param("redirect", "/admin/dashboard"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    public void staticResourcesArePublic() throws Exception {
        // CSS
        mockMvc.perform(get("/css/style.css"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("text/css"));

        // JS
        mockMvc.perform(get("/js/index.js"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("text/javascript"));

        // Library CSS
        mockMvc.perform(get("/libraries/bootstrap.min.css"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("text/css"));
    }

    @Test
    public void indexHasNavAndLockAria() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("role=\"navigation\"")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("aria-label=\"Admin login\"")));
    }
}
