<%-- 
    Document   : orderhistorymanagement
    Created on : Mar 19, 2025, 3:57:00 PM
    Author     : Nam
--%>

<%@page import="model.OrderInfo"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>

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
    Integer currentStatus = (Integer) request.getAttribute("currentStatus");

    List<OrderInfo> orders = (List<OrderInfo>) request.getAttribute("orders");
%>



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
            .order-history {
                background: white;
                padding: 15px;
                border-radius: 8px;
                box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
            }

            .nav-tabs {
                border-bottom: 2px solid #ddd;
            }

            .nav-tabs .nav-item {
                margin-right: 20px;
            }

            .nav-tabs .nav-link {
                color: black;
                font-weight: 500;
                font-size: 16px;
            }

            .nav-tabs .nav-link.active {
                color: red;
                border-bottom: 2px solid red;
            }

            /* Feedback styling */
            .feedback-modal .modal-content {
                border-radius: 10px;
            }

            .star-rating {
                font-size: 2rem;
                color: grey;
                cursor: pointer;
            }

            .star-rating .star.selected {
                color: gold;
            }

            .feedback-btn {
                margin-left: 5px;
            }

            .icon-btn {
                background: none;
                border: none;
                color: #0d6efd;
                cursor: pointer;
                padding: 0 5px;
            }

            .icon-btn.edit {
                color: #198754;
            }

            .icon-btn.delete {
                color: #dc3545;
            }

            .star-rating .star {
                cursor: pointer;
                display: inline-block;
                margin-right: 5px;
            }

            .star-rating .star.selected i,
            .star-rating .star.hover i {
                color: #ffc107; /* gold color for selected stars */
            }

            .feedback-form {
                padding: 15px;
                background-color: #f8f9fa;
                border-radius: 8px;
            }
        </style>
    </head>

    <body>
        <jsp:include page="header.jsp"></jsp:include>
        <%
            String successMessage = (String) session.getAttribute("successMessage");
            if (successMessage != null) {
                session.removeAttribute("successMessage");
        %>
        <script>
            alert("<%= successMessage%>");
        </script>
        <%
            }

            String errorMessage = (String) request.getAttribute("errorMessage");
            if (errorMessage != null) {
        %>
        <script>
            alert("<%= errorMessage%>");
        </script>
        <%
            }
        %>
        <div class="container my-5">
            <h3>Tài Khoản</h3>
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

                <!-- Order History Section -->
                <div class="col-md-9">
                    <div class="order-history">
                        <ul class="nav nav-tabs">
                            <li class="nav-item">
                                <a class="nav-link <%= (currentStatus == 1) ? "active" : ""%>" href="listOrders?status=1">Waiting for Confirmation</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link <%= (currentStatus == 2) ? "active" : ""%>" href="listOrders?status=2">Confirmed</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link <%= (currentStatus == 3) ? "active" : ""%>" href="listOrders?status=3">In Delivery</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link <%= (currentStatus == 4) ? "active" : ""%>" href="listOrders?status=4">Delivered</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link <%= (currentStatus == 5) ? "active" : ""%>" href="listOrders?status=5">Canceled</a>
                            </li>
                        </ul>

                        <!-- Danh sách đơn hàng -->
                        <div class="table-responsive mt-3">
                            <table class="table table-bordered text-center">
                                <thead class="table-dark">
                                    <tr>
                                        <th>Order ID</th>
                                        <th>Order Date</th>
                                        <th>Total Amount</th>
                                        <th>Status</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <%
                                        if (orders != null && !orders.isEmpty()) {
                                            for (OrderInfo order : orders) {
                                    %>
                                    <tr>
                                        <td><%= order.getOrderID()%></td>
                                        <td><%= order.getOrderDate()%></td>
                                        <td><%= (order.getTotalPrice() != 0.0) ? String.format("%,.0f", order.getTotalPrice()) + " VND" : "N/A"%></td>
                                        <td>
                                            <%
                                                switch (order.getOrderStatus()) {
                                                    case 1:
                                                        out.print("Chờ xác nhận");
                                                        break;
                                                    case 2:
                                                        out.print("Đã xác nhận");
                                                        break;
                                                    case 3:
                                                        out.print("Đang giao");
                                                        break;
                                                    case 4:
                                                        out.print("Đã giao");
                                                        break;
                                                    case 5:
                                                        out.print("Đã hủy");
                                                        break;
                                                    default:
                                                        out.print("Không xác định");
                                                }
                                            %>
                                        </td>
                                        <td>
                                            <a href="viewOrderDetails_customer?id=<%= order.getOrderID()%>" class="btn btn-primary btn-sm">
                                                <i class="fas fa-eye"></i> View
                                            </a>
                                            <% if (order.getOrderStatus() == 4) {%>
                                            <a href="javascript:void(0);" onclick="showFeedbackModal('<%= order.getOrderID()%>');" class="btn btn-success btn-sm feedback-btn">
                                                <i class="fas fa-comment"></i> Feedback
                                            </a>
                                            <% } %>
                                            <% if (order.getOrderStatus() == 1) {%>
                                            <a href="CancelOrder?orderId=<%= order.getOrderID()%>" class="btn btn-danger" 
                                               onclick="return confirm('Are you sure you want to cancel this order?');">
                                                Cancel Order
                                            </a>
                                            <% } %>
                                        </td>
                                    </tr>
                                    <%
                                        }
                                    } else {
                                    %>
                                    <tr>
                                        <td colspan="5">No orders available.</td>
                                    </tr>
                                    <% }%>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Feedback Modal -->
        <div class="modal fade" id="feedbackModal" tabindex="-1" aria-labelledby="feedbackModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="feedbackModalLabel">Product Feedback</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <div id="feedbackProductList">
                            <div class="text-center">
                                <div class="spinner-border" role="status">
                                    <span class="visually-hidden">Loading...</span>
                                </div>
                                <p>Loading products...</p>
                            </div>
                        </div>
                        <!-- Hidden input for orderDetailID -->
                        <input type="hidden" name="orderDetailId" value="${orderDetail.orderDetailID}">
                    </div>
                </div>
            </div>
        </div>

        <jsp:include page="footer.jsp"></jsp:include>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
        <script src="https://kit.fontawesome.com/a076d05399.js" crossorigin="anonymous"></script>

        <script>
                                                   // Function to show the feedback modal
                                                   function showFeedbackModal(orderId) {
                                                       $('#feedbackModal').modal('show');

                                                       // Load the products from this order
                                                       $.ajax({
                                                           url: 'GetOrderProductsForFeedback',
                                                           type: 'GET',
                                                           data: {
                                                               orderId: orderId
                                                           },
                                                           success: function (response) {
                                                               const orderDetailId = response.orderDetailId; // Make sure this is correct
                                                               $('#orderDetailId').val(orderDetailId); // Set the hidden input value

                                                               $('#feedbackProductList').html(response);

                                                               // Assuming the response contains orderDetailID, set it here
                                                               // Adjust this line based on your actual response structure

                                                               // Initialize star ratings for all products
                                                               initAllStarRatings();

                                                               // Set up feedback form submission
                                                               setupFeedbackForms();
                                                           },
                                                           error: function () {
                                                               $('#feedbackProductList').html('<div class="alert alert-danger">Error loading products. Please try again later.</div>');
                                                           }
                                                       });
                                                   }

                                                   // Initialize all star ratings
                                                   function initAllStarRatings() {
                                                       $('.feedback-form').each(function () {
                                                           const form = $(this);
                                                           const stars = form.find('.star-rating .star');
                                                           const ratingInput = form.find('input[name="rating"]');

                                                           stars.on('click', function () {
                                                               const value = $(this).data('value');
                                                               ratingInput.val(value);

                                                               stars.removeClass('selected');
                                                               $(this).prevAll('.star').addBack().addClass('selected');
                                                           });

                                                           // Hover effect
                                                           stars.hover(
                                                                   function () {
                                                                       const value = $(this).data('value');
                                                                       $(this).prevAll('.star').addBack().addClass('hover');
                                                                   },
                                                                   function () {
                                                                       stars.removeClass('hover');
                                                                   }
                                                           );
                                                       });
                                                   }

                                                   // Set up feedback form submission
                                                   function setupFeedbackForms() {
                                                       $('.feedback-form').submit(function (e) {
                                                           e.preventDefault();

                                                           const form = $(this);
                                                           const productId = form.find('input[name="productId"]').val();
                                                           const rating = form.find('input[name="rating"]').val();
                                                           const comment = form.find('textarea[name="comment"]').val();
                                                           const orderDetailId = form.find('input[name="orderDetailId"]').val(); // Get orderDetailID

                                                           if (!rating) {
                                                               Swal.fire({
                                                                   icon: 'warning',
                                                                   title: 'Rating Required',
                                                                   text: 'Please select a star rating before submitting'
                                                               });
                                                               return;
                                                           }

                                                           $.ajax({
                                                               url: 'feedback',
                                                               type: 'POST',
                                                               data: {
                                                                   productId: productId,
                                                                   rating: rating,
                                                                   comment: comment,
                                                                   orderDetailId: orderDetailId
                                                               },
                                                               success: function (response) {
                                                                   // Show success message and disable the form
                                                                   form.html('<div class="alert alert-success"><i class="fas fa-check-circle me-2"></i> Your feedback has been submitted successfully!</div>');

                                                                   Swal.fire({
                                                                       icon: 'success',
                                                                       title: 'Thank you!',
                                                                       text: 'Your feedback has been submitted successfully'
                                                                   });
                                                               },
                                                               error: function (xhr) {
                                                                   Swal.fire({
                                                                       icon: 'error',
                                                                       title: 'Error',
                                                                       text: 'There was a problem submitting your feedback. Please try again later.'
                                                                   });
                                                                   form.append('<div class="alert alert-danger mt-3">Error submitting feedback: ' + xhr.responseText + '</div>');
                                                               }
                                                           });
                                                       });
                                                   }

        </script>
    </body>
</html>