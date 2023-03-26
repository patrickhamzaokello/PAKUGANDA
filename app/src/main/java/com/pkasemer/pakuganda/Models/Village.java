
package com.pkasemer.pakuganda.Models;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Village {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("county")
    @Expose
    private String county;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("subcounty")
    @Expose
    private String subcounty;
    @SerializedName("long")
    @Expose
    private String _long;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("population")
    @Expose
    private String population;
    @SerializedName("families")
    @Expose
    private String families;
    @SerializedName("lastupdate")
    @Expose
    private String lastupdate;
    @SerializedName("sourceWater")
    @Expose
    private String sourceWater;
    @SerializedName("sourceDeepWell")
    @Expose
    private String sourceDeepWell;
    @SerializedName("priority")
    @Expose
    private String priority;
    @SerializedName("iconPath")
    @Expose
    private String iconPath;
    @SerializedName("activities")
    @Expose
    private List<Activity> activities;
    @SerializedName("leaders")
    @Expose
    private List<Leader> leaders;
    @SerializedName("deepWell")
    @Expose
    private List<DeepWell> deepWell;
    @SerializedName("needs")
    @Expose
    private List<Need> needs;
    @SerializedName("outreach")
    @Expose
    private List<Outreach> outreach;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSubcounty() {
        return subcounty;
    }

    public void setSubcounty(String subcounty) {
        this.subcounty = subcounty;
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

    public String getPopulation() {
        return population;
    }

    public void setPopulation(String population) {
        this.population = population;
    }

    public String getFamilies() {
        return families;
    }

    public void setFamilies(String families) {
        this.families = families;
    }

    public String getLastupdate() {
        return lastupdate;
    }

    public void setLastupdate(String lastupdate) {
        this.lastupdate = lastupdate;
    }

    public String getSourceWater() {
        return sourceWater;
    }

    public void setSourceWater(String sourceWater) {
        this.sourceWater = sourceWater;
    }

    public String getSourceDeepWell() {
        return sourceDeepWell;
    }

    public void setSourceDeepWell(String sourceDeepWell) {
        this.sourceDeepWell = sourceDeepWell;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    public List<Leader> getLeaders() {
        return leaders;
    }

    public void setLeaders(List<Leader> leaders) {
        this.leaders = leaders;
    }

    public List<DeepWell> getDeepWell() {
        return deepWell;
    }

    public void setDeepWell(List<DeepWell> deepWell) {
        this.deepWell = deepWell;
    }

    public List<Need> getNeeds() {
        return needs;
    }

    public void setNeeds(List<Need> needs) {
        this.needs = needs;
    }

    public List<Outreach> getOutreach() {
        return outreach;
    }

    public void setOutreach(List<Outreach> outreach) {
        this.outreach = outreach;
    }

}
