<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Đăng nhập - Lumina BMS</title>

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
            max-width: 600px;
            margin: auto;
            padding-top: 50px;
            display: flex;
            flex-direction: column;
            align-items: center;
        }

        .badge {
            color: #00d8ff;
            font-weight: bold;
            letter-spacing: 2px;
            text-transform: uppercase;
            margin-bottom: 10px;
            text-align: center;
        }

        h1 {
            font-size: 45px;
            margin: 0 0 15px 0;
            text-align: center;
            white-space: nowrap;
        }

        .container > p {
            font-size: 1.1rem;
            color: rgba(255,255,255,0.8);
        }

        .login-card {
            width: 500px;
            margin-top: 30px;
            padding: 40px;
            border-radius: 16px;

            background: rgba(255,255,255,0.15);
            backdrop-filter: blur(15px);
            -webkit-backdrop-filter: blur(15px);
            box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3);
            border: 1px solid rgba(255, 255, 255, 0.2);
        }

        .login-card label {
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

        .input-wrapper {
            position: relative;
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

        .forgot-password {
            margin-top: 20px;
            text-align: center;
        }

        .forgot-password a {
            color: rgba(255, 255, 255, 0.8);
            text-align: center;
            white-space: nowrap;
            text-decoration: none;
            font-size: 0.9rem;
            transition: color 0.3s;
        }

        .forgot-password a:hover {
            color: #00d8ff;
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
    <h1>Chào mừng trở lại</h1>
    <p>Truy cập an toàn vào hệ thống quản lý tòa nhà.</p>

    <div class="login-card">
        <% if (request.getAttribute("error") != null) { %>
            <div class="error-box"><%= request.getAttribute("error") %></div>
        <% } %>

        <form action="login" method="post">

            <label>Tên đăng nhập</label>
            <input type="text" name="username" placeholder="Nhập tên đăng nhập hoặc email" required>

            <label>Mật khẩu</label>
            <div class="input-wrapper">
                <input type="password" name="password" id="loginPassword" placeholder="••••••••" required>
                <i class="fa-solid fa-eye-slash toggle-password" onclick="togglePassword('loginPassword', this)"></i>
            </div>

            <button type="submit">
                Đăng nhập vào Bảng điều khiển
            </button>
            
            <div class="forgot-password">
                <a href="forgotPassword">Quên mật khẩu?</a>
            </div>

        </form>
    </div>

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
</script>

</body>
</html>
