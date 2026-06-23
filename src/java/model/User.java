package model;

import java.sql.Timestamp;

public class User {
    private int userId;
    private String username;
    private String passwordHash;
    private String fullName;
    private String email;
    private String phone;
    private int roleId;
    private String roleName; // join từ Roles, chỉ dùng khi hiển thị
    private boolean active;
    private Timestamp updatedAt;
    private Integer updatedBy;
    private Timestamp createdAt;
    private Timestamp lastLogin;
    private int failedLoginCount;
    private Timestamp lockoutUntil;

    public User() {}

    // Getters & Setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public int getRoleId() { return roleId; }
    public void setRoleId(int roleId) { this.roleId = roleId; }

    public String getRoleName() { return roleName; }
    public void setRoleName(String roleName) { this.roleName = roleName; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    public Integer getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(Integer updatedBy) { this.updatedBy = updatedBy; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getLastLogin() { return lastLogin; }
    public void setLastLogin(Timestamp lastLogin) { this.lastLogin = lastLogin; }

    public int getFailedLoginCount() { return failedLoginCount; }
    public void setFailedLoginCount(int failedLoginCount) { this.failedLoginCount = failedLoginCount; }

    public Timestamp getLockoutUntil() { return lockoutUntil; }
    public void setLockoutUntil(Timestamp lockoutUntil) { this.lockoutUntil = lockoutUntil; }
}
