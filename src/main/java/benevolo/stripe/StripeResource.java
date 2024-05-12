package benevolo.stripe;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.RefundCreateParams;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.IOException;


@Produces(MediaType.APPLICATION_JSON)
@Path("/payment")
public class StripeResource {
    @POST
    @Path("/create-payment-intent")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createPaymentIntent(String payload) {
        try {

            // Deserialisierung des JSON-Payloads
            ObjectMapper mapper = new ObjectMapper();
            JsonNode requestData = mapper.readTree(payload);

            // Extrahieren der erforderlichen Informationen aus dem Payload
            int amount = requestData.get("amount").asInt();
            String currency = requestData.get("currency").asText();
            String productDescription = requestData.get("product").asText();


            Stripe.apiKey = "sk_test_51P3MkWCzSI00rA1V3Jz9JRoRABinh5Kg8lalcChpg75yHagHchVlMV4dw8FQnjsd0pMNI56DH4GI87fs7T5xCUF100tEoZu7dP";

            // Erstellen der PaymentIntent-Parameter
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder().setAmount((long) amount).setCurrency(currency).setDescription(productDescription).build();

            // Erstellen des PaymentIntent
            PaymentIntent paymentIntent = PaymentIntent.create(params);
            String clientSecret = paymentIntent.getClientSecret();
            String id = paymentIntent.getId();
            String jsonResponse = "{\"client_secret\": \"" + clientSecret + "\", \"id\": \"" + id + "\"}";


            return Response.ok().entity(jsonResponse).build();
        } catch (IOException | StripeException e) {
            // Rückgabe einer fehlerhaften Antwort im Falle eines Fehlers
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Fehler beim Erstellen des PaymentIntent: " + e.getMessage()).build();
        }
    }
    @POST
    @Path("/create-refund")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createRefund(String payload) throws JsonProcessingException, StripeException {
        try {
            Stripe.apiKey = "sk_test_51P3MkWCzSI00rA1V3Jz9JRoRABinh5Kg8lalcChpg75yHagHchVlMV4dw8FQnjsd0pMNI56DH4GI87fs7T5xCUF100tEoZu7dP";
            ObjectMapper mapper = new ObjectMapper();
            JsonNode requestData = mapper.readTree(payload);
            String paymentIntentId = requestData.get("paymentIntent").asText();

            RefundCreateParams params = RefundCreateParams.builder().setPaymentIntent(paymentIntentId).build();
            Refund refund = Refund.create(params);
            return Response.ok().entity(refund.getId()).build();
        }catch(StripeException e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Fehler beim erstellen der Rueckerstattung").build();
        }
    }
}
