package org.eshishkin.edu.cometwatcher.web;


import org.eshishkin.edu.cometwatcher.MongoTestResource;
import org.eshishkin.edu.cometwatcher.VaultTestResource;
import org.junit.jupiter.api.Test;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;

@QuarkusTest
@QuarkusTestResource(MongoTestResource.class)
@QuarkusTestResource(VaultTestResource.class)
public class MaintenanceControllerITCase {

    @Test
    public void testRun_NoAuthData() {
        given()
            .when()
                .contentType(ContentType.JSON)
                .post("/maintenance/run")
            .then()
                .log().all()
                .statusCode(401);
    }

    @Test
    public void testRun_IncorrectPassword() {
        given()
            .when()
                .contentType(ContentType.JSON)
                .auth().basic("admin", "super_password")
                .post("/maintenance/run")
            .then()
                .log().all()
                .statusCode(403);
    }

    @Test
    public void testRun_IncorrectRole() {
        given()
            .when()
                .contentType(ContentType.JSON)
                .auth().basic("user", "qwerty")
                .post("/maintenance/run")
            .then()
                .log().all()
                .statusCode(403);
    }

    @Test
    public void testRun_Success() {
        given()
            .when()
                .contentType(ContentType.JSON)
                .auth().basic("admin", "qwerty")
                .post("/maintenance/run")
            .then()
                .log().all()
                .statusCode(200);
    }
}
