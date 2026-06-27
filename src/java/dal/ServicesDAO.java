package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.ServiceTypes;
import utils.DBConnection;

public class ServicesDAO {

    public List<ServiceTypes> getServicesByResidentId(int residentId) {
        List<ServiceTypes> list = new ArrayList<>();
        String sql = "SELECT st.* FROM ResidentServices rs "
                + "JOIN ServiceTypes st ON rs.ServiceTypeId = st.ServiceTypeId "
                + "WHERE rs.ResidentId = ? AND rs.IsActive = 1";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, residentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ServiceTypes s = new ServiceTypes();
                    s.setServiceTypeId(rs.getInt("ServiceTypeId"));
                    s.setServiceName(rs.getString("ServiceName"));
                    s.setUnit(rs.getString("Unit"));
                    s.setPricePerUnit(rs.getDouble("PricePerUnit"));
                    list.add(s);
                }
            }
        } catch (SQLException e) {
            System.err.println("Loi trong getServicesByResidentId: " + e.getMessage());
        }
        return list;
    }

    public List<ServiceTypes> getAvailableServicesPaging(int residentId, int page, int pageSize) {
        List<ServiceTypes> list = new ArrayList<>();
        int offset = (page - 1) * pageSize;
        String sql = "SELECT * FROM ServiceTypes st "
                + "WHERE NOT EXISTS ( "
                + " SELECT 1 FROM ResidentServices rs "
                + " WHERE rs.ServiceTypeId = st.ServiceTypeId "
                + " AND rs.ResidentId = ? AND rs.IsActive = 1 ) "
                + "ORDER BY st.ServiceTypeId "
                + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, residentId);
            ps.setInt(2, offset);
            ps.setInt(3, pageSize);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ServiceTypes s = new ServiceTypes();
                    s.setServiceTypeId(rs.getInt("ServiceTypeId"));
                    s.setServiceName(rs.getString("ServiceName"));
                    s.setUnit(rs.getString("Unit"));
                    s.setPricePerUnit(rs.getDouble("PricePerUnit"));
                    list.add(s);
                }
            }
        } catch (SQLException e) {
            System.err.println("Loi trong getAvailableServicesPaging: " + e.getMessage());
        }
        return list;
    }

    public int countAvailableServices(int residentId) {
        String sql = "SELECT COUNT(*) FROM ServiceTypes st "
                + "WHERE NOT EXISTS ( "
                + " SELECT 1 FROM ResidentServices rs "
                + " WHERE rs.ServiceTypeId = st.ServiceTypeId "
                + " AND rs.ResidentId = ? AND rs.IsActive = 1 )";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, residentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Loi trong countAvailableServices: " + e.getMessage());
        }
        return 0;
    }

    public void registerServices(int residentId, int apartmentId, String[] serviceIds) {
        String checkSql = "SELECT IsActive FROM ResidentServices WHERE ResidentId = ? AND ServiceTypeId = ?";
        String insertSql = "INSERT INTO ResidentServices (ResidentId, ServiceTypeId, RegisteredAt, IsActive) VALUES (?, ?, GETDATE(), 1)";
        String updateSql = "UPDATE ResidentServices SET IsActive = 1, RegisteredAt = GETDATE() WHERE ResidentId = ? AND ServiceTypeId = ?";
        String priceSql = "SELECT PricePerUnit FROM ServiceTypes WHERE ServiceTypeId = ?";

        BillDAO billDAO = new BillDAO();

        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);
            try {
                for (String id : serviceIds) {
                    int serviceId = Integer.parseInt(id);

                    double price = 0;
                    try (PreparedStatement priceSt = con.prepareStatement(priceSql)) {
                        priceSt.setInt(1, serviceId);
                        try (ResultSet priceRs = priceSt.executeQuery()) {
                            if (priceRs.next()) {
                                price = priceRs.getDouble("PricePerUnit");
                            }
                        }
                    }

                    boolean exists = false;
                    try (PreparedStatement checkSt = con.prepareStatement(checkSql)) {
                        checkSt.setInt(1, residentId);
                        checkSt.setInt(2, serviceId);
                        try (ResultSet rs = checkSt.executeQuery()) {
                            if (rs.next()) {
                                exists = true;
                            }
                        }
                    }

                    if (exists) {
                        try (PreparedStatement updateSt = con.prepareStatement(updateSql)) {
                            updateSt.setInt(1, residentId);
                            updateSt.setInt(2, serviceId);
                            updateSt.executeUpdate();
                        }
                    } else {
                        try (PreparedStatement insertSt = con.prepareStatement(insertSql)) {
                            insertSt.setInt(1, residentId);
                            insertSt.setInt(2, serviceId);
                            insertSt.executeUpdate();
                        }
                    }

                    billDAO.createBill(apartmentId, price, serviceId);
                }
                con.commit();
            } catch (Exception e) {
                con.rollback();
                throw e;
            }
        } catch (Exception e) {
            System.err.println("Loi trong registerServices: " + e.getMessage());
        }
    }

    public void cancelService(int residentId, int serviceId) {
        String deactivateSql = "UPDATE ResidentServices SET IsActive = 0 WHERE ResidentId = ? AND ServiceTypeId = ?";
        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);
            try {
                try (PreparedStatement ps = con.prepareStatement(deactivateSql)) {
                    ps.setInt(1, residentId);
                    ps.setInt(2, serviceId);
                    ps.executeUpdate();
                }

                int apartmentId = new UserDAO().getApartmentIdByUser(residentId);
                new BillDAO().deleteLatestBillByApartment(apartmentId, serviceId);
                
                con.commit();
            } catch (Exception e) {
                con.rollback();
                throw e;
            }
        } catch (Exception e) {
            System.err.println("Loi trong cancelService: " + e.getMessage());
        }
    }
}
