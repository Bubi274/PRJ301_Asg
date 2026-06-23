package dao;

import dal.DBContext;
import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    /** UC-AD-01: Lấy user theo username để verify password ở Servlet */
    public User getByUsername(String username) {
        DBContext db = new DBContext();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM Users WHERE Username = ?";
        try {
            con = db.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { db.closeConnection(con, ps, rs); } catch (SQLException ex) { ex.printStackTrace(); }
        }
        return null;
    }

    /** UC-AD-01: Check tài khoản có đang bị lockout không (gọi sp_Login_CheckLockout) */
    public boolean isLockedOut(String username) {
        DBContext db = new DBContext();
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            con = db.getConnection();
            cs = con.prepareCall("{call sp_Login_CheckLockout(?)}");
            cs.setString(1, username);
            rs = cs.executeQuery();
            return rs.next(); // có dòng => đang bị khóa
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { db.closeConnection(con, cs, rs); } catch (SQLException ex) { ex.printStackTrace(); }
        }
        return false;
    }

    /** UC-AD-01: Gọi khi sai password */
    public void onFailedPassword(String username) {
        callSimpleProc("{call sp_Login_OnFailedPassword(?)}", username);
    }

    /** UC-AD-01: Gọi khi login thành công */
    public void onLoginSuccess(String username) {
        callSimpleProc("{call sp_Login_OnSuccess(?)}", username);
    }

    private void callSimpleProc(String sql, String username) {
        DBContext db = new DBContext();
        Connection con = null;
        CallableStatement cs = null;
        try {
            con = db.getConnection();
            cs = con.prepareCall(sql);
            cs.setString(1, username);
            cs.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { db.closeConnection(con, cs, null); } catch (SQLException ex) { ex.printStackTrace(); }
        }
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

    /** UC-AD-04: Cập nhật account. UpdatedBy lấy từ session admin đang đăng nhập. */
    public boolean updateUser(User u, int updatedByAdminId) {
        DBContext db = new DBContext();
        Connection con = null;
        PreparedStatement ps = null;
        // UpdatedAt KHÔNG set ở đây — trigger trg_Users_UpdatedAt tự động set
        String sql = "UPDATE Users SET FullName=?, Email=?, Phone=?, RoleId=?, IsActive=?, UpdatedBy=? "
                + "WHERE UserId=?";
        try {
            con = db.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, u.getFullName());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getPhone());
            ps.setInt(4, u.getRoleId());
            ps.setBoolean(5, u.isActive());
            ps.setInt(6, updatedByAdminId);
            ps.setInt(7, u.getUserId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try { db.closeConnection(con, ps, null); } catch (SQLException ex) { ex.printStackTrace(); }
        }
    }

    /** UC-AD-05: Soft delete — chỉ disable Users.IsActive, KHÔNG động ApartmentResidents/ResidentServices */
    public String softDeleteUser(int userId) {
        DBContext db = new DBContext();
        Connection con = null;
        CallableStatement cs = null;
        try {
            con = db.getConnection();
            cs = con.prepareCall("{call sp_DeleteUserAccount(?)}");
            cs.setInt(1, userId);
            cs.execute();
            return "SUCCESS";
        } catch (SQLException e) {
            if (e.getErrorCode() == 50001) {
                return "NOT_FOUND";
            }
            e.printStackTrace();
            return "ERROR";
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        } finally {
            try { db.closeConnection(con, cs, null); } catch (SQLException ex) { ex.printStackTrace(); }
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
        u.setUpdatedAt(rs.getTimestamp("UpdatedAt"));
        int updatedBy = rs.getInt("UpdatedBy");
        u.setUpdatedBy(rs.wasNull() ? null : updatedBy);
        u.setCreatedAt(rs.getTimestamp("CreatedAt"));
        u.setLastLogin(rs.getTimestamp("LastLogin"));
        u.setFailedLoginCount(rs.getInt("FailedLoginCount"));
        u.setLockoutUntil(rs.getTimestamp("LockoutUntil"));
        return u;
    }
}
