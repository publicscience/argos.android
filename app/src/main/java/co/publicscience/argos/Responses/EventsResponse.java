package co.publicscience.argos.Responses;

import java.util.ArrayList;

import co.publicscience.argos.Models.Event;

public class EventsResponse {
    private ArrayList<Event> results;
    private Pagination pagination;

    public ArrayList<Event> getEvents() {
        return results;
    }
    public Pagination getPagination() {
        return pagination;
    }
}
