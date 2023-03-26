
package com.pkasemer.pakuganda.Models;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class NeedsBase {

    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("version")
    @Expose
    private Integer version;
    @SerializedName("notice_home")
    @Expose
    private List<NoticeHome> noticeHome;
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

    public List<NoticeHome> getNoticeHome() {
        return noticeHome;
    }

    public void setNoticeHome(List<NoticeHome> noticeHome) {
        this.noticeHome = noticeHome;
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
