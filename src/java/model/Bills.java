package model;

import java.sql.Date;

public class Bills {
    private int billId;
    private int apartmentID;
    private int billingMonth;
    private int billingYear;
    private double totalAmount;
    private String status;
    private Date createdAt;
    private int serviceTypeId;
    private String serviceName;
    
    public Bills() {}

    public Bills(int billId, int apartmentID, int billingMonth, int billingYear, double totalAmount, 
                 String status, Date createdAt, int serviceTypeId, String serviceName) {
        this.billId = billId;
        this.apartmentID = apartmentID;
        this.billingMonth = billingMonth;
        this.billingYear = billingYear;
        this.totalAmount = totalAmount;
        this.status = status;
        this.createdAt = createdAt;
        this.serviceTypeId = serviceTypeId;
        this.serviceName = serviceName;
    }

    public int getBillId() { return billId; }
    public void setBillId(int billId) { this.billId = billId; }

    public int getApartmentID() { return apartmentID; }
    public void setApartmentID(int apartmentID) { this.apartmentID = apartmentID; }

    public int getBillingMonth() { return billingMonth; }
    public void setBillingMonth(int billingMonth) { this.billingMonth = billingMonth; }

    public int getBillingYear() { return billingYear; }
    public void setBillingYear(int billingYear) { this.billingYear = billingYear; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public int getServiceTypeId() { return serviceTypeId; }
    public void setServiceTypeId(int serviceTypeId) { this.serviceTypeId = serviceTypeId; }

    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
}
