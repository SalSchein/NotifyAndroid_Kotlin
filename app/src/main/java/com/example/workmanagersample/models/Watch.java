
package com.example.workmanagersample.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Watch {

    @SerializedName("watch_mac")
    @Expose
    private Object watchMac;
    @SerializedName("patient_id")
    @Expose
    private Object patientId;
    @SerializedName("watch_model")
    @Expose
    private Object watchModel;
    @SerializedName("last_log_collection")
    @Expose
    private Object lastLogCollection;

    public Object getWatchMac() {
        return watchMac;
    }

    public void setWatchMac(String watchMac) {
        this.watchMac = watchMac;
    }

    public Object getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public Object getWatchModel() {
        return watchModel;
    }

    public void setWatchModel(String watchModel) {
        this.watchModel = watchModel;
    }

    public Object getLastLogCollection() {
        return lastLogCollection;
    }

    public void setLastLogCollection(Object lastLogCollection) {
        this.lastLogCollection = lastLogCollection;
    }

}
