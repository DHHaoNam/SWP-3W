<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Voucher Management</title>
        <!-- Font Awesome -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css" />
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
            .filter-form {
                margin-bottom: 20px;
            }
            .no-voucher {
                text-align: center;
                color: #dc3545;
                font-size: 1.2em;
                margin-top: 20px;
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

            <div class="content">
                <h2>Voucher Management</h2>

                <!-- Filter Form -->
                <form action="VoucherController" method="get" class="filter-form row g-3">
                    <div class="col-md-2">
                        <input type="text" name="voucherID" class="form-control" placeholder="Search by Voucher ID" value="${param.voucherID}">
                    </div>
                    <div class="col-md-2">
                        <select name="status" class="form-select">
                            <option value="" ${param.status == null || param.status == '' ? 'selected' : ''}>All Status</option>
                            <option value="Active" ${param.status == 'Active' ? 'selected' : ''}>Active</option>
                            <option value="Inactive" ${param.status == 'Inactive' ? 'selected' : ''}>Inactive</option>
                        </select>
                    </div>
                    <div class="col-md-2">
                        <select name="categoryID" class="form-select">
                            <option value="" ${param.categoryID == null || param.categoryID == '' ? 'selected' : ''}>All Categories</option>
                            <c:forEach var="c" items="${listC}">
                                <option value="${c.categoryID}" ${param.categoryID == c.categoryID.toString() ? 'selected' : ''}>${c.categoryName}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-md-2">
                        <select name="sort" class="form-select">
                            <option value="" ${param.sort == null || param.sort == '' ? 'selected' : ''}>Sort By</option>
                            <option value="oldest" ${param.sort == 'oldest' ? 'selected' : ''}>Oldest</option>
                            <option value="latest" ${param.sort == 'latest' ? 'selected' : ''}>Latest</option>
                            <option value="discount_asc" ${param.sort == 'discount_asc' ? 'selected' : ''}>Discount Ascending</option>
                            <option value="discount_desc" ${param.sort == 'discount_desc' ? 'selected' : ''}>Discount Descending</option>
                        </select>
                    </div>
                    <div class="col-md-2">
                        <button type="submit" class="btn btn-success w-100"><i class="fas fa-filter"></i> Filter</button>
                    </div>
                    <div class="col-md-2">
                        <a href="VoucherController?action=add" class="btn btn-secondary w-100">
                            <i class="fas fa-plus"></i> Add New
                        </a>
                    </div>

                </form>

                <!-- Voucher Table -->
                <c:choose>
                    <c:when test="${empty listV}">
                        <div class="no-voucher">
                            No Vouchers Found!
                        </div>
                    </c:when>
                    <c:otherwise>
                        <table class="table table-striped table-bordered">
                            <thead class="table-dark">
                                <tr>
                                    <th>Voucher ID</th>
                                    <th>Title</th>
                                    <th>Discount (%)</th>
                                    <th>Status</th>
                                    <th>Category</th>
                                    <th>Start Time</th> <!-- Thêm cột Start Time -->
                                    <th>End Time</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="v" items="${listV}">
                                    <tr>
                                        <td>${v.voucherID}</td>
                                        <td>${v.title}</td>
                                        <td>${v.discount}</td>
                                        <td>${v.status}</td>
                                        <td>${v.categoryName}</td>
                                        <td>${v.startTime}</td> <!-- Hiển thị giá trị Start Time -->
                                        <td>${v.endTime}</td>
                                        <td class="d-flex gap-2">
                                            <a href="VoucherController?action=edit&id=${v.voucherID}" class="btn btn-primary btn-sm">
                                                <i class="fas fa-pen"></i> Edit
                                            </a>
                                            <form action="VoucherController" method="post" onsubmit="return confirm('Are you sure you want to delete this voucher?');">
                                                <input type="hidden" name="action" value="delete"/>
                                                <input type="hidden" name="voucherID" value="${v.voucherID}"/>
                                                <button type="submit" class="btn btn-danger btn-sm">
                                                    <i class="fas fa-trash"></i> Delete
                                                </button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </body>
</html>