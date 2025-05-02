<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Feedback Management</title>
        <!-- Font Awesome -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css" />
        <!-- Bootstrap -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
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

            /* Style cho thông điệp không tìm thấy */
            .no-feedback {
                text-align: center;
                color: #dc3545; /* Màu đỏ để nổi bật */
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
                <h2>Feedback Management</h2>

                <!-- Filter Form -->
                <form class="row g-3 mb-4 align-items-end" method="post" action="FeedbackController">
                    <input type="hidden" name="action" value="filter">
                    <div class="col-md-2">
                        <label for="productID" class="form-label">Product ID</label>
                        <input type="text" id="productID" name="productID" class="form-control" placeholder="Enter Product ID" value="${param.productID}">
                    </div>

                    <div class="col-md-2">
                        <label for="status" class="form-label">Status</label>
                        <select id="status" name="status" class="form-select">
                            <option value="" ${param.status == null || param.status == '' ? 'selected' : ''}>All Status</option>
                            <option value="pending" ${param.status == 'pending' ? 'selected' : ''}>Pending</option>
                            <option value="approved" ${param.status == 'approved' ? 'selected' : ''}>Approved</option>
                            <option value="rejected" ${param.status == 'rejected' ? 'selected' : ''}>Rejected</option>
                        </select>
                    </div>

                    <div class="col-md-2">
                        <label for="sortDate" class="form-label">Sort by Date</label>
                        <select id="sortDate" name="sortDate" class="form-select">
                            <option value="" ${param.sortDate == null || param.sortDate == '' ? 'selected' : ''}>Default</option>
                            <option value="oldest" ${param.sortDate == 'oldest' ? 'selected' : ''}>Oldest</option>
                            <option value="latest" ${param.sortDate == 'latest' ? 'selected' : ''}>Latest</option>
                        </select>
                    </div>

                    <div class="col-md-2">
                        <label for="replyStatus" class="form-label">Reply Status</label>
                        <select id="replyStatus" name="replyStatus" class="form-select">
                            <option value="" ${param.replyStatus == null || param.replyStatus == '' ? 'selected' : ''}>All</option>
                            <option value="hasReply" ${param.replyStatus == 'hasReply' ? 'selected' : ''}>Has Reply</option>
                            <option value="noReply" ${param.replyStatus == 'noReply' ? 'selected' : ''}>No Reply</option>
                        </select>
                    </div>

                    <div class="col-md-2 d-grid">
                        <button type="submit" class="btn btn-success">
                            <i class="fas fa-filter"></i> Filter
                        </button>
                    </div>
                </form>

                <!-- Kiểm tra nếu danh sách rỗng thì hiển thị thông điệp -->
                <c:choose>
                    <c:when test="${empty listP}">
                        <div class="no-feedback">
                            <p>No Feedback found!</p>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <table class="table table-striped table-bordered">
                            <thead class="table-dark">
                                <tr>
                                    <th>Feedback ID</th>
                                    <th>Customer ID</th>
                                    <th>Product ID</th>
                                    <th>Comment</th>
                                    <th>Reply</th>
                                    <th>Date</th>
                                    <th>Status</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="f" items="${listP}">
                                    <tr>
                                        <td>${f.feedbackID}</td>
                                        <td>${f.customerID}</td>
                                        <td>${f.productID}</td>
                                        <td>${f.comment}</td>
                                        <td>${f.reply}</td>
                                        <td>${f.feedback_date}</td>
                                        <td>${f.status}</td>
                                        <td>
                                            <a href="FeedbackController?action=newedit&id=${f.feedbackID}" class="btn btn-primary">
                                                <i class="fas fa-pen"></i> Edit
                                            </a>
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