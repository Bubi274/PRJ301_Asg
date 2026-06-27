package dal;

import model.User;
import model.Request;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    /** Dashboard: Đếm tổng số user trong hệ thống */
    public int countUsers() {
        String sql = "SELECT COUNT(*) FROM Users";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /** UC-AD-01: Lấy user theo username hoặc email để verify password ở Servlet */
    public User getByUsernameOrEmail(String identifier) {
        String sql = "SELECT * FROM Users WHERE Username = ? OR Email = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, identifier);
            ps.setString(2, identifier);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /** UC-AD-02: Lấy toàn bộ user kèm RoleName (JOIN Roles) */
    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT u.*, r.RoleName FROM Users u "
                + "JOIN Roles r ON u.RoleId = r.RoleId ORDER BY u.UserId";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                User u = mapRow(rs);
                u.setRoleName(rs.getString("RoleName"));
                list.add(u);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public User getById(int userId) {
        String sql = "SELECT u.*, r.RoleName FROM Users u "
                + "JOIN Roles r ON u.RoleId = r.RoleId WHERE u.UserId = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User u = mapRow(rs);
                    u.setRoleName(rs.getString("RoleName"));
                    return u;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /** UC-AD-03: kiểm tra trùng username trước khi insert (UNIQUE constraint cũng chặn ở DB) */
    public boolean isUsernameExists(String username) {
        String sql = "SELECT 1 FROM Users WHERE Username = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /** UC-AD-03: Thêm account mới */
    public boolean insertUser(User u, String passwordHash) {
        String sql = "INSERT INTO Users (Username, PasswordHash, FullName, Email, Phone, RoleId, IsActive) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, u.getUsername());
            ps.setString(2, passwordHash);
            ps.setString(3, u.getFullName());
            ps.setString(4, u.getEmail());
            ps.setString(5, u.getPhone());
            ps.setInt(6, u.getRoleId());
            ps.setBoolean(7, u.isActive());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /** UC-AD-04: Cập nhật account. */
    public boolean updateUser(User u) {
        String sql = "UPDATE Users SET FullName=?, Email=?, Phone=?, RoleId=?, IsActive=? "
                + "WHERE UserId=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, u.getFullName());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getPhone());
            ps.setInt(4, u.getRoleId());
            ps.setBoolean(5, u.isActive());
            ps.setInt(6, u.getUserId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /** Hard delete - Yêu cầu phải xóa các dữ liệu liên quan trước */
    public String hardDeleteUser(int userId) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("DELETE FROM Users WHERE UserId = ?")) {
            ps.setInt(1, userId);
            int rows = ps.executeUpdate();
            if (rows > 0) return "SUCCESS";
            return "NOT_FOUND";
        } catch (SQLException e) {
            if (e.getErrorCode() == 547) {
                return "CONSTRAINT";
            }
            e.printStackTrace();
            return "ERROR";
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    /** Quên mật khẩu: Xác thực đúng Username và Email */
    public boolean verifyUsernameAndEmail(String username, String email) {
        String sql = "SELECT 1 FROM Users WHERE Username = ? AND Email = ? AND IsActive = 1";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /** Quên mật khẩu: Cập nhật mật khẩu mới */
    public boolean updatePassword(String username, String newPasswordHash) {
        String sql = "UPDATE Users SET PasswordHash = ? WHERE Username = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, newPasswordHash);
            ps.setString(2, username);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getApartmentIdByUser(int userId) {
        String sql = "SELECT ApartmentId FROM ApartmentResidents WHERE UserId = ? AND IsActive = 1";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("ApartmentId");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public List<Request> getRequestsByResidentId(int residentId) {
        List<Request> list = new ArrayList<>();
        String sql = "SELECT r.*, u.FullName AS ResidentName "
                + "FROM Requests r "
                + "LEFT JOIN Users u ON r.ResidentId = u.UserId "
                + "WHERE r.ResidentId = ? "
                + "ORDER BY r.CreatedAt DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, residentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Request r = new Request();
                    r.setRequestId(rs.getInt("RequestId"));
                    r.setResidentId(rs.getInt("ResidentId"));
                    r.setRequestTypeId(rs.getInt("RequestTypeId"));
                    r.setTitle(rs.getString("Title"));
                    r.setDescription(rs.getString("Description"));
                    r.setStatus(rs.getString("Status"));
                    r.setResidentName(rs.getString("ResidentName"));
                    if (rs.getTimestamp("CreatedAt") != null) {
                        r.setCreatedAt(rs.getTimestamp("CreatedAt"));
                    }
                    list.add(r);
                }
            }
        } catch (SQLException e) {
            System.err.println("Loi trong getRequestsByResidentId: " + e.getMessage());
        }
        return list;
    }

    public List<User> getResidents() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM Users WHERE RoleId = 4 AND IsActive = 1 ORDER BY FullName";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private User mapRow(ResultSet rs) throws SQLException {
        User u = new User();
        u.setUserId(rs.getInt("UserId"));
        u.setUsername(rs.getString("Username"));
        u.setPasswordHash(rs.getString("PasswordHash"));
        u.setFullName(rs.getString("FullName"));
        u.setEmail(rs.getString("Email"));
        u.setPhone(rs.getString("Phone"));
        u.setRoleId(rs.getInt("RoleId"));
        u.setActive(rs.getBoolean("IsActive"));
        return u;
    }
}
