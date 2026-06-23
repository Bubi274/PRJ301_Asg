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
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            color: white;
            background:
                linear-gradient(rgba(0,60,70,0.85), rgba(0,40,60,0.9)),
                url('https://images.unsplash.com/photo-1486406146926-c627a92ad1ab?q=80&w=2070&auto=format&fit=crop');
            background-size: cover;
            background-position: center;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
        }

        .header-text {
            text-align: center;
            margin-bottom: 20px;
        }

        .header-text h1 {
            font-size: 36px;
            margin: 0 0 10px 0;
            font-weight: 700;
        }

        .header-text p {
            font-size: 14px;
            color: rgba(255,255,255,0.7);
            margin: 0;
        }

        .stepper {
            display: flex;
            align-items: center;
            justify-content: center;
            margin-bottom: 30px;
            gap: 15px;
            position: relative;
        }

        .step {
            display: flex;
            align-items: center;
            gap: 8px;
            font-size: 13px;
            color: rgba(255,255,255,0.8);
            z-index: 2;
        }

        .step-circle {
            width: 28px;
            height: 28px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-weight: bold;
            font-size: 14px;
        }

        .step.completed .step-circle {
            background-color: #2ecc71;
            color: white;
        }

        .step.active .step-circle {
            background-color: #00d8ff;
            color: white;
        }

        .step-line {
            width: 100px;
            height: 2px;
            background-color: #2ecc71;
            z-index: 1;
        }

        .card {
            width: 480px;
            padding: 35px 40px;
            border-radius: 16px;
            background: rgba(255,255,255,0.1);
            backdrop-filter: blur(20px);
            -webkit-backdrop-filter: blur(20px);
            box-shadow: 0 8px 32px rgba(0, 0, 0, 0.4);
            border: 1px solid rgba(255, 255, 255, 0.15);
            box-sizing: border-box;
        }

        .input-group {
            margin-bottom: 25px;
            position: relative;
        }

        .input-group label {
            display: block;
            margin-bottom: 10px;
            font-weight: bold;
            font-size: 14px;
            color: #00d8ff;
        }
        
        .input-group label i {
            margin-right: 5px;
        }

        .input-wrapper {
            position: relative;
        }

        input {
            width: 100%;
            padding: 14px 40px 14px 15px;
            border-radius: 6px;
            border: 1px solid rgba(255, 255, 255, 0.2);
            background: rgba(255, 255, 255, 0.1);
            color: white;
            box-sizing: border-box;
            font-size: 16px;
            outline: none;
            transition: all 0.3s;
        }

        input:focus {
            border-color: #00d8ff;
            background: rgba(255, 255, 255, 0.15);
        }

        .toggle-password {
            position: absolute;
            right: 15px;
            top: 50%;
            transform: translateY(-50%);
            color: rgba(255,255,255,0.5);
            cursor: pointer;
            transition: color 0.3s;
        }

        .toggle-password:hover {
            color: white;
        }

        /* Thước đo sức mạnh */
        .strength-indicator {
            margin-top: -15px;
            margin-bottom: 25px;
        }
        
        .strength-bar {
            height: 4px;
            background: rgba(255, 255, 255, 0.1);
            border-radius: 2px;
            overflow: hidden;
            margin-top: 10px;
            margin-bottom: 8px;
            display: flex;
        }

        .strength-segment {
            height: 100%;
            flex: 1;
            background: transparent;
            transition: background 0.3s;
        }

        .strength-text {
            font-size: 13px;
            display: flex;
            align-items: center;
            gap: 5px;
        }

        /* Box Yêu cầu mật khẩu */
        .requirements-box {
            background: rgba(0, 0, 0, 0.2);
            border: 1px solid rgba(255, 255, 255, 0.1);
            border-radius: 8px;
            padding: 15px;
            margin-bottom: 25px;
        }

        .requirements-title {
            font-size: 13px;
            font-weight: bold;
            margin-bottom: 10px;
            color: rgba(255,255,255,0.9);
        }

        .req-list {
            list-style: none;
            padding: 0;
            margin: 0;
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 8px;
        }

        .req-list li {
            font-size: 12px;
            color: rgba(255, 255, 255, 0.5);
            display: flex;
            align-items: center;
            gap: 5px;
            transition: color 0.3s;
        }

        .req-list li i {
            font-size: 10px;
        }
        
        .req-list li.valid {
            color: #2ecc71;
        }

        /* Match Indicator */
        .match-indicator {
            font-size: 13px;
            color: rgba(255,255,255,0.5);
            margin-top: 5px;
            margin-bottom: 25px;
            display: flex;
            align-items: center;
            gap: 5px;
        }

        .match-indicator.match {
            color: #2ecc71;
        }

        button {
            width: 100%;
            padding: 15px;
            border: none;
            border-radius: 6px;
            background: #00d8ff;
            color: #003c46;
            font-size: 16px;
            font-weight: bold;
            cursor: pointer;
            transition: all 0.3s;
            display: flex;
            justify-content: center;
            align-items: center;
            gap: 8px;
        }

        button:hover {
            background: #00bce0;
        }
        
        button:disabled {
            background: rgba(255,255,255,0.2);
            color: rgba(255,255,255,0.5);
            cursor: not-allowed;
        }

        .back-link {
            text-align: center;
            margin-top: 25px;
        }

        .back-link a {
            color: rgba(255, 255, 255, 0.6);
            text-decoration: none;
            font-size: 13px;
            transition: color 0.3s;
        }

        .back-link a:hover {
            color: white;
        }

        .error-box {
            background: rgba(231, 76, 60, 0.2);
            color: #ff6b6b;
            border: 1px solid rgba(231, 76, 60, 0.4);
            padding: 12px;
            border-radius: 6px;
            margin-bottom: 20px;
            font-size: 13px;
            text-align: center;
        }
    </style>
</head>
<body>

<div class="header-text">
    <h1>Đặt mật khẩu mới</h1>
    <p>Tạo mật khẩu mạnh để bảo vệ tài khoản của bạn.</p>
</div>

<div class="stepper">
    <div class="step completed">
        <div class="step-circle"><i class="fa-solid fa-check"></i></div>
        Xác thực
    </div>
    <div class="step-line"></div>
    <div class="step active">
        <div class="step-circle">2</div>
        Đặt mật khẩu
    </div>
</div>

<div class="card">
    <% if (request.getAttribute("error") != null) { %>
        <div class="error-box">
            <i class="fa-solid fa-triangle-exclamation"></i>
            <%= request.getAttribute("error") %>
        </div>
    <% } %>

    <form action="${pageContext.request.contextPath}/resetPassword" method="post" id="resetForm">
        
        <div class="input-group">
            <label><i class="fa-solid fa-lock"></i> Mật khẩu mới</label>
            <div class="input-wrapper">
                <input type="password" name="newPassword" id="newPassword" required>
                <i class="fa-solid fa-eye-slash toggle-password" onclick="togglePassword('newPassword', this)"></i>
            </div>
        </div>

        <div class="strength-indicator">
            <div class="strength-bar">
                <div class="strength-segment" id="seg-1"></div>
                <div class="strength-segment" id="seg-2"></div>
                <div class="strength-segment" id="seg-3"></div>
                <div class="strength-segment" id="seg-4"></div>
            </div>
            <div class="strength-text" id="strength-text">
                💪 Chưa nhập mật khẩu
            </div>
        </div>

        <div class="requirements-box">
            <div class="requirements-title">Yêu cầu mật khẩu:</div>
            <ul class="req-list">
                <li id="req-length"><i class="fa-solid fa-check"></i> Ít nhất 8 ký tự</li>
                <li id="req-upper"><i class="fa-solid fa-check"></i> Có chữ hoa (A-Z)</li>
                <li id="req-lower"><i class="fa-solid fa-check"></i> Có chữ thường (a-z)</li>
                <li id="req-number"><i class="fa-solid fa-check"></i> Có chữ số (0-9)</li>
                <li id="req-special"><i class="fa-solid fa-check"></i> Có ký tự đặc biệt (!@#$...)</li>
            </ul>
        </div>

        <div class="input-group" style="margin-bottom: 10px;">
            <label><i class="fa-solid fa-lock"></i> Xác nhận mật khẩu mới</label>
            <div class="input-wrapper">
                <input type="password" name="confirmPassword" id="confirmPassword" required>
                <i class="fa-solid fa-eye-slash toggle-password" onclick="togglePassword('confirmPassword', this)"></i>
            </div>
        </div>

        <div class="match-indicator" id="match-indicator">
            Mật khẩu khớp
        </div>

        <button type="submit" id="submitBtn" disabled>
            <i class="fa-solid fa-check"></i> Xác nhận đặt lại mật khẩu
        </button>

        <div class="back-link">
            <a href="${pageContext.request.contextPath}/login">
                <i class="fa-solid fa-angle-left"></i> Quay lại bước trước
            </a>
        </div>

    </form>
</div>

<script>
    function togglePassword(inputId, icon) {
        var input = document.getElementById(inputId);
        if (input.type === "password") {
            input.type = "text";
            icon.classList.remove("fa-eye-slash");
            icon.classList.add("fa-eye");
        } else {
            input.type = "password";
            icon.classList.remove("fa-eye");
            icon.classList.add("fa-eye-slash");
        }
    }

    const newPassword = document.getElementById('newPassword');
    const confirmPassword = document.getElementById('confirmPassword');
    const submitBtn = document.getElementById('submitBtn');

    // Requirements elements
    const reqLength = document.getElementById('req-length');
    const reqUpper = document.getElementById('req-upper');
    const reqLower = document.getElementById('req-lower');
    const reqNumber = document.getElementById('req-number');
    const reqSpecial = document.getElementById('req-special');

    // Strength elements
    const segments = [
        document.getElementById('seg-1'),
        document.getElementById('seg-2'),
        document.getElementById('seg-3'),
        document.getElementById('seg-4')
    ];
    const strengthText = document.getElementById('strength-text');
    const matchIndicator = document.getElementById('match-indicator');

    function updateRequirementsAndStrength() {
        const val = newPassword.value;
        
        // Check Requirements
        const isLength = val.length >= 8;
        const isUpper = /[A-Z]/.test(val);
        const isLower = /[a-z]/.test(val);
        const isNumber = /[0-9]/.test(val);
        const isSpecial = /[^A-Za-z0-9]/.test(val);

        reqLength.className = isLength ? 'valid' : '';
        reqUpper.className = isUpper ? 'valid' : '';
        reqLower.className = isLower ? 'valid' : '';
        reqNumber.className = isNumber ? 'valid' : '';
        reqSpecial.className = isSpecial ? 'valid' : '';

        // Calculate Strength
        let strength = 0;
        if (isLength) strength++;
        if (isUpper && isLower) strength++;
        if (isNumber) strength++;
        if (isSpecial) strength++;

        // Reset segments
        segments.forEach(seg => seg.style.background = 'transparent');
        strengthText.style.color = 'white';

        if (val.length === 0) {
            strengthText.innerHTML = "💪 Chưa nhập mật khẩu";
        } else if (strength === 0 || val.length < 8) {
            segments[0].style.background = '#e74c3c'; // Yếu
            strengthText.innerHTML = "💪 Yếu";
            strengthText.style.color = '#e74c3c';
        } else if (strength === 2) {
            segments[0].style.background = '#f39c12';
            segments[1].style.background = '#f39c12';
            strengthText.innerHTML = "💪 Trung bình";
            strengthText.style.color = '#f39c12';
        } else if (strength === 3) {
            segments[0].style.background = '#3498db';
            segments[1].style.background = '#3498db';
            segments[2].style.background = '#3498db';
            strengthText.innerHTML = "💪 Mạnh";
            strengthText.style.color = '#3498db';
        } else if (strength >= 4) {
            segments[0].style.background = '#00d8ff';
            segments[1].style.background = '#00d8ff';
            segments[2].style.background = '#00d8ff';
            segments[3].style.background = '#00d8ff';
            strengthText.innerHTML = "💪 Rất mạnh";
            strengthText.style.color = '#00d8ff';
        }

        checkMatchAndForm();
    }

    function checkMatchAndForm() {
        const val1 = newPassword.value;
        const val2 = confirmPassword.value;
        
        const isLength = val1.length >= 8;
        const isUpper = /[A-Z]/.test(val1);
        const isLower = /[a-z]/.test(val1);
        const isNumber = /[0-9]/.test(val1);
        const isSpecial = /[^A-Za-z0-9]/.test(val1);
        
        const allValid = isLength && isUpper && isLower && isNumber && isSpecial;

        if (val2.length > 0) {
            if (val1 === val2) {
                matchIndicator.className = 'match-indicator match';
                matchIndicator.innerHTML = 'Mật khẩu khớp';
            } else {
                matchIndicator.className = 'match-indicator';
                matchIndicator.style.color = '#e74c3c';
                matchIndicator.innerHTML = 'Mật khẩu không khớp';
            }
        } else {
            matchIndicator.className = 'match-indicator';
            matchIndicator.style.color = 'rgba(255,255,255,0.5)';
            matchIndicator.innerHTML = 'Mật khẩu khớp';
        }

        // Enable submit only if all requirements are met and passwords match exactly
        if (allValid && val1 === val2 && val1.length > 0) {
            submitBtn.disabled = false;
        } else {
            submitBtn.disabled = true;
        }
    }

    newPassword.addEventListener('input', updateRequirementsAndStrength);
    confirmPassword.addEventListener('input', checkMatchAndForm);
</script>

</body>
</html>
