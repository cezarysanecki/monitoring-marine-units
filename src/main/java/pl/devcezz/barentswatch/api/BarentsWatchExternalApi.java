package pl.devcezz.barentswatch.api;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.QueryParam;
import java.util.List;

@RegisterRestClient(configKey = "barents-watch-api")
public interface BarentsWatchExternalApi {

    @GET
    List<OpenPosition> getVesselsPositionsFor(@HeaderParam("Authorization") String token,
                                              @QueryParam(value = "Xmin") double xMin,
                                              @QueryParam(value = "Xmax") double xMax,
                                              @QueryParam(value = "Ymin") double yMin,
                                              @QueryParam(value = "Ymax") double yMax);
}

