<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Đặt lại mật khẩu - Lumina BMS</title>

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">

    <style>
        body {
            margin: 0;
            min-height: 100vh;
            font-family: Arial, sans-serif;
            color: white;
            background:
                linear-gradient(rgba(0,60,70,0.8), rgba(0,40,60,0.85)),
                url('https://images.unsplash.com/photo-1486406146926-c627a92ad1ab?q=80&w=2070&auto=format&fit=crop');
            background-size: cover;
            background-position: center;
        }

        header {
            padding: 25px 40px;
        }

        .logo {
            font-size: 1.2rem;
            font-weight: bold;
            letter-spacing: 1px;
            color: white;
        }

        .logo i {
            margin-right: 8px;
            color: #00d8ff;
        }

        .container {
            width: 500px;
            margin: auto;
            padding-top: 50px;
        }

        .badge {
            color: #00d8ff;
            font-weight: bold;
            letter-spacing: 2px;
            text-transform: uppercase;
            margin-bottom: 10px;
        }

        h1 {
            font-size: 40px;
            margin: 0 0 15px 0;
        }

        .container > p {
            font-size: 1.1rem;
            color: rgba(255,255,255,0.8);
        }

        .card {
            margin-top: 30px;
            padding: 40px;
            border-radius: 16px;
            background: rgba(255,255,255,0.15);
            backdrop-filter: blur(15px);
            -webkit-backdrop-filter: blur(15px);
            box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3);
            border: 1px solid rgba(255, 255, 255, 0.2);
        }

        .card label {
            display: block;
            margin-bottom: 8px;
            font-weight: bold;
        }

        input {
            width: 100%;
            padding: 15px;
            margin: 10px 0 20px 0;
            border-radius: 8px;
            border: none;
            background: rgba(255, 255, 255, 0.1);
            color: white;
            box-sizing: border-box;
            font-size: 1rem;
            outline: none;
            transition: border 0.3s;
        }

        input::placeholder {
            color: rgba(255, 255, 255, 0.6);
        }

        input:focus {
            border: 1px solid #00d8ff;
            background: rgba(255, 255, 255, 0.15);
        }

        button {
            width: 100%;
            padding: 18px;
            border: none;
            border-radius: 8px;
            background: #00d8ff;
            color: white;
            font-size: 18px;
            font-weight: bold;
            cursor: pointer;
            transition: background 0.3s;
        }

        button:hover {
            background: #00bce0;
        }

        .error-box {
            background: rgba(231, 76, 60, 0.2);
            color: #ff6b6b;
            border: 1px solid rgba(231, 76, 60, 0.4);
            padding: 12px 15px;
            border-radius: 8px;
            margin-bottom: 20px;
            font-size: 0.9rem;
        }

        .success-box {
            background: rgba(46, 204, 113, 0.2);
            color: #2ecc71;
            border: 1px solid rgba(46, 204, 113, 0.4);
            padding: 20px;
            border-radius: 8px;
            margin-bottom: 20px;
            font-size: 1rem;
            line-height: 1.8;
        }

        .new-password-display {
            font-size: 24px;
            font-weight: bold;
            color: #00d8ff;
            letter-spacing: 3px;
            background: rgba(0,0,0,0.2);
            padding: 10px 20px;
            border-radius: 8px;
            margin: 10px 0;
            display: inline-block;
        }

        .back-link {
            margin-top: 20px;
            text-align: center;
        }

        .back-link a {
            color: rgba(255, 255, 255, 0.8);
            text-decoration: none;
            font-size: 0.9rem;
            transition: color 0.3s;
        }

        .strength-meter {
            height: 5px;
            background: rgba(255, 255, 255, 0.1);
            margin-top: -15px;
            margin-bottom: 5px;
            border-radius: 3px;
            overflow: hidden;
            transition: all 0.3s ease;
        }

        .strength-meter div {
            height: 100%;
            width: 0;
            transition: all 0.3s ease;
        }

        .strength-text {
            font-size: 0.85rem;
            margin-bottom: 20px;
            display: block;
        }
    </style>
</head>
<body>

<header>
    <div class="logo">
        <i class="fa-solid fa-building"></i>
        VITALITY CMS
    </div>
</header>

<div class="container">

    <div class="badge">Lumina BMS</div>
    <h1>Reset Password</h1>
    <p>Nhập username và email đã đăng ký để nhận mật khẩu mới.</p>

    <div class="card">

        <% if (request.getAttribute("newPassword") != null) { %>
            <!-- Hiển thị mật khẩu mới sau khi reset thành công -->
            <div class="success-box">
                <i class="fa-solid fa-circle-check"></i>
                <strong>Đặt lại mật khẩu thành công!</strong><br><br>
                Mật khẩu mới của bạn là:<br>
                <span class="new-password-display"><%= request.getAttribute("newPassword") %></span><br><br>
                <small>⚠️ Vui lòng đăng nhập và đổi mật khẩu ngay sau đó.</small>
            </div>
            <div class="back-link">
                <a href="${pageContext.request.contextPath}/login">
                    <i class="fa-solid fa-arrow-left"></i> Quay lại Đăng nhập
                </a>
            </div>
        <% } else { %>
            <!-- Form đặt lại mật khẩu -->
            <% if (request.getAttribute("error") != null) { %>
                <div class="error-box">
                    <i class="fa-solid fa-triangle-exclamation"></i>
                    <%= request.getAttribute("error") %>
                </div>
            <% } %>

            <form action="${pageContext.request.contextPath}/resetPassword" method="post" id="resetForm">
                
                <p style="margin-bottom: 20px; font-size: 0.9rem; color: #00d8ff;">
                    <i class="fa-solid fa-circle-user"></i> Xin chào, <strong><%= session.getAttribute("reset_user") %></strong>
                </p>

                <label><i class="fa-solid fa-lock"></i> Mật khẩu mới</label>
                <input type="password" name="newPassword" id="newPassword"
                       placeholder="Nhập mật khẩu mới (tối thiểu 6 ký tự)"
                       minlength="6"
                       required onkeyup="checkStrength()">
                
                <div class="strength-meter"><div id="strength-bar"></div></div>
                <span class="strength-text" id="strength-text"></span>

                <label><i class="fa-solid fa-lock"></i> Xác nhận mật khẩu mới</label>
                <input type="password" name="confirmPassword"
                       placeholder="Nhập lại mật khẩu mới"
                       minlength="6"
                       required>

                <button type="submit">
                    <i class="fa-solid fa-key"></i> Lưu mật khẩu
                </button>

                <div class="back-link">
                    <a href="${pageContext.request.contextPath}/login">
                        <i class="fa-solid fa-arrow-left"></i> Quay lại Đăng nhập
                    </a>
                </div>

            </form>
        <% } %>
    </div>

</div>

<script>
    function checkStrength() {
        var password = document.getElementById("newPassword").value;
        var meter = document.getElementById("strength-bar");
        var text = document.getElementById("strength-text");

        if (password.length === 0) {
            meter.style.width = "0%";
            text.innerHTML = "";
            return;
        }

        var strength = 0;
        
        // Điều kiện 1: Dài hơn 6 ký tự
        if (password.length >= 6) strength += 1;
        // Điều kiện 2: Có kết hợp chữ cái và số
        if (password.match(/(?=.*[a-z])(?=.*[0-9])/i)) strength += 1;
        // Điều kiện 3: Có chữ hoa và ký tự đặc biệt hoặc rất dài
        if (password.match(/(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[^a-zA-Z0-9])/)) strength += 1;
        else if (password.length >= 10 && strength == 2) strength += 1;

        if (password.length < 6) {
            meter.style.width = "33%";
            meter.style.background = "#ff4757"; // Yếu - Đỏ
            text.innerHTML = "Độ mạnh: <span style='color:#ff4757'>Yếu (Cần ít nhất 6 ký tự)</span>";
        } else if (strength === 1) {
            meter.style.width = "33%";
            meter.style.background = "#ff4757"; // Yếu - Đỏ
            text.innerHTML = "Độ mạnh: <span style='color:#ff4757'>Yếu (Nên thêm số và chữ hoa)</span>";
        } else if (strength === 2) {
            meter.style.width = "66%";
            meter.style.background = "#ffa502"; // TB - Vàng
            text.innerHTML = "Độ mạnh: <span style='color:#ffa502'>Trung bình</span>";
        } else if (strength >= 3) {
            meter.style.width = "100%";
            meter.style.background = "#2ed573"; // Mạnh - Xanh lá
            text.innerHTML = "Độ mạnh: <span style='color:#2ed573'>Mạnh</span>";
        }
    }
</script>

</body>
</html>
