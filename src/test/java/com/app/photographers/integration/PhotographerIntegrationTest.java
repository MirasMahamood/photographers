package com.app.photographers.integration;

import com.app.photographers.integration.config.TestRedisConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestRedisConfig.class)
@AutoConfigureMockMvc
public class PhotographerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String API_URL = "/api/v1/photographers";

    @Test
    public void blockUnauthorizedUsers() throws Exception {
        mockMvc.perform(post(API_URL)).andExpect(status().isUnauthorized());
        mockMvc.perform(get(API_URL)).andExpect(status().isUnauthorized());
        mockMvc.perform(get(API_URL + "/{id}", 1L)).andExpect(status().isUnauthorized());
        mockMvc.perform(get(API_URL + "/event/{id}", 1L)).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser("miras")
    void createPhotographerSuccessfully() throws Exception {
        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John Doe\",\"description\":\"A skilled photographer\",\"contact\":\"1234567890\",\"avatar\":\"https://example.com/avatar.jpg\",\"eventType\":{\"id\":1}}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    @WithMockUser("miras")
    void getPhotographerByIdSuccessfully() throws Exception {
        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John Doe\",\"description\":\"A skilled photographer\",\"contact\":\"1234567890\",\"avatar\":\"https://example.com/avatar.jpg\",\"eventType\":{\"id\":1}}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));
        mockMvc.perform(get(API_URL + "/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @WithMockUser("miras")
    void getAllPhotographersSuccessfully() throws Exception {
        mockMvc.perform(get(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.photographers").exists())
                .andExpect(jsonPath("$.totalItems").value(2))
                .andExpect(jsonPath("$.currentPage").value(0))
                .andExpect(jsonPath("$.hasNext").value(false));
    }

    @Test
    @WithMockUser("miras")
    void getAllPhotographersByEventTypeSuccessfully() throws Exception {
        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John Doe\",\"description\":\"A skilled photographer\",\"contact\":\"1234567890\",\"avatar\":\"https://example.com/avatar.jpg\",\"eventType\":{\"id\":2}}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventType.id").value(2L));

        mockMvc.perform(get(API_URL + "/event/{id}", 2L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalItems").value(1))
                .andExpect(jsonPath("$.currentPage").value(0))
                .andExpect(jsonPath("$.hasNext").value(false));
    }

    @Test
    @WithMockUser("miras")
    void createPhotographerWithNullName() throws Exception {
        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":null,\"description\":\"A skilled photographer\",\"contact\":\"1234567890\",\"avatar\":\"https://example.com/avatar.jpg\",\"eventType\":{\"id\":1}}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("[Name is required]"));
    }

    @Test
    @WithMockUser("miras")
    void createPhotographerWithLongName() throws Exception {
        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"" + "a".repeat(101) + "\",\"description\":\"A skilled photographer\",\"contact\":\"1234567890\",\"avatar\":\"https://example.com/avatar.jpg\",\"eventType\":{\"id\":1}}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("[Name should not be more than 100 characters]"));
    }

    @Test
    @WithMockUser("miras")
    void createPhotographerWithLongDescription() throws Exception {
        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"a\",\"description\":\"" + "a".repeat(1001) + "\",\"contact\":\"1234567890\",\"avatar\":\"https://example.com/avatar.jpg\",\"eventType\":{\"id\":1}}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("[Description should not be more than 1000 characters]"));
    }

    @Test
    @WithMockUser("miras")
    void createPhotographerWithInvalidContact() throws Exception {
        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John Doe\",\"description\":\"A skilled photographer\",\"contact\":\"" + "a".repeat(21) + "\",\"avatar\":\"https://example.com/avatar.jpg\",\"eventType\":{\"id\":1}}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("[Contact should not be more than 20 characters]"));
    }

    @Test
    @WithMockUser("miras")
    void createPhotographerWithMissingEventType() throws Exception {
        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John Doe\",\"description\":\"A skilled photographer\",\"contact\":\"1234567890\",\"avatar\":\"https://example.com/avatar.jpg\",\"eventType\":null}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("[Event type ID is required]"));
    }

    @Test
    @WithMockUser("miras")
    void createPhotographerWithEmptyContentThrowsException() throws Exception {
        mockMvc.perform(post(API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser("miras")
    void getPhotographerByIdNotFound() throws Exception {
        mockMvc.perform(get(API_URL + "/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Photographer not found"));
    }
}
