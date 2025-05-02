<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Revenue Chart</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css" integrity="sha512-Kc323vGBEqzTmouAECnVceyQqyqdsSiqLQISBL29aUW4U/M7pSPA/gEUZQqv1cwx4OnYxTxve5UMg5GT6L4JJg==" crossorigin="anonymous" referrerpolicy="no-referrer" />
        <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/3.7.0/chart.min.js"></script>
        <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="./CSS/Style.css" />
        <link rel="stylesheet" href="./CSS/HeaderAndFooter_CSS.css" />
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css"
              rel="stylesheet">

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
            .card {
                margin-left: 300px;
                margin-right: 50px;
                margin-bottom: 50px;
            }
        </style>
    </head>
    <body class="bg-light">
        <jsp:include page="admin_dashboard_header.jsp"></jsp:include>           
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

        <div class="card">

            <!-- Top Customers -->
            <div class="card mb-4 shadow-sm" style="margin-left: 50px;">
                <div class="card-header bg-primary text-white">
                    <h5 class="mb-0"><i class="fas fa-user"></i> Top 5 Customers</h5>
                </div>
                <div class="card-body">
                    <ul class="list-group">
                        <c:forEach var="cust" items="${topCustomers}" varStatus="status">
                            <li class="list-group-item d-flex justify-content-between align-items-center">
                                <span>
                                    <span class="badge badge-pill badge-secondary mr-2">${status.index + 1}</span>
                                    ${cust.customerName}
                                </span>
                                <span class="badge badge-success badge-pill">
                                    <fmt:formatNumber value="${cust.totalSpent}" type="currency" currencySymbol="₫"/>
                                </span>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
            </div>



            <!-- Top Products -->
            <div class="card mb-4 shadow-sm" style="margin-left: 50px;">
                <div class="card-header bg-warning text-dark">
                    <h5 class="mb-0"><i class="fas fa-star"></i> Top 5 Products</h5>
                </div>
                <div class="card-body">
                    <ul class="list-group">
                        <c:forEach var="prod" items="${topProducts}" varStatus="status">
                            <li class="list-group-item d-flex justify-content-between align-items-center">
                                <span>
                                    <span class="badge badge-pill badge-secondary mr-2">${status.index + 1}</span>
                                    ${prod.productName}
                                </span>
                                <span class="badge badge-info badge-pill">${prod.totalSold}</span>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
            </div>



            <div class="card-body">
                <h1 class="card-title">Revenue Chart</h1>

                <!-- Date Range Picker -->
                <form id="dateForm" class="mb-4">
                    <div class="form-row mb-3">
                        <div class="form-group col-md-6">
                            <label for="startDate">From date:</label>
                            <input type="date" id="startDate" name="startDate" value="${startDate}" class="form-control">
                        </div>
                        <div class="form-group col-md-6">
                            <label for="endDate">To date:</label>
                            <input type="date" id="endDate" name="endDate" value="${endDate}" class="form-control">
                        </div>
                    </div>
                    <button type="button" onclick="updateChart()" class="btn btn-primary">Update</button>
                </form>

                <!-- Loading Indicator -->
                <div id="loadingIndicator" class="d-none">
                    <p class="text-center text-secondary">Loading data...</p>
                </div>

                <!-- Combo Chart -->
                <div class="mb-4">
                    <canvas id="comboChart" style="height: 400px;"></canvas>
                </div>

                <!-- Total Revenue -->
                <div class="alert alert-light">
                    <p class="font-weight-bold">
                        Total revenue: 
                        <span id="totalRevenue">
                            <fmt:formatNumber value="${totalRevenue}" type="currency" currencySymbol="₫"/>
                        </span>
                    </p>
                </div>
            </div>
        </div>
    </div>

    <script>
        let comboChart;
        // Initialize chart with data from server
        document.addEventListener('DOMContentLoaded', function() {
        initializeChart();
        });
        function initializeChart() {
        const ctx = document.getElementById('comboChart').getContext('2d');
        const data = {
        labels: [
        <c:forEach items="${revenueData}" var="item" varStatus="status">
        '<fmt:formatDate value="${item.date}" pattern="dd/MM/yyyy"/>'${!status.last ? ',' : ''}
        </c:forEach>
        ],
                datasets: [
                {
                type: 'bar',
                        label: 'Revenue',
                        data: [
        <c:forEach items="${revenueData}" var="item" varStatus="status">
            ${item.revenue}${!status.last ? ',' : ''}
        </c:forEach>
                        ],
                        backgroundColor: 'rgba(54, 162, 235, 0.2)',
                        borderColor: 'rgba(54, 162, 235, 1)',
                        borderWidth: 1
                },
                {
                type: 'line',
                        label: 'Revenue',
                        data: [
        <c:forEach items="${revenueData}" var="item" varStatus="status">
            ${item.revenue}${!status.last ? ',' : ''}
        </c:forEach>
                        ],
                        fill: false,
                        borderColor: 'rgb(75, 192, 192)',
                        tension: 0.1
                }
                ]
        };
        if (comboChart) {
        comboChart.destroy();
        }

        comboChart = new Chart(ctx, {
        type: 'combo',
                data: data,
                options: {
                responsive: true,
                        maintainAspectRatio: false,
                        scales: {
                        y: {
                        beginAtZero: true,
                                ticks: {
                                callback: function(value) {
                                return new Intl.NumberFormat('vi-VN', {
                                style: 'currency',
                                        currency: 'VND'
                                }).format(value);
                                }
                                }
                        }
                        },
                        plugins: {
                        tooltip: {
                        callbacks: {
                        label: function(context) {
                        return new Intl.NumberFormat('vi-VN', {
                        style: 'currency',
                                currency: 'VND'
                        }).format(context.raw);
                        }
                        }
                        }
                        }
                }
        });
        }

        // Function to update chart with new date range
        function updateChart() {
        const startDate = document.getElementById('startDate').value;
        const endDate = document.getElementById('endDate').value;
        const loadingIndicator = document.getElementById('loadingIndicator');
        loadingIndicator.classList.remove('d-none');
        fetch(`/RevenueController?startDate=${startDate}&endDate=${endDate}`)
                .then(response => response.json())
                .then(data => {
                loadingIndicator.classList.add('d-none');
                updateChartWithData(data);
                });
        }

        // Function to update chart with fetched data
        function updateChartWithData(data) {
        const ctx = document.getElementById('comboChart').getContext('2d');
        const newData = {
        labels: data.revenueData.map(item => item.date),
                datasets: [
                {
                type: 'bar',
                        label: 'Revenue',
                        data: data.revenueData.map(item => item.revenue),
                        backgroundColor: 'rgba(54, 162, 235, 0.2)',
                        borderColor: 'rgba(54, 162, 235, 1)',
                        borderWidth: 1
                },
                {
                type: 'line',
                        label: 'Revenue',
                        data: data.revenueData.map(item => item.revenue),
                        fill: false,
                        borderColor: 'rgb(75, 192, 192)',
                        tension: 0.1
                }
                ]
        };
        if (comboChart) {
        comboChart.destroy();
        }

        comboChart = new Chart(ctx, {
        type: 'combo',
                data: newData,
                options: comboChart.options
        });
        // Update total revenue display
        const totalRevenue = data.totalRevenue;
        document.getElementById('totalRevenue').textContent = new Intl.NumberFormat('vi-VN', {
        style: 'currency',
                currency: 'VND'
        }).format(totalRevenue);
        }
    </script>
</body>
</html>
