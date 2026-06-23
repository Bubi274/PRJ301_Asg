package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Request;
import model.StaffProfile;
import utils.DBConnection;

/**
 * DAO class for managing request assignments (UC-MN-07).
 * Connects Managers, Requests, and Staff.
 */
public class AssignmentDAO {

    /**
     * Retrieves all requests with 'Pending' status.
     * Joins Requests, Users, Apartments, and RequestTypes.
     *
     * @return List of pending Request objects
     */
    public List<Request> getPendingRequests() {
        List<Request> list = new ArrayList<>();
        String sql = "Select r.RequestId, r.ResidentId, r.ApartmentId, r.RequestTypeId, "
                + "r.Title, r.Description, r.TransferDate, r.Status, r.CreatedAt, r.ApprovedBy, r.AssignedTo, "
                + "ru.FullName As ResidentName, a.ApartmentNumber, rt.TypeName As RequestTypeName "
                + "From Requests r "
                + "Inner Join Users ru On r.ResidentId = ru.UserId "
                + "Inner Join Apartments a On r.ApartmentId = a.ApartmentId "
                + "Inner Join RequestTypes rt On r.RequestTypeId = rt.RequestTypeId "
                + "Where r.Status = 'Pending' "
                + "Order By r.RequestId Desc";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Request req = mapResultSetToRequest(rs);
                list.add(req);
            }
        } catch (SQLException e) {
            System.err.println("Loi trong getPendingRequests: " + e.getMessage());
        }
        return list;
    }

    /**
     * Retrieves a request details by request ID, including join names.
     *
     * @param requestId request ID
     * @return Request object, or null if not found
     */
    public Request getRequestById(int requestId) {
        String sql = "Select r.RequestId, r.ResidentId, r.ApartmentId, r.RequestTypeId, "
                + "r.Title, r.Description, r.TransferDate, r.Status, r.CreatedAt, r.ApprovedBy, r.AssignedTo, "
                + "ru.FullName As ResidentName, a.ApartmentNumber, rt.TypeName As RequestTypeName, "
                + "sa.FullName As AssignedToName, ma.FullName As ApprovedByName "
                + "From Requests r "
                + "Inner Join Users ru On r.ResidentId = ru.UserId "
                + "Inner Join Apartments a On r.ApartmentId = a.ApartmentId "
                + "Inner Join RequestTypes rt On r.RequestTypeId = rt.RequestTypeId "
                + "Left Join Users sa On r.AssignedTo = sa.UserId "
                + "Left Join Users ma On r.ApprovedBy = ma.UserId "
                + "Where r.RequestId = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, requestId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Request r = mapResultSetToRequest(rs);
                    r.setAssignedToName(rs.getString("AssignedToName"));
                    r.setApprovedByName(rs.getString("ApprovedByName"));
                    return r;
                }
            }
        } catch (SQLException e) {
            System.err.println("Loi trong getRequestById: " + e.getMessage());
        }
        return null;
    }

    /**
     * Retrieves active staff members who hold a specific position ID.
     * Useful for suggesting appropriate staff for a specific request type.
     *
     * @param positionId the required position ID
     * @return List of active StaffProfile objects
     */
    public List<StaffProfile> getStaffByPosition(int positionId) {
        List<StaffProfile> list = new ArrayList<>();
        String sql = "Select sp.Id, sp.UserId, sp.PositionId, "
                + "u.FullName, u.Username, u.Email, u.Phone, u.IsActive, "
                + "p.PositionName, p.Description As PositionDesc "
                + "From StaffProfiles sp "
                + "Inner Join Users u On sp.UserId = u.UserId "
                + "Inner Join Positions p On sp.PositionId = p.PositionId "
                + "Where sp.PositionId = ? And u.IsActive = 1";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, positionId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
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
                    list.add(sp);
                }
            }
        } catch (SQLException e) {
            System.err.println("Loi trong getStaffByPosition: " + e.getMessage());
        }
        return list;
    }

    /**
     * Assigns a request to a staff member.
     * Updates Requests table and inserts a new log record into RequestLogs inside a transaction.
     *
     * @param requestId the ID of the request
     * @param staffId the ID of the staff member
     * @param managerId the ID of the manager performing the assignment
     * @throws SQLException if transaction fails
     */
    public void assignRequest(int requestId, int staffId, int managerId) throws SQLException {
        Connection conn = null;
        PreparedStatement psUpdate = null;
        PreparedStatement psLog = null;

        String sqlUpdate = "Update Requests Set Status = 'Approved', AssignedTo = ?, ApprovedBy = ? "
                + "Where RequestId = ? And Status = 'Pending'";
        String sqlLog = "Insert Into RequestLogs (RequestId, UpdatedBy, OldStatus, NewStatus, UpdatedAt) "
                + "Values (?, ?, 'Pending', 'Approved', Getdate())";

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction

            psUpdate = conn.prepareStatement(sqlUpdate);
            psUpdate.setInt(1, staffId);
            psUpdate.setInt(2, managerId);
            psUpdate.setInt(3, requestId);
            int rows = psUpdate.executeUpdate();

            if (rows == 0) {
                throw new SQLException("Khong the cap nhat Request. Yeu cau co the khong ton tai hoac da duoc xu ly truoc do.");
            }

            psLog = conn.prepareStatement(sqlLog);
            psLog.setInt(1, requestId);
            psLog.setInt(2, managerId);
            psLog.executeUpdate();

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
            if (psUpdate != null) psUpdate.close();
            if (psLog != null) psLog.close();
            if (conn != null) conn.close();
        }
    }

    /**
     * Helper to map ResultSet row to Request object.
     */
    private Request mapResultSetToRequest(ResultSet rs) throws SQLException {
        Request r = new Request();
        r.setRequestId(rs.getInt("RequestId"));
        r.setResidentId(rs.getInt("ResidentId"));
        r.setApartmentId(rs.getInt("ApartmentId"));
        r.setRequestTypeId(rs.getInt("RequestTypeId"));
        r.setTitle(rs.getString("Title"));
        r.setDescription(rs.getString("Description"));
        r.setTransferDate(rs.getTimestamp("TransferDate"));
        r.setStatus(rs.getString("Status"));
        r.setCreatedAt(rs.getTimestamp("CreatedAt"));
        r.setApprovedBy(rs.getInt("ApprovedBy"));
        r.setAssignedTo(rs.getInt("AssignedTo"));
        r.setResidentName(rs.getString("ResidentName"));
        r.setApartmentNumber(rs.getString("ApartmentNumber"));
        r.setRequestTypeName(rs.getString("RequestTypeName"));
        return r;
    }
}
