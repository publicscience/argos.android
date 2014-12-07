package co.publicscience.argos.Models;

import android.text.format.DateUtils;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Cluster implements Comparable<Cluster>, Serializable {
    private Integer id;
    private String title;
    private String image;
    private Float score;

    @SerializedName("created_at")
    private Date createdAt;

    @SerializedName("updated_at")
    private Date updatedAt;

    public Integer getID() {
        return id;
    }
    public void setID(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public Float getScore() {
        return score;
    }
    public void setScore(Float score) {
        this.score = score;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }


    public String getDaysAgo() {
        return getDaysAgo(new Date(), createdAt);
    }
    public String getTimeAgo() {
        Date now = new Date();
        return DateUtils.getRelativeTimeSpanString(createdAt.getTime(), now.getTime(), DateUtils.MINUTE_IN_MILLIS).toString();
    }
    private static String getDaysAgo(Date to, Date from) {
        return DateUtils.getRelativeTimeSpanString(from.getTime(), to.getTime(), DateUtils.DAY_IN_MILLIS).toString();
    }


    @Override
    public int compareTo(Cluster cluster) {
        // Datetime, descending (most recent first).
        return cluster.getCreatedAt().compareTo(getCreatedAt());
    }

    public static HashMap<Integer,String> splitClustersByDay(List<? extends Cluster> clusters) {
        // Sort the list by createdAt.
        Collections.sort(clusters);

        Date today = new Date();
        Date current = clusters.get(0).getCreatedAt();
        int pos = 0;

        HashMap<Integer,String> mapping = new HashMap<Integer,String>();
        mapping.put(pos, getDaysAgo(today, current));

        for (Cluster c : clusters) {
            Date createdAt = c.getCreatedAt();
            if (!sameDay(current, createdAt)) {
                current = createdAt;
                mapping.put(pos, getDaysAgo(today, current));
            }
            pos++;
        }
        return mapping;
    }

    private static Boolean sameDay(Date date1, Date date2) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        return fmt.format(date1).equals(fmt.format(date2));
    }
}
