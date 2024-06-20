package com.benevolo.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.ws.rs.core.Response;


public class StripeLogic {

    private String payload = "";
    private final String STRIPE_SECRET;
    public StripeLogic(String payload, String stripeSecret) {
        this.STRIPE_SECRET = stripeSecret;
        this.payload = payload;
    }


    public Response createPaymentIntent() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode requestData = mapper.readTree(payload);
            int amount = requestData.get("amount").asInt();
            String currency = requestData.get("currency").asText();
            String productDescription = requestData.get("product").asText();
            Stripe.apiKey = STRIPE_SECRET;

            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder().setAmount((long) amount).setCurrency(currency).setDescription(productDescription).build();
            PaymentIntent paymentIntent = PaymentIntent.create(params);
            String clientSecret = paymentIntent.getClientSecret();
            String id = paymentIntent.getId();
            String jsonResponse = "{\"client_secret\": \"4" + clientSecret + "\", \"id\": \"" + id + "\"}";
            return Response.ok().entity(jsonResponse).build();
        } catch (JsonProcessingException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Fehler beim Erstellen des PaymentIntent: " + e.getMessage()).build();
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }
}

