package com.postechhackaton.relatorios.application.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
@SpringBootTest
@TestPropertySource(properties = {
        "spring.application.name=TestApp",
        "spring.application.description=Test Description"
})
public class DocsConfigTest {

    @Value("${spring.application.name}")
    private String appName;

    @Value("${spring.application.description}")
    private String appDescription;

    @Mock
    private OpenAPI openAPI;

    @InjectMocks
    private DocsConfig docsConfig;

    @Test
    public void testCustomOpenAPI() {
        MockitoAnnotations.openMocks(this);

        ReflectionTestUtils.setField(docsConfig, "appName", "Test Title");
        ReflectionTestUtils.setField(docsConfig, "appDescription", "Test Description");


        OpenAPI customOpenAPI = docsConfig.customOpenAPI();

        // Asserts
        assertTrue(customOpenAPI.getInfo().getTitle().contains("Test Title"));
        assertEquals("Test Description", customOpenAPI.getInfo().getDescription());
        assertEquals("https://github.com/postech-hackaton-company-sa", customOpenAPI.getInfo().getContact().getUrl());
        assertEquals("MIT License", customOpenAPI.getInfo().getLicense().getName());
    }
}