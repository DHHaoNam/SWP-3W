<%@page import="model.Customer"%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.List"%>
<%@ page import="model.Cart, model.PaymentMethod, model.Address" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%
    List<Cart> cartItems = (List<Cart>) request.getAttribute("cartItems");
    List<PaymentMethod> paymentMethods = (List<PaymentMethod>) request.getAttribute("paymentMethods");
    Address defaultAddress = (Address) request.getAttribute("defaultAddress");
    Customer customer = (Customer) request.getAttribute("customer");
    Double total = (Double) request.getAttribute("total");
%>


<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Thanh toán</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="./CSS/Style.css">
        <link rel="stylesheet" href="./CSS/HeaderAndFooter_CSS.css">
        <link rel="stylesheet" href="./CSS/home.css">
        <script src="./Script/header-script.js"></script>
        <style>
            .empty-cart-message {
                color: red;
                font-weight: bold;
            }

            .empty-cart-container {
                display: flex;
                flex-direction: column;
                align-items: center;
                justify-content: center;
                height: 300px; /* Điều chỉnh chiều cao theo mong muốn */
            }

            .btn-secondary {
                background-color: #6c757d;
                color: white;
                padding: 10px 15px;
                border-radius: 5px;
                text-decoration: none;
                font-weight: bold;
                transition: background-color 0.3s ease;
            }

            .btn-secondary:hover {
                background-color: #5a6268;
            }

            /* Định dạng các phần hiển thị giá trị */
            .total-amount, .total-after-discount {
                display: flex;
                justify-content: space-between;
                align-items: center;
                font-size: 1.2rem;
            }

            .total-amount b, .total-after-discount b {
                font-weight: bold;
            }

            .total-amount span, .total-after-discount span {
                font-size: 1.4rem;
                color: #333;
            }

            .total-amount .price {
                font-size: 1.4rem;
                color: #007bff; /* Màu xanh cho giá trị ban đầu */
            }

            .total-after-discount .price {
                font-size: 1.6rem;
                color: #ff5733; /* Màu đỏ cho giá trị sau giảm giá */
                font-weight: bold;
            }

            /* Đảm bảo thông tin "Voucher" có vẻ ngoài rõ ràng */
            .voucher-section {
                margin-top: 20px;
                display: flex;
                flex-direction: column;
                align-items: flex-end;
            }

            .voucher-section .voucher-input {
                margin-right: 10px;
                width: 80%;
            }


        </style>

    </head>
    <body>


        <jsp:include page="header.jsp"></jsp:include>

            <div class="container mt-4 order-history">
                <h3 class="text-center mb-4">Checkout</h3>

            <% if (cartItems == null || cartItems.isEmpty()) { %>
            <div class="empty-cart-container text-center">
                <% String errorMessage = (String) request.getAttribute("errorMessage"); %>
                <% if (errorMessage != null) {%>
                <div style="color: red; font-weight: bold; margin-top: 10px;">
                    <%= errorMessage%>
                </div>
                <% } %>
                <p class="empty-cart-message">Your cart is empty.</p>
                <a class="btn btn-secondary" href="home">Continue Shopping</a>
            </div>
            <% } else { %>
            <form action="checkout" method="post">
                <table class="table table-bordered text-center">
                    <thead class="table-primary">
                        <tr>
                            <th>Product</th>
                            <th>Quantity</th>
                            <th>Price</th>
                            <th>Total</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (Cart item : cartItems) {%>
                        <tr>
                            <td class="product-name"><%= item.getProduct().getProductName()%></td>
                            <td><%= item.getQuantity()%></td>
                            <td><fmt:formatNumber value="<%= item.getProduct().getPrice()%>" pattern="#,###đ"/></td>
                            <td><fmt:formatNumber value="<%= item.getQuantity() * item.getProduct().getPrice()%>" pattern="#,###đ"/></td>
                        </tr>
                        <% }%>
                    </tbody>
                </table>



                <!-- Thông tin khách hàng -->
                <div class="mb-3">
                    <label class="form-label">Full Name:</label>
                    <input type="text" class="form-control" name="fullName" 
                           value="<%= (customer != null) ? customer.getFullName() : ""%>" readonly>
                </div>
                <div class="mb-3">
                    <label class="form-label">Phone Number:</label>
                    <input type="text" class="form-control" name="phoneNumber" 
                           value="<%= (customer != null) ? customer.getPhone() : ""%>" readonly>
                </div>
                <div class="mb-3">
                    <label class="form-label">Email:</label>
                    <input type="email" class="form-control" name="email" 
                           value="<%= (customer != null) ? customer.getEmail() : ""%>" readonly>
                </div>

                <!-- Địa chỉ giao hàng -->
                <div class="mb-3">
                    <label class="form-label">Shipping Address:</label>
                    <input type="text" class="form-control" name="deliveryAddress" 
                           value="<%= (defaultAddress != null) ? defaultAddress.getAddressDetail() : ""%>" required>
                </div>

                <input type="hidden" name="paymentMethodID" value="<%= paymentMethods.get(0).getPaymentMethodID()%>">

                <!-- Phương thức thanh toán -->
                <div class="mb-3">
                    <label class="form-label">Payment Method:</label>
                    <input type="text" class="form-control" value="<%= paymentMethods.get(0).getMethodName()%>" readonly />
                </div>

                <!-- Ô nhập voucher -->
                <%
                    Double discountedTotal = (Double) request.getAttribute("discountedTotal");
                %>
                <p class="text-end total-amount">
                    <b>Total Amount: </b>
                    <span class="price"><fmt:formatNumber value="<%= total%>" pattern="#,###đ"/></span>
                </p>

                <% if (discountedTotal != null) {%>
                <p class="text-end total-after-discount">
                    <b>Total After Discount: </b>
                    <span class="price"><fmt:formatNumber value="<%= discountedTotal%>" pattern="#,###đ"/></span>
                </p>
                <% }%>
                <!-- Phần nhập voucher -->
                <div class="voucher-section">
                    <label class="form-label">Voucher title: </label>
                    <div class="d-flex">
                        <input type="text" class="form-control voucher-input" name="voucherCode" value="<%= request.getAttribute("voucherCode") != null ? request.getAttribute("voucherCode") : ""%>" placeholder="Enter your voucher title">
                        <button type="submit" name="action" value="applyVoucher" class="btn btn-primary ms-2">Apply</button>
                    </div>
                    <!-- Hiển thị thông báo sau khi nhập mã -->
                    <%
                        String successMessage = (String) request.getAttribute("successMessage");
                        String errorMessage = (String) request.getAttribute("errorMessage");
                        if (successMessage != null) {
                    %>
                    <div class="alert alert-success mt-2"><%= successMessage%></div>
                    <%
                    } else if (errorMessage != null) {
                    %>
                    <div class="alert alert-danger mt-2"><%= errorMessage%></div>
                    <%
                        }
                    %>                </div>

                <div class="text-center" style="margin-bottom: 30px">
                    <button type="submit" class="btn btn-success" style="border-radius: 20px">Checkout</button>
                    <a href="cart.jsp" class="btn btn-secondary">Back to Cart</a>
                </div>
            </form>
            <% }%>
        </div>

        <jsp:include page="footer.jsp"></jsp:include>

    </body>

</html>
