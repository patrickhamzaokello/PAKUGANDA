
package com.example.pakuganda.Models;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class MapBase {

    @SerializedName("mapFeatures")
    @Expose
    private List<MapFeature> mapFeatures;
    @SerializedName("types")
    @Expose
    private List<Type> types;

    public List<MapFeature> getMapFeatures() {
        return mapFeatures;
    }

    public void setMapFeatures(List<MapFeature> mapFeatures) {
        this.mapFeatures = mapFeatures;
    }

    public List<Type> getTypes() {
        return types;
    }

    public void setTypes(List<Type> types) {
        this.types = types;
    }

}
