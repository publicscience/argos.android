package co.publicscience.argos.Models;

import java.io.Serializable;

public class SummarySentence implements Serializable {
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
