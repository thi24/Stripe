package com.benevolo.client;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.RefundCreateParams;

public class StripeClient {
    public PaymentIntent createPaymentIntent(PaymentIntentCreateParams param, String apiKey){
        try {
            Stripe.apiKey = apiKey;
            return PaymentIntent.create(param);
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }
    public void createRefund(RefundCreateParams param, String apiKey){
        try {
            Stripe.apiKey = apiKey;
            Refund.create(param);
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }
}
