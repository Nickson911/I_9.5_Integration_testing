package com.druzanova.springbootconditional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringBootConditionalApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    private final static GenericContainer<?> containerDev = new GenericContainer<>("devapp")
            .withExposedPorts(8080);
    private final static GenericContainer<?> containerProd = new GenericContainer<>("prodapp")
            .withExposedPorts(8081);
    private static final String ENDPOINT = "/profile";

    @BeforeAll
    public static void setUp() {
        containerDev.start();
        containerProd.start();
    }

    @Test
    void contextLoadsDev() {
        Integer port = containerDev.getMappedPort(8080);

        ResponseEntity<String> actual = restTemplate.getForEntity("http://localhost:" +
                port + ENDPOINT, String.class);
        Assertions.assertEquals("Current profile is dev", actual.getBody());
    }

    @Test
    void contextLoadsProd() {
        Integer port = containerProd.getMappedPort(8081);

        ResponseEntity<String> actual = restTemplate.getForEntity("http://localhost:" +
                port + ENDPOINT, String.class);

        Assertions.assertEquals("Current profile is production", actual.getBody());
    }
}
