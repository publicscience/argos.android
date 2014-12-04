package co.publicscience.argos.Services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import co.publicscience.argos.Models.Concept;
import co.publicscience.argos.Models.Event;
import co.publicscience.argos.Responses.EventsResponse;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.Callback;

public class ArgosService {
    private static final String BASE_URL = "http://argos.starbase.in";
    private final ArgosServiceInterface mService;

    public ArgosService() {
        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestInterceptor.RequestFacade request) {
                request.addHeader("Accept", "application/json");
            }
        };

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(BASE_URL)
                .setRequestInterceptor(requestInterceptor)
                .setConverter(new GsonConverter(gson))
                .setLogLevel(RestAdapter.LogLevel.NONE)
                .build();

        mService = restAdapter.create(ArgosServiceInterface.class);
    }

    public ArgosServiceInterface getAPI() {
        return mService;
    }

    public interface ArgosServiceInterface {
        @GET("/events")
        void getEvents(Callback<EventsResponse> cb);

        @GET("/events/{id}")
        void getEvent(@Path("id") int eventId, Callback<Event> cb);

        @GET("/concepts/{slug}")
        void getConcept(@Path("slug") String conceptSlug, Callback<Concept> cb);
    }
}