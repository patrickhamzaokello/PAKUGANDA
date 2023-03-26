
package com.pkasemer.pakuganda.Models;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class ActivitiesHome {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("dateCarriedout")
    @Expose
    private String dateCarriedout;
    @SerializedName("dateCompleted")
    @Expose
    private String dateCompleted;
    @SerializedName("nextDueDate")
    @Expose
    private String nextDueDate;
    @SerializedName("InchargePerson")
    @Expose
    private String inchargePerson;
    @SerializedName("village")
    @Expose
    private String village;
    @SerializedName("dateAdded")
    @Expose
    private String dateAdded;
    @SerializedName("long")
    @Expose
    private String _long;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("iconPath")
    @Expose
    private String iconPath;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDateCarriedout() {
        return dateCarriedout;
    }

    public void setDateCarriedout(String dateCarriedout) {
        this.dateCarriedout = dateCarriedout;
    }

    public String getDateCompleted() {
        return dateCompleted;
    }

    public void setDateCompleted(String dateCompleted) {
        this.dateCompleted = dateCompleted;
    }

    public String getNextDueDate() {
        return nextDueDate;
    }

    public void setNextDueDate(String nextDueDate) {
        this.nextDueDate = nextDueDate;
    }

    public String getInchargePerson() {
        return inchargePerson;
    }

    public void setInchargePerson(String inchargePerson) {
        this.inchargePerson = inchargePerson;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getLong() {
        return _long;
    }

    public void setLong(String _long) {
        this._long = _long;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

}
