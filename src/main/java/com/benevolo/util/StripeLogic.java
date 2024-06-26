package com.benevolo.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.RefundCreateParams;

public class StripeLogic {

    public PaymentIntentCreateParams createPaymentIntent(String payload) {
        if (payload == null || payload.isEmpty()) {
            return null;
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode requestData = mapper.readTree(payload);
            int amount = requestData.get("amount").asInt();
            String currency = requestData.get("currency").asText();
            String productDescription = requestData.get("product").asText();

            return PaymentIntentCreateParams.builder().setAmount((long) amount).setCurrency(currency).setDescription(productDescription).build();

        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public RefundCreateParams createRefund(String payload) throws JsonProcessingException {
        if(payload == null || payload.isEmpty()){
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        JsonNode requestData = mapper.readTree(payload);
        long amount = requestData.get("amount").asLong();
        String paymentIntentId = requestData.get("paymentIntent").asText();
        return RefundCreateParams.builder().setPaymentIntent(paymentIntentId).setAmount(amount).build();
    }
}