<%-- 
    Document   : register
    Created on : Oct 16, 2024, 2:23:35 PM
    Author     : admin
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Register</title>
        <!-- Font Awesome -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css"
              integrity="sha512-Kc323vGBEqzTmouAECnVceyQqyqdsSiqLQISBL29aUW4U/M7pSPA/gEUZQqv1cwx4OnYxTxve5UMg5GT6L4JJg=="
              crossorigin="anonymous" referrerpolicy="no-referrer" />
        <!-- Bootstrap -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css"
              rel="stylesheet">
        <!-- Header and Footer CSS -->
        <link rel="stylesheet" href="./CSS/HeaderAndFooter_CSS.css">
        <link rel="stylesheet" href="./CSS/home.css">
        <script src="./Script/header-script.js"></script>
        <style>
            body {
                display: flex;
                flex-direction: column;
                min-height: 100vh;
                margin: 0;
            }

            .register-form {
                flex: 1;
                min-width: 500px;
                margin: 100px auto;
                padding: 20px;
                background-color: #ffe3d5; /* Màu cam nhạt */
                border-radius: 8px;
                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            }

            .register-form h2 {
                margin-top: 15px;
                color: #FC6E51; /* Màu cam đậm */
            }

            .register-form .form-label {
                color: #FC6E51; /* Màu cam đậm */
            }

            .register-form .form-control {
                border: 1px solid #FC6E51; /* Màu cam đậm */
            }

            .register-form .btn-primary {
                background-color: #FC6E51; /* Màu cam đậm */
                border-color: #FC6E51; /* Màu cam đậm */
            }

            .register-form .btn-primary:hover {
                background-color: #d85a3f; /* Màu cam đậm hơn cho hover */
                border-color: #d85a3f; /* Màu cam đậm hơn cho hover */
            }

            .form-text a {
                color: #FC6E51; /* Màu cam đậm */
                text-decoration: underline;
            }

            .error {
                color: #d85a3f; /* Màu đỏ cam đậm cho thông báo lỗi */
                margin-bottom: 10px;
                font-size: 14px;
            }
        </style>

    </head>
    <body>
        <jsp:include page="header.jsp"></jsp:include>

            <div class="register-form">
                <h2>Register</h2>               
                <form action="register" method="post">
                    <div class="mb-3">
                        <label for="fullName" class="form-label">Full Name</label>
                        <input type="text" class="form-control" id="fullName" name="fullName" required>
                    </div>
                    <div class="mb-3">
                        <label for="username" class="form-label">Username</label>
                        <input type="text" class="form-control" id="username" name="username" required>
                    <c:if test="${not empty sessionScope.errorName}">
                        <p class="error"><c:out value="${sessionScope.errorName}" /></p>
                    </c:if>
                </div>
                <div class="mb-3">
                    <label for="email" class="form-label">Email</label>
                    <input type="email" class="form-control" id="email" name="email" required>
                    <c:if test="${not empty sessionScope.errorEmail}">
                        <p class="error"><c:out value="${sessionScope.errorEmail}" /></p>
                    </c:if>
                </div>
                <div class="mb-3">
                    <label for="phone" class="form-label">Phone</label>
                    <input type="text" class="form-control" id="phone" name="phone" required>
                    <c:if test="${not empty sessionScope.errorPhone}">
                        <p class="error"><c:out value="${sessionScope.errorPhone}" /></p>
                    </c:if>
                    <c:if test="${not empty sessionScope.errorPhoneFormat}">
                        <p class="error"><c:out value="${sessionScope.errorPhoneFormat}" /></p>
                    </c:if>
                </div>
                <div class="mb-3 position-relative">
                    <label for="password" class="form-label">Password</label>
                    <input type="password" class="form-control" id="password" name="password" required>
                    <span toggle="#password" class="fa fa-fw fa-eye toggle-password position-absolute" 
                          style="top: 50%; right: 10px; cursor: pointer; transform: translateY(60%); color: #FC6E51;"></span>
                    <c:if test="${not empty sessionScope.errorPassword}">
                        <p class="error"><c:out value="${sessionScope.errorPassword}" /></p>
                    </c:if>
                </div>

                <div class="mb-3 position-relative">
                    <label for="confirmPassword" class="form-label">Confirm Password</label>
                    <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" required>
                    <span toggle="#confirmPassword" class="fa fa-fw fa-eye toggle-password position-absolute" 
                          style="top: 50%; right: 10px; cursor: pointer; transform: translateY(60%); color: #FC6E51;"></span>
                    <c:if test="${not empty sessionScope.errorConfirm}">
                        <p class="error"><c:out value="${sessionScope.errorConfirm}" /></p>
                    </c:if>
                </div>

                <div class="d-grid gap-2">
                    <button type="submit" class="btn btn-primary">Register</button>
                </div>
                <c:if test="${not empty sessionScope.registersuccessmassage}">
                    <div class="alert alert-success mt-3">${sessionScope.registersuccessmassage}</div>
                </c:if> 
                <div class="form-text">
                    Already have an account? <a href="login.jsp">Login Here</a>
                </div>
                <c:if test="${not empty sessionScope.error}">
                    <p class="error"><c:out value="${sessionScope.error}" /></p>
                </c:if>
                <c:if test="${not empty requestScope.message}">
                    <p class="error"><c:out value="${requestScope.message}" /></p>
                </c:if>
            </form>
        </div>
        <jsp:include page="footer.jsp"></jsp:include>
            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
            <script>
                document.querySelectorAll(".toggle-password").forEach(function (element) {
                    element.addEventListener("click", function () {
                        const targetInput = document.querySelector(this.getAttribute("toggle"));
                        const type = targetInput.getAttribute("type") === "password" ? "text" : "password";
                        targetInput.setAttribute("type", type);
                        this.classList.toggle("fa-eye-slash"); // Chuyển đổi biểu tượng mắt
                    });
                });
            </script>
        <% session.removeAttribute("errorName"); %>
        <% session.removeAttribute("errorEmail"); %>
        <% session.removeAttribute("errorPhone"); %>
        <% session.removeAttribute("errorPhoneFormat"); %>
        <% session.removeAttribute("errorPassword"); %>
        <% session.removeAttribute("errorConfirm"); %>
        <% session.removeAttribute("errorGeneral");%>
        <% session.removeAttribute("registersuccessmassage");%>
    </body>
</html>