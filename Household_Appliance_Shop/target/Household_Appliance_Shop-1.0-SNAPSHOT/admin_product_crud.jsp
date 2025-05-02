<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Admin Dashboard</title>
        <!-- Font Awesome -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css"
              integrity="sha512-Kc323vGBEqzTmouAECnVceyQqyqdsSiqLQISBL29aUW4U/M7pSPA/gEUZQqv1cwx4OnYxTxve5UMg5GT6L4JJg=="
              crossorigin="anonymous" referrerpolicy="no-referrer" />
        <!-- Bootstrap -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css"
              rel="stylesheet">
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
                <h1>Product Management</h1>

                <!-- Search Form -->
                <!-- Form tìm kiếm -->
                <form method="GET" action="searchProduct" class="mb-3">
                    <div class="row">
                        <div class="col-md-8">
                            <input type="text" name="search" class="form-control" placeholder="Search product by name" value="${param.search}">
                        </div>
                        <div class="col-md-4">
                            <button type="submit" class="btn btn-primary w-100"><i class="fas fa-search"></i> Search</button>
                        </div>
                    </div>
                </form>
                <c:set var="i" value="${currentPage}" />        
                <form method="GET" action="ProductController" class="mb-3">
                    <!-- Giữ lại input hidden để duy trì trang hiện tại -->
                    <input type="hidden" name="index" value="${currentPage}"/>

                    <div class="row">
                        <div class="col-md-4">
                            <input type="number" name="minPrice" class="form-control" placeholder="Min Price" value="${param.minPrice}"/>
                        </div>
                        <div class="col-md-4">
                            <input type="number" name="maxPrice" class="form-control" placeholder="Max Price" value="${param.maxPrice}"/>
                        </div>
                        <div class="col-md-4">
                            <button type="submit" class="btn btn-secondary w-100"><i class="fas fa-filter"></i> Filter</button>
                        </div>
                    </div>
                </form>

                <c:if test="${sessionScope.managerRole == 1}">

                    <!-- Add Product Button -->
                    <a href="<%=request.getContextPath()%>/new" class="btn btn-success mb-3"><i class="fas fa-plus"></i> Add New Product</a>
                </c:if>
                <c:if test="${not empty error}">
                    <div class="alert alert-danger" role="alert">
                        ${error}
                    </div>
                </c:if>
                <!-- Product Table -->
                <c:if test="${not empty requestScope.p}">
                    <table class="table table-bordered">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Product Name</th>
                                <th>Price</th>
                                <th>Quantity</th>
                                <th>SubCategory</th>
                                <th>Brand</th>
                                <th>Description</th>
                                <th>Image</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="p" items="${requestScope.p}">
                                <tr>
                                    <td>${p.productID}</td>
                                    <td>${p.productName}</td>
                                    <td><fmt:formatNumber value="${p.price}" pattern="#,###₫"/></td>
                                    <td>${p.stock_Quantity}</td>                        
                                    <c:forEach var="subcategories" items="${subcategories}">
                                        <c:if test="${p.subCategoryID == subcategories.subCategoryID}">
                                            <td>${subcategories.subCategoryName}</td>
                                        </c:if>
                                    </c:forEach>
                                    <c:forEach var="brand" items="${brand}">
                                        <c:if test="${p.brandID == brand.brandID}">
                                            <td>${brand.brandName}</td>
                                        </c:if>
                                    </c:forEach>
                                    <c:forEach var="brand" items="${brand}">
                                        <c:if test="${p.brandID == brand.brandID}">
                                            <td>${brand.brandName}</td>
                                        </c:if>
                                    </c:forEach>
                                    <td><img src="${p.image}" alt="${p.productName}" style="width: 50px; height: auto;"></td>
                                    <td>
                                        <a href="newedit?id=${p.productID}" class="btn btn-primary btn-sm"><i class="fas fa-edit"></i> Edit</a>
                                        <c:if test="${sessionScope.managerRole == 1}">
                                            <a href="delete?id=${p.productID}" class="btn btn-danger btn-sm"
                                               onclick="return confirm('Are you sure you want to delete this product?');"><i class="fas fa-trash"></i> Delete</a>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:if>

                <!-- Hiển thị thông báo nếu không có sản phẩm -->
                <c:if test="${empty requestScope.p}">
                    <p class="text-center text-danger">No products found.</p>
                </c:if>


                
            </div>
        </div>

        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
        <script>
                                                   document.addEventListener('DOMContentLoaded', function () {
                                                       // Get the current URL
                                                       const currentURL = window.location.href;

                                                       // Get all pagination links
                                                       const paginationLinks = document.querySelectorAll('.pagination a');

                                                       // Loop through all pagination links
                                                       paginationLinks.forEach(link => {
                                                           const parentLi = link.closest('li');
                                                           const isPageNumber = !parentLi.classList.contains('disabled') && !link.getAttribute('aria-label');

                                                           // Check if the link's href matches the current URL and it's a page number link
                                                           if (currentURL.includes(link.href) && isPageNumber) {
                                                               link.classList.add('active');
                                                           }
                                                       });
                                                   });
        </script>
    </body>

</html>


