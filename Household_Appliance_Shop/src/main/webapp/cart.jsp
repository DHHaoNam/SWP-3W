<%@page import="model.Cart"%>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="model.Product" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Shopping Cart</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="./CSS/Style.css">
        <link rel="stylesheet" href="./CSS/HeaderAndFooter_CSS.css">
        <link rel="stylesheet" href="./CSS/home.css">
        <script src="./Script/header-script.js"></script>

        <style>
            :root {
                --primary-color: #FC6E51;
                --primary-dark: #e85d3f;
                --bg-light: #f8f9fa;
                --text-color: #333;
            }
            body {
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                background-color: var(--bg-light);
            }
            .order-history {
                background-color: white;
                padding: 30px;
                border-radius: 8px;
                box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
            }
            .product-name {
                font-size: 1.2rem;
                font-weight: bold;
                color: var(--primary-color);
            }
            table {
                width: 100%;
                border-collapse: collapse;
                font-size: 15px;
            }
            th, td {
                padding: 15px;
                text-align: left;
                border-bottom: 1px solid #ddd;
                vertical-align: middle;
            }
            .btn-danger {
                background-color: var(--primary-dark);
                border: none;
                border-radius: 20px;
                padding: 8px 15px;
                color: white;
                transition: background-color 0.3s;
            }
            .btn-danger:hover {
                background-color: var(--primary-color);
            }
            .quantity-control {
                display: flex;
                align-items: center;
                gap: 10px;
            }
            .quantity-btn {
                background-color: var(--primary-color);
                border: none;
                color: white;
                border-radius: 5px;
                padding: 8px 12px;
                cursor: pointer;
            }
            .quantity-btn:hover {
                background-color: var(--primary-dark);
            }
            .quantity-input {
                width: 60px;
                text-align: center;
                border: 1px solid var(--primary-color);
                border-radius: 5px;
                padding: 5px;
            }
            td img {
                width: 100px; /* Điều chỉnh chiều rộng */
                height: 100px; /* Điều chỉnh chiều cao */
                object-fit: cover; /* Đảm bảo ảnh không bị méo */
                border-radius: 10px; /* Bo tròn nhẹ góc ảnh */
                border: 1px solid #ddd; /* Viền nhẹ giúp ảnh trông chuyên nghiệp */
                padding: 5px; /* Thêm khoảng cách giữa ảnh và viền */
                background-color: white; /* Nền trắng giúp ảnh nổi bật */
            }
            .continue-shopping {
                margin-bottom: 50px; /* Điều chỉnh khoảng cách theo ý muốn */
            }

        </style>
    </head>

    <c:if test="${not empty sessionScope.error}">
        <div class="alert alert-danger text-center">${sessionScope.error}</div>
        <c:remove var="error" scope="session"/>
    </c:if>

    <body>
        <%-- Header --%>
        <jsp:include page="header.jsp"></jsp:include>

            <div class="container mt-4">
                <h3 class="text-center mb-4">Your Shopping Cart</h3>

                <form action="cart" method="post">
                    <table class="table table-bordered text-center">
                        <thead class="table-primary">
                            <tr>  
                                <th>Product Name</th>
                                <th>Image</th>
                                <th>Quantity</th>
                                <th>Price</th>
                                <th>Total Price</th>
                                <th></th>
                            </tr>
                        </thead>
                        <tbody>
                        <%
                            List<Cart> cartItems = (List<Cart>) request.getAttribute("cartlists");
                            if (cartItems != null && !cartItems.isEmpty()) {
                                for (Cart item : cartItems) {
                        %>
                        <tr>
                            <td><%= item.getProduct().getProductName()%></td>
                            <td><img src="<%= item.getProduct().getImage()%>" class="img-thumbnail" style="width: 100px; height: 100px;"></td>
                            <td>
                                <div class="d-flex align-items-center justify-content-center">
                                    <input type="hidden" name="productId" value="<%= item.getProduct().getProductID()%>">
                                    <input type="number" class="form-control text-center" name="quantity" 
                                           value="<%= item.getQuantity()%>" min="1" 
                                           style="width: 60px;">
                                </div>
                            </td>
                            <td><fmt:formatNumber value="<%= item.getProduct().getPrice()%>" pattern="#,###đ"/></td>
                            <td><fmt:formatNumber value="<%= item.getTotalPrice()%>" pattern="#,###đ"/></td>
                            <td>
                                <button type="submit" name="removeProductId" value="<%= item.getProduct().getProductID()%>" 
                                        class="btn btn-danger btn-sm" onclick="return confirmDelete();">
                                    Remove
                                </button>
                            </td>
                        </tr>
                        <% }
                        } else { %>
                        <tr><td colspan="6" class="text-center">Your cart is empty.</td></tr>
                        <% }%>
                    </tbody>
                </table>

                <%-- Cập nhật giỏ hàng --%>
                <div class="text-end">
                    <button type="submit" class="btn btn-primary">Update Cart</button>
                </div>
            </form>

            <%-- Tổng giá --%>
            <%
                double totalPrice = 0;
                if (cartItems != null) {
                    for (Cart item : cartItems) {
                        totalPrice += item.getTotalPrice();
                    }
                }
            %>
            <div class="text-end mt-3">
                <h5><strong>Total Amount: <fmt:formatNumber value="<%= totalPrice%>" pattern="#,###đ"/> </strong></h5>
            </div>


            <%-- Nút quay về trang mua hàng --%>
            <div class="text-center mt-3 continue-shopping">
                <a href="home" class="btn btn-secondary">Continue Shopping</a>              
                <form action="checkout" method="get" style="display: inline;">
                    <button type="submit" class="btn btn-success paybtn" style="border-radius: 20px">Place Order</button>
                </form>
            </div>
        </div>

        <%-- Footer --%>
        <jsp:include page="footer.jsp"></jsp:include>

        <script>
            function updateTotalPrice() {
                let totalPrice = 0;
                document.querySelectorAll('input[name="quantity"]').forEach(input => {
                    let row = input.closest('tr');
                    let price = parseFloat(row.cells[3].innerText.replace('$', ''));
                    let quantity = parseInt(input.value);
                    totalPrice += price * quantity;
                });

                document.getElementById('total-price').textContent = totalPrice.toLocaleString('vi-VN');
            }

            document.querySelectorAll('input[name="quantity"]').forEach(input => {
                input.addEventListener('change', updateTotalPrice);
            });

            updateTotalPrice(); // Cập nhật tổng giá khi trang tải xong



            function confirmDelete() {
                return window.confirm("Are you sure you want to remove this product from your cart?");
            }



        </script>

    </body>
</html>
