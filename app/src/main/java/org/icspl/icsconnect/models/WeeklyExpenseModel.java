package org.icspl.icsconnect.models;

public class WeeklyExpenseModel
{
public String date;
public String particular_expense;
public String total_amount;
public String igst;
public String cgst;
public String sgst;
public String gst_no;
public String service_provider;
public String paid_by;
public String inv_amount;
public String client_no;
public String browse_file;
public String activity_summary;

 public WeeklyExpenseModel(String date, String particular_expense, String total_amount, String igst, String cgst, String sgst, String gst_no, String service_provider, String paid_by, String inv_amount, String client_no, String browse_file, String activity_summary) {
        this.date = date;
        this.particular_expense = particular_expense;
        this.total_amount = total_amount;
        this.igst = igst;
        this.cgst = cgst;
        this.sgst = sgst;
        this.gst_no = gst_no;
        this.service_provider = service_provider;
        this.paid_by = paid_by;
        this.inv_amount = inv_amount;
        this.client_no = client_no;
        this.browse_file = browse_file;
        this.activity_summary = activity_summary;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getParticular_expense() {
        return particular_expense;
    }

    public void setParticular_expense(String particular_expense) {
        this.particular_expense = particular_expense;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getIgst() {
        return igst;
    }

    public void setIgst(String igst) {
        this.igst = igst;
    }

    public String getCgst() {
        return cgst;
    }

    public void setCgst(String cgst) {
        this.cgst = cgst;
    }

    public String getSgst() {
        return sgst;
    }

    public void setSgst(String sgst) {
        this.sgst = sgst;
    }

    public String getGst_no() {
        return gst_no;
    }

    public void setGst_no(String gst_no) {
        this.gst_no = gst_no;
    }

    public String getService_provider() {
        return service_provider;
    }

    public void setService_provider(String service_provider) {
        this.service_provider = service_provider;
    }

    public String getPaid_by() {
        return paid_by;
    }

    public void setPaid_by(String paid_by) {
        this.paid_by = paid_by;
    }

    public String getInv_amount() {
        return inv_amount;
    }

    public void setInv_amount(String inv_amount) {
        this.inv_amount = inv_amount;
    }

    public String getClient_no() {
        return client_no;
    }

    public void setClient_no(String client_no) {
        this.client_no = client_no;
    }

    public String getBrowse_file() {
        return browse_file;
    }

    public void setBrowse_file(String browse_file) {
        this.browse_file = browse_file;
    }

    public String getActivity_summary() {
        return activity_summary;
    }

    public void setActivity_summary(String activity_summary) {
        this.activity_summary = activity_summary;
    }
}
