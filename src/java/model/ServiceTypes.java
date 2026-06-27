package model;

public class ServiceTypes {
    private int serviceTypeId;
    private String serviceName;
    private String unit;
    private double pricePerUnit;

    public ServiceTypes() {}

    public ServiceTypes(int serviceTypeId, String serviceName, String unit, double pricePerUnit) {
        this.serviceTypeId = serviceTypeId;
        this.serviceName = serviceName;
        this.unit = unit;
        this.pricePerUnit = pricePerUnit;
    }

    public int getServiceTypeId() { return serviceTypeId; }
    public void setServiceTypeId(int serviceTypeId) { this.serviceTypeId = serviceTypeId; }

    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public double getPricePerUnit() { return pricePerUnit; }
    public void setPricePerUnit(double pricePerUnit) { this.pricePerUnit = pricePerUnit; }
}
