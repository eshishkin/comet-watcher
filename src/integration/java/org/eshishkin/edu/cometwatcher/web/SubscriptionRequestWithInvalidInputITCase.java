package org.eshishkin.edu.cometwatcher.web;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.eshishkin.edu.cometwatcher.TestUtils.evaluate;
import static org.eshishkin.edu.cometwatcher.TestUtils.loadResource;

@QuarkusTest
public class SubscriptionRequestWithInvalidInputITCase {
    private static final String ENDPOINT_SUBSCRIBERS = "/subscribers";

    private String template;

    @BeforeEach
    public void before() {
        template = loadResource(
                "subscription_request_example.js",
                SubscriptionRequestWithInvalidInputITCase.class
        );
    }

    @Test
    public void testCreateSubscription_EmptyRequest() {
        given()
                .when()
                    .body("{}")
                    .contentType(ContentType.JSON)
                    .post(ENDPOINT_SUBSCRIBERS)
                .then()
                    .statusCode(400);
    }

    @Test
    public void testCreateSubscription_InvalidEmail() {
        given()
                .when()
                    .body(evaluate(template, Map.of("user", "invalid_email")))
                    .contentType(ContentType.JSON)
                    .post(ENDPOINT_SUBSCRIBERS)
                .then()
                    .statusCode(400);
    }

    @Test
    @Disabled
    public void testCreateSubscription_LatitudeIsBiggerThan90() {
        given()
                .when()
                    .body(evaluate(template, Map.of("latitude", "90.05")))
                    .contentType(ContentType.JSON)
                    .post(ENDPOINT_SUBSCRIBERS)
                .then()
                    .statusCode(400);
    }

    @Test
    @Disabled
    public void testCreateSubscription_LatitudeIsLessThanMinus90() {
        given()
                .when()
                    .body(evaluate(template, Map.of("latitude", "-90.05")))
                    .contentType(ContentType.JSON)
                    .post(ENDPOINT_SUBSCRIBERS)
                .then()
                    .statusCode(400);
    }


    @Test
    public void testCreateSubscription_AltitudeIsBiggerThan90() {
        given()
                .when()
                    .body(evaluate(template, Map.of("altitude", 91)))
                    .contentType(ContentType.JSON)
                    .post(ENDPOINT_SUBSCRIBERS)
                .then()
                    .statusCode(400);
    }

    @Test
    public void testCreateSubscription_AltitudeIsLessThanMinus90() {
        given()
                .when()
                    .body(evaluate(template, Map.of("altitude", -91)))
                    .contentType(ContentType.JSON)
                    .post(ENDPOINT_SUBSCRIBERS)
                .then()
                    .statusCode(400);
    }


    @Test
    public void testCreateSubscription_UnsupportedInterval() {
        given()
                .when()
                    .body(evaluate(template, Map.of("interval", "TEST")))
                    .contentType(ContentType.JSON)
                    .post(ENDPOINT_SUBSCRIBERS)
                .then()
                    .statusCode(400);
    }
}
