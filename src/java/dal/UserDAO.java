package dal;

import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    /** Dashboard: Đếm tổng số user trong hệ thống */
    public int countUsers() {
        DBContext db = new DBContext();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT COUNT(*) FROM Users";
        try {
            con = db.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { db.closeConnection(con, ps, rs); } catch (SQLException ex) { ex.printStackTrace(); }
        }
        return 0;
    }

    /** UC-AD-01: Lấy user theo username hoặc email để verify password ở Servlet */
    public User getByUsernameOrEmail(String identifier) {
        DBContext db = new DBContext();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM Users WHERE Username = ? OR Email = ?";
        try {
            con = db.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, identifier);
            ps.setString(2, identifier);
            rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { db.closeConnection(con, ps, rs); } catch (SQLException ex) { ex.printStackTrace(); }
        }
        return null;
    }


    /** UC-AD-02: Lấy toàn bộ user kèm RoleName (JOIN Roles) */
    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        DBContext db = new DBContext();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT u.*, r.RoleName FROM Users u "
                + "JOIN Roles r ON u.RoleId = r.RoleId ORDER BY u.UserId";
        try {
            con = db.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                User u = mapRow(rs);
                u.setRoleName(rs.getString("RoleName"));
                list.add(u);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { db.closeConnection(con, ps, rs); } catch (SQLException ex) { ex.printStackTrace(); }
        }
        return list;
    }

    public User getById(int userId) {
        DBContext db = new DBContext();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT u.*, r.RoleName FROM Users u "
                + "JOIN Roles r ON u.RoleId = r.RoleId WHERE u.UserId = ?";
        try {
            con = db.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            if (rs.next()) {
                User u = mapRow(rs);
                u.setRoleName(rs.getString("RoleName"));
                return u;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { db.closeConnection(con, ps, rs); } catch (SQLException ex) { ex.printStackTrace(); }
        }
        return null;
    }

    /** UC-AD-03: kiểm tra trùng username trước khi insert (UNIQUE constraint cũng chặn ở DB) */
    public boolean isUsernameExists(String username) {
        DBContext db = new DBContext();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT 1 FROM Users WHERE Username = ?";
        try {
            con = db.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { db.closeConnection(con, ps, rs); } catch (SQLException ex) { ex.printStackTrace(); }
        }
        return false;
    }

    /** UC-AD-03: Thêm account mới */
    public boolean insertUser(User u, String passwordHash) {
        DBContext db = new DBContext();
        Connection con = null;
        PreparedStatement ps = null;
        String sql = "INSERT INTO Users (Username, PasswordHash, FullName, Email, Phone, RoleId, IsActive) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            con = db.getConnection();
            ps = con.prepareStatement(sql);
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
        } finally {
            try { db.closeConnection(con, ps, null); } catch (SQLException ex) { ex.printStackTrace(); }
        }
    }

    /** UC-AD-04: Cập nhật account. */
    public boolean updateUser(User u) {
        DBContext db = new DBContext();
        Connection con = null;
        PreparedStatement ps = null;
        String sql = "UPDATE Users SET FullName=?, Email=?, Phone=?, RoleId=?, IsActive=? "
                + "WHERE UserId=?";
        try {
            con = db.getConnection();
            ps = con.prepareStatement(sql);
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
        } finally {
            try { db.closeConnection(con, ps, null); } catch (SQLException ex) { ex.printStackTrace(); }
        }
    }

    /** Hard delete - Yêu cầu phải xóa các dữ liệu liên quan trước */
    public String hardDeleteUser(int userId) {
        DBContext db = new DBContext();
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = db.getConnection();
            ps = con.prepareStatement("DELETE FROM Users WHERE UserId = ?");
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
        } finally {
            try { db.closeConnection(con, ps, null); } catch (SQLException ex) { ex.printStackTrace(); }
        }
    }

    /** Quên mật khẩu: Xác thực đúng Username và Email */
    public boolean verifyUsernameAndEmail(String username, String email) {
        DBContext db = new DBContext();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT 1 FROM Users WHERE Username = ? AND Email = ? AND IsActive = 1";
        try {
            con = db.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, email);
            rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { db.closeConnection(con, ps, rs); } catch (SQLException ex) { ex.printStackTrace(); }
        }
        return false;
    }

    /** Quên mật khẩu: Cập nhật mật khẩu mới */
    public boolean updatePassword(String username, String newPasswordHash) {
        DBContext db = new DBContext();
        Connection con = null;
        PreparedStatement ps = null;
        String sql = "UPDATE Users SET PasswordHash = ? WHERE Username = ?";
        try {
            con = db.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, newPasswordHash);
            ps.setString(2, username);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try { db.closeConnection(con, ps, null); } catch (SQLException ex) { ex.printStackTrace(); }
        }
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
