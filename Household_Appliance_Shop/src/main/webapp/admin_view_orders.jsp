<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Admin Dashboard - Order Details</title>
        <!-- Font Awesome -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css" integrity="sha512-Kc323vGBEqzTmouAECnVceyQqyqdsSiqLQISBL29aUW4U/M7pSPA/gEUZQqv1cwx4OnYxTxve5UMg5GT6L4JJg==" crossorigin="anonymous" referrerpolicy="no-referrer" />
        <!-- Bootstrap -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
        <!-- Custom CSS -->
        <link rel="stylesheet" href="./CSS/Style.css" />
        <link rel="stylesheet" href="./CSS/HeaderAndFooter_CSS.css" />
        <style>
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
                margin-left: 250px; /* Space for the sidebar */
                padding: 20px;
                background-color: #f8f9fa;
                width: calc(100% - 250px);
                float: left;
            }

            /* Prevent floating issues */
            .clearfix::after {
                content: "";
                display: table;
                clear: both;
            }
        </style>
    </head>
    <body>
        <jsp:include page="admin_dashboard_header.jsp"></jsp:include>


            <div class="wrapper clearfix">
                <!-- Sidebar -->
                <div class="sidebar">
                <c:if test="${sessionScope.managerRole == 1}">
                    <h3>Admin Dashboard</h3>            
                </c:if>    
                <c:if test="${sessionScope.managerRole == 2}">
                    <h3>Staff Dashboard</h3></c:if> 
                <c:if test="${sessionScope.managerRole == 1}">
                    <a href="CategoryController"><i class="fas fa-list"></i> Category Management</a>
                </c:if>        
                <a href="ProductController"><i class="fas fa-box"></i> Product Management</a>
                <a href="CustomerController_temp"><i class="fas fa-users"></i> Customer Management</a>
                <a href="listAdminOrders"><i class="fas fa-shopping-cart"></i> Order Management</a>
                <a href="FeedbackController"><i class="fas fa-comments"></i> Feedback Management</a>
                <c:if test="${sessionScope.managerRole == 1}">
                    <a href="VoucherController"><i class="fas fa-ticket-alt"></i> Voucher Management</a>
                </c:if>   
                <c:if test="${sessionScope.managerRole == 1}">
                    <a href="RevenueController"><i class="fa-solid fa-chart-simple"></i> Revenue Management</a>
                </c:if> 
            </div>

            <!-- Order Details Content -->
            <div class="content">
                <h1 class="mb-4">Order Details</h1>

                <div class="order-details mb-4">
                    <!-- Order ID -->
                    <div class="mb-3">
                        <label for="orderID" class="form-label">Order ID</label>
                        <input type="text" class="form-control" id="orderID" value="${OrderInfo.orderID}" readonly>
                    </div>
                    <!-- Customer Name -->
                    <div class="mb-3">
                        <label for="fullName" class="form-label">Customer</label>
                        <input type="text" class="form-control" id="fullName"  value="${Customer.fullName}" readonly>
                    </div>
                    <!-- Order Date -->
                    <div class="mb-3">
                        <label for="orderDate" class="form-label">Order Date</label>
                        <input type="text" class="form-control" id="orderDate" value="${OrderInfo.orderDate}" readonly>
                    </div>
                    <!-- Status -->
                    <div class="mb-3">
                        <label class="form-label">Status</label>
                        <input type="text" class="form-control" value="${OrderInfo.orderStatus == 1 ? 'Chưa xác nhận' : 
                                                                         OrderInfo.orderStatus == 2 ? 'Đã xác nhận' : 
                                                                         OrderInfo.orderStatus == 3 ? 'Đang giao' : 
                                                                         OrderInfo.orderStatus == 4 ? 'Đã giao' : 
                                                                         OrderInfo.orderStatus == 5 ? 'Đã hủy' : 
                                                                         'Khác'}" readonly>
                    </div>
                    <!-- Address -->
                    <div class="mb-3">
                        <label for="address" class="form-label">Address</label>
                        <input type="text" class="form-control" id="address" value="${OrderInfo.deliveryAddress}" readonly>
                    </div>
                    <!-- Phone Number -->
                    <div class="mb-3">
                        <label for="phone" class="form-label">Phone Number</label>
                        <input type="text" class="form-control" id="phone" value="${Customer.phone}" readonly>
                    </div>
                </div>

                <!-- Order Items Table -->
                <h3 class="mt-4">Order Items</h3>
                <table class="table table-bordered">
                    <thead class="table-light"><tr>
                            <th>Image</th>
                            <th>Product Name</th>
                            <th>Quantity</th>
                            <th>Price</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="detail" items="${orderDetails}">
                            <c:forEach var="prod" items="${product}">
                                <c:if test="${detail.productID == prod.productID}">
                                    <tr>
                                        <td><img src="${prod.image}" alt="${prod.productName}" style="width: 50px; height: auto;"></td>
                                        <td>${prod.productName}</td>
                                        <td>${detail.quantity}</td>
                                        <td><fmt:formatNumber value="${prod.price}" type="number" pattern="#,### VND"/></td>
                                    </tr>
                                </c:if>
                            </c:forEach>
                        </c:forEach>
                    </tbody>
                </table>

                <!-- Total Price -->
                <div class="mb-3">
                    <label for="totalAmount" class="form-label">Total Amount</label>
                    <input type="text" class="form-control" id="totalAmount" 
                           value="<fmt:formatNumber value='${OrderInfo.totalPrice}' type='number' pattern='#,### VND'/>" readonly>
                </div>

                <!-- Action Buttons -->
                <div class="mt-4">


                    <a href="ProductController" class="btn btn-secondary"><i class="fas fa-arrow-left"></i> Back</a>

                </div>
            </div>
        </div>

        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>