package com.benevolo.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.RefundCreateParams;

import jakarta.ws.rs.core.Response;


public class StripeLogic {
    private String payload = "";
    public StripeLogic(String payload, String stripeSecret) {
        Stripe.apiKey = stripeSecret;
        this.payload = payload;
    }
    public Response createPaymentIntent() {
        if(this.payload == null || this.payload.isEmpty()) {
            return Response.noContent().build();
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode requestData = mapper.readTree(payload);
            int amount = requestData.get("amount").asInt();
            String currency = requestData.get("currency").asText();
            String productDescription = requestData.get("product").asText();

            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder().setAmount((long) amount).setCurrency(currency).setDescription(productDescription).build();
            PaymentIntent paymentIntent = PaymentIntent.create(params);
            String clientSecret = paymentIntent.getClientSecret();
            String id = paymentIntent.getId();
            String jsonResponse = "{\"client_secret\": \"" + clientSecret + "\", \"id\": \"" + id + "\"}";
            return Response.ok().entity(jsonResponse).build();
        } catch (JsonProcessingException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Fehler beim Erstellen des PaymentIntent: " + e.getMessage()).build();
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }
    public  Response createRefund() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode requestData = mapper.readTree(this.payload);
        long amount = requestData.get("amount").asLong();
        String paymentIntentId = requestData.get("paymentIntent").asText();
        try {
            RefundCreateParams params = RefundCreateParams.builder().setPaymentIntent(paymentIntentId).setAmount(amount).build();
            Refund.create(params);
            return Response.noContent().build();
        } catch (StripeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Fehler beim erstellen der Rueckerstattung").build();
        }
    }
}