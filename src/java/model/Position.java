package model;

/**
 * Model class representing a staff position/job title.
 * Maps to the Positions table in the database.
 */
public class Position {

    private int positionId;
    private String positionName;
    private String description;

    // -------------------------------------------------------
    // Constructors
    // -------------------------------------------------------

    public Position() {
    }

    public Position(int positionId, String positionName, String description) {
        this.positionId = positionId;
        this.positionName = positionName;
        this.description = description;
    }

    // -------------------------------------------------------
    // Getters & Setters
    // -------------------------------------------------------

    public int getPositionId() {
        return positionId;
    }

    public void setPositionId(int positionId) {
        this.positionId = positionId;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Position{positionId=" + positionId
                + ", positionName=" + positionName + "}";
    }
}
