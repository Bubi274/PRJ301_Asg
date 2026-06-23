package model;

import java.util.Date;

/**
 * Model class representing a user in the ApartmentManagement system.
 * Maps to the Users table in the database.
 *
 * Roles:
 * 1 = Admin
 * 2 = Manager
 * 3 = Staff
 * 4 = Resident
 *
 * Shared across SF1, SF2, SF3, SF4, SF5.
 */
public class User {

    // -------------------------------------------------------
    // Fields — table columns
    // -------------------------------------------------------
    private int userId;
    private String username;
    private String passwordHash;
    private String fullName;
    private String email;
    private String phone;
    private int roleId;
    private boolean isActive;
    private Date createdAt;

    // -------------------------------------------------------
    // Constructors
    // -------------------------------------------------------

    public User() {
    }

    public User(int userId, String username, String passwordHash,
            String fullName, String email, String phone,
            int roleId, boolean isActive, Date createdAt) {
        this.userId = userId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.roleId = roleId;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }

    // -------------------------------------------------------
    // Getters & Setters — core fields
    // -------------------------------------------------------

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    // -------------------------------------------------------
    // Helper methods
    // -------------------------------------------------------

    /**
     * Returns the role name based on roleId.
     * Useful for display in JSP without extra DB call.
     */
    public String getRoleName() {
        switch (roleId) {
            case 1:
                return "Admin";
            case 2:
                return "Manager";
            case 3:
                return "Staff";
            case 4:
                return "Resident";
            default:
                return "Unknown";
        }
    }

    /**
     * Returns true if this user is a Staff (roleId = 3).
     */
    public boolean isStaff() {
        return roleId == 3;
    }

    /**
     * Returns true if this user is a Manager (roleId = 2).
     */
    public boolean isManager() {
        return roleId == 2;
    }

    /**
     * Returns true if this user is an Admin (roleId = 1).
     */
    public boolean isAdmin() {
        return roleId == 1;
    }

    /**
     * Returns true if this user is a Resident (roleId = 4).
     */
    public boolean isResident() {
        return roleId == 4;
    }

    @Override
    public String toString() {
        return "User{userId=" + userId
                + ", username=" + username
                + ", fullName=" + fullName
                + ", roleId=" + roleId
                + ", isActive=" + isActive + "}";
    }
}