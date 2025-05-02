<%-- Document : account.jsp Created on : Oct 17, 2024, 1:40:00 PM Author : admin --%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
                margin: 0; /* Reset margin */
                padding-left: 0; /* Reset padding */
            }

            .col-md-3 ul h3 {
                font-size: 20px;
                margin-bottom: 20px;
                padding-left: 20px; /* Align with list items */
                font-weight: bold;
                color: #FC6E51;
            }

            .col-md-3 ul li {
                margin-bottom: 0; /* Remove default margin */
            }

            .col-md-3 ul li a {
                text-decoration: none;
                font-size: 16px;
                color: #333;
                transition: color 0.2s, background-color 0.2s;
                display: block; /* Make link fill the li */
                padding: 10px 20px; /* Add padding */
                border-radius: 4px; /* Slight rounding */
            }

            .col-md-3 ul li a:hover,
            .col-md-3 ul li a.active { /* Style for active link */
                color: #FC6E51;
                background-color: #fcece8; /* Light background on hover/active */
            }

            /* Account Information Section */
            .account-info {
                background-color: white;
                padding: 30px;
                border-radius: 8px;
                box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
                min-height: 300px; /* Ensure minimum height */
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
                transition: background-color 0.2s;
            }

            .account-info .btn:hover {
                background-color: #F56B4C;
            }

            /* --- Voucher List Specific CSS --- */
            .voucher-list-container {
                display: grid; /* Using grid for layout */
                grid-template-columns: repeat(auto-fit, minmax(250px, 1fr)); /* Responsive columns */
                gap: 20px; /* Spacing between cards */
                margin-top: 20px; /* Space below the heading */
            }

            .voucher-card {
                background-color: #fff;
                border: 1px solid #e0e0e0;
                border-left: 5px solid #FC6E51; /* Accent border */
                border-radius: 8px;
                padding: 20px;
                box-shadow: 0 2px 5px rgba(0, 0, 0, 0.05);
                transition: box-shadow 0.3s ease, transform 0.3s ease;
                display: flex;
                flex-direction: column; /* Stack content vertically */
            }

            .voucher-card:hover {
                box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
                transform: translateY(-3px); /* Slight lift effect */
            }

            .voucher-card .voucher-title {
                font-size: 1.15em;
                font-weight: bold;
                color: #333; /* Darker title */
                margin-bottom: 10px;
                flex-grow: 1; /* Allow title to take available space if needed */
            }

            .voucher-card .voucher-discount {
                font-size: 0.95em;
                color: #28a745; /* Green for discount */
                font-weight: bold;
                margin-bottom: 8px;
            }

            .voucher-card .voucher-category {
                font-size: 0.9em;
                color: #6c757d; /* Grey for less important info */
                margin-bottom: 0;
                margin-top: auto; /* Push category to the bottom */
            }

            .no-vouchers-message {
                color: #555;
                font-style: italic;
                margin-top: 20px;
            }
            .voucher-card .btn {
                align-self: flex-start; /* Để nút nằm bên trái */
                margin-top: auto; /* Đẩy nút xuống dưới cùng nếu cần */
            }

            /* --- End Voucher List CSS --- */

        </style>
    </head>

    <body>

        <jsp:include page="header.jsp"></jsp:include>

            <!-- Info Account Section -->
            <div class="container my-5">
                <div class="row">
                    <!-- Sidebar -->
                    <div class="col-md-3">
                        <ul>
                            <!-- You might want to dynamically add 'active' class based on the current page -->
                            <li><a href="CustomerManagement">Account Information</a></li>
                            <li><a href="listAddress">Address List</a></li>
                            <li><a href="listOrders">Order History</a></li>
                            <li><a href="voucher" class="active">Voucher</a></li> <!-- Added active class for example -->
                            <li><a href="changepassword">Change Password</a></li>
                            <li><a href="logout">Logout</a></li>
                        </ul>
                    </div>

                    <!-- Voucher list -->
                    <div class="col-md-9">
                        <div class="account-info">
                            <h3>My Vouchers</h3>
                            <div class="voucher-list-container">
                            <c:choose>
                                <c:when test="${not empty voucherList}">
                                    <c:forEach  items="${voucherList}" var="v">
                                        <div class="voucher-card">
                                            <h5 class="voucher-title">${v.title}</h5>
                                            <p class="voucher-discount">${v.discount}% trên tổng đơn hàng.</p>
                                            <p class="voucher-category">Category: ${v.categoryName}</p>
                                            <a href="home?categoryid=${v.categoryID}" class="btn btn-sm btn-outline-primary mt-3">See Products</a>
                                        </div>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <p class="no-vouchers-message">You currently don't have any 
                                    </c:otherwise>
                                </c:choose>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <jsp:include page="footer.jsp"></jsp:include>

        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>