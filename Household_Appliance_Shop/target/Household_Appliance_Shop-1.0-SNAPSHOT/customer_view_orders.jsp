<!DOCTYPE html>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Order Details</title>
        <!-- Font Awesome -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css" crossorigin="anonymous" referrerpolicy="no-referrer" />
        <!-- Bootstrap -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="./CSS/HeaderAndFooter_CSS.css">
        <link rel="stylesheet" href="./CSS/home.css">
        <script src="./Script/header-script.js"></script>

        <style>
            body {
                font-family: 'Arial', sans-serif;
                background-color: #f7f7f7;
            }
            .order-details {
                background: white;
                padding: 20px;
                border-radius: 8px;
                box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
            }
            .order-details h3 {
                font-size: 22px;
                font-weight: bold;
                margin-bottom: 20px;
                color: #333;
            }
            .table th, .table td {
                text-align: center;
                vertical-align: middle;
            }
        </style>
    </head>
    <body>
        <jsp:include page="header.jsp"></jsp:include>

            <div class="container my-5">
                <h3>Order Details</h3>
                <div class="order-details">
                    <div class="mb-3">
                        <label class="form-label">Order ID</label>
                        <input type="text" class="form-control" value="${OrderInfo.orderID}" readonly>
                </div>
                <div class="mb-3">
                    <label class="form-label">Customer</label>                 
                    <input type="text" class="form-control" value="${Customer.fullName}" readonly>
                </div>
                <div class="mb-3">
                    <label class="form-label">Order Date</label>
                    <input type="text" class="form-control" value="${OrderInfo.orderDate}" readonly>
                </div>
                <div class="mb-3">
                    <label class="form-label">Status</label>
                    <input type="text" class="form-control" value="${OrderInfo.orderStatus == 1 ? 'Chưa xác nhận' : 
                                                                     OrderInfo.orderStatus == 2 ? 'Đã xác nhận' : 
                                                                     OrderInfo.orderStatus == 3 ? 'Đang giao' : 
                                                                     OrderInfo.orderStatus == 4 ? 'Đã giao' : 
                                                                     OrderInfo.orderStatus == 5 ? 'Đã hủy' : 
                                                                     'Khác'}" readonly>
                </div>
                <div class="mb-3">
                    <label class="form-label">Address</label>
                    <input type="text" class="form-control" value="${OrderInfo.deliveryAddress}" readonly>
                </div>
                <div class="mb-3">
                    <label class="form-label">Phone Number</label>
                    <input type="text" class="form-control" value="${Customer.phone}" readonly>
                </div>

                <h3 class="mt-4">Ordered Products</h3>
                <div class="table-responsive">
                    <table class="table table-bordered">
                        <thead class="table-dark">
                            <tr>
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
                </div>

                <div class="mb-3">
                    <label class="form-label">Total Amount</label>
                    <input type="text" class="form-control" value="<fmt:formatNumber value='${OrderInfo.totalPrice}' type='number' pattern='#,### VND'/>" readonly>
                </div>
                <div class="text-center mt-4">
                    <a href="listOrders" class="btn btn-secondary">
                        <i class="fas fa-arrow-left"></i> Back to Order History
                    </a>
                </div>
            </div>
        </div>

        <jsp:include page="footer.jsp"></jsp:include>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
