package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Position;
import utils.DBConnection;

/**
 * DAO class for managing positions lookup (Positions table).
 * Shared lookup service across modules.
 */
public class PositionDAO {

    /**
     * Retrieves all staff positions from the database.
     *
     * @return List of Position objects
     */
    public List<Position> getAllPositions() {
        List<Position> list = new ArrayList<>();
        String sql = "Select PositionId, PositionName, Description From Positions Order By PositionId";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Position pos = new Position();
                pos.setPositionId(rs.getInt("PositionId"));
                pos.setPositionName(rs.getString("PositionName"));
                pos.setDescription(rs.getString("Description"));
                list.add(pos);
            }
        } catch (SQLException e) {
            System.err.println("Loi trong getAllPositions: " + e.getMessage());
        }
        return list;
    }

    /**
     * Retrieves a specific position by its ID.
     *
     * @param positionId position ID
     * @return Position object, or null if not found
     */
    public Position getPositionById(int positionId) {
        String sql = "Select PositionId, PositionName, Description From Positions Where PositionId = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, positionId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Position pos = new Position();
                    pos.setPositionId(rs.getInt("PositionId"));
                    pos.setPositionName(rs.getString("PositionName"));
                    pos.setDescription(rs.getString("Description"));
                    return pos;
                }
            }
        } catch (SQLException e) {
            System.err.println("Loi trong getPositionById: " + e.getMessage());
        }
        return null;
    }
}
