package model;

import java.sql.Date;

public class Payments {
    private int paymentId;
    private int billId;
    private String paymentMethod;
    private Date paymentDate;
    private double amount;
    private String transactionCode;
    private String status;

    public Payments() {}

    public Payments(int paymentId, int billId, String paymentMethod, Date paymentDate, 
                    double amount, String transactionCode, String status) {
        this.paymentId = paymentId;
        this.billId = billId;
        this.paymentMethod = paymentMethod;
        this.paymentDate = paymentDate;
        this.amount = amount;
        this.transactionCode = transactionCode;
        this.status = status;
    }

    public int getPaymentId() { return paymentId; }
    public void setPaymentId(int paymentId) { this.paymentId = paymentId; }

    public int getBillId() { return billId; }
    public void setBillId(int billId) { this.billId = billId; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public Date getPaymentDate() { return paymentDate; }
    public void setPaymentDate(Date paymentDate) { this.paymentDate = paymentDate; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getTransactionCode() { return transactionCode; }
    public void setTransactionCode(String transactionCode) { this.transactionCode = transactionCode; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
