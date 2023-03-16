
package com.example.pakuganda.Models;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class DeepWell {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("depth")
    @Expose
    private String depth;
    @SerializedName("InChargeName")
    @Expose
    private String inChargeName;

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

    public String getDepth() {
        return depth;
    }

    public void setDepth(String depth) {
        this.depth = depth;
    }

    public String getInChargeName() {
        return inChargeName;
    }

    public void setInChargeName(String inChargeName) {
        this.inChargeName = inChargeName;
    }

}
