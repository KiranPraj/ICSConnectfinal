package org.icspl.icsconnect.models;

public class OutStationExpensesModel
{
    public String to_date_local;
    public String from_date_local;
//    public String expense_local;
//    public String igst_local;
//    public String cgst_local;
//    public String sgst_local;
//    public String gst_no_local;
//    public String service_provider_local;
//    public String paid_by_local;
//    public String invoiceable_local;
//    public String narration_local;
//    public String browse_file_local;

    public String location_boarding;
    public String expense_boarding;
    public String igst_boarding;
    public String cgst_boarding;
    public String sgst_boarding;
    public String gst_no_boarding;
    public String service_provider_boarding;
    public String paid_by_boarding;
    public String invoiceable_boarding;
    public String narration_boarding;
    public String browse_file_boarding;
    public String activity_summary;
    public OutStationExpensesModel(String to_date_local, String from_date_local,String location_boarding, String expense_boarding, String igst_boarding, String cgst_boarding, String sgst_boarding, String gst_no_boarding, String service_provider_boarding, String paid_by_boarding, String invoiceable_boarding, String narration_boarding, String browse_file_boarding, String activity_summary) {
        this.to_date_local = to_date_local;
        this.from_date_local = from_date_local;
//        this.expense_local = expense_local;
//        this.igst_local = igst_local;
//        this.cgst_local = cgst_local;
//        this.sgst_local = sgst_local;
//        this.gst_no_local = gst_no_local;
//        this.service_provider_local = service_provider_local;
//        this.paid_by_local = paid_by_local;
//        this.invoiceable_local = invoiceable_local;
//        this.narration_local = narration_local;
//        this.browse_file_local = browse_file_local;
        this.location_boarding = location_boarding;
        this.expense_boarding = expense_boarding;
        this.igst_boarding = igst_boarding;
        this.cgst_boarding = cgst_boarding;
        this.sgst_boarding = sgst_boarding;
        this.gst_no_boarding = gst_no_boarding;
        this.service_provider_boarding = service_provider_boarding;
        this.paid_by_boarding = paid_by_boarding;
        this.invoiceable_boarding = invoiceable_boarding;
        this.narration_boarding = narration_boarding;
        this.browse_file_boarding = browse_file_boarding;
        this.activity_summary = activity_summary;
    }

    public String getTo_date_local() {
        return to_date_local;
    }

    public void setTo_date_local(String to_date_local) {
        this.to_date_local = to_date_local;
    }

    public String getFrom_date_local() {
        return from_date_local;
    }

    public void setFrom_date_local(String from_date_local) {
        this.from_date_local = from_date_local;
    }

    public String getLocation_boarding() {
        return location_boarding;
    }

    public void setLocation_boarding(String location_boarding) {
        this.location_boarding = location_boarding;
    }

    public String getExpense_boarding() {
        return expense_boarding;
    }

    public void setExpense_boarding(String expense_boarding) {
        this.expense_boarding = expense_boarding;
    }

    public String getIgst_boarding() {
        return igst_boarding;
    }

    public void setIgst_boarding(String igst_boarding) {
        this.igst_boarding = igst_boarding;
    }

    public String getCgst_boarding() {
        return cgst_boarding;
    }

    public void setCgst_boarding(String cgst_boarding) {
        this.cgst_boarding = cgst_boarding;
    }

    public String getSgst_boarding() {
        return sgst_boarding;
    }

    public void setSgst_boarding(String sgst_boarding) {
        this.sgst_boarding = sgst_boarding;
    }

    public String getGst_no_boarding() {
        return gst_no_boarding;
    }

    public void setGst_no_boarding(String gst_no_boarding) {
        this.gst_no_boarding = gst_no_boarding;
    }

    public String getService_provider_boarding() {
        return service_provider_boarding;
    }

    public void setService_provider_boarding(String service_provider_boarding) {
        this.service_provider_boarding = service_provider_boarding;
    }

    public String getPaid_by_boarding() {
        return paid_by_boarding;
    }

    public void setPaid_by_boarding(String paid_by_boarding) {
        this.paid_by_boarding = paid_by_boarding;
    }

    public String getInvoiceable_boarding() {
        return invoiceable_boarding;
    }

    public void setInvoiceable_boarding(String invoiceable_boarding) {
        this.invoiceable_boarding = invoiceable_boarding;
    }

    public String getNarration_boarding() {
        return narration_boarding;
    }

    public void setNarration_boarding(String narration_boarding) {
        this.narration_boarding = narration_boarding;
    }

    public String getBrowse_file_boarding() {
        return browse_file_boarding;
    }

    public void setBrowse_file_boarding(String browse_file_boarding) {
        this.browse_file_boarding = browse_file_boarding;
    }

    public String getActivity_summary() {
        return activity_summary;
    }

    public void setActivity_summary(String activity_summary) {
        this.activity_summary = activity_summary;
    }
}
