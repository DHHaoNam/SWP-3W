<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Home</title>
        <!-- Font Awesome -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css" integrity="sha512-Kc323vGBEqzTmouAECnVceyQqyqdsSiqLQISBL29aUW4U/M7pSPA/gEUZQqv1cwx4OnYxTxve5UMg5GT6L4JJg==" crossorigin="anonymous" referrerpolicy="no-referrer" />
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
        <!--        <link rel="stylesheet" href="./CSS/Style.css">-->
        <link rel="stylesheet" href="./CSS/HeaderAndFooter_CSS.css">
        <link rel="stylesheet" href="./CSS/home.css">
        <script src="./Script/header-script.js"></script>
        <style>
            .cart-section {
                border: 1px solid #FC6E51; /* Đường viền màu chủ đạo */
                border-radius: 8px; /* Bo góc phần khung */
                padding: 20px; /* Khoảng cách trong */
                background-color: #fff; /* Màu nền trắng */
                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* Đổ bóng nhẹ */
            }

            .cart-title {
                font-size: 20px;
                font-weight: bold;
                color: #FC6E51; /* Màu chữ đồng bộ */
                margin-bottom: 15px;
            }

            .list-group-item {
                border: none; /* Loại bỏ đường viền mặc định */
                padding: 10px 15px; /* Khoảng đệm */
                display: flex;
                justify-content: space-between;
                background-color: #f8f9fa; /* Màu nền nhẹ */
                margin-bottom: 5px; /* Khoảng cách giữa các mục */
                border-radius: 6px; /* Bo góc */
                transition: background-color 0.3s ease; /* Hiệu ứng hover */
            }

            .list-group-item:hover {
                background-color: #ffe8e0; /* Màu nhấn khi hover */
            }

            .minicart-quantity {
                background-color: #FC6E51 !important; /* Giữ màu chủ đạo */
                color: #fff !important; /* Chữ trắng */
                font-size: 14px; /* Cỡ chữ */
            }

            .cart-total {
                font-size: 18px;
                color: #333; /* Màu chữ đậm */
                margin-top: 10px; /* Khoảng cách trên */
                padding-top: 10px;
                border-top: 1px solid #ddd; /* Đường viền phân cách */
            }

            .minicart-btn {
                background-color: #FC6E51;
                color: #fff;
                border: none;
                border-radius: 50px;
                width: 100%; /* Kích thước đầy đủ */
                padding: 10px;
                transition: background-color 0.3s ease, transform 0.2s ease; /* Hiệu ứng */
            }

            .minicart-btn:hover {
                background-color: #e65c41;
                transform: translateY(-2px); /* Nhấn mạnh */
            }
            .pagination a.active {
                background-color: #FC6E51; /* Màu cam */
                color: white; /* Chữ trắng */
                border-color: #FC6E51; /* Màu viền cam */
            }

            .product-card {
                display: flex;
                flex-direction: column;
                justify-content: space-between;
                height: 100%;
            }

            .main-content {
                margin-bottom: 50px; /* Điều chỉnh khoảng cách theo ý muốn */
            }
            .btn-group-cart {
                display: flex;
                gap: 5px; /* Khoảng cách giữa hai nút */
                justify-content: center;
                margin-top: 5px; /* Đẩy sát lên trên */
            }

            .btn-cart, .btn-order {
                flex: 1;
                background-color: #FC6E51;
                color: white;
                font-weight: bold;
                border: none;
                border-radius: 6px;
                padding: 6px 8px;
                font-size: 13px;
                text-align: center;
                white-space: nowrap;
            }

            .btn-cart:hover, .btn-order:hover {
                background-color: #e65c41;
                transform: translateY(-1px);
            }

            /* Căn chỉnh lại phần tình trạng */
            .text-danger {
                font-size: 14px;
                margin-top: 2px; /* Giảm khoảng cách với phần trên */
                margin-bottom: 0px; /* Loại bỏ khoảng trống dưới */
                color: #333;
                line-height: 1.2; /* Giảm chiều cao dòng để thu gọn */
            }

            .card-text {

            }

            .subcategory-list {
                display: none; /* Ẩn danh sách subcategory */
            }

            .list-group-item:hover .subcategory-list {
                display: block; /* Hiện danh sách subcategory khi hover */
            }
        </style>
    </head>

    <body>
        <!-- Header -->
        <jsp:include page="header.jsp"></jsp:include>

            <!-- Thanh tìm kiếm và menu trong cùng một form -->
            <form action="home-search" method="GET">
                <div class="search-bar">
                    <div class="container">
                        <div class="d-flex justify-content-center">
                            <input type="text" name="search" class="form-control search-input" placeholder="Search for products...">
                            <button type="submit" class="btn search-btn"><i class="fa-solid fa-search"></i></button>
                        </div>
                    </div>
                </div>
            </form>


            <div class="container-fluid main-content px-5">
                <div class="row">
                    <!-- Sidebar Danh mục -->
                    <div class="col-md-3">
                        <div class="menu-nav">
                            <ul class="list-group">
                                <li class="list-group-item">
                                    <a href="home" class="d-block">All</a>
                                </li>
                            <c:forEach items="${requestScope.categorys}" var="c">
                                <li class="list-group-item">
                                    <a href="home?categoryid=${c.categoryID}" 
                                       class="d-block ${param.categoryid == c.categoryID ? 'active' : ''}">
                                        ${c.categoryName}
                                    </a>

                                    <!-- Hiện SubCategory nếu Category này đang được chọn -->
                                    <ul class="list-group mt-1 subcategory-list">
                                        <c:forEach items="${requestScope.subcategories}" var="s">
                                            <c:if test="${s.categoryID == c.categoryID}">
                                                <li class="list-group-item">
                                                    <a href="home?categoryid=${c.categoryID}&subcategoryid=${s.subCategoryID}" 
                                                       class="d-block ms-3 ${param.subcategoryid == s.subCategoryID ? 'active' : ''}">
                                                        ${s.subCategoryName}
                                                    </a>
                                                </li>
                                            </c:if>
                                        </c:forEach>
                                    </ul>
                                </li>
                            </c:forEach>
                        </ul>
                    </div>
                </div>

                <!-- Nội dung chính -->

                <div class="col-md-9">
                    <!-- Menu Thương Hiệu (Brands) -->
                    <div class="d-flex flex-wrap justify-content-start mb-3">
                        <a href="home" class="btn btn-outline-primary m-1">Tất cả</a>
                        <c:forEach items="${requestScope.brands}" var="b">
                            <a href="home?brandid=${b.brandID}&categoryid=${param.categoryid}" 
                               class="btn btn-outline-secondary m-1 ${param.brandid == b.brandID ? 'active' : ''}">
                                ${b.brandName}
                            </a>
                        </c:forEach>
                    </div>

                    <!-- Danh sách sản phẩm -->
                    <div class="row g-4">
                        <c:choose>
                            <c:when test="${empty requestScope.products}">
                                <div class="col-12 text-center">
                                    <p class="text-danger">No products found.</p>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <c:forEach items="${requestScope.products}" var="p">
                                    <div class="col-sm-12 col-md-6 col-lg-4">
                                        <div class="card product-card">
                                            <a href="product-detail?productID=${p.productID}">
                                                <img src="${p.image != null ? p.image : 'default-image.jpg'}" class="card-img-top" alt="${p.productName}">
                                            </a>
                                            <div class="card-body">
                                                <a style="text-decoration: none" href="product-detail?productID=${p.productID}">
                                                    <h5 class="card-title">${p.productName}</h5>
                                                </a>
                                                <p class="card-text"><fmt:formatNumber value="${p.price}" pattern="#,###đ"/> </p>
                                            </div>
                                            <c:if test="${p.stock_Quantity > 0}">
                                                <p class="text-danger">Status: In stock</p>
                                                <div class="btn-group-cart">
                                                    <form action="ProductDetailController" method="post" style="display:inline;">
                                                        <input type="hidden" name="productID" value="${p.productID}">
                                                        <input type="hidden" name="stock_Quantity" value="1"> 
                                                        <button type="submit" class="btn btn-cart">Add to Cart</button>
                                                    </form>
                                                </div>
                                            </c:if>
                                            <c:if test="${p.stock_Quantity <= 0}">
                                                <p class="text-danger">Status: Out of Stock</p>
                                                <div class="btn-group-cart">
                                                    <button type="button" class="btn btn-cart" disabled>Out of Stock</button>
                                                    <button type="button" class="btn btn-order" disabled>Cannot Order</button>
                                                </div>
                                            </c:if>
                                        </div>
                                    </div>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>

                    </div>
                </div>
            </div>
        </div>

        <div class="float-end">
            <jsp:include page="pagination.jsp">
                <jsp:param name="baseUrl" value="home" />
            </jsp:include>
        </div>



        <!-- Footer -->
        <jsp:include page="footer.jsp"></jsp:include>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
        <script src="./Script/Script.js"></script>
        <script>

            document.addEventListener('DOMContentLoaded', function () {
                // Get all menu items
                const menuItems = document.querySelectorAll('.menu-nav a');

                // Get the current URL
                const currentURL = window.location.href;

                // Add click event listener to each menu item
                menuItems.forEach(item => {
                    // Check if this is the current page
                    if (currentURL === item.href) {
                        item.classList.add('active');
                    }

                    // Add click event listener
                    item.addEventListener('click', function () {
                        // Remove active class from all items
                        menuItems.forEach(menuItem => {
                            menuItem.classList.remove('active');
                        });

                        // Add active class to clicked item
                        this.classList.add('active');
                    });
                });

                // If no active item is found, activate "Tất cả" by default
                if (!document.querySelector('.menu-nav a.active')) {
                    const allProductsLink = document.querySelector('.menu-nav a[href="home"]');
                    if (allProductsLink) {
                        allProductsLink.classList.add('active');
                    }
                }
            });
            document.addEventListener('DOMContentLoaded', function () {
                // Get the current URL
                const currentURL = window.location.href;

                // Get all pagination links
                const paginationLinks = document.querySelectorAll('.pagination a');

                // Loop through all pagination links
                paginationLinks.forEach(link => {
                    const parentLi = link.closest('li');
                    const isPageNumber = !parentLi.classList.contains('disabled') && !link.getAttribute('aria-label');

                    // Check if the link's href matches the current URL and it's a page number link
                    if (currentURL.includes(link.href) && isPageNumber) {
                        link.classList.add('active');
                    }
                });
            });

            $(document).ready(function () {
                $('.list-group-item').hover(
                        function () {
                            $(this).find('.subcategory-list').stop(true, true).slideDown(200);
                        },
                        function () {
                            $(this).find('.subcategory-list').stop(true, true).slideUp(200);
                        }
                );
            });


        </script>
    </body>
</html>
