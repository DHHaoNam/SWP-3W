<%-- 
    Document   : changepassword
    Created on : Mar 2, 2025, 10:05:25 PM
    Author     : Nam
--%>

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
        <title>Thay Đổi Mật Khẩu</title>
        <!-- Font Awesome -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css"
              integrity="sha512-Kc323vGBEqzTmouAECnVceyQqyqdsSiqLQISBL29aUW4U/M7pSPA/gEUZQqv1cwx4OnYxTxve5UMg5GT6L4JJg=="
              crossorigin="anonymous" referrerpolicy="no-referrer" />
        <!-- Bootstrap -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css"
              rel="stylesheet">
        <!-- Header and Footer CSS -->
        <link rel="stylesheet" href="./CSS/HeaderAndFooter_CSS.css">
        <link rel="stylesheet" href="./CSS/home.css">
        <script src="./Script/header-script.js"></script>

        <style>
            body {
                font-family: 'Arial', sans-serif;
                background-color: #f7f7f7;
            }

            /* Sidebar Customization */
            .col-md-3 ul {
                background-color: white;
                padding: 20px;
                border-radius: 8px;
                box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
                list-style-type: none;
            }

            .col-md-3 ul h3 {
                font-size: 20px;
                margin-bottom: 20px;
                font-weight: bold;
                color: #FC6E51;
            }

            .col-md-3 ul li {
                margin-bottom: 15px;
            }

            .col-md-3 ul li a {
                text-decoration: none;
                font-size: 16px;
                color: #333;
                transition: color 0.2s;
            }

            .col-md-3 ul li a:hover {
                color: #FC6E51;
            }

            /* Account Information Section */
            .account-info {
                background-color: white;
                padding: 30px;
                border-radius: 8px;
                box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
            }

            .account-info h3 {
                font-size: 22px;
                font-weight: bold;
                margin-bottom: 30px;
                color: #333;
            }

            .account-info .info-item {
                margin-bottom: 20px;
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
            }

            .account-info .btn {
                background-color: #FC6E51;
                color: white;
                padding: 10px 20px;
                border: none;
                border-radius: 4px;
                cursor: pointer;
            }

            .account-info .btn:hover {
                background-color: #F56B4C;
            }
        </style>
    </head>


    <body>
        <jsp:include page="header.jsp"></jsp:include>
            <div class = "row"
                 <!-- Sidebar -->
                <div class="col-md-3">
    <ul>
        <li><a href="CustomerManagement">Account Information</a></li>
        <li><a href="listAddress">Address List</a></li>
        <li><a href="listOrders">Order History</a></li>
        <li><a href="changepassword">Change Password</a></li>
        <li><a href="logout">Logout</a></li>
    </ul>
</div>

                <!-- Change Password Form -->
                <div class="container my-5 col-md-9">
                    <h3>Change Password</h3>
                    <div class="account-info">
                        <form action="changepassword" method="post">
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
        </div>

        <jsp:include page="footer.jsp"></jsp:include>

        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
