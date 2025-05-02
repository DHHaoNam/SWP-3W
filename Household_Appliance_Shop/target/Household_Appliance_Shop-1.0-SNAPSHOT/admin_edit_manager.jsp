<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - Edit Manager</title>
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css">
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
            
            <c:if test="${sessionScope.managerRole == 1}">
                <a href="RevenueController"><i class="fa-solid fa-chart-simple"></i> Revenue Management</a>
            </c:if> 
        </div>

        <!-- Form to edit manager -->
        <div class="content">
            <h1 class="mb-4">Edit Manager</h1>
            <form method="POST" action="admin-update-manager" class="border p-4 rounded bg-light">
                <div class="mb-3">
                    <label for="id" class="form-label">Manager ID</label>
                    <input type="text" class="form-control" id="id" name="id" value="${manager.managerID}" readonly>
                </div>
                <div class="mb-3">
                    <label for="name" class="form-label">Full Name</label>
                    <input type="text" class="form-control" id="name" name="name" value="${manager.fullName}" required>
                </div>
                <div class="mb-3">
                    <label for="username" class="form-label">Username</label>
                    <input type="text" class="form-control" id="username" name="username" value="${manager.userName}" required>
                </div>
                <div class="mb-3">
                    <label for="password" class="form-label">Password</label>
                    <input type="text" class="form-control" id="password" name="password" value="${manager.password}" required>
                </div>
                <div class="mb-3">
                    <label for="email" class="form-label">Email</label>
                    <input type="email" class="form-control" id="email" name="email" value="${manager.email}" required>
                </div>
                <div class="mb-3">
                    <label for="phone_number" class="form-label">Phone Number</label>
                    <input type="text" class="form-control" id="phone_number" name="phone_number" value="${manager.phone}" required>
                </div>
                <div class="mb-3">
                    <label for="roleID" class="form-label">Role</label>
                    <select class="form-control" id="roleID" name="roleID">
                        <c:forEach var="role" items="${roleList}">
                            <option value="${role.roleID}" ${manager.roleID == role.roleID ? 'selected' : ''}>${role.roleName}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="mb-3">
                    <label for="registrationDate" class="form-label">Registration Date</label>
                    <input type="text" class="form-control" id="registrationDate" name="registrationDate" value="${manager.registrationDate}" readonly>
                </div>
                <div class="mb-3">
                    <label for="status" class="form-label">Status</label>
                    <select class="form-control" id="status" name="status">
                        <option value="1" ${manager.status ? 'selected' : ''}>Active</option>
                        <option value="0" ${!manager.status ? 'selected' : ''}>Inactive</option>
                    </select>
                </div>

                <!-- Success/Error Message -->
                <c:if test="${not empty message}">
                    <div class="alert alert-success">${message}</div>
                </c:if>
                <c:if test="${not empty error}">
                    <div class="alert alert-danger">${error}</div>
                </c:if>

                <button type="submit" class="btn btn-primary"><i class="fas fa-save"></i> Save Changes</button>
                <a href="ManagerController" class="btn btn-secondary ms-2">Back</a>
            </form>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>



