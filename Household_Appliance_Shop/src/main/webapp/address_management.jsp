<%-- 
    Document   : address_management
    Created on : Oct 17, 2024, 3:47:16 PM
    Author     : admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="model.Address" %>

<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Address Information</title>
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

            /* Navbar Customization */
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

            /* Address Management Section */
            .address-card {
                background-color: white;
                padding: 20px;
                border-radius: 8px;
                box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
                margin-bottom: 20px;
                display: flex;
                justify-content: space-between;
                align-items: center;
            }

            .address-card h5 {
                margin-bottom: 10px;
            }

            .address-card p {
                margin-bottom: 5px;
                font-size: 14px;
                color: #666;
            }

            .address-actions {
                display: flex;
                align-items: center;
            }

            .address-actions a {
                margin-right: 10px;
            }

            .btn-primary,
            .btn-danger,
            .btn-success {
                margin-right: 10px;
                background-color: #FC6E51;
                border-color: #FC6E51;
            }

            .btn-primary:hover,
            .btn-danger:hover,
            .btn-success:hover {
                background-color: #F56B4C;
                border-color: #F56B4C;
            }

            /* Default Address */
            .default-address {
                border: 2px solid #FC6E51;
                background-color: #fff3cd;
            }

            /* Form New Address */
            .form-control {
                margin-bottom: 10px;
            }

            .form-group {
                margin-bottom: 15px;
            }

            /* Footer Customization */
            .footer {
                background-color: #333;
                color: white;
                padding: 20px 0;
                margin-top: 40px;
            }

            .footer p {
                margin: 0;
                font-size: 14px;
            }

            .footer strong {
                color: #FC6E51;
            }

            .content{
                margin: 100px auto;
            }
        </style>
        <script>
            function confirmDelete() {
                return confirm("Are you sure you want to delete this address?");
            }
        </script>

    </head>

    <body>
        <jsp:include page="header.jsp"></jsp:include>
            <!-- Address Management Section -->
            <div class="container content">
                <h3 class="text-center mb-4">Address Information</h3>

                <div class="row">
                    <!-- Sidebar -->
                    <div class="col-md-3">
                         <ul>
                            <li><a href="CustomerManagement">Account Information</a></li>
                            <li><a href="listAddress">Address List</a></li>
                            <li><a href="listOrders">Order History</a></li>
                            <li><a href="voucher" class="active">Voucher</a></li> <!-- Added active class for example -->
                            <li><a href="changepassword">Change Password</a></li>
                            <li><a href="logout">Logout</a></li>
                        </ul>
                    </div>

                    <!-- Address Cards -->
                    <div class="address-list col-md-6">
                    <c:forEach var="address" items="${listAddresses}">
                        <div class="address-card ${address.isDefault() == 1 ? 'default-address' : ''}">
                            <div>
                                <h5 class="mb-1"><i class="fa-solid fa-map-marker-alt"></i> Address</h5>
                                <p class="mb-1">${address.getAddressDetail()}</p>
                                <c:if test="${address.isDefault() == 1}">
                                    <span class="badge bg-warning text-dark">Default</span>
                                </c:if>
                            </div>
                            <div class="address-actions">
                                <a href="updateAddress?addressID=${address.getAddressID()}" class="btn btn-danger btn-sm"><i class="fa-solid fa-pen"></i> Edit</a>
                                <a href="deleteAddress?id=${address.getAddressID()}" class="btn btn-danger btn-sm" onclick="return confirmDelete();"><i class="fa-solid fa-trash"></i> Delete</a>
                            </div>
                        </div>
                    </c:forEach>
                </div>
                <!-- New Address Form -->
                <div class="col-md-3">
                    <button class="btn btn-success mb-3" onclick="toggleAddressForm()">Enter New Address</button>
                    <div id="newAddressForm" style="display:none;">
                        <form action="addAddress" method="POST">
                            <input type="text" class="form-control" placeholder="Address" name="addressDetail" required>
                            <div class="form-check">
                                <input type="checkbox" class="form-check-input" id="defaultAddress" name="is_default">
                                <label class="form-check-label" for="defaultAddress">Set as Default Address</label>
                            </div>
                            <button type="submit" class="btn btn-success mt-3">Add New</button>
                        </form>
                    </div>

                </div>
            </div>
        </div>



    </div>
</div>

<!-- Footer -->
<jsp:include page="footer.jsp"></jsp:include>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
                        function toggleAddressForm() {
                            var form = document.getElementById("newAddressForm");
                            if (form.style.display === "none") {
                                form.style.display = "block";
                            } else {
                                form.style.display = "none";
                            }
                        }
</script>
</body>

</html>
