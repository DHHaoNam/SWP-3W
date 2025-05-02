<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Admin Orders Dashboard</title>
        <!-- Font Awesome -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css"
              integrity="sha512-Kc323vGBEqzTmouAECnVceyQqyqdsSiqLQISBL29aUW4U/M7pSPA/gEUZQqv1cwx4OnYxTxve5UMg5GT6L4JJg=="
              crossorigin="anonymous" referrerpolicy="no-referrer" />
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
                <h3>Staff Dashboard</h3>            
            </c:if> 
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

            <!-- Main content -->
            <div class="content">
                <h1>Order Management</h1>

                <!-- Thông báo trạng thái -->
                <c:if test="${param.message != null}">
                    <div class="alert alert-success">${param.message}</div>
                </c:if>
                <c:if test="${param.error != null}">
                    <div class="alert alert-danger">${param.error}</div>
                </c:if>

                <!-- Form tìm đơn hàng theo số điện thoại  -->
                <form method="GET" action="searchByPhone" class="mb-3">
                    <div class="row">
                        <div class="col-md-6">
                            <input type="text" name="phone" class="form-control" placeholder="Search by phone number" value="${param.phone}">
                        </div>
                        <div class="col-md-2">
                            <button type="submit" class="btn btn-primary">Search</button>
                        </div>
                    </div>
                </form>

                <!-- Hiển thị thông báo lỗi nếu không tìm thấy đơn hàng -->
                <c:if test="${not empty error}">
                    <div class="alert alert-warning mt-2">${error}</div>
                </c:if>

                <!-- Form lọc đơn hàng theo trạng thái -->

                <!-- Bảng hiển thị danh sách đơn hàng -->
                <table class="table table-bordered">
                    <thead class="table-dark">
                        <tr>
                            <th>Order ID</th>
                            <th>Customer</th>
                            <th>Status</th>
                            <th>Order Date</th>
                          
                            <th>PaymentMethod</th>
                            <th>Total Price</th>
                            <th>Delivery Address</th>
                            <th>Actions</th>

                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="order" items="${listOrders}">
                            <tr>
                                <td>${order.orderID}</td>
                                <c:forEach items="${requestScope.customer}" var="cu">
                                    <c:if test="${order.customerID == cu.customerID}">                                 
                                        <td >${cu.fullName}</td>
                                    </c:if>  
                                </c:forEach>
                                <td>
                                    <c:choose>
                                        <c:when test="${order.orderStatus == 1}">Chưa xác nhận</c:when> 
                                        <c:when test="${order.orderStatus == 2}">Đã xác nhận</c:when>
                                        <c:when test="${order.orderStatus == 3}">Đang giao</c:when>
                                        <c:when test="${order.orderStatus == 4}">Đã giao</c:when>
                                        <c:when test="${order.orderStatus == 5}">Đã hủy</c:when>
                                        <c:otherwise>Khác</c:otherwise>
                                    </c:choose>
                                </td>
                                <td>${order.orderDate}</td>

                             
                                <c:forEach items="${requestScope.payment}" var="pay">
                                    <c:if test="${order.paymentMethodID == pay.paymentMethodID}">                                 
                                        <td >${pay.methodName}</td>
                                    </c:if>  
                                </c:forEach>
                                <td>
                                    <fmt:formatNumber value="${order.totalPrice}" type="number" groupingUsed="true"/> VND
                                </td>
                                <td>${order.deliveryAddress}</td>


                                <td>
                                    <a href="viewOrderDetails?id=${order.orderID}" class="btn btn-primary btn-sm">
                                        <i class="fas fa-eye"></i> View
                                    </a>

                                    <form method="POST" action="updateOrderStatus" style="display: inline;">
                                        <input type="hidden" name="id" value="${order.orderID}" />

                                        <!-- Xác định trạng thái tiếp theo -->
                                        <c:choose>
                                            <c:when test="${order.orderStatus == 1}">
                                                <input type="hidden" name="status" value="2" />
                                                <button type="submit" class="btn btn-primary btn-sm">
                                                    <i class="fas fa-check"></i> Confirm.
                                                </button>
                                            </c:when>
                                            <c:when test="${order.orderStatus == 2}">
                                                <input type="hidden" name="status" value="3" />
                                                <button type="submit" class="btn btn-warning btn-sm">
                                                    <i class="fas fa-truck"></i> Start delivery.
                                                </button>
                                            </c:when>
                                            <c:when test="${order.orderStatus == 3}">
                                                <input type="hidden" name="status" value="4" />
                                                <button type="submit" class="btn btn-success btn-sm">
                                                    <i class="fas fa-box"></i> Complete the delivery.
                                                </button>
                                            </c:when>
                                        </c:choose>
                                    </form>


                                    <c:if test="${sessionScope.managerRole == 1}">
                                        <a href="deleteOrder?id=${order.orderID}" class="btn btn-danger btn-sm" onclick="return confirmDelete(${order.orderID});">
                                            <i class="fas fa-trash"></i> Delete
                                        </a>                
                                    </c:if> 


                                </td>

                        <script>
                            function confirmDelete(orderID) {
                                if (confirm("Are you sure you want to delete this order?")) {
                                    window.location.href = "deleteOrder?id=" + orderID;
                                }
                                return false;
                            }
                        </script>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>

        <script>
            document.addEventListener("DOMContentLoaded", function () {
                // Lấy tham số 'status' từ URL
                const urlParams = new URLSearchParams(window.location.search);
                const status = urlParams.get("status");

                // Nếu có trạng thái, cuộn đến vị trí đơn hàng đầu tiên có trạng thái đó
                if (status) {
                    const targetRow = document.getElementById("status-" + status);
                    if (targetRow) {
                        targetRow.scrollIntoView({behavior: "smooth", block: "start"});
                    }
                }
            });
        </script>
        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
