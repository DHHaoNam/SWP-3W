<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Edit Product</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css" integrity="sha512-Kc323vGBEqzTmouAECnVceyQqyqdsSiqLQISBL29aUW4U/M7pSPA/gEUZQqv1cwx4OnYxTxve5UMg5GT6L4JJg==" crossorigin="anonymous" referrerpolicy="no-referrer" />
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="./CSS/Style.css" />
        <link rel="stylesheet" href="./CSS/HeaderAndFooter_CSS.css" />
        <style>
            body {
                background-color: #f8f9fa;
            }
            .sidebar {
                width: 230px;
                background-color: #343a40;
                padding: 20px;
                height: 100vh;
                position: fixed;
                top: 0;
                left: 0;
                color: white;
            }
            .sidebar h3 {
                margin-bottom: 20px;
                font-size: 18px;
                text-transform: uppercase;
                text-align: center;
            }
            .sidebar a {
                display: block;
                color: white;
                padding: 12px;
                text-decoration: none;
                border-radius: 5px;
                margin-bottom: 8px;
                transition: background 0.3s;
            }
            .sidebar a:hover {
                background: #495057;
            }
            .content {
                margin-left: 250px;
                padding: 30px;
            }
            .form-container {
                max-width: 700px;
                background: white;
                padding: 30px;
                border-radius: 8px;
                box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
            }
        </style>
    </head>
    <body>
        <jsp:include page="admin_dashboard_header.jsp" />
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

        <div class="content">
            <div class="container">
                <div class="form-container mx-auto">
                    <h2 class="text-center mb-4">Edit Product</h2>

                    <!-- Category selection form -->
                    <form method="GET" action="/newedit">
                        <input type="hidden" name="action" value="edit"/>
                        <input type="hidden" name="id" value="${p.productID}"/>
                        <div class="mb-3">
                            <label for="category" class="form-label">Category</label>
                            <select class="form-select" id="category" name="categoryID" onchange="this.form.submit()">
                                <option value="">Select category</option>
                                <c:forEach var="category" items="${categories}">
                                    <option value="${category.categoryID}" 
                                            ${param.categoryID == category.categoryID || category.categoryID == selectedCategoryID ? 'selected' : ''}>
                                        ${category.categoryName}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                    </form>

                    <!-- Main product update form -->
                    <form method="POST" action="/edit">
                        <input type="hidden" name="action" value="update"/>
                        <input type="hidden" name="id" value="${p.productID}"/>
                        <input type="hidden" name="categoryID" value="${selectedCategoryID}"/>

                        <!-- SubCategory -->
                        <div class="mb-3">
                            <label for="subcategory" class="form-label">SubCategory</label>
                            <select class="form-select" id="subcategory" name="subCategoryID" required>
                                <option value="">Select SubCategory</option>
                                <c:forEach var="sc" items="${subCategories}">
                                    <option value="${sc.subCategoryID}" ${sc.subCategoryID == p.subCategoryID ? 'selected' : ''}>${sc.subCategoryName}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <!-- Product Name -->
                        <div class="mb-3">
                            <label class="form-label">Product Name</label>
                            <input type="text" class="form-control" name="name" value="${p.productName}" required/>
                        </div>

                        <!-- Price -->
                        <div class="mb-3">
                            <label class="form-label">Price</label>
                            <input type="number" step="0.01" class="form-control" name="price" value="${p.price}" required/>
                        </div>

                        <!-- Description -->
                        <div class="mb-3">
                            <label class="form-label">Description</label>
                            <input type="text" class="form-control" name="description" value="${p.description}" required/>
                        </div>

                        <!-- Quantity -->
                        <div class="mb-3">
                            <label class="form-label">Quantity</label>
                            <input type="number" class="form-control" name="quantity" value="${p.stock_Quantity}" required/>
                        </div>

                        <!-- Brand -->
                        <div class="mb-3">
                            <label class="form-label">Brand</label>
                            <select class="form-select" name="brandID" required>
                                <option value="">Select Brand</option>
                                <c:forEach var="brand" items="${brand}">
                                    <option value="${brand.brandID}" ${brand.brandID == p.brandID ? 'selected' : ''}>${brand.brandName}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <!-- Attributes -->
                        <c:if test="${not empty attributes}">
                            <h5>Product Attributes</h5>
                            <c:forEach var="attr" items="${attributes}">
                                <div class="mb-3">
                                    <label class="form-label">${attr.attributeName}</label>
                                    <input type="hidden" name="attributeID" value="${attr.attributeID}" />
                                    <input type="text" class="form-control" name="attributeValue"
                                           value="${productAttributeMap[attr.attributeID]}" placeholder="Enter ${attr.attributeName}" />
                                </div>
                            </c:forEach>
                        </c:if>

                        <!-- Image -->
                        <div class="mb-3">
                            <label class="form-label">Image URL</label>
                            <input type="text" class="form-control" name="image" value="${p.image}" required/>
                        </div>

                        <c:if test="${not empty error}">
                            <div class="alert alert-danger" role="alert">
                                ${error}
                            </div>
                        </c:if>


                        <button type="submit" class="btn btn-primary">Update Product</button>
                    </form>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>



