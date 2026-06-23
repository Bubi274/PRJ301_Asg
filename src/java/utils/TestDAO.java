package utils;

import dal.PositionDAO;
import dal.AssignmentDAO;
import dal.NotificationDAO;
import dal.StaffDAO;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import model.Notification;
import model.Position;
import model.Request;
import model.StaffProfile;
import model.User;

/**
 * Console test class to verify DAO layer implementation.
 * Runs CRUD, Search, and Transaction methods using main function.
 */
public class TestDAO {

    public static void main(String[] args) {
        System.out.println("=== KHOI DONG KIEM THU TANG DAO ===");

        // 1. Kiem tra ket noi CSDL
        try (Connection conn = DBConnection.getConnection()) {
            System.out.println("[DBConnection] Ket noi database thanh cong!");
        } catch (SQLException e) {
            System.err.println("[DBConnection] Ket noi database that bai: " + e.getMessage());
            return;
        }

        PositionDAO positionDAO = new PositionDAO();
        StaffDAO staffDAO = new StaffDAO();
        AssignmentDAO assignmentDAO = new AssignmentDAO();
        NotificationDAO notificationDAO = new NotificationDAO();

        // 2. Kiem thu StaffDAO / PositionDAO
        System.out.println("\n--- TEST POSITIONDAO & STAFFDAO ---");
        
        // 2.1. Lay danh sach vi tri tu PositionDAO
        List<Position> positions = positionDAO.getAllPositions();
        System.out.println("Danh sach cac vi tri (" + positions.size() + "):");
        for (Position pos : positions) {
            System.out.println("  - ID: " + pos.getPositionId() + " | " + pos.getPositionName());
        }

        // 2.2. Lay danh sach nhan vien
        List<StaffProfile> staffList = staffDAO.getAllStaff();
        System.out.println("Danh sach nhan vien hien tai (" + staffList.size() + "):");
        if (!staffList.isEmpty()) {
            StaffProfile first = staffList.get(0);
            System.out.println("  Nhan vien dau tien: " + first.getFullName() 
                    + " | Username: " + first.getUsername() 
                    + " | Vị trí: " + first.getPositionName() 
                    + " | Trang thai: " + first.getStatusLabel());
        }

        // 2.3. Tim kiem nhan vien
        String searchKeyword = "Bao"; // Co the tim 'Bao Tri'
        List<StaffProfile> searchResults = staffDAO.searchStaff(searchKeyword, 0);
        System.out.println("Ket qua tim kiem tu khoa '" + searchKeyword + "' (" + searchResults.size() + "):");
        for (StaffProfile sp : searchResults) {
            System.out.println("  - " + sp.getFullName() + " | Vị trí: " + sp.getPositionName());
        }

        // 2.4. Kiem tra ton tai username / email
        String testUser = "staff_bt";
        boolean userExist = staffDAO.isUsernameExist(testUser);
        System.out.println("Username '" + testUser + "' co ton tai? " + userExist);

        // 2.5. Them moi nhan vien (neu username chua ton tai)
        String newUsername = "staff_test_temp";
        if (!staffDAO.isUsernameExist(newUsername)) {
            User newUser = new User();
            newUser.setUsername(newUsername);
            newUser.setPasswordHash("hash_test_123");
            newUser.setFullName("Nguyen Van Nhan Vien Test");
            newUser.setEmail("staff_test@email.com");
            newUser.setPhone("0999888777");
            
            try {
                // Them nhan vien test voi PositionId = 4 (Nhan vien bao tri)
                staffDAO.addStaff(newUser, 4);
                System.out.println("[StaffDAO] Them nhan vien test '" + newUsername + "' thanh cong!");
                
                // Thu xem thong tin chi tiet nhan vien vua them
                List<StaffProfile> checkList = staffDAO.searchStaff(newUsername, 0);
                if (!checkList.isEmpty()) {
                    StaffProfile newlyAdded = checkList.get(0);
                    System.out.println("  Nhan vien moi add: ID User: " + newlyAdded.getUserId() + " | " + newlyAdded.getFullName());
                    
                    // Test cap nhat nhan vien
                    User userToUpdate = new User();
                    userToUpdate.setUserId(newlyAdded.getUserId());
                    userToUpdate.setFullName("Nguyen Van Nhan Vien Cap Nhat");
                    userToUpdate.setEmail(newlyAdded.getEmail());
                    userToUpdate.setPhone("0111222333");
                    // Doi sang PositionId = 3 (Ky su)
                    staffDAO.updateStaff(userToUpdate, 3);
                    System.out.println("[StaffDAO] Cap nhat thong tin nhan vien thanh cong!");

                    // Test vo hieu hoa nhan vien
                    staffDAO.deactivateStaff(newlyAdded.getUserId());
                    StaffProfile updated = staffDAO.getStaffById(newlyAdded.getUserId());
                    System.out.println("  Nhan vien sau cap nhat: " + updated.getFullName() 
                            + " | Phone: " + updated.getPhone() 
                            + " | Vi tri moi: " + updated.getPositionName()
                            + " | Trang thai: " + updated.getStatusLabel());
                }
            } catch (SQLException e) {
                System.err.println("[StaffDAO] Loi khi them/sua nhan vien: " + e.getMessage());
            }
        } else {
            System.out.println("Username '" + newUsername + "' da ton tai tu truoc, khong chay lai luong add.");
        }


        // 3. Kiem thu AssignmentDAO
        System.out.println("\n--- TEST ASSIGNMENTDAO ---");
        
        // 3.1. Lay danh sach request pending
        List<Request> pendingReqs = assignmentDAO.getPendingRequests();
        System.out.println("So luong yeu cau dang cho xu ly (Pending): " + pendingReqs.size());
        for (Request req : pendingReqs) {
            System.out.println("  - ID: " + req.getRequestId() + " | Tieu de: " + req.getTitle() 
                    + " | Cu dan: " + req.getResidentName() + " | Can ho: " + req.getApartmentNumber());
        }

        // 3.2. Test phan cong request
        if (!pendingReqs.isEmpty()) {
            Request firstPending = pendingReqs.get(0);
            int testRequestId = firstPending.getRequestId();
            
            // Lay nhan vien ky thuat phu hop de phan cong (vi tri id = 4 - nhan vien bao tri)
            List<StaffProfile> technicians = assignmentDAO.getStaffByPosition(4);
            if (!technicians.isEmpty()) {
                int testStaffId = technicians.get(0).getUserId();
                int testManagerId = 2; // Tran Truong Ban (Manager)
                
                System.out.println("Tien hanh phan cong request ID: " + testRequestId 
                        + " cho Nhan vien ID: " + testStaffId + " (" + technicians.get(0).getFullName() + ")");
                try {
                    assignmentDAO.assignRequest(testRequestId, testStaffId, testManagerId);
                    System.out.println("[AssignmentDAO] Phan cong thanh cong (da commit transaction).");
                    
                    // Check lai trang thai
                    Request checkReq = assignmentDAO.getRequestById(testRequestId);
                    System.out.println("  Trang thai moi cua Request " + testRequestId + ": " + checkReq.getStatus() 
                            + " | Duoc duyet boi: " + checkReq.getApprovedByName() 
                            + " | Giao cho: " + checkReq.getAssignedToName());
                } catch (SQLException e) {
                    System.err.println("[AssignmentDAO] Phan cong that bai: " + e.getMessage());
                }
            } else {
                System.out.println("Khong co nhan vien an ninh/ky thuat phu hop de test phan cong.");
            }
        } else {
            System.out.println("Khong co request Pending nao trong DB de test phan cong.");
        }


        // 4. Kiem thu NotificationDAO
        System.out.println("\n--- TEST NOTIFICATIONDAO ---");
        
        // 4.1. Xem danh sach thong bao chung hien tai
        List<Notification> globalNotifs = notificationDAO.getGlobalNotifications();
        System.out.println("So luong thong bao chung (Global) hien tai: " + globalNotifs.size());
        if (!globalNotifs.isEmpty()) {
            System.out.println("  Thong bao moi nhat: " + globalNotifs.get(0).getTitle() 
                    + " | Nguoi gui: " + globalNotifs.get(0).getSenderName() 
                    + " | Ngay tao: " + globalNotifs.get(0).getCreatedAt());
        }

        // 4.2. Test gui thong bao chung (Broadcast)
        String broadcastTitle = "Thong bao test bang java console";
        String broadcastContent = "Noi dung thong bao he thong cap nhat de kiem tra DAO layer.";
        int senderManagerId = 2; // Tran Truong Ban
        
        System.out.println("Gui thong bao chung moi...");
        notificationDAO.sendBroadcast(broadcastTitle, broadcastContent, Notification.TYPE_SYSTEM, senderManagerId);
        
        // Lay lai danh sach de verify
        List<Notification> updatedGlobalNotifs = notificationDAO.getGlobalNotifications();
        System.out.println("So luong thong bao chung sau khi gui: " + updatedGlobalNotifs.size());
        if (!updatedGlobalNotifs.isEmpty()) {
            Notification newlySent = updatedGlobalNotifs.get(0);
            System.out.println("  Thong bao vua gui co dung tieu de khong? " 
                    + newlySent.getTitle().equals(broadcastTitle));
            System.out.println("  Loai badge CSS cua JSP: " + newlySent.getBadgeClass());
        }

        System.out.println("\n=== HOAN THANH KIEM THU TANG DAO ===");
    }
}
