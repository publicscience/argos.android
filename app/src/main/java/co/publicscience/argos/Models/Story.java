package co.publicscience.argos.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Story extends Cluster {
    private List<String> summary;
    private List<Event> events = new ArrayList<Event>();
    private List<Concept> concepts = new ArrayList<Concept>();

    @SerializedName("num_events")
    private Integer numEvents;

    public List<String> getSummary() {
        return summary;
    }
    public void setSummary(List<String> summary) {
        this.summary = summary;
    }

    public List<Event> getEvents() {
        return events;
    }
    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public List<Concept> getConcepts() {
        return concepts;
    }
    public void setConcepts(List<Concept> concepts) {
        this.concepts = concepts;
    }

    public Integer getNumEvents() {
        return numEvents;
    }
    public void setNumEvents(Integer numEvents) {
        this.numEvents = numEvents;
    }
}
