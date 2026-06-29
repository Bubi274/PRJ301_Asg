package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import model.Notification;
import utils.DBConnection;

/**
 * DAO class for managing notifications and announcements (UC-MN-08 & UC-ST-05).
 */
public class NotificationDAO {

    /**
     * Sends a global broadcast notification to all residents/users.
     * Sets IsGlobal = 1 and ReceiverId = NULL.
     *
     * @param title    announcement title
     * @param content  announcement body content
     * @param type     type (System, Alert, etc.)
     * @param senderId manager user ID who sends the notification
     */
    public void sendBroadcast(String title, String content, String type, int senderId) {
        String sql = "Insert Into Notifications (Title, Content, Type, SenderId, ReceiverId, ApartmentId, IsRead, CreatedAt, IsGlobal) "
                + "Values (?, ?, ?, ?, Null, Null, 0, Getdate(), 1)";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, title.trim());
            ps.setString(2, content.trim());
            ps.setString(3, type.trim());
            ps.setInt(4, senderId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Loi trong sendBroadcast: " + e.getMessage());
        }
    }

    /**
     * Sends a private notification to a specific user.
     * Sets IsGlobal = 0.
     *
     * @param title       notification title
     * @param content     notification body content
     * @param type        type (Billing, Request, Alert, etc.)
     * @param senderId    user ID of sender
     * @param receiverId  user ID of receiver
     * @param apartmentId apartment ID (0 if none)
     */
    public void sendPrivateNotification(String title, String content, String type, int senderId, int receiverId,
            int apartmentId) {
        String sql = "Insert Into Notifications (Title, Content, Type, SenderId, ReceiverId, ApartmentId, IsRead, CreatedAt, IsGlobal) "
                + "Values (?, ?, ?, ?, ?, ?, 0, Getdate(), 0)";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, title.trim());
            ps.setString(2, content.trim());
            ps.setString(3, type.trim());
            ps.setInt(4, senderId);
            ps.setInt(5, receiverId);
            if (apartmentId > 0) {
                ps.setInt(6, apartmentId);
            } else {
                ps.setNull(6, Types.INTEGER);
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Loi trong sendPrivateNotification: " + e.getMessage());
        }
    }

    /**
     * Retrieves all global broadcast notifications (IsGlobal = 1).
     * Ordered by creation date descending.
     *
     * @return List of Notification objects
     */
    public List<Notification> getGlobalNotifications() {
        List<Notification> list = new ArrayList<>();
        String sql = "Select n.NotificationId, n.Title, n.Content, n.Type, n.SenderId, n.ReceiverId, n.ApartmentId, n.IsRead, n.CreatedAt, n.IsGlobal, "
                + "u.FullName As SenderName "
                + "From Notifications n "
                + "Inner Join Users u On n.SenderId = u.UserId "
                + "Where n.IsGlobal = 1 "
                + "Order By n.CreatedAt Desc";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSetToNotification(rs));
            }
        } catch (SQLException e) {
            System.err.println("Loi trong getGlobalNotifications: " + e.getMessage());
        }
        return list;
    }

    /**
     * Retrieves all notifications sent by a specific manager.
     * Ordered by creation date descending.
     *
     * @param senderId manager user ID
     * @return List of Notification objects
     */
    public List<Notification> getNotificationsBySender(int senderId) {
        List<Notification> list = new ArrayList<>();
        String sql = "Select n.NotificationId, n.Title, n.Content, n.Type, n.SenderId, n.ReceiverId, n.ApartmentId, n.IsRead, n.CreatedAt, n.IsGlobal, "
                + "u.FullName As SenderName "
                + "From Notifications n "
                + "Inner Join Users u On n.SenderId = u.UserId "
                + "Where n.SenderId = ? "
                + "Order By n.CreatedAt Desc";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, senderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToNotification(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Loi trong getNotificationsBySender: " + e.getMessage());
        }
        return list;
    }

    /**
     * Retrieves notifications for a specific user (either global OR directly
     * addressed to them).
     * Ordered by creation date descending.
     *
     * @param userId target user ID (e.g. Resident, Staff)
     * @return List of Notification objects
     */
    public List<Notification> getNotificationsForUser(int userId) {
        List<Notification> list = new ArrayList<>();
        String sql = "Select n.NotificationId, n.Title, n.Content, n.Type, n.SenderId, n.ReceiverId, n.ApartmentId, n.IsRead, n.CreatedAt, n.IsGlobal, "
                + "u.FullName As SenderName "
                + "From Notifications n "
                + "Inner Join Users u On n.SenderId = u.UserId "
                + "Where n.IsGlobal = 1 Or n.ReceiverId = ? "
                + "Order By n.CreatedAt Desc";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToNotification(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Loi trong getNotificationsForUser: " + e.getMessage());
        }
        return list;
    }

    /**
     * Marks a specific notification as read (IsRead = 1).
     *
     * @param notificationId ID of the notification
     */
    public void markAsRead(int notificationId) {
        String sql = "Update Notifications Set IsRead = 1 Where NotificationId = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, notificationId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Loi trong markAsRead: " + e.getMessage());
        }
    }

    public Notification getNotificationById(int id) {
        String sql = "SELECT n.NotificationId, n.Title, n.Content, n.Type, n.SenderId, n.ReceiverId, n.ApartmentId, n.IsRead, n.CreatedAt, n.IsGlobal, u.FullName AS SenderName "
                + "FROM Notifications n "
                + "LEFT JOIN Users u ON n.SenderId = u.UserId "
                + "WHERE n.NotificationId = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToNotification(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Loi trong getNotificationById: " + e.getMessage());
        }
        return null;
    }

    /**
     * Helper to map ResultSet row to Notification object.
     */
    private Notification mapResultSetToNotification(ResultSet rs) throws SQLException {
        Notification n = new Notification();
        n.setNotificationId(rs.getInt("NotificationId"));
        n.setTitle(rs.getString("Title"));
        n.setContent(rs.getString("Content"));
        n.setType(rs.getString("Type"));
        n.setSenderId(rs.getInt("SenderId"));
        n.setReceiverId(rs.getInt("ReceiverId"));
        n.setApartmentId(rs.getInt("ApartmentId"));
        n.setRead(rs.getBoolean("IsRead"));
        n.setCreatedAt(rs.getTimestamp("CreatedAt"));
        n.setGlobal(rs.getBoolean("IsGlobal"));
        n.setSenderName(rs.getString("SenderName"));
        return n;
    }
}
