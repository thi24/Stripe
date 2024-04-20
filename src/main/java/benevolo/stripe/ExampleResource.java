package benevolo.stripe;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.net.RequestOptions;
import com.stripe.param.ChargeCreateParams;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.IOException;

@Path("/hello")
public class ExampleResource {



    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello from Quarkus REST";
    }
    @POST
    @Path("/charge")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createCharge(String payload) {
        try {
            // Deserialisierung des JSON-Payloads
            ObjectMapper mapper = new ObjectMapper();
            JsonNode requestData = mapper.readTree(payload);

            // Extrahieren der erforderlichen Informationen aus dem Payload
            String token = requestData.get("token").asText();
            int amount = requestData.get("amount").asInt();
            String currency = requestData.get("currency").asText();

            // Setzen Sie Ihren Stripe-API-Schlüssel
            Stripe.apiKey = "sk_test_51P3MkWCzSI00rA1V3Jz9JRoRABinh5Kg8lalcChpg75yHagHchVlMV4dw8FQnjsd0pMNI56DH4GI87fs7T5xCUF100tEoZu7dP";

            // Erstellen der Charge-Parameter
            ChargeCreateParams params = ChargeCreateParams.builder()
                    .setAmount(Long.valueOf(amount))
                    .setCurrency(currency)
                    .setSource(token)
                    .build();

            // Erstellen der Charge
            Charge charge = Charge.create(params);

            // Rückgabe einer erfolgreichen Antwort
            return Response.ok().entity("Zahlung erfolgreich abgeschlossen. Charge ID: " + charge.getId()).build();
        } catch (IOException | StripeException e) {
            // Rückgabe einer fehlerhaften Antwort im Falle eines Fehlers
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Fehler beim Verarbeiten der Zahlung: " + e.getMessage()).build();
        }
    }

}
