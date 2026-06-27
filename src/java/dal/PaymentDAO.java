package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Payments;
import utils.DBConnection;

public class PaymentDAO {

    public void insertPendingPayment(int billId, double amount, String txnRef) {
        String sql = "INSERT INTO Payments (BillId, PaymentMethod, PaymentDate, Amount, TransactionCode, Status) "
                + "VALUES (?, 'VNPAY', GETDATE(), ?, ?, 'PENDING')";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, billId);
            ps.setDouble(2, amount);
            ps.setString(3, txnRef);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Loi trong insertPendingPayment: " + e.getMessage());
        }
    }

    public void updatePaymentStatus(String txnRef, String status) {
        String sql = "UPDATE Payments SET Status = ? WHERE TransactionCode = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setString(2, txnRef);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Loi trong updatePaymentStatus: " + e.getMessage());
        }
    }

    public List<Payments> getPaymentsByMonthYear(int apartmentId, Integer month, Integer year) {
        List<Payments> list = new ArrayList<>();
        String sql = "SELECT p.* FROM Payments p "
                + "JOIN Bills b ON p.BillId = b.BillId "
                + "WHERE b.ApartmentId = ? "
                + "AND UPPER(p.Status) = 'SUCCESS'";

        if (month != null) {
            sql += " AND MONTH(p.PaymentDate) = ?";
        }
        if (year != null) {
            sql += " AND YEAR(p.PaymentDate) = ?";
        }
        sql += " ORDER BY p.PaymentDate DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            int index = 1;
            ps.setInt(index++, apartmentId);
            if (month != null) {
                ps.setInt(index++, month);
            }
            if (year != null) {
                ps.setInt(index++, year);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Payments p = new Payments();
                    p.setPaymentId(rs.getInt("PaymentId"));
                    p.setBillId(rs.getInt("BillId"));
                    p.setPaymentMethod(rs.getString("PaymentMethod"));
                    p.setPaymentDate(rs.getDate("PaymentDate"));
                    p.setAmount(rs.getDouble("Amount"));
                    p.setTransactionCode(rs.getString("TransactionCode"));
                    p.setStatus(rs.getString("Status"));
                    list.add(p);
                }
            }
        } catch (SQLException e) {
            System.err.println("Loi trong getPaymentsByMonthYear: " + e.getMessage());
        }
        return list;
    }
}
