package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.Apartments;
import utils.DBConnection;

public class ApartmentDAO {

    public Apartments getApartmentByResidentId(int userId) {
        String sql = "SELECT a.*, u.FullName AS OwnerName FROM Apartments a "
                + "JOIN ApartmentResidents ar ON a.ApartmentId = ar.ApartmentId "
                + "LEFT JOIN Users u ON a.OwnerId = u.UserId "
                + "WHERE ar.UserId = ? AND ar.IsActive = 1";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Apartments a = new Apartments();
                    a.setApartmentId(rs.getInt("ApartmentId"));
                    a.setApartmentNumber(rs.getString("ApartmentNumber"));
                    a.setFloor(rs.getInt("Floor"));
                    a.setArea(rs.getDouble("Area"));
                    a.setTypes(rs.getString("Types"));
                    a.setStatus(rs.getString("Status"));
                    a.setOwnerId(rs.getInt("OwnerId"));
                    a.setOwnerName(rs.getString("OwnerName"));
                    return a;
                }
            }
        } catch (SQLException e) {
            System.err.println("Loi trong getApartmentByResidentId: " + e.getMessage());
        }
        return null;
    }

    public int countTotalApartments() {
        String sql = "SELECT COUNT(*) FROM Apartments";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("Loi trong countTotalApartments: " + e.getMessage());
        }
        return 0;
    }

    public int countApartmentsByStatus(String status) {
        String sql = "SELECT COUNT(*) FROM Apartments WHERE Status = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Loi trong countApartmentsByStatus: " + e.getMessage());
        }
        return 0;
    }

    public boolean isApartmentNumberExists(String apartmentNumber) {
        String sql = "SELECT 1 FROM Apartments WHERE ApartmentNumber = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, apartmentNumber);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Loi trong isApartmentNumberExists: " + e.getMessage());
        }
        return false;
    }

    public int countAllApartments(String searchKeyword, String statusFilter, Integer floorFilter) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM Apartments a LEFT JOIN Users u ON a.OwnerId = u.UserId WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            sql.append(" AND (a.ApartmentNumber LIKE ? OR u.FullName LIKE ?)");
            params.add("%" + searchKeyword.trim() + "%");
            params.add("%" + searchKeyword.trim() + "%");
        }
        if (statusFilter != null && !statusFilter.trim().isEmpty()) {
            sql.append(" AND a.Status = ?");
            params.add(statusFilter.trim());
        }
        if (floorFilter != null) {
            sql.append(" AND a.Floor = ?");
            params.add(floorFilter);
        }

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Loi trong countAllApartments: " + e.getMessage());
        }
        return 0;
    }

    public List<Apartments> getAllApartments(String searchKeyword, String statusFilter, Integer floorFilter, int offset, int limit) {
        List<Apartments> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT a.*, u.FullName AS OwnerName FROM Apartments a LEFT JOIN Users u ON a.OwnerId = u.UserId WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            sql.append(" AND (a.ApartmentNumber LIKE ? OR u.FullName LIKE ?)");
            params.add("%" + searchKeyword.trim() + "%");
            params.add("%" + searchKeyword.trim() + "%");
        }
        if (statusFilter != null && !statusFilter.trim().isEmpty()) {
            sql.append(" AND a.Status = ?");
            params.add(statusFilter.trim());
        }
        if (floorFilter != null) {
            sql.append(" AND a.Floor = ?");
            params.add(floorFilter);
        }
        sql.append(" ORDER BY a.ApartmentNumber ASC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
        params.add(offset);
        params.add(limit);

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Apartments a = new Apartments();
                    a.setApartmentId(rs.getInt("ApartmentId"));
                    a.setApartmentNumber(rs.getString("ApartmentNumber"));
                    a.setFloor(rs.getInt("Floor"));
                    a.setArea(rs.getDouble("Area"));
                    a.setTypes(rs.getString("Types"));
                    a.setStatus(rs.getString("Status"));
                    a.setOwnerId(rs.getInt("OwnerId"));
                    a.setOwnerName(rs.getString("OwnerName"));
                    list.add(a);
                }
            }
        } catch (SQLException e) {
            System.err.println("Loi trong getAllApartments: " + e.getMessage());
        }
        return list;
    }

    public boolean addApartment(Apartments apartment) {
        String sqlApartment = "INSERT INTO Apartments (ApartmentNumber, Floor, Area, Types, Status, OwnerId) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlResident = "INSERT INTO ApartmentResidents (ApartmentId, UserId, ResidentType, MoveInDate, IsActive) VALUES (?, ?, 'Owner', GETDATE(), 1)";

        Connection con = null;
        PreparedStatement psApartment = null;
        PreparedStatement psResident = null;
        ResultSet rsKeys = null;

        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false);

            // Insert into Apartments
            psApartment = con.prepareStatement(sqlApartment, Statement.RETURN_GENERATED_KEYS);
            psApartment.setString(1, apartment.getApartmentNumber());
            psApartment.setInt(2, apartment.getFloor());
            psApartment.setDouble(3, apartment.getArea());
            psApartment.setString(4, apartment.getTypes());
            psApartment.setString(5, apartment.getStatus());
            if (apartment.getOwnerId() > 0) {
                psApartment.setInt(6, apartment.getOwnerId());
            } else {
                psApartment.setNull(6, java.sql.Types.INTEGER);
            }

            int affectedRows = psApartment.executeUpdate();
            if (affectedRows == 0) {
                con.rollback();
                return false;
            }

            // Get generated ApartmentId
            rsKeys = psApartment.getGeneratedKeys();
            int apartmentId = 0;
            if (rsKeys.next()) {
                apartmentId = rsKeys.getInt(1);
            } else {
                con.rollback();
                return false;
            }

            // If an owner is assigned, insert into ApartmentResidents
            if (apartment.getOwnerId() > 0) {
                psResident = con.prepareStatement(sqlResident);
                psResident.setInt(1, apartmentId);
                psResident.setInt(2, apartment.getOwnerId());
                psResident.executeUpdate();
            }

            con.commit();
            return true;
        } catch (SQLException e) {
            System.err.println("Loi trong addApartment: " + e.getMessage());
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    System.err.println("Loi khi rollback addApartment: " + ex.getMessage());
                }
            }
            return false;
        } finally {
            if (rsKeys != null) try { rsKeys.close(); } catch (SQLException e) {}
            if (psApartment != null) try { psApartment.close(); } catch (SQLException e) {}
            if (psResident != null) try { psResident.close(); } catch (SQLException e) {}
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (SQLException e) {}
            }
        }
    }
}
