package model;

/**
 * Model class representing a staff profile.
 * Maps to the StaffProfiles table (JOIN with Users and Positions).
 *
 * StaffProfile = 1 nhân viên có vị trí cụ thể trong tòa nhà.
 */
public class StaffProfile {

    // -------------------------------------------------------
    // Fields mapping StaffProfiles table columns
    // -------------------------------------------------------
    private int id;           // StaffProfiles.Id
    private int userId;       // StaffProfiles.UserId
    private int positionId;   // StaffProfiles.PositionId

    // -------------------------------------------------------
    // Join fields (from Users and Positions tables)
    // -------------------------------------------------------
    private String fullName;      // Users.FullName
    private String username;      // Users.Username
    private String email;         // Users.Email
    private String phone;         // Users.Phone
    private boolean isActive;     // Users.IsActive
    private String positionName;  // Positions.PositionName
    private String positionDesc;  // Positions.Description

    // -------------------------------------------------------
    // Constructors
    // -------------------------------------------------------

    public StaffProfile() {
    }

    public StaffProfile(int id, int userId, int positionId) {
        this.id = id;
        this.userId = userId;
        this.positionId = positionId;
    }

    // -------------------------------------------------------
    // Getters & Setters — table columns
    // -------------------------------------------------------

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPositionId() {
        return positionId;
    }

    public void setPositionId(int positionId) {
        this.positionId = positionId;
    }

    // -------------------------------------------------------
    // Getters & Setters — join fields
    // -------------------------------------------------------

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getPositionDesc() {
        return positionDesc;
    }

    public void setPositionDesc(String positionDesc) {
        this.positionDesc = positionDesc;
    }

    // -------------------------------------------------------
    // Helper
    // -------------------------------------------------------

    /**
     * Returns "Đang làm việc" or "Đã vô hiệu hóa" for display in JSP.
     */
    public String getStatusLabel() {
        return isActive ? "Đang làm việc" : "Đã vô hiệu hóa";
    }

    @Override
    public String toString() {
        return "StaffProfile{id=" + id
                + ", userId=" + userId
                + ", fullName=" + fullName
                + ", positionName=" + positionName
                + ", isActive=" + isActive + "}";
    }
}
