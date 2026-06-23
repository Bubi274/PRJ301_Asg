<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.io.*" %>
<%@ page import="dal.DBContext" %>
<!DOCTYPE html>
<html>
<head>
    <title>Kiểm tra Database Connection</title>
</head>
<body>
    <h2>Kết quả kết nối Database:</h2>
    <%
        try {
            DBContext db = new DBContext();
            Connection con = db.getConnection();
            if (con != null && !con.isClosed()) {
                out.println("<p style='color:green; font-weight:bold;'>✅ KẾT NỐI DATABASE THÀNH CÔNG!</p>");
                
                // Test Query
                PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) FROM Users");
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    out.println("<p>✅ Tìm thấy bảng Users. Số lượng user: " + rs.getInt(1) + "</p>");
                }
                
                // Test Query 2
                PreparedStatement ps2 = con.prepareStatement("SELECT 1 FROM Users WHERE Username = 'admin_gd' AND Email = 'gd@happyhouse.com'");
                ResultSet rs2 = ps2.executeQuery();
                if (rs2.next()) {
                    out.println("<p style='color:blue;'>✅ Bảng Users CÓ CHỨA admin_gd và gd@happyhouse.com</p>");
                } else {
                    out.println("<p style='color:red;'>❌ Bảng Users KHÔNG CHỨA admin_gd và gd@happyhouse.com</p>");
                }
                
                con.close();
            } else {
                out.println("<p style='color:red; font-weight:bold;'>❌ KẾT NỐI THẤT BẠI (Connection Null)</p>");
            }
        } catch (Exception e) {
            out.println("<p style='color:red; font-weight:bold;'>❌ LỖI KẾT NỐI (Exception):</p>");
            out.println("<pre style='background:#eee; padding:10px;'>");
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            out.println(sw.toString());
            out.println("</pre>");
        }
    %>
</body>
</html>
