package model;

/**
 * Model class representing a support request type in the system.
 * Maps to the RequestTypes table.
 */
public class RequestType {
    private int requestTypeId;
    private String typeName;
    private int defaultPositionId;

    public RequestType() {
    }

    public RequestType(int requestTypeId, String typeName, int defaultPositionId) {
        this.requestTypeId = requestTypeId;
        this.typeName = typeName;
        this.defaultPositionId = defaultPositionId;
    }

    public int getRequestTypeId() {
        return requestTypeId;
    }

    public void setRequestTypeId(int requestTypeId) {
        this.requestTypeId = requestTypeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getDefaultPositionId() {
        return defaultPositionId;
    }

    public void setDefaultPositionId(int defaultPositionId) {
        this.defaultPositionId = defaultPositionId;
    }
}
