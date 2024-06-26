package com.benevolo.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.RefundCreateParams;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    @Test
    public void testCreatePaymentIntentNullPayload(){
        String payload = null;
        PaymentIntentCreateParams params  = stripeLogic.createPaymentIntent(payload);

        assertEquals(null, params);
    }
    @Test
    public void testCreatePaymentIntentInvalidPayload(){
        String payload = "{\"amount\": \"100\", \"currency\": \"usd\", \"product\": \"test product\"}";
        PaymentIntentCreateParams params  = stripeLogic.createPaymentIntent(payload);

        assertTrue(params.toString().contains("com.stripe.param.PaymentIntentCreateParams"));
    }
    @Test
    public void testCreateRefund() throws JsonProcessingException {
        String payload = "{\"amount\": 100, \"paymentIntent\": \"pi_123\"}";
        RefundCreateParams params = stripeLogic.createRefund(payload);

        assertEquals(100, params.getAmount());
        assertEquals("pi_123", params.getPaymentIntent());
    }
    @Test
    public void testCreateRefundEmptyPayload() throws JsonProcessingException {
        String payload = "";
        RefundCreateParams params = stripeLogic.createRefund(payload);

        assertTrue(params == null);
    }
    @Test
    public void testCreateRefundInvalidPayload() throws JsonProcessingException {
        String payload = "{\"amount\": \"100\", \"paymentIntent\": \"pi_123\"}";
        RefundCreateParams params = stripeLogic.createRefund(payload);

        assertTrue(params.toString().contains("com.stripe.param.RefundCreateParams"));
    }
}