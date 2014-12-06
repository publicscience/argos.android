package co.publicscience.argos.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Article implements Serializable {
    private String title;
    private Source source;

    @SerializedName("ext_url")
    private String extUrl;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Source getSource() { return source; }
    public void setSource(Source source) { this.source = source; }

    public String getExtUrl() { return extUrl; }
    public void setExtUrl(String extUrl) { this.extUrl = extUrl; }
}
