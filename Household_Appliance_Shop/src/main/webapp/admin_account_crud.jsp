<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Admin Dashboard - Account Management</title>
        <!-- Font Awesome -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css"
              integrity="sha512-Kc323vGBEqzTmouAECnVceyQqyqdsSiqLQISBL29aUW4U/M7pSPA/gEUZQqv1cwx4OnYxTxve5UMg5GT6L4JJg=="
              crossorigin="anony
              mous" referrerpolicy="no-referrer" />
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
        <c:if test="${not empty customers}">
            <div class="content">
                <h1>Customer Management</h1>
                <c:if test="${sessionScope.managerRole == 1}">
                    <a href="ManagerController" class="btn btn-secondary mb-3">
                        <i class="fas fa-user-shield"></i> Manager Management
                    </a>
                </c:if>


                <!-- Search Form -->
                <form method="GET" action="admin-search-account" class="mb-3">
                    <div class="row">
                        <div class="col-md-6">
                            <input type="text" name="search" class="form-control" placeholder="Search account by phone number" value="${param.search}">
                        </div>
                        <div class="col-md-2">
                            <button type="submit" class="btn btn-primary w-100"><i class="fas fa-search"></i> Search</button>
                        </div>
                    </div>
                </form>
                <!-- Account Table -->
                <table class="table table-bordered">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Name</th>
                            <th>Email</th>
                            <th>Phone Number</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="a" items="${customers}">
                            <tr>
                                <td>${a.customerID}</td>
                                <td>${a.fullName}</td>                             
                                <td>${a.email}</td>
                                <td>${a.phone}</td>
                                <td>  
                                    <c:choose>
                                        <c:when test="${a.status}">
                                            <span class="badge bg-success">Active</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-danger">Inactive</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <a href="/admin-view-account?id=${a.customerID}" class="btn btn-info btn-sm">
                                        <i class="fas fa-eye"></i> View Details
                                    </a>

                                    <c:if test="${sessionScope.managerRole == 1}">
                                        <a href="/admin-edit-account?id=${a.customerID}" class="btn btn-primary btn-sm"><i class="fas fa-edit"></i> Edit</a>
                                        <a href="/admin-delete-account?id=${a.customerID}" class="btn btn-danger btn-sm"
                                           onclick="return confirm('Are you sure you want to delete accountID = ${a.customerID}');"><i class="fas fa-trash"></i> Delete</a>                             
                                        <a href="/admin-toggle-status?id=${a.customerID}" class="btn ${a.status ? 'btn-warning' : 'btn-success'} btn-sm">
                                            <i class="fas ${a.status ? 'fa-toggle-off' : 'fa-toggle-on'}"></i> 
                                            ${a.status ? 'Deactivate' : 'Activate'}
                                        </a>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                <c:if test="${not empty error}">
                    <strong>${error}</strong>
                </c:if>
            </div>
        </c:if>

        <!-- Nếu không có tài khoản, hiển thị trang trống hoặc thông báo -->
        <c:if test="${empty customers}">
            <div class="content text-center mt-5">
                <h3 class="text-danger">No accounts found.</h3>
                <a href="CustomerController_temp" class="btn btn-primary mt-3">Back to Account List</a>
            </div>
        </c:if>


        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>


