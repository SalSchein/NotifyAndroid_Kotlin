
package com.example.workmanagersample.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseDatum {

    @SerializedName("notification_id")
    @Expose
    private Integer notificationId;
    @SerializedName("event_time")
    @Expose
    private Long eventTime;
    @SerializedName("event_type")
    @Expose
    private String eventType;
    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("resolved_at")
    @Expose
    private Object resolvedAt;
    @SerializedName("resolved_by")
    @Expose
    private Object resolvedBy;
    @SerializedName("dismissed")
    @Expose
    private Object dismissed;
    @SerializedName("patient")
    @Expose
    private Patient patient;
    @SerializedName("watch")
    @Expose
    private Watch watch;
    @SerializedName("access_point")
    @Expose
    private AccessPoint accessPoint;

    public Integer getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Integer notificationId) {
        this.notificationId = notificationId;
    }

    public Long getEventTime() {
        return eventTime;
    }

    public void setEventTime(Long eventTime) {
        this.eventTime = eventTime;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Object getResolvedAt() {
        return resolvedAt;
    }

    public void setResolvedAt(Object resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    public Object getResolvedBy() {
        return resolvedBy;
    }

    public void setResolvedBy(Object resolvedBy) {
        this.resolvedBy = resolvedBy;
    }

    public Object getDismissed() {
        return dismissed;
    }

    public void setDismissed(Object dismissed) {
        this.dismissed = dismissed;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Watch getWatch() {
        return watch;
    }

    public void setWatch(Watch watch) {
        this.watch = watch;
    }

    public AccessPoint getAccessPoint() {
        return accessPoint;
    }

    public void setAccessPoint(AccessPoint accessPoint) {
        this.accessPoint = accessPoint;
    }

}
