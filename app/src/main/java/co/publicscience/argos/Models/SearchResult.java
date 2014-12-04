package co.publicscience.argos.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class SearchResult implements Serializable {
    private String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    private String title;
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    private String type;
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    private Integer id;
    public Integer getID() {
        return id;
    }
    public void setID(Integer id) {
        this.id = id;
    }

    private String slug;
    public String getSlug() {
        return slug;
    }
    public void setSlug(String slug) {
        this.slug = title;
    }

    private String summary;
    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }

    private String image;
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    @SerializedName("created_at")
    private Date createdAt;
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Concept asConcept() {
        Concept concept = new Concept();
        concept.setName(name);
        concept.setSlug(slug);
        concept.setImage(image);
        concept.setSummary(summary);
        return concept;
    }

    public Event asEvent() {
        Event event = new Event();
        event.setTitle(title);
        event.setID(id);
        event.setImage(image);
        event.setCreatedAt(createdAt);
        //event.setSummary(summary); // TO DO search returns summaries as a string, not a list
        return event;
    }

    public Story asStory() {
        Story story = new Story();
        story.setTitle(title);
        story.setID(id);
        story.setImage(image);
        story.setCreatedAt(createdAt);
        //story.setSummary(summary); // TO DO search returns summaries as a string, not a list
        return story;
    }
}

