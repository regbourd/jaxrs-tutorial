package fr.mickaelbaron.jaxrstutorialexercice3;

import java.util.List;
import java.util.logging.Level;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Mickael BARON (baron.mickael@gmail.com)
 */
public class TrainBookingResourceIntegrationTest extends JerseyTest{
    
    @Override
    protected Application configure() {
        ResourceConfig resourceConfig = new ResourceConfig(TrainBookingResource.class);
        resourceConfig.property(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL_SERVER, Level.WARNING.getName());
        return resourceConfig;
    }
    
    @Test
    public void createTrainBookingTest() {
        // Given
        TrainBooking trainBooking = new TrainBooking();
        trainBooking.setNumberPlaces(3);
        trainBooking.setTrainId("TR123");

        // When
        Response response = target("/trains/bookings").request(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(trainBooking));

        // Then
        Assert.assertEquals("Http Response should be 200: ", Status.OK.getStatusCode(), response.getStatus());
    }
    
    @Test
    public void createTrainBookingWithBadTrainIdTest() {
        // Given
        TrainBooking trainBooking = new TrainBooking();
        trainBooking.setNumberPlaces(3);
        trainBooking.setTrainId("BADTR123");
        
        // When
        Response response = target("/trains/bookings").request(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(trainBooking));

        // Then
        Assert.assertEquals("Http Response should be 404: ", Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }
    
    @Test
    public void getTrainBookingsTest() {
        // Given
        TrainBooking currentTrainBooking = createTrainBooking("TR123", 3);

        // When
        Response response = target("/trains/bookings").request(MediaType.APPLICATION_JSON_TYPE).get();
        
        List<TrainBooking> entities = response.readEntity(new GenericType<List<TrainBooking>>() {});
        
        // Then
        Assert.assertEquals("Http Response should be 200: ", Status.OK.getStatusCode(), response.getStatus());
        Assert.assertEquals(1, entities.size());
        Assert.assertTrue(entities.stream().anyMatch(current -> "TR123".equals(current.getTrainId())));
    }
    
    @Test
    public void getTrainBookingTest() {
        // Given
        TrainBooking currentTrainBooking = createTrainBooking("TR123", 3);

        // When
        Response response = target("/trains/bookings").path(currentTrainBooking.getId()).request(MediaType.APPLICATION_JSON_TYPE).get();

        // Then
        Assert.assertEquals("Http Response should be 200: ", Status.OK.getStatusCode(), response.getStatus());
    }
    
    @Test
    public void getTrainBookingWithBadTrainBookingIdTest() {
        // Given
        String trainBookingId = "FAKETRAINBOOKINGID";

        // When
        Response response = target("/trains/bookings").path(trainBookingId).request(MediaType.APPLICATION_JSON_TYPE).get();


        // Then
        Assert.assertEquals("Http Response should be 404: ", Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }
    
    @Test
    public void removeTrainBookingTest() {
        // Given
        TrainBooking currentTrainBooking = createTrainBooking("TR123", 3);

        // When
        Response response = target("/trains/bookings").path(currentTrainBooking.getId()).request(MediaType.APPLICATION_JSON_TYPE).delete();

        // Then
        Assert.assertEquals("Http Response should be 204: ", Status.NO_CONTENT.getStatusCode(), response.getStatus());
    }
    
    @Test
    public void removeTrainBookingWithBadTrainBookingIdTest() {
        // Given
        String trainBookingId = "FAKETRAINBOOKINGID";

        // When
        Response response = target("/trains/bookings").path(trainBookingId).request(MediaType.APPLICATION_JSON_TYPE).delete();

        // Then
        Assert.assertEquals("Http Response should be 404: ", Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }
		
	private TrainBooking createTrainBooking(String trainId, int numberPlaces) {
		TrainBooking trainBooking = new TrainBooking();
		trainBooking.setNumberPlaces(numberPlaces);
		trainBooking.setTrainId(trainId);
		Response response = target("/trains/bookings").request(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(trainBooking));
		Assert.assertEquals("Http Response should be 200: ", Status.OK.getStatusCode(), response.getStatus());
				
		return response.readEntity(TrainBooking.class);
	}
}
