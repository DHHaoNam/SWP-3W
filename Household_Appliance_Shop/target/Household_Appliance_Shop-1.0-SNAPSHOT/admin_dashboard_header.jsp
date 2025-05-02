<%-- 
    Document   : header
    Created on : Oct 17, 2024, 8:06:54 PM
    Author     : admin
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!-- FontAwesome -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">

<!-- Navbar -->
<nav class="navbar navbar-expand-lg navbar-light bg-light">
    <div class="container">
        <a class="navbar-brand rounded-pill logo" href="home"><img src="" alt="">Metal</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav"
                aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse justify-content-end" id="navbarNav">

            <!-- User Dropdown -->
            <div class="user-section">
                <div class="position-relative">
                    <a href="#" class="nav-link user-toggle">
                        <i class="fa-solid fa-user fa-lg"></i> <!-- Icon User -->
                        <c:if test="${sessionScope.acc != null}">
                            <span class="username-display">${acc.username}</span>
                        </c:if>
                    </a>
                    <div id="userMenu" class="user-menu">
                        <c:if test="${sessionScope.customer != null || sessionScope.manager != null}">
                            <ul class="list-unstyled">
                                <p>Hello, ${sessionScope.manager.userName}!</p>
                                <li><a href="managerchangepassword.jsp"><i class="fas fa-key me-2"></i>Change Password</a></li>
                                <li><a href="logout"><i class="fas fa-sign-out-alt me-2"></i>Logout</a></li>
                            </ul>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>
    </div>
</nav>

<!-- CSS -->
<style>
    .user-menu {
        display: none;
        position: absolute;
        top: 100%;
        right: 0;
        background: white;
        border: 1px solid #ccc;
        padding: 10px;
        box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.1);
        z-index: 1000;
        min-width: 160px;
        border-radius: 5px;
    }

    .user-menu ul {
        list-style: none;
        margin: 0;
        padding: 0;
    }

    .user-menu ul li {
        padding: 8px 10px;
    }

    .user-menu ul li a {
        text-decoration: none;
        color: #333;
        display: block;
    }

    .user-menu ul li a:hover {
        background-color: #f0f0f0;
    }

    /* Hiển thị menu khi có class "show" */
    .user-menu.show {
        display: block;
    }
</style>

<!-- JavaScript -->
<script>
    document.addEventListener("DOMContentLoaded", function () {
        var userToggle = document.querySelector(".user-toggle");
        var userMenu = document.getElementById("userMenu");

        userToggle.addEventListener("click", function (event) {
            event.preventDefault();
            userMenu.classList.toggle("show");
        });

        // Ẩn menu khi click ra ngoài
        document.addEventListener("click", function (event) {
            if (!userToggle.contains(event.target) && !userMenu.contains(event.target)) {
                userMenu.classList.remove("show");
            }
        });
    });
</script>
