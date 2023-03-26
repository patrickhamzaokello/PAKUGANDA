
package com.pkasemer.pakuganda.Models;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class SelectedVillage {

    @SerializedName("version")
    @Expose
    private Integer version;
    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("SingleVillage")
    @Expose
    private List<SingleVillage> singleVillage;
    @SerializedName("total_pages")
    @Expose
    private Integer totalPages;
    @SerializedName("total_results")
    @Expose
    private Integer totalResults;

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public List<SingleVillage> getSingleVillage() {
        return singleVillage;
    }

    public void setSingleVillage(List<SingleVillage> singleVillage) {
        this.singleVillage = singleVillage;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

}
