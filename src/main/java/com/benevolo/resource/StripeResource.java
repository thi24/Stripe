package com.benevolo.resource;

import com.benevolo.util.StripeLogic;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.stripe.exception.StripeException;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;



@Produces(MediaType.APPLICATION_JSON)
@Path("/payment")
public class StripeResource {
   private static String stripeApiKey;
    @Inject
    public StripeResource(@ConfigProperty(name="STRIPE_API_KEY") String stripeSecret) {
        stripeApiKey = stripeSecret;
    }

    @POST
    @Path("/payment-intent")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createPaymentIntent(String payload) {
            StripeLogic logic = new StripeLogic(payload, stripeApiKey);
            return logic.createPaymentIntent();
    }
    @POST
    @Path("/refund")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createRefund(String payload) throws JsonProcessingException, StripeException {
            StripeLogic logic = new StripeLogic(payload, stripeApiKey);
            return logic.createRefund();
    }
}
