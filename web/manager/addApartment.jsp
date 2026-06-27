<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Thêm căn hộ mới - Lumina BMS</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <style>
        body { 
            margin: 0; 
            min-height: 100vh; 
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; 
            color: white; 
            background: linear-gradient(rgba(0, 50, 60, 0.8), rgba(0, 30, 45, 0.9)), url('https://images.unsplash.com/photo-1545324418-cc1a3fa10c00?q=80&w=2070&auto=format&fit=crop'); 
            background-size: cover; 
            background-position: center; 
            background-attachment: fixed;
            display: flex; 
            justify-content: center; 
            align-items: center; 
            padding: 20px 0; 
        }
        
        .container { 
            width: 520px; 
            padding: 40px; 
            border-radius: 16px; 
            background: rgba(255, 255, 255, 0.15); 
            backdrop-filter: blur(15px); 
            -webkit-backdrop-filter: blur(15px); 
            box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3); 
            border: 1px solid rgba(255, 255, 255, 0.2); 
        }
        
        h2 { 
            font-size: 28px; 
            color: white; 
            margin-top: 0; 
            margin-bottom: 25px; 
            text-align: center; 
            border-bottom: 1px solid rgba(255, 255, 255, 0.2); 
            padding-bottom: 15px; 
            font-weight: 400;
        }
        
        .form-group { 
            margin-bottom: 20px; 
        }
        
        .form-group label { 
            display: block; 
            margin-bottom: 8px; 
            font-weight: bold; 
            font-size: 14px; 
            color: rgba(255, 255, 255, 0.9); 
        }
        
        .form-group input, .form-group select { 
            width: 100%; 
            padding: 12px 15px; 
            border: 1px solid rgba(255, 255, 255, 0.3); 
            border-radius: 8px; 
            background: rgba(255, 255, 255, 0.1); 
            color: white; 
            box-sizing: border-box; 
            font-size: 1rem; 
            outline: none; 
            transition: all 0.3s; 
        }
        
        .form-group input:focus, .form-group select:focus { 
            border: 1px solid #00d8ff; 
            background: rgba(255, 255, 255, 0.2); 
        }
        
        .form-group option { 
            background: #002530; 
            color: white; 
        }
        
        .error-box { 
            background: rgba(231, 76, 60, 0.2); 
            color: #ff6b6b; 
            border: 1px solid rgba(231, 76, 60, 0.4); 
            padding: 12px 15px; 
            border-radius: 8px; 
            margin-bottom: 20px; 
            font-size: 0.9rem; 
            text-align: center; 
            font-weight: 500;
        }
        
        .btn { 
            display: inline-block; 
            padding: 14px 20px; 
            border: none; 
            border-radius: 8px; 
            font-size: 16px; 
            font-weight: bold; 
            cursor: pointer; 
            transition: all 0.3s; 
            text-decoration: none; 
            width: 48%; 
            box-sizing: border-box; 
            text-align: center; 
        }
        
        .btn:active { 
            transform: scale(0.98); 
        }
        
        .btn-primary { 
            background: #00d8ff; 
            color: white; 
        }
        
        .btn-primary:hover { 
            background: #00bce0; 
        }
        
        .btn-secondary { 
            background: rgba(255, 255, 255, 0.2); 
            color: white; 
            border: 1px solid rgba(255, 255, 255, 0.3); 
        }
        
        .btn-secondary:hover { 
            background: rgba(255, 255, 255, 0.3); 
        }
        
        .action-buttons { 
            display: flex; 
            justify-content: space-between; 
            margin-top: 30px; 
        }
    </style>
</head>
<body>
<div class="container">
    <h2><i class="fa-solid fa-hotel"></i> Thêm căn hộ mới</h2>

    <c:if test="${not empty error}">
        <div class="error-box">
            <i class="fa-solid fa-triangle-exclamation"></i> ${error}
        </div>
    </c:if>

    <form action="${pageContext.request.contextPath}/manager/addApartment" method="post" id="addApartmentForm">
        <div class="form-group">
            <label for="apartmentNumber">Số căn hộ</label>
            <input type="text" name="apartmentNumber" id="apartmentNumber" required 
                   placeholder="Ví dụ: A1-01, B2-05..." value="${apartmentNumber}">
        </div>
        
        <div class="form-group">
            <label for="floor">Tầng</label>
            <input type="number" name="floor" id="floor" required min="1" 
                   placeholder="Ví dụ: 1, 2, 3..." value="${floor}">
        </div>
        
        <div class="form-group">
            <label for="area">Diện tích (m²)</label>
            <input type="number" name="area" id="area" required min="0.1" step="0.01" 
                   placeholder="Ví dụ: 65.5" value="${area}">
        </div>
        
        <div class="form-group">
            <label for="types">Loại phòng</label>
            <select name="types" id="types" required>
                <option value="">-- Chọn loại phòng --</option>
                <option value="Studio" ${types == 'Studio' ? 'selected' : ''}>Studio</option>
                <option value="1PN" ${types == '1PN' ? 'selected' : ''}>1PN (1 phòng ngủ)</option>
                <option value="2PN" ${types == '2PN' ? 'selected' : ''}>2PN (2 phòng ngủ)</option>
                <option value="3PN" ${types == '3PN' ? 'selected' : ''}>3PN (3 phòng ngủ)</option>
                <option value="Penthouse" ${types == 'Penthouse' ? 'selected' : ''}>Penthouse</option>
            </select>
        </div>
        
        <div class="form-group">
            <label for="status">Trạng thái căn hộ</label>
            <select name="status" id="status" required>
                <option value="Vacant" ${status == 'Vacant' ? 'selected' : ''}>Vacant (Trống)</option>
                <option value="Occupied" ${status == 'Occupied' ? 'selected' : ''}>Occupied (Đang ở)</option>
                <option value="Under Repair" ${status == 'Under Repair' ? 'selected' : ''}>Under Repair (Đang bảo trì)</option>
            </select>
        </div>
        
        <div class="form-group">
            <label for="ownerId">Cư dân sở hữu (Chủ hộ)</label>
            <select name="ownerId" id="ownerId">
                <option value="">-- Căn hộ trống (Chưa gán cư dân) --</option>
                <c:forEach var="res" items="${residents}">
                    <option value="${res.userId}" ${ownerId == res.userId ? 'selected' : ''}>
                        ${res.fullName} (${res.username})
                    </option>
                </c:forEach>
            </select>
        </div>
        
        <div class="action-buttons">
            <a class="btn btn-secondary" href="${pageContext.request.contextPath}/manager/apartments">Hủy</a>
            <button type="submit" class="btn btn-primary">Lưu căn hộ</button>
        </div>
    </form>
</div>

<script>
    // Client-side UX improvement: 
    // If a resident is selected as owner, automatically set status to 'Occupied' if it was 'Vacant'.
    // If "-- Căn hộ trống --" is selected, automatically set status to 'Vacant' if it was 'Occupied'.
    const ownerSelect = document.getElementById('ownerId');
    const statusSelect = document.getElementById('status');

    ownerSelect.addEventListener('change', function() {
        if (this.value !== "") {
            if (statusSelect.value === "Vacant") {
                statusSelect.value = "Occupied";
            }
        } else {
            if (statusSelect.value === "Occupied") {
                statusSelect.value = "Vacant";
            }
        }
    });

    statusSelect.addEventListener('change', function() {
        if (this.value === "Vacant") {
            ownerSelect.value = "";
        }
    });
</script>
</body>
</html>
