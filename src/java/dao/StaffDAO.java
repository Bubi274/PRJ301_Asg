package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.StaffProfile;
import model.User;
import utils.DBConnection;

/**
 * DAO class for managing staff profiles and positions (UC-MN-06).
 * Performs CRUD operations on Users, StaffProfiles, and Positions tables.
 */
public class StaffDAO {

    /**
     * Retrieves all staff profiles.
     * Joins StaffProfiles, Users, and Positions tables.
     *
     * @return List of StaffProfile objects
     */
    public List<StaffProfile> getAllStaff() {
        List<StaffProfile> list = new ArrayList<>();
        String sql = "Select sp.Id, sp.UserId, sp.PositionId, "
                + "u.FullName, u.Username, u.Email, u.Phone, u.IsActive, "
                + "p.PositionName, p.Description As PositionDesc "
                + "From StaffProfiles sp "
                + "Inner Join Users u On sp.UserId = u.UserId "
                + "Inner Join Positions p On sp.PositionId = p.PositionId "
                + "Order By sp.Id Desc";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                StaffProfile staff = mapResultSetToStaffProfile(rs);
                list.add(staff);
            }
        } catch (SQLException e) {
            System.err.println("Loi trong getAllStaff: " + e.getMessage());
        }
        return list;
    }

    /**
     * Searches staff profiles by keyword (username, fullname, email, phone)
     * and filters by Position ID if specified.
     *
     * @param keyword the search query
     * @param positionId position filter (0 to disable)
     * @return List of matched StaffProfile objects
     */
    public List<StaffProfile> searchStaff(String keyword, int positionId) {
        List<StaffProfile> list = new ArrayList<>();
        String sql = "Select sp.Id, sp.UserId, sp.PositionId, "
                + "u.FullName, u.Username, u.Email, u.Phone, u.IsActive, "
                + "p.PositionName, p.Description As PositionDesc "
                + "From StaffProfiles sp "
                + "Inner Join Users u On sp.UserId = u.UserId "
                + "Inner Join Positions p On sp.PositionId = p.PositionId "
                + "Where (u.FullName Like ? Or u.Username Like ? Or u.Email Like ? Or u.Phone Like ?) "
                + "And (? = 0 Or sp.PositionId = ?) "
                + "Order By sp.Id Desc";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String searchPattern = "%" + keyword.trim() + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            ps.setString(3, searchPattern);
            ps.setString(4, searchPattern);
            ps.setInt(5, positionId);
            ps.setInt(6, positionId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToStaffProfile(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Loi trong searchStaff: " + e.getMessage());
        }
        return list;
    }

    /**
     * Retrieves a staff profile by User ID.
     *
     * @param userId the ID of the user
     * @return StaffProfile object, or null if not found
     */
    public StaffProfile getStaffById(int userId) {
        String sql = "Select sp.Id, sp.UserId, sp.PositionId, "
                + "u.FullName, u.Username, u.Email, u.Phone, u.IsActive, "
                + "p.PositionName, p.Description As PositionDesc "
                + "From StaffProfiles sp "
                + "Inner Join Users u On sp.UserId = u.UserId "
                + "Inner Join Positions p On sp.PositionId = p.PositionId "
                + "Where u.UserId = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToStaffProfile(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Loi trong getStaffById: " + e.getMessage());
        }
        return null;
    }

    /**
     * Adds a new staff user and their staff profile.
     * Executes inside a transaction.
     *
     * @param user user details
     * @param positionId position ID
     * @throws SQLException if insert fails
     */
    public void addStaff(User user, int positionId) throws SQLException {
        Connection conn = null;
        PreparedStatement psUser = null;
        PreparedStatement psProfile = null;
        ResultSet rsKeys = null;

        String sqlUser = "Insert Into Users (Username, PasswordHash, FullName, Email, Phone, RoleId, IsActive, CreatedAt) "
                + "Values (?, ?, ?, ?, ?, 3, 1, Getdate())";
        String sqlProfile = "Insert Into StaffProfiles (UserId, PositionId) Values (?, ?)";

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction

            psUser = conn.prepareStatement(sqlUser, Statement.RETURN_GENERATED_KEYS);
            psUser.setString(1, user.getUsername().trim());
            psUser.setString(2, user.getPasswordHash().trim());
            psUser.setString(3, user.getFullName().trim());
            psUser.setString(4, user.getEmail().trim());
            psUser.setString(5, user.getPhone().trim());

            psUser.executeUpdate();
            rsKeys = psUser.getGeneratedKeys();
            int userId = 0;
            if (rsKeys.next()) {
                userId = rsKeys.getInt(1);
            }

            if (userId > 0) {
                psProfile = conn.prepareStatement(sqlProfile);
                psProfile.setInt(1, userId);
                psProfile.setInt(2, positionId);
                psProfile.executeUpdate();
            } else {
                throw new SQLException("Khong lay duoc generated UserId sau khi them User.");
            }

            conn.commit(); // Commit transaction
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback transaction
                } catch (SQLException rollbackEx) {
                    System.err.println("Loi rollback giao dich: " + rollbackEx.getMessage());
                }
            }
            throw e;
        } finally {
            if (rsKeys != null) rsKeys.close();
            if (psUser != null) psUser.close();
            if (psProfile != null) psProfile.close();
            if (conn != null) conn.close();
        }
    }

    /**
     * Updates staff user details and their position.
     * Executes inside a transaction.
     *
     * @param user user details to update
     * @param positionId new position ID
     * @throws SQLException if update fails
     */
    public void updateStaff(User user, int positionId) throws SQLException {
        Connection conn = null;
        PreparedStatement psUser = null;
        PreparedStatement psProfile = null;

        String sqlUser = "Update Users Set FullName = ?, Email = ?, Phone = ? Where UserId = ?";
        String sqlProfile = "Update StaffProfiles Set PositionId = ? Where UserId = ?";

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            psUser = conn.prepareStatement(sqlUser);
            psUser.setString(1, user.getFullName().trim());
            psUser.setString(2, user.getEmail().trim());
            psUser.setString(3, user.getPhone().trim());
            psUser.setInt(4, user.getUserId());
            psUser.executeUpdate();

            psProfile = conn.prepareStatement(sqlProfile);
            psProfile.setInt(1, positionId);
            psProfile.setInt(2, user.getUserId());
            psProfile.executeUpdate();

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    System.err.println("Loi rollback giao dich: " + rollbackEx.getMessage());
                }
            }
            throw e;
        } finally {
            if (psUser != null) psUser.close();
            if (psProfile != null) psProfile.close();
            if (conn != null) conn.close();
        }
    }

    /**
     * Deactivates a staff member (sets IsActive = 0).
     *
     * @param userId user ID of staff member
     */
    public void deactivateStaff(int userId) {
        String sql = "Update Users Set IsActive = 0 Where UserId = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Loi trong deactivateStaff: " + e.getMessage());
        }
    }

    /**
     * Activates a staff member (sets IsActive = 1).
     *
     * @param userId user ID of staff member
     */
    public void activateStaff(int userId) {
        String sql = "Update Users Set IsActive = 1 Where UserId = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Loi trong activateStaff: " + e.getMessage());
        }
    }



    /**
     * Checks if username already exists in the system.
     *
     * @param username username to check
     * @return true if exists, false otherwise
     */
    public boolean isUsernameExist(String username) {
        String sql = "Select Count(*) From Users Where Username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Loi trong isUsernameExist: " + e.getMessage());
        }
        return false;
    }

    /**
     * Checks if email already exists in the system.
     *
     * @param email email to check
     * @return true if exists, false otherwise
     */
    public boolean isEmailExist(String email) {
        String sql = "Select Count(*) From Users Where Email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Loi trong isEmailExist: " + e.getMessage());
        }
        return false;
    }

    /**
     * Helper to map ResultSet row to StaffProfile object.
     */
    private StaffProfile mapResultSetToStaffProfile(ResultSet rs) throws SQLException {
        StaffProfile sp = new StaffProfile();
        sp.setId(rs.getInt("Id"));
        sp.setUserId(rs.getInt("UserId"));
        sp.setPositionId(rs.getInt("PositionId"));
        sp.setFullName(rs.getString("FullName"));
        sp.setUsername(rs.getString("Username"));
        sp.setEmail(rs.getString("Email"));
        sp.setPhone(rs.getString("Phone"));
        sp.setActive(rs.getBoolean("IsActive"));
        sp.setPositionName(rs.getString("PositionName"));
        sp.setPositionDesc(rs.getString("PositionDesc"));
        return sp;
    }
}
