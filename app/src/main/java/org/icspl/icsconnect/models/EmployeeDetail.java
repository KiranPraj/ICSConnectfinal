package org.icspl.icsconnect.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EmployeeDetail {


    @SerializedName("EmployeeDetails")
    @Expose
    public List<EmployeeDetail> employeeDetails = null;

    @SerializedName("photo")
    @Expose
    public String photo;
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("masteradmin")
    @Expose
    public String masteradmin;
    @SerializedName("Station")
    @Expose
    public String station;
    @SerializedName("Doj")
    @Expose
    public String doj;
    @SerializedName("Role")
    @Expose
    public String role;

}
