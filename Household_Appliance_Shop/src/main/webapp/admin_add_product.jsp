<%-- 
    Document   : admin_add_product
    Created on : Oct 18, 2024, 12:41:13 PM
    Author     : admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Add New Product</title>

        <!-- Font Awesome -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css" crossorigin="anonymous" referrerpolicy="no-referrer" />

        <!-- Bootstrap -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">

        <!-- Custom CSS -->
        <link rel="stylesheet" href="./CSS/Style.css" />
        <link rel="stylesheet" href="./CSS/HeaderAndFooter_CSS.css" />

        <style>
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
            .content {
                margin-left: 250px;
                padding: 20px;
                background-color: #f8f9fa;
                width: calc(100% - 250px);
            }
            .clearfix::after {
                content: "";
                display: table;
                clear: both;
            }
        </style>
    </head>
    <body>

        <jsp:include page="admin_dashboard_header.jsp" />

        <!-- Sidebar -->
        <div class="sidebar">
            <c:choose>
                <c:when test="${sessionScope.managerRole == 1}">
                    <h3>Admin Dashboard</h3>
                    <a href="CategoryController"><i class="fas fa-list"></i> Category Management</a>

                </c:when>
                <c:when test="${sessionScope.managerRole == 2}">
                    <h3>Staff Dashboard</h3>
                </c:when>
            </c:choose>
            <a href="ProductController"><i class="fas fa-box"></i> Product Management</a>
            <a href="CustomerController_temp"><i class="fas fa-users"></i> Customer Management</a>
            <a href="listAdminOrders"><i class="fas fa-shopping-cart"></i> Order Management</a>
            <a href="FeedbackController"><i class="fas fa-comments"></i> Feedback Management</a>
            <c:if test="${sessionScope.managerRole == 1}">
                <a href="VoucherController"><i class="fas fa-ticket-alt"></i> Voucher Management</a>
            </c:if>   
            <a href="RevenueController"><i class="fa-solid fa-chart-simple"></i> Revenue Management</a>
        </div>

        <!-- Main Content -->
        <div class="content">
            <div class="container mt-5">
                <h1>Add New Product</h1>
                <form method="POST" action="/new">
                    <div class="mb-3">
                        <label for="category" class="form-label">Category</label>
                        <select class="form-select" id="category" name="categoryID" onchange="this.form.submit()">
                            <option value="">Select category</option>
                            <c:forEach var="category" items="${category}">
                                <option value="${category.categoryID}" ${param.categoryID == category.categoryID ? 'selected' : ''}>
                                    ${category.categoryName}
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                </form>

                <form action="insert" method="POST">
                    <div class="mb-3">
                        <label for="name" class="form-label">Product Name</label>
                        <input type="text" class="form-control" id="name" name="name" required>
                    </div>
                    <div class="mb-3">
                        <label for="price" class="form-label">Price</label>
                        <input type="number" step="0.01" class="form-control" id="price" name="price" required>
                    </div>
                    <div class="mb-3">
                        <label for="description" class="form-label">Description</label>
                        <input type="text" class="form-control" id="description" name="description" required>
                    </div>
                    <div class="mb-3">
                        <label for="quantity" class="form-label">Quantity</label>
                        <input type="number" class="form-control" id="quantity" name="quantity" required>
                    </div>


                    <div class="mb-3">
                        <label for="subcategory" class="form-label">Loại phụ</label>
                        <select class="form-select" id="subcategory" name="subcategoryID" required>
                            <option value="">Chọn loại phụ</option>
                            <c:forEach var="sub" items="${subCategories}">
                                <option value="${sub.subCategoryID}">${sub.subCategoryName}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="mb-3">
                        <label for="brand" class="form-label">Brand</label>
                        <select class="form-select" id="brand" name="brandID" required>
                            <option value="">Select Brand</option>
                            <c:forEach var="brand" items="${brand}">
                                <option value="${brand.brandID}">${brand.brandName}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <c:if test="${not empty attributes}">
                        <h5>Product Attributes</h5>
                        <c:forEach var="attr" items="${attributes}">
                            <div class="mb-3">
                                <label class="form-label">${attr.attributeName}</label>
                                <input type="text" class="form-control" name="attribute_${attr.attributeID}" placeholder="Enter ${attr.attributeName}" />
                            </div>
                        </c:forEach>
                    </c:if>

                    <div class="mb-3">
                        <label for="image" class="form-label">Image URL</label>
                        <input type="text" class="form-control" id="image" name="image" required>
                    </div>

                    <c:if test="${not empty error}">
                        <div class="alert alert-danger">${error}</div>
                    </c:if>

                    <button type="submit" class="btn btn-primary"><i class="fas fa-save"></i> Save Product</button>
                </form>
            </div>
        </div>




        <!-- Bootstrap Bundle JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>



