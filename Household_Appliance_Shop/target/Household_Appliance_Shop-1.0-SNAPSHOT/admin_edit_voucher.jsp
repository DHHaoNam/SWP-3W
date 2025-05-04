<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Edit Voucher</title>
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
                float: left;
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
            <!-- Main content for editing voucher -->
            <div class="content">
                <h2>Edit Voucher</h2>
                <c:if test="${not empty error}">
                    <div class="alert alert-danger" role="alert">
                        ${error}
                    </div>
                </c:if>

                <form action="VoucherController" method="post">
                    <input type="hidden" name="action" value="updateVoucher">
                    <input type="hidden" name="voucherID" value="${voucher.voucherID}">

                    <div class="mb-3">
                        <label for="title" class="form-label">Voucher Title</label>
                        <input type="text" class="form-control" id="title" name="title" value="${voucher.title}" required>
                    </div>

                    <div class="mb-3">
                        <label for="discount" class="form-label">Discount (%)</label>
                        <input type="number" class="form-control" id="discount" name="discount" value="${voucher.discount}" min="0" max="100" step="0.01" required>
                    </div>

                    <div class="mb-3">
                        <label for="category" class="form-label">Category</label>
                        <select class="form-select" id="category" name="categoryID" required>
                            <c:forEach var="cat" items="${categories}">
                                <option value="${cat.categoryID}" ${cat.categoryID == voucher.categoryID ? 'selected' : ''}>${cat.categoryName}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="mb-3">
                        <label for="status" class="form-label">Status</label>
                        <select class="form-select" id="status" name="status" required>
                            <option value="Active" ${voucher.status == 'Active' ? 'selected' : ''}>Active</option>
                            <option value="Inactive" ${voucher.status == 'Inactive' ? 'selected' : ''}>Inactive</option>
                        </select>
                    </div>

                    <div class="mb-3">
                        <label for="startTime" class="form-label">Start Time</label>
                        <input type="datetime-local" class="form-control" id="startTime" name="startTime"
                               value="<fmt:formatDate value='${voucher.startTime}' pattern='yyyy-MM-dd\'T\'HH:mm' />"
                               required>
                    </div>

                    <div class="mb-3">
                        <label for="endTime" class="form-label">End Time</label>
                        <input type="datetime-local" class="form-control" id="endTime" name="endTime"
                               value="<fmt:formatDate value='${voucher.endTime}' pattern='yyyy-MM-dd\'T\'HH:mm' />"
                               required>
                    </div>


                    <button type="submit" class="btn btn-primary">Update Voucher</button>
                    <a href="VoucherController" class="btn btn-secondary">Cancel</a>
                </form>
            </div>
        </div>
    </body>
</html>