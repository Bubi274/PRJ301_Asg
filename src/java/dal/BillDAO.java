package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.Bills;
import utils.DBConnection;

public class BillDAO {

    public List<Bills> getUnpaidBillsByApartment(int apartmentId) {
        List<Bills> list = new ArrayList<>();
        String sql = "SELECT b.BillId, b.ApartmentId, b.BillingMonth, b.BillingYear, "
                + "b.TotalAmount, b.Status, b.CreatedAt, "
                + "bd.ServiceTypeId, s.ServiceName "
                + "FROM Bills b "
                + "JOIN BillDetails bd ON b.BillId = bd.BillId "
                + "JOIN ServiceTypes s ON bd.ServiceTypeId = s.ServiceTypeId "
                + "WHERE b.ApartmentId = ? AND b.Status='UNPAID' "
                + "ORDER BY b.CreatedAt DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, apartmentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Bills b = new Bills();
                    b.setBillId(rs.getInt("BillId"));
                    b.setApartmentID(rs.getInt("ApartmentId"));
                    b.setBillingMonth(rs.getInt("BillingMonth"));
                    b.setBillingYear(rs.getInt("BillingYear"));
                    b.setTotalAmount(rs.getDouble("TotalAmount"));
                    b.setStatus(rs.getString("Status"));
                    b.setCreatedAt(rs.getDate("CreatedAt"));
                    b.setServiceTypeId(rs.getInt("ServiceTypeId"));
                    b.setServiceName(rs.getString("ServiceName"));
                    list.add(b);
                }
            }
        } catch (SQLException e) {
            System.err.println("Loi trong getUnpaidBillsByApartment: " + e.getMessage());
        }
        return list;
    }

    public double getBillAmount(int billId) {
        String sql = "SELECT TotalAmount FROM Bills WHERE BillId = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, billId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("TotalAmount");
                }
            }
        } catch (SQLException e) {
            System.err.println("Loi trong getBillAmount: " + e.getMessage());
        }
        return 0;
    }

    public void updateBillsToPaid(String txnRef) {
        String sql = "UPDATE b "
                + "SET b.Status='PAID' "
                + "FROM Bills b "
                + "JOIN Payments p ON b.BillId = p.BillId "
                + "WHERE p.TransactionCode = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, txnRef);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Loi trong updateBillsToPaid: " + e.getMessage());
        }
    }

    public void createBill(int apartmentId, double amount, int serviceId) {
        String insertBillSql = "INSERT INTO Bills "
                + "(ApartmentId, BillingMonth, BillingYear, TotalAmount, Status, CreatedAt) "
                + "VALUES (?, MONTH(GETDATE()), YEAR(GETDATE()), ?, 'UNPAID', GETDATE())";

        String insertDetailSql = "INSERT INTO BillDetails(BillId, ServiceTypeId, Quantity, Amount) "
                + "VALUES (?, ?, 1, ?)";

        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);
            try (PreparedStatement psBill = con.prepareStatement(insertBillSql, Statement.RETURN_GENERATED_KEYS)) {
                psBill.setInt(1, apartmentId);
                psBill.setDouble(2, amount);
                psBill.executeUpdate();

                try (ResultSet rs = psBill.getGeneratedKeys()) {
                    if (rs.next()) {
                        int newBillId = rs.getInt(1);
                        try (PreparedStatement psDetail = con.prepareStatement(insertDetailSql)) {
                            psDetail.setInt(1, newBillId);
                            psDetail.setInt(2, serviceId);
                            psDetail.setDouble(3, amount);
                            psDetail.executeUpdate();
                        }
                    }
                }
                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
        } catch (SQLException e) {
            System.err.println("Loi trong createBill: " + e.getMessage());
        }
    }

    public void deleteLatestBillByApartment(int apartmentId, int serviceId) {
        String findBillSql = "SELECT b.BillId FROM Bills b "
                + "JOIN BillDetails bd ON b.BillId = bd.BillId "
                + "WHERE b.ApartmentId = ? AND bd.ServiceTypeId = ? AND b.Status = 'UNPAID'";

        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);
            try (PreparedStatement psFind = con.prepareStatement(findBillSql)) {
                psFind.setInt(1, apartmentId);
                psFind.setInt(2, serviceId);
                try (ResultSet rs = psFind.executeQuery()) {
                    if (rs.next()) {
                        int targetBillId = rs.getInt("BillId");
                        String delDetailSql = "DELETE FROM BillDetails WHERE BillId = ?";
                        try (PreparedStatement psDelDetail = con.prepareStatement(delDetailSql)) {
                            psDelDetail.setInt(1, targetBillId);
                            psDelDetail.executeUpdate();
                        }
                        String delBillSql = "DELETE FROM Bills WHERE BillId = ?";
                        try (PreparedStatement psDelBill = con.prepareStatement(delBillSql)) {
                            psDelBill.setInt(1, targetBillId);
                            psDelBill.executeUpdate();
                        }
                    }
                }
                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
        } catch (SQLException e) {
            System.err.println("Loi trong deleteLatestBillByApartment: " + e.getMessage());
        }
    }
}
