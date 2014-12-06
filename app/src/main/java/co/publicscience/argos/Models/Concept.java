package co.publicscience.argos.Models;

import java.io.Serializable;
import java.util.List;

public class Concept implements Comparable<Concept>, Serializable {
    private String name;
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    private String slug;
    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    private List<String> sources;
    public List<String> getSources() { return sources; }
    public void setSources(List<String> sources) { this.sources = sources; }

    private String summary;
    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }

    private String image;
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    private Float score;
    public Float getScore() { return score; }
    public void setScore(Float score) { this.score = score; }

    private List<Story> stories;
    public List<Story> getStories() { return stories; }
    public void setStories(List<Story> stories) { this.stories = stories; }

    @Override
    public int compareTo(Concept concept) {
        // Score, descending (higher score first).
        return concept.getScore().compareTo(getScore());
    }
}
