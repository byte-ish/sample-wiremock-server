package org.example;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        // Set up WireMock server on port 8080 and enable templating
        // Set up WireMock server on port 8080 with response templating enabled
        WireMockServer wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig()
                .port(8080)
                .globalTemplating(true));


        wireMockServer.start();

        // Define a stub for the ML endpoint with CORS headers
        wireMockServer.stubFor(post(urlEqualTo("/api/predict"))
                .withHeader("Content-Type", equalTo("application/json"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withHeader("Access-Control-Allow-Origin", "*")
                        .withHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS")
                        .withHeader("Access-Control-Allow-Headers", "Content-Type, Authorization")
//                        .withBody("{ \"prediction\": \"{{jsonPath request.body '$.description'}}\" }")));  // Correct JSONPath templating
                        .withBody("{ \"prediction\": \"Prediction from the ML Model: {{jsonPath request.body '$.description'}}\" }")));  // Concatenate "Prefix: " with the description

        wireMockServer.stubFor(options(urlEqualTo("/api/predict"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Access-Control-Allow-Origin", "*")
                        .withHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS")
                        .withHeader("Access-Control-Allow-Headers", "Content-Type, Authorization")));

        System.out.println("WireMock server started on port 8080 with stubbed response.");
    }
}
