<%-- Document : account.jsp Created on : Oct 17, 2024, 1:40:00 PM Author : admin --%>

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
        <title>Account Information</title>
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

            .account-info input[type="text"],
            .account-info input[type="email"] {
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

            <!-- Info Account Section -->
            <div class="container my-5">
                <h3>Account</h3>
            <c:if test="${not empty requestScope.message}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    ${requestScope.message}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>

            <c:if test="${not empty requestScope.error}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    ${requestScope.error}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>

            <div class="row">
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


                <c:if test="${sessionScope.customer != null}">
                    <div class="col-md-9">
                        <div class="account-info">
                           <h3>Update Account Information</h3>
                            <form action="update-profile" method="post">

                                <div class="info-item">
                                    <label for="username">Full Name:</label>
                                    <input type="text" name="fullName" id="fullName" value="${sessionScope.customer.fullName}">
                                </div>

                                <div class="info-item">
                                    <label for="email">Email:</label>
                                    <input type="email" name="email" id="email" value="${sessionScope.customer.email}" readonly>
                                </div>
                                <div class="info-item">
                                    <label for="phone">Phone Number:</label>
                                    <input type="text" name="phone" id="phone_number" value="${sessionScope.customer.phone}">
                                </div>
                                <c:if test="${not empty errorMessage}">
                                    <div class="alert alert-danger">${errorMessage}</div>
                                </c:if>
                                <c:if test="${not empty successMessage}">
                                    <div class="alert alert-success">${successMessage}</div>
                                </c:if>
                                <div class="info-item mx-auto">
                                    <button type="submit" class="btn">Update</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </c:if>
            </div>
        </div>

        <jsp:include page="footer.jsp"></jsp:include>

        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>

</html>