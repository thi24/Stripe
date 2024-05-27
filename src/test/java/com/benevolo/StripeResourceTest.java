package com.benevolo;

import com.stripe.exception.StripeException;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class StripeResourceTest {
    @Test
    void testPaymentIntent()  {
        given()
                .when().post("/payment/payment-intent")
                .then()
                .statusCode(200)
                .body(is("{\"client_secret\": \"pi_1Jb2e4J9QF5b1e1b1e1b1e1b\", \"id\": \"pi_1Jb2e4J9QF5b1e1b1e1b1e1b\"}"));
    }
    @Test
    void testRefund() {
        given()
                .when().post("/payment/refund")
                .then()
                .statusCode(200)
                .body(is("{\"id\": \"re_1Jb2e4J9QF5b1e1b1e1b1e1b\"}"));
    }


}