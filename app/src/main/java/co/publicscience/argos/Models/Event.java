package co.publicscience.argos.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Event extends Cluster {
    private List<SummarySentence> summary;
    private List<Article> articles = new ArrayList<Article>();
    private List<Concept> concepts = new ArrayList<Concept>();
    private List<Story> stories = new ArrayList<Story>();

    @SerializedName("num_articles")
    private Integer numArticles;

    public Integer getNumArticles() {
        return numArticles;
    }
    public void setNumArticles(Integer numArticles) {
        this.numArticles = numArticles;
    }

    public List<SummarySentence> getSummary() {
        return summary;
    }
    public void setSummary(List<SummarySentence> summary) {
        this.summary = summary;
    }

    public List<Article> getArticles() {
        return articles;
    }
    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public List<Concept> getConcepts() {
        return concepts;
    }
    public void setConcepts(List<Concept> concepts) {
        this.concepts = concepts;
    }

    public List<Story> getStories() {
        return stories;
    }
    public void setStories(List<Story> stories) {
        this.stories = stories;
    }


    public static class SummarySentence implements Serializable {
        private String sentence;
        public String getSentence() { return sentence; }
        public void setSentence(String sentence) { this.sentence = sentence; }

        private String source;
        public String getSource() { return source; }
        public void setSource(String source) { this.source = source; }

        private String url;
        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }
    }
}
