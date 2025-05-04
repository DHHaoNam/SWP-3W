<%-- 
    Document   : edit_address
    Created on : Oct 27, 2024, 10:35:34 PM
    Author     : ADMIN
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Thông Tin Địa Chỉ</title>
        <!-- Font Awesome -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css"
              integrity="sha512-Kc323vGBEqzTmouAECnVceyQqyqdsSiqLQISBL29aUW4U/M7pSPA/gEUZQqv1cwx4OnYxTxve5UMg5GT6L4JJg=="
              crossorigin="anonymous" referrerpolicy="no-referrer" />
        <!-- Bootstrap -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
        <!-- Custom CSS -->
        <link rel="stylesheet" href="./CSS/HeaderAndFooter_CSS.css">
        <link rel="stylesheet" href="./CSS/home.css">
        <script src="./Script/header-script.js"></script>

        <style>
            body {
                font-family: 'Arial', sans-serif;
                background-color: #f7f7f7;
            }

            /*            Navbar Customization*/
            .navbar {
                box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            }

            .navbar-brand img {
                max-width: 150px;
            }

            .nav-link {
                font-size: 16px;
                font-weight: 500;
                color: #333;
            }

            .nav-link:hover {
                color: #FC6E51;
            }

            /*            Sidebar Customization */
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

            Container styling
            .col-md-6 {
                margin: auto;
                max-width: 500px;
                padding: 20px;
                background-color: #f8f9fa;
                border: 1px solid #e0e0e0;
                border-radius: 8px;
            }

            .form-update {
                padding: 50px 50px 50px 20px;
                background: white;
                box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
                margin-bottom: 50px;
                border-radius: 10px;
                margin-left: 18px;
            }

            .form-control {
                margin-bottom: 15px;
            }
        </style>
    </head>

    <body>
        <jsp:include page="header.jsp"></jsp:include>
            <!-- Address Management Section -->
            <div class="container content">
                <h3 class="text-center my-5">Update Address</h3>

                <div class="row">
                    <!-- Sidebar -->
                    <div class="col-md-3">
                        <ul>
                            <h3>Account</h3>
                            <li><a href="CustomerManagement">Account Information</a></li>
                            <li><a href="listAddress">Address List</a></li>
                            <li><a href="listOrders">Order History</a></li>
                            <li><a href="voucher" class="active">Voucher</a></li> <!-- Added active class for example -->
                            <li><a href="changepassword">Change Password</a></li>
                            <li><a href="logout">Logout</a></li>
                        </ul>
                    </div>

                    <!-- Address Cards -->
                    <div class="col-md-6 form-update">
                    <c:set var="address" value="${updateAddress}" />
                    <form action="updateAddress" method="POST">
                        <input type="hidden" name="addressID" value="${address.getAddressID()}">
                        <div class="mb-3">
                            <label for="addressDetail" class="form-label">Address:</label>
                            <input type="text" class="form-control" id="addressDetail" name="addressDetail" value="${address.getAddressDetail()}" required>
                        </div>

                        <div class="form-check">
                            <input type="checkbox" class="form-check-input" id="defaultAddress" name="isDefault" ${address.isDefault() == 1 ? 'checked' : ''}>
                            <label class="form-check-label" for="defaultAddress">Set as default address</label>
                        </div>

                        <button type="submit" class="btn btn-success mt-3">Update</button>
                    </form>


                </div>

            </div>
        </div>



        <!-- Footer -->
        <jsp:include page="footer.jsp"></jsp:include>

        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
