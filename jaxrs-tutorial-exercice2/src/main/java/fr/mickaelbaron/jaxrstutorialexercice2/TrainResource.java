package fr.mickaelbaron.jaxrstutorialexercice2;

import java.util.List;
import java.util.Optional;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("trains")
@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public class TrainResource {
    
    public TrainResource() {}
    
        
    @GET
    public List<Train> getTrains(){
        return TrainBookingDB.getTrains();
    }
    
    @Path("/trainid/{trainId}")   
    @GET
    public Train getTrain(@PathParam("trainId") String trainId) {
        System.out.println("getTrain");

        Optional<Train> trainById = TrainBookingDB.getTrainById(trainId);

        if (trainById.isPresent()) {
            return trainById.get();
        } else {
           throw new NotFoundException();
        }
    }   
    
    
    @Path("/search")   
    @GET
    public Response searchTrainsByCriteria(
                    @DefaultValue("départ") @QueryParam("departure") String departure, 
                    @DefaultValue("arrivée") @QueryParam("arrival") String arrival, 
                    @DefaultValue("0000") @QueryParam("departureTime") String departureTime) {
        System.out.println("TrainResource.searchTrainsByCriteria()");

        List<Train> trains = TrainBookingDB.getTrains();
        
        return Response.status(Response.Status.OK)
                        .header("param1", departure)
                        .header("param2", arrival)
                        .header("param3", departureTime)
                        .entity(trains.subList(0, 2))
                        .build();
        
    }   
    
    @Path("bookings")
    public BookTrainResource getTrainBookingResource() {
        return new BookTrainResource();
    }
}
