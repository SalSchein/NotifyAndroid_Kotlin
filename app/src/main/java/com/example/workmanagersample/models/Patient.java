
package com.example.workmanagersample.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Patient {

    @SerializedName("last_name")
    @Expose
    private Object lastName;
    @SerializedName("first_name")
    @Expose
    private Object firstName;
    @SerializedName("patient_id")
    @Expose
    private Object patientId;
    @SerializedName("escape_monitor")
    @Expose
    private Integer escapeMonitor;
    @SerializedName("weight")
    @Expose
    private Integer weight;
    @SerializedName("age")
    @Expose
    private Integer age;
    @SerializedName("height")
    @Expose
    private Integer height;
    @SerializedName("sex")
    @Expose
    private Integer sex;
    @SerializedName("assigned_room")
    @Expose
    private Object assignedRoom;

    public Object getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Object getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Object getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public Integer getEscapeMonitor() {
        return escapeMonitor;
    }

    public void setEscapeMonitor(Integer escapeMonitor) {
        this.escapeMonitor = escapeMonitor;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Object getAssignedRoom() {
        return assignedRoom;
    }

    public void setAssignedRoom(Object assignedRoom) {
        this.assignedRoom = assignedRoom;
    }

}
