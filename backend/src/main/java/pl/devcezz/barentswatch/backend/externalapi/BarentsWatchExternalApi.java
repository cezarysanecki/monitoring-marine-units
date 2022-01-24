package pl.devcezz.barentswatch.backend.externalapi;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import java.util.List;

@RegisterRestClient(configKey = "barents-watch-api")
public interface BarentsWatchExternalApi {

    @GET
    @Path(value = "/openpositions")
    List<OpenPosition> fetchOpenPositionsFor(@HeaderParam("Authorization") String token,
                                             @QueryParam(value = "Xmin") double xMin,
                                             @QueryParam(value = "Xmax") double xMax,
                                             @QueryParam(value = "Ymin") double yMin,
                                             @QueryParam(value = "Ymax") double yMax);

    @GET
    @Path("/openposition/{mmsi}")
    OpenPosition fetchOpenPositionFor(@HeaderParam("Authorization") String token,
                                      @PathParam(value = "mmsi") Integer mmsi);
}

