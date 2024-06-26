package com.benevolo.util;

import com.stripe.param.PaymentIntentCreateParams;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class StripeLogicTest {

    StripeLogic stripeLogic;

    @BeforeEach
    public void Setup() {
        stripeLogic = new StripeLogic();
    }
    @Test
    public void testCreatePaymentIntent(){
        String payload = "{\"amount\": 100, \"currency\": \"usd\", \"product\": \"test product\"}";
        PaymentIntentCreateParams params  = stripeLogic.createPaymentIntent(payload);

        assertEquals(100, params.getAmount());
        assertEquals("usd", params.getCurrency());
        assertEquals("test product", params.getDescription());
    }
    @Test
    public void testCreatePaymentIntentEmptyPayload(){
        String payload = "";
        PaymentIntentCreateParams params  = stripeLogic.createPaymentIntent(payload);

        assertEquals(null, params);
    }

}