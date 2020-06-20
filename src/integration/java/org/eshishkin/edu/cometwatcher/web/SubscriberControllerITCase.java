package org.eshishkin.edu.cometwatcher.web;

import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.eshishkin.edu.cometwatcher.MongoTestResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

import static org.eshishkin.edu.cometwatcher.TestUtils.evaluate;
import static org.eshishkin.edu.cometwatcher.TestUtils.loadResource;
import static org.hamcrest.Matchers.is;
import static io.restassured.RestAssured.given;

@QuarkusTest
@QuarkusTestResource(MongoTestResource.class)
public class SubscriberControllerITCase {

    private String template;

    @BeforeEach
    public void before() {
        template = loadResource(
                "subscription_request_example.js",
                SubscriptionRequestWithInvalidInputITCase.class
        );
    }


    @Test
    public void testSuccessfulCreation() {
        String email = getEmail();

        create(email).statusCode(201);

        get(email).statusCode(200)
                .body("email", is(email))
                .body("observerLatitude", is("0"))
                .body("observerLongitude", is("0"))
                .body("interval", is("DAILY"))
                .body("observerTimeZone", is("UTC"));

        delete(email).statusCode(204);
    }

    @Test
    public void testDuplicateSubscriptions() {
        String email = getEmail();

        create(email).statusCode(201);

        get(email).statusCode(200)
                .body("email", is(email));

        create(email).statusCode(409);

        delete(email).statusCode(204);
    }

    @Test
    public void testAlreadyDeletedSubscription() {
        delete(getEmail()).statusCode(404);
    }

    private ValidatableResponse create(String email) {
        return given()
                .when()
                    .body(evaluate(template, Map.of("user", email)))
                    .contentType(ContentType.JSON)
                    .post("/subscribers")
                .then()
                    .log().all();
    }

    private ValidatableResponse get(String email) {
        return given()
                .when()
                    .accept(ContentType.JSON)
                    .get("/subscribers/" + email)
                .then()
                    .log().all();
    }

    private ValidatableResponse delete(String email) {
        return given()
                .when()
                    .log().all()
                    .delete("/subscribers/" + email)
                .then()
                    .log().all();
    }

    private String getEmail() {
        return RandomStringUtils.randomAlphabetic(10) + "@example.com";
    }
}
