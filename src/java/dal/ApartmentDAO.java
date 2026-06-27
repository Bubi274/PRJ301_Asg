package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
}
