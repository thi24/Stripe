package com.benevolo.resource;

import com.benevolo.client.StripeClient;
import com.benevolo.util.StripeLogic;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;

import static org.hamcrest.Matchers.startsWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@QuarkusTest
class StripeResourceTest {

    @Mock
    private StripeClient stripeClient;

    @Mock
    private StripeLogic stripeLogic;

    @BeforeEach
    void setup() throws NoSuchFieldException, IllegalAccessException {
        MockitoAnnotations.openMocks(this);
        Field stripeApiKeyField = StripeResource.class.getDeclaredField("stripeApiKey");
        stripeApiKeyField.setAccessible(true);
        stripeApiKeyField.set(null, System.getenv("STRIPE_API_KEY"));
    }

    @Test
    void testCreatePaymentIntent() throws JsonProcessingException {
        // Arrange

        String payload = "{\"amount\": 100, \"currency\": \"eur\", \"product\": \"test product\"}";
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder().setAmount(100L).setCurrency("usd").setDescription("test product").build();
        PaymentIntent paymentIntent = new PaymentIntent();
        paymentIntent.setClientSecret("client_secret");
        paymentIntent.setId("id");

        when(stripeLogic.createPaymentIntent(any(String.class))).thenReturn(params);
        when(stripeClient.createPaymentIntent(any(PaymentIntentCreateParams.class), any(String.class))).thenReturn(paymentIntent);
        // Act and Assert
        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(payload)
                .when()
                .post("/payment/payment-intent")
                .then()
                .statusCode(200)
                .body("client_secret", startsWith("pi_"));

    }

    @Test
    void testCreateRefund() throws JsonProcessingException {
        // Arrange
        String payload = "{\"amount\": 1, \"paymentIntent\": \"pi_3PVClhCzSI00rA1V1r3H6eqd\"}";
        when(stripeLogic.createRefund(any(String.class))).thenReturn(null);
        // Act and Assert
        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(payload)
                .when()
                .post("/payment/refund")
                .then()
                .statusCode(204);
    }

    @Test
    void testCreateRefundWithEmptyPayload() {
        // Arrange
        String payload = "";
        // Act and Assert
        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(payload)
                .when()
                .post("/payment/refund")
                .then()
                .statusCode(400);
    }
}