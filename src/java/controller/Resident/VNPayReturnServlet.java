package controller.Resident;

import dal.BillDAO;
import dal.PaymentDAO;
import java.io.IOException;
import java.util.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import utils.VNPayConfig;

@WebServlet("/vnpay-return")
public class VNPayReturnServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Map<String, String> fields = new HashMap<>();
            for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements();) {
                String fieldName = params.nextElement();
                String fieldValue = request.getParameter(fieldName);
                if (fieldValue != null && fieldValue.length() > 0) {
                    fields.put(fieldName, fieldValue);
                }
            }

            String vnp_SecureHash = fields.remove("vnp_SecureHash");
            List<String> fieldNames = new ArrayList<>(fields.keySet());
            Collections.sort(fieldNames);

            StringBuilder hashData = new StringBuilder();
            for (String field : fieldNames) {
                String value = fields.get(field);
                hashData.append(field);
                hashData.append("=");
                hashData.append(java.net.URLEncoder.encode(value, "US-ASCII"));
                hashData.append("&");
            }
            if (hashData.length() > 0) {
                hashData.deleteCharAt(hashData.length() - 1);
            }

            String checkSum = hmacSHA512(VNPayConfig.vnp_HashSecret, hashData.toString());

            String txnRef = request.getParameter("vnp_TxnRef");
            String responseCode = request.getParameter("vnp_ResponseCode");

            PaymentDAO paymentDAO = new PaymentDAO();
            BillDAO billDAO = new BillDAO();

            boolean isValidSignature = checkSum.equalsIgnoreCase(vnp_SecureHash);

            if (isValidSignature) {
                if ("00".equals(responseCode)) {
                    paymentDAO.updatePaymentStatus(txnRef, "SUCCESS");
                    billDAO.updateBillsToPaid(txnRef);
                    request.setAttribute("status", "SUCCESS");
                    request.setAttribute("message", "Thanh toán hóa đơn thành công!");
                } else {
                    paymentDAO.updatePaymentStatus(txnRef, "FAILED");
                    request.setAttribute("status", "FAILED");
                    request.setAttribute("message", "Thanh toán thất bại (Lỗi: " + responseCode + ")!");
                }
            } else {
                request.setAttribute("status", "FAILED");
                request.setAttribute("message", "Chữ ký giao dịch không hợp lệ hoặc giao dịch đã bị hủy!");
            }

            request.getRequestDispatcher("/Resident/payment-result.jsp").forward(request, response);
        } catch (Exception e) {
            System.err.println("Loi khi xử lý ket qua VNPAY: " + e.getMessage());
            response.sendRedirect("user-bills");
        }
    }

    private String hmacSHA512(String key, String data) throws Exception {
        Mac hmac = Mac.getInstance("HmacSHA512");
        hmac.init(new SecretKeySpec(key.getBytes(), "HmacSHA512"));
        byte[] bytes = hmac.doFinal(data.getBytes());
        StringBuilder hash = new StringBuilder();
        for (byte b : bytes) {
            hash.append(String.format("%02x", b));
        }
        return hash.toString();
    }
}
