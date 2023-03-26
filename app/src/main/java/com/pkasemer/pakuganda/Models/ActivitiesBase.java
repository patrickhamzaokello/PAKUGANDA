
package com.pkasemer.pakuganda.Models;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class ActivitiesBase {

    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("version")
    @Expose
    private Integer version;
    @SerializedName("activities_home")
    @Expose
    private List<ActivitiesHome> activitiesHome;
    @SerializedName("total_pages")
    @Expose
    private Integer totalPages;
    @SerializedName("total_results")
    @Expose
    private String totalResults;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public List<ActivitiesHome> getActivitiesHome() {
        return activitiesHome;
    }

    public void setActivitiesHome(List<ActivitiesHome> activitiesHome) {
        this.activitiesHome = activitiesHome;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public String getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(String totalResults) {
        this.totalResults = totalResults;
    }

}
