package co.publicscience.argos.Models;

import android.text.format.DateUtils;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Event implements Comparable<Event>, Serializable {
    private Integer id;
    private String title;
    private Float score;
    private List<SummarySentence> summary;
    private String image;
    private List<Article> articles = new ArrayList<Article>();
    private List<Concept> concepts = new ArrayList<Concept>();
    private List<Story> stories = new ArrayList<Story>();

    @SerializedName("created_at")
    private Date createdAt;

    @SerializedName("updated_at")
    private Date updatedAt;

    @SerializedName("num_articles")
    private Integer numArticles;

    public Integer getID() { return id; }
    public void setID(Integer id) { this.id = id; }
    public Integer getNumArticles() { return numArticles; }
    public void setNumArticles(Integer numArticles) { this.numArticles = numArticles; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
    public Float getScore() { return score; }
    public void setScore(Float score) { this.score = score; }
    public List<SummarySentence> getSummary() {
        return summary;
    }
    public void setSummary(List<SummarySentence> summary) {
        this.summary = summary;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
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


    public String getDaysAgo() {
        Date today = new Date();
        return getDaysAgo(today, createdAt);
    }
    public String getTimeAgo() {
        return DateUtils.getRelativeTimeSpanString(createdAt.getTime(), new Date().getTime(), 0).toString();
    }



    public static HashMap<Integer,String> splitEvents(List<Event> events) {
        // Sort the list by createdAt.
        Collections.sort(events);

        Date today = new Date();
        Date current = events.get(0).getCreatedAt();
        int pos = 0;

        HashMap<Integer,String> mapping = new HashMap<Integer,String>();
        mapping.put(pos, getDaysAgo(today, current));

        for (Event e : events) {
            Date createdAt = e.getCreatedAt();
            if (!sameDay(current, createdAt)) {
                current = createdAt;
                mapping.put(pos, getDaysAgo(today, current));
            }
            pos++;
        }
        return mapping;
    }

    private static String getDaysAgo(Date fromDate, Date toDate){
        long days = (fromDate.getTime() - toDate.getTime()) / 86400000;

        if(days == 0) return "Today";
        else if(days == 1) return "Yesterday";
        else return days + " days ago";
    }

    private static Boolean sameDay(Date date1, Date date2) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        return fmt.format(date1).equals(fmt.format(date2));
    }

    @Override
    public int compareTo(Event event) {
        // Datetime, descending (most recent first).
        return event.getCreatedAt().compareTo(getCreatedAt());
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
