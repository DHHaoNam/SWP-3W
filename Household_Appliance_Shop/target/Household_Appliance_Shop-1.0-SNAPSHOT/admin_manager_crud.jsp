<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Admin Dashboard - Manager Management</title>
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
                float: left;
            }
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
            <a href="ManagerController"><i class="fas fa-user-tie"></i> Manager Management</a>
            <a href="listAdminOrders"><i class="fas fa-shopping-cart"></i> Order Management</a>
            <a href="FeedbackController"><i class="fas fa-comments"></i> Feedback Management</a>
                <c:if test="${sessionScope.managerRole == 1}">
                    <a href="VoucherController"><i class="fas fa-ticket-alt"></i> Voucher Management</a>
                </c:if>   
            <c:if test="${sessionScope.managerRole == 1}">
                <a href="RevenueController"><i class="fa-solid fa-chart-simple"></i> Revenue Management</a>
            </c:if> 
        </div>

        <c:if test="${not empty managers}">
            <div class="content">
                <h1>Manager Management</h1> 
                <div class="mb-3">
                    <a href="CustomerController_temp" class="btn btn-secondary">
                        <i class="fas fa-arrow-left"></i> Back to Customer Management
                    </a>
                </div>
                <div class="mb-3">
                    <a href="admin-add-manager" class="btn btn-primary">
                        ➕ Add New Manager
                    </a>
                </div>



                <!-- Search Form -->
                <form method="GET" action="admin-search-manager" class="mb-3">
                    <div class="row">
                        <div class="col-md-6">
                            <input type="text" name="search" class="form-control" placeholder="Search manager by phone number" value="${param.search}">
                        </div>
                        <div class="col-md-2">
                            <button type="submit" class="btn btn-primary w-100"><i class="fas fa-search"></i> Search</button>
                        </div>
                    </div>
                </form>
                <c:if test="${not empty sessionScope.message}">
                    <div class="alert alert-success" role="alert">
                        ${sessionScope.message}
                    </div>
                    <c:remove var="message" scope="session"/>
                </c:if>

                <c:if test="${not empty sessionScope.error}">
                    <div class="alert alert-danger" role="alert">
                        ${sessionScope.error}
                    </div>
                    <c:remove var="error" scope="session"/>
                </c:if>

                <!-- Manager Table -->
                <table class="table table-bordered">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Full Name</th>
                            <th>Email</th>
                            <th>Phone</th>
                            <th>Status</th>
                            <th>Role</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="m" items="${managers}">
                            <tr>
                                <td>${m.managerID}</td>
                                <td>${m.fullName}</td>                             
                                <td>${m.email}</td>
                                <td>${m.phone}</td>                             
                                <td>  
                                    <c:choose>
                                        <c:when test="${m.status}">
                                            <span class="badge bg-success">Active</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-danger">Inactive</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <span>
                                        <c:forEach var="role" items="${roleList}">
                                            <c:if test="${m.roleID == role.roleID}">
                                                ${role.roleName}
                                            </c:if>
                                        </c:forEach>
                                    </span>
                                </td>
                                <td>
                                    <a href="/admin-view-manager?id=${m.managerID}" class="btn btn-info btn-sm">
                                        <i class="fas fa-eye"></i> View Details
                                    </a>
                                    <c:if test="${sessionScope.managerRole == 1}">
                                        <a href="/admin-edit-manager?id=${m.managerID}" class="btn btn-primary btn-sm"><i class="fas fa-edit"></i> Edit</a>
                                        <a href="/admin-delete-manager?id=${m.managerID}" class="btn btn-danger btn-sm"
                                           onclick="return confirm('Are you sure you want to delete managerID = ${m.managerID}');"><i class="fas fa-trash"></i> Delete</a>                             
                                        <a href="/admin-toggle-manager-status?id=${m.managerID}" class="btn ${m.status ? 'btn-warning' : 'btn-success'} btn-sm">
                                            <i class="fas ${m.status ? 'fa-toggle-off' : 'fa-toggle-on'}"></i> 
                                            ${m.status ? 'Deactivate' : 'Activate'}
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

        <c:if test="${empty managers}">
            <div class="content text-center mt-5">
                <h3 class="text-danger">No managers found.</h3>
                <a href="ManagerController" class="btn btn-primary mt-3">Back to Manager List</a>
            </div>
        </c:if>

        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>



