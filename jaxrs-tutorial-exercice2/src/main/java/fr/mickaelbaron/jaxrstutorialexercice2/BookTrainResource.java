package fr.mickaelbaron.jaxrstutorialexercice2;

import java.util.List;
import java.util.Optional;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("bookings")
@Consumes({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
public class BookTrainResource {
    
    public BookTrainResource() {}
    
    @POST
    @Path("{trainId}/{numberPlaces}")
    public Response createTrainBooking(@PathParam("trainId") String trainId, @PathParam("numberPlaces") int numberPlaces) {
        TrainBooking trainBooking = null;
               
        while(trainBooking == null) {
            int noIdResa = (int) (Math.random() * 1000);
            trainBooking = TrainBookingDB.getTrainBookings().stream()
                            .filter(x -> String.valueOf(noIdResa).equals(x.getId()))
                            .findAny()
                            .orElse(null);
            
            if(trainBooking == null) {
                trainBooking= new TrainBooking();
                trainBooking.setNumberPlaces(numberPlaces);
                trainBooking.setTrainId(trainId);
                trainBooking.setId(String.valueOf(noIdResa));
            } else {
                trainBooking = null;                
            }
        } 
        
        TrainBookingDB.getTrainBookings().add(trainBooking);
        return Response.status(Response.Status.OK).entity(trainBooking).build();
        
    }
    
    @POST
    public Response createTrainBooking(TrainBooking trainBooking) {
        
        Optional<TrainBooking> findTrainBookingId = TrainBookingDB.getTrainBookingById(trainBooking.getId());        
        
        if (findTrainBookingId.isPresent()) {
            TrainBookingDB.getTrainBookings().add(trainBooking);
            return Response.status(Response.Status.OK).entity(trainBooking).build();
        } else {
            throw new NotFoundException();
        }  
        
        
        
    }
    
    @GET
    public List<TrainBooking> getTrainBookings(){
        return TrainBookingDB.getTrainBookings();
        
    }
    
    @GET
    @Path("/{trainBookingId}")
    public TrainBooking getTrainBooking(@PathParam("trainBookingId") String trainBookingId) {
        
        Optional<TrainBooking> findTrainBookingId = TrainBookingDB.getTrainBookingById(trainBookingId);
        
        if (findTrainBookingId.isPresent()) {
            return findTrainBookingId.get();
        } else {
            throw new NotFoundException();
        }     
       
    }
    
    @DELETE
    @Path("/{trainBookingId}")
    public void removeBookTrain(@PathParam("trainBookingId") String trainBookingId) {
        Optional<TrainBooking> findTrainBookingId = TrainBookingDB.getTrainBookingById(trainBookingId);       
        
        if(findTrainBookingId.isPresent()) {
            TrainBookingDB.getTrainBookings().remove(findTrainBookingId.get());
        } else {
            throw new NotFoundException();
        }
        
    }

}
