package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Request;
import model.RequestType;
import utils.DBConnection;

/**
 * DAO class for managing support requests (SF4).
 * Handles CRUD and workflow operations for Resident and Staff.
 */
public class RequestDAO {

    /**
     * Retrieves all support requests submitted by a specific resident.
     */
    public List<Request> getRequestsByResident(int residentId) {
        List<Request> list = new ArrayList<>();
        String sql = "Select r.RequestId, r.ResidentId, r.ApartmentId, r.RequestTypeId, "
                + "r.Title, r.Description, r.TransferDate, r.Status, r.CreatedAt, r.ApprovedBy, r.AssignedTo, "
                + "ru.FullName As ResidentName, a.ApartmentNumber, rt.TypeName As RequestTypeName "
                + "From Requests r "
                + "Inner Join Users ru On r.ResidentId = ru.UserId "
                + "Inner Join Apartments a On r.ApartmentId = a.ApartmentId "
                + "Inner Join RequestTypes rt On r.RequestTypeId = rt.RequestTypeId "
                + "Where r.ResidentId = ? "
                + "Order By r.CreatedAt Desc";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, residentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Request r = mapResultSetToRequest(rs);
                    list.add(r);
                }
            }
        } catch (SQLException e) {
            System.err.println("Loi trong getRequestsByResident: " + e.getMessage());
        }
        return list;
    }

    /**
     * Retrieves request details by ID including join fields.
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
     * Retrieves all requests assigned to a specific staff member.
     */
    public List<Request> getTasksByStaff(int staffId) {
        List<Request> list = new ArrayList<>();
        String sql = "Select r.RequestId, r.ResidentId, r.ApartmentId, r.RequestTypeId, "
                + "r.Title, r.Description, r.TransferDate, r.Status, r.CreatedAt, r.ApprovedBy, r.AssignedTo, "
                + "ru.FullName As ResidentName, a.ApartmentNumber, rt.TypeName As RequestTypeName "
                + "From Requests r "
                + "Inner Join Users ru On r.ResidentId = ru.UserId "
                + "Inner Join Apartments a On r.ApartmentId = a.ApartmentId "
                + "Inner Join RequestTypes rt On r.RequestTypeId = rt.RequestTypeId "
                + "Where r.AssignedTo = ? "
                + "Order By r.CreatedAt Desc";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, staffId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Request r = mapResultSetToRequest(rs);
                    list.add(r);
                }
            }
        } catch (SQLException e) {
            System.err.println("Loi trong getTasksByStaff: " + e.getMessage());
        }
        return list;
    }

    /**
     * Retrieves all support request categories (RequestTypes).
     */
    public List<RequestType> getAllRequestTypes() {
        List<RequestType> list = new ArrayList<>();
        String sql = "Select RequestTypeId, TypeName, DefaultPositionId From RequestTypes Order By TypeName";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                RequestType rt = new RequestType(
                        rs.getInt("RequestTypeId"),
                        rs.getString("TypeName"),
                        rs.getInt("DefaultPositionId")
                );
                list.add(rt);
            }
        } catch (SQLException e) {
            System.err.println("Loi trong getAllRequestTypes: " + e.getMessage());
        }
        return list;
    }

    /**
     * Inserts a new support request into the database.
     */
    public boolean insertRequest(Request req) {
        String sql = "Insert Into Requests (ResidentId, ApartmentId, RequestTypeId, Title, Description, Status, CreatedAt) "
                + "Values (?, ?, ?, ?, ?, 'Pending', Getdate())";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, req.getResidentId());
            ps.setInt(2, req.getApartmentId());
            ps.setInt(3, req.getRequestTypeId());
            ps.setString(4, req.getTitle().trim());
            if (req.getDescription() == null || req.getDescription().trim().isEmpty()) {
                ps.setNull(5, java.sql.Types.NVARCHAR);
            } else {
                ps.setString(5, req.getDescription().trim());
            }
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Loi trong insertRequest: " + e.getMessage());
            return false;
        }
    }

    /**
     * Updates request status and logs the history change in RequestLogs in a transaction.
     */
    public boolean updateRequestStatus(int requestId, String oldStatus, String newStatus, int updatedBy) {
        String sqlUpdate = "Update Requests Set Status = ? Where RequestId = ?";
        if ("Cancelled".equalsIgnoreCase(newStatus)) {
            sqlUpdate = "Update Requests Set Status = ?, AssignedTo = NULL, ApprovedBy = NULL Where RequestId = ?";
        }
        String sqlLog = "Insert Into RequestLogs (RequestId, UpdatedBy, OldStatus, NewStatus, UpdatedAt) "
                + "Values (?, ?, ?, ?, Getdate())";

        Connection conn = null;
        PreparedStatement psUpdate = null;
        PreparedStatement psLog = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            psUpdate = conn.prepareStatement(sqlUpdate);
            psUpdate.setString(1, newStatus);
            psUpdate.setInt(2, requestId);
            int rows = psUpdate.executeUpdate();

            if (rows > 0) {
                psLog = conn.prepareStatement(sqlLog);
                psLog.setInt(1, requestId);
                psLog.setInt(2, updatedBy);
                psLog.setString(3, oldStatus);
                psLog.setString(4, newStatus);
                psLog.executeUpdate();

                conn.commit();
                return true;
            } else {
                conn.rollback();
                return false;
            }
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.err.println("Loi rollback: " + ex.getMessage());
                }
            }
            System.err.println("Loi trong updateRequestStatus: " + e.getMessage());
            return false;
        } finally {
            try {
                if (psUpdate != null) psUpdate.close();
                if (psLog != null) psLog.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                System.err.println("Loi dong resource: " + ex.getMessage());
            }
        }
    }

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
