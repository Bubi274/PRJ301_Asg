package controller.Resident;

import dal.BillDAO;
import dal.PaymentDAO;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.VNPayConfig;

@WebServlet("/create-payment")
public class VNPayCreatePaymentServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String[] billIds = request.getParameterValues("billIds");

        if (billIds == null || billIds.length == 0) {
            request.setAttribute("errBill", "Hãy chọn ít nhất một khoản thanh toán.");
            request.getRequestDispatcher("user-bills").forward(request, response);
            return;
        }

        BillDAO billDAO = new BillDAO();
        PaymentDAO paymentDAO = new PaymentDAO();

        double totalAmount = 0;
        String txnRef = String.valueOf(System.currentTimeMillis());

        for (String id : billIds) {
            try {
                int billId = Integer.parseInt(id);
                double amount = billDAO.getBillAmount(billId);
                totalAmount += amount;
                paymentDAO.insertPendingPayment(billId, amount, txnRef);
            } catch (NumberFormatException e) {
                // Ignore invalid ID
            }
        }

        try {
            Map<String, String> vnpParams = new HashMap<>();
            vnpParams.put("vnp_Version", "2.1.0");
            vnpParams.put("vnp_Command", "pay");
            vnpParams.put("vnp_TmnCode", VNPayConfig.vnp_TmnCode);
            vnpParams.put("vnp_Amount", String.valueOf((long) (totalAmount * 100)));
            vnpParams.put("vnp_CurrCode", "VND");
            vnpParams.put("vnp_TxnRef", txnRef);
            vnpParams.put("vnp_OrderInfo", "Thanh toan hoa don chung cu");
            vnpParams.put("vnp_OrderType", "other");
            vnpParams.put("vnp_Locale", "vn");
            vnpParams.put("vnp_ReturnUrl", VNPayConfig.vnp_ReturnUrl);
            vnpParams.put("vnp_IpAddr", request.getRemoteAddr());

            String createDate = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            vnpParams.put("vnp_CreateDate", createDate);

            String paymentUrl = VNPayConfig.getPaymentUrl(vnpParams);
            response.sendRedirect(paymentUrl);
        } catch (Exception e) {
            System.err.println("Loi khi tao link VNPAY: " + e.getMessage());
            response.sendRedirect("user-bills");
        }
    }
}
