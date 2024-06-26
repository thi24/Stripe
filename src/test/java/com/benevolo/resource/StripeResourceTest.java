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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.lang.reflect.Field;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
@QuarkusTest
public class StripeResourceTest {

    @Mock
    private StripeClient stripeClient;

    @Mock
    private StripeLogic stripeLogic;

    @BeforeEach
    public void setup() throws NoSuchFieldException, IllegalAccessException{
        MockitoAnnotations.openMocks(this);
        Field stripeApiKeyField = StripeResource.class.getDeclaredField("stripeApiKey");
        stripeApiKeyField.setAccessible(true);
        stripeApiKeyField.set(null, System.getenv("STRIPE_API_KEY"));
    }

    @Test
    public void testCreatePaymentIntent() throws JsonProcessingException {
        // Arrange

        String payload = "{\"amount\": 100, \"currency\": \"usd\", \"product\": \"test product\"}";
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
}