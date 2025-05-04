<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    Cookie[] cookies = request.getCookies();
    boolean hasUserCookie = false;
    if (cookies != null) {
        for (Cookie cookie : cookies) {
            if ("user".equals(cookie.getName())) {
                hasUserCookie = true;
                break;
            }
        }
    }
    if (!hasUserCookie) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Change Password</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css"
              integrity="sha512-Kc323vGBEqzTmouAECnVceyQqyqdsSiqLQISBL29aUW4U/M7pSPA/gEUZQqv1cwx4OnYxTxve5UMg5GT6L4JJg=="
              crossorigin="anonymous" referrerpolicy="no-referrer"/>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
        <style>
            body {
                font-family: 'Arial', sans-serif;
                background-color: #f8f9fa;
            }

            /* Sidebar Styles */
            .sidebar {
                width: 250px;
                background-color: #343a40;
                padding: 20px;
                height: 100%;
                position: fixed;
                top: 0;
                left: 0;
                color: white;
                display: flex;
                flex-direction: column;
                align-items: flex-start;
            }

            .sidebar h3 {
                margin-bottom: 30px;
                text-transform: uppercase;
                font-size: 18px;
            }

            .sidebar a {
                color: white;
                text-decoration: none;
                display: block;
                padding: 10px 15px;
                margin: 5px 0;
                width: 100%;
                text-align: left;
                border-radius: 5px;
            }

            .sidebar a:hover {
                background-color: #495057;
                transition: background-color 0.3s;
            }

            /* Main content */
            .content {
                margin-left: 250px;
                padding: 20px;
                background-color: #f8f9fa;
                width: calc(100% - 250px);
            }

            /* Change Password Form */
            .account-info {
                background-color: white;
                padding: 30px;
                border-radius: 8px;
                box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
                max-width: 500px;
                margin: auto;
            }

            .account-info h3 {
                font-size: 22px;
                font-weight: bold;
                margin-bottom: 20px;
                color: #333;
                text-align: center;
            }

            .account-info .info-item {
                margin-bottom: 15px;
            }

            .account-info label {
                font-weight: bold;
                margin-bottom: 5px;
                display: block;
                color: #555;
            }

            .account-info input[type="password"] {
                width: 100%;
                padding: 10px;
                border: 1px solid #ddd;
                border-radius: 4px;
                margin-bottom: 10px;
                font-size: 14px;
            }

            .account-info .btn {
                background-color: #FC6E51;
                color: white;
                padding: 10px 20px;
                border: none;
                border-radius: 4px;
                cursor: pointer;
                width: 100%;
            }

            .account-info .btn:hover {
                background-color: #F56B4C;
            }

            .alert {
                margin-top: 10px;
            }
        </style>
    </head>
    <body>

        <jsp:include page="admin_dashboard_header.jsp"></jsp:include>
            <div class="sidebar">
                <h3>Admin Dashboard</h3>
            <c:if test="${sessionScope.managerRole == 1}">
                <a href="CategoryController"><i class="fas fa-list"></i> Category Management</a>
            </c:if>        
            <a href="ProductController"><i class="fas fa-box"></i> Product Management</a>
            <a href="CustomerController_temp"><i class="fas fa-users"></i> Account Management</a>
            <a href="listAdminOrders"><i class="fas fa-shopping-cart"></i> Order Management</a>
            <a href="FeedbackController"><i class="fas fa-comments"></i> Feedback Management</a>
            <c:if test="${sessionScope.managerRole == 1}">
                <a href="VoucherController"><i class="fas fa-ticket-alt"></i> Voucher Management</a>
            </c:if>   
            <c:if test="${sessionScope.managerRole == 1}">
                <a href="RevenueController"><i class="fa-solid fa-chart-simple"></i> Revenue Management</a>
            </c:if> 

        </div>

        <div class="content">
            <h1>Change Password</h1>
            <div class="account-info">
                <h3>Change Password</h3>
                <form action="managerchangepassword" method="post">
                    <div class="info-item">
                        <label for="oldPassword">Current Password</label>
                        <input type="password" id="oldPassword" name="oldPassword" required>
                    </div>
                    <div class="info-item">
                        <label for="newPassword">New Password</label>
                        <input type="password" id="newPassword" name="newPassword" required>
                    </div>
                    <div class="info-item">
                        <label for="confirmPassword">Confirm New Password</label>
                        <input type="password" id="confirmPassword" name="confirmPassword" required>
                    </div>
                    <div class="info-item">
                        <button type="submit" class="btn">Confirm</button>
                    </div>
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger">${error}</div>
                    </c:if>
                    <c:if test="${not empty success}">
                        <div class="alert alert-success">${success}</div>
                        <c:remove var="success" scope="session"/>
                    </c:if>
                </form>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
