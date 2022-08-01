
package com.example.workmanagersample.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AccessPoint {

    @SerializedName("access_point_mac")
    @Expose
    private Object accessPointMac;
    @SerializedName("description")
    @Expose
    private Object description;
    @SerializedName("near_exit")
    @Expose
    private Object nearExit;
    @SerializedName("access_point_model")
    @Expose
    private Object accessPointModel;
    @SerializedName("access_point_ip")
    @Expose
    private String accessPointIp;

    public Object getAccessPointMac() {
        return accessPointMac;
    }

    public void setAccessPointMac(Object accessPointMac) {
        this.accessPointMac = accessPointMac;
    }

    public Object getDescription() {
        return description;
    }

    public void setDescription(Object description) {
        this.description = description;
    }

    public Object getNearExit() {
        return nearExit;
    }

    public void setNearExit(Object nearExit) {
        this.nearExit = nearExit;
    }

    public Object getAccessPointModel() {
        return accessPointModel;
    }

    public void setAccessPointModel(Object accessPointModel) {
        this.accessPointModel = accessPointModel;
    }

    public String getAccessPointIp() {
        return accessPointIp;
    }

    public void setAccessPointIp(String accessPointIp) {
        this.accessPointIp = accessPointIp;
    }

}
