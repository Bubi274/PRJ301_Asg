package utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class VNPayConfig {

    // Mã Website (vnp_TmnCode) và Secret Key (vnp_HashSecret) do VNPAY cung cấp trong môi trường Sandbox (Test)
    public static String vnp_TmnCode = "3I6VKAH4";
    // Key được gửi lúc 15:11 (Mới nhất): 7QBHSIGSAPDXLBMPFRYX7FXB24Z1R30Y
    // Key được gửi lúc 15:10 (Cũ hơn): 0TR8UZOWCNGMLPBTVBF3D19G2V93LC47
    public static String vnp_HashSecret = "7QBHSIGSAPDXLBMPFRYX7FXB24Z1R30Y";
    public static String vnp_Url = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    public static String vnp_ReturnUrl = "http://localhost:8080/TestApartment/vnpay-return";

    public static String hmacSHA512(String key, String data) throws Exception {
        Mac hmac512 = Mac.getInstance("HmacSHA512");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
        hmac512.init(secretKeySpec);
        byte[] bytes = hmac512.doFinal(data.getBytes(StandardCharsets.UTF_8));
        StringBuilder hash = new StringBuilder();
        for (byte b : bytes) {
            hash.append(String.format("%02x", b));
        }
        return hash.toString();
    }

    public static String getPaymentUrl(Map<String, String> params) throws Exception {
        List<String> fieldNames = new ArrayList<>(params.keySet());
        Collections.sort(fieldNames);

        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        for (String fieldName : fieldNames) {
            String fieldValue = params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                hashData.append('&');

                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                query.append('&');
            }
        }

        String hashDataStr = hashData.substring(0, hashData.length() - 1);
        String queryUrl = query.substring(0, query.length() - 1);

        String secureHash = hmacSHA512(vnp_HashSecret, hashDataStr);
        queryUrl += "&vnp_SecureHash=" + secureHash;

        return vnp_Url + "?" + queryUrl;
    }
}
