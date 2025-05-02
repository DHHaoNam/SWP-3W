<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Add Attribute</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css"/>
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
            .sidebar a {
                color: white;
                text-decoration: none;
                display: block;
                padding: 10px 15px;
                margin: 5px 0;
                border-radius: 5px;
            }
            .sidebar a:hover {
                background-color: #495057;
            }
            .content {
                margin-left: 250px;
                padding: 20px;
                background-color: #f8f9fa;
                min-height: 100vh;
            }
        </style>
    </head>
    <body>

        <jsp:include page="admin_dashboard_header.jsp" />

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

            <!-- Main Content -->
            <div class="content">
                <h2 class="mb-4">Attribute Management for Category ID: <span class="text-primary">${categoryID}</span></h2>

                <div class="row">
                    <!-- Add Form -->
                    <div class="col-md-6">
                        <div class="card shadow p-3">
                            <h4 class="card-title mb-3">Add Attribute</h4>
                            <form action="insert-attribute" method="post">
                                <div class="mb-3">
                                    <label for="attributeName" class="form-label">Attribute Name</label>
                                    <input type="text" class="form-control" id="attributeName" name="attributeName" required>
                                    <input type="hidden" name="categoryID" value="${categoryID}">

                                </div>

                                <!-- display error -->
                                <c:choose>
                                    <c:when test="${error == 'duplicate'}">
                                        <div style="color: red; font-weight: bold;">
                                            This attribute name already exists in the selected category.
                                        </div>
                                    </c:when>
                                    <c:when test="${error == 'exception'}">
                                        <div style="color: red; font-weight: bold;">
                                            An unexpected error occurred. Please try again later.
                                        </div>
                                    </c:when>
                                    <c:when test="${success == 'added'}">
                                        <div style="color: green; font-weight: bold;">
                                            Attribute added successfully!
                                        </div>
                                    </c:when>
                                </c:choose>

                                <!--submit-->
                                <button type="submit" class="btn btn-primary">Save Attribute</button>
                            </form>
                        </div>
                    </div>

                    <!-- Attribute List -->
                    <div class="col-md-6">
                        <div class="card shadow p-3">
                            <h4 class="card-title mb-3">Attribute List</h4>

                            <!--display delete result-->
                            <c:choose>
                                <c:when test="${success == 'deleted'}">
                                    <div class="alert alert-success" role="alert">
                                        Attribute deleted successfully!
                                    </div>
                                </c:when>
                                <c:when test="${error == 'foreign_key_constraint'}">
                                    <div class="alert alert-danger" role="alert">
                                        Cannot delete this attribute. It is being used in other data!
                                    </div>
                                </c:when>
                                <c:when test="${error == 'link_not_found_or_failed'}">
                                    <div class="alert alert-warning" role="alert">
                                        Failed to remove link between category and attribute. Deletion aborted.
                                    </div>
                                </c:when>
                                <c:when test="${error == 'exception'}">
                                    <div class="alert alert-danger" role="alert">
                                        An unexpected error occurred during deletion. Please try again.
                                    </div>
                                </c:when>
                            </c:choose>


                            <table class="table table-bordered table-hover">
                                <thead class="table-light">
                                    <tr>
                                        <th>ID</th>
                                        <th>Attribute Name</th>
                                        <th>Action</th>

                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="attributes" items="${requestScope.attributes}">
                                        <tr>
                                            <td>${attributes.attributeID}</td>
                                            <td>${attributes.attributeName}</td>
                                            <td>
                                                <a href="newedit-attribute?id=${attributes.attributeID}&categoryID=${categoryID}" class="btn btn-primary btn-sm">
                                                    <i class="fas fa-edit"></i> Edit
                                                </a>
                                                <a href="delete-attribute?id=${attributes.attributeID}&categoryID=${categoryID}" 
                                                   class="btn btn-danger btn-sm"
                                                   onclick="return confirm('Are you sure you want to delete this attribute?');">
                                                    <i class="fas fa-trash"></i> Delete
                                                </a>

                                            </td>
                                        </tr>
                                    </c:forEach>
                                    <c:if test="${empty requestScope.attributes}">
                                        <tr>
                                            <td colspan="2" class="text-center text-muted">No attributes found.</td>
                                        </tr>
                                    </c:if>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>



