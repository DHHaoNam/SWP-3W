<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Login</title>

        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css"
              integrity="sha512-Kc323vGBEqzTmouAECnVceyQqyqdsSiqLQISBL29aUW4U/M7pSPA/gEUZQqv1cwx4OnYxTxve5UMg5GT6L4JJg=="
              crossorigin="anonymous" referrerpolicy="no-referrer" />
        <!-- Bootstrap -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css"
              rel="stylesheet">
        <!-- Header and Footer CSS -->
        <link rel="stylesheet" href="./CSS/HeaderAndFooter_CSS.css" />

        <link rel="stylesheet" href="./CSS/home.css">
        <script src="./Script/header-script.js"></script>
        <style>
            body {
                display: flex;
                flex-direction: column;
                min-height: 100vh;
                margin: 0;
            }

            .login-form {
                flex: 1;
                min-width: 500px;
                margin: 100px auto;
                padding: 20px;
                background-color: #ffe3d5; /* Màu cam nhạt */
                border-radius: 8px;
                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            }

            .login-form h2 {
                margin-top: 15px;
                color: #FC6E51; /* Màu cam đậm */
            }

            .login-form .form-label {
                color: #FC6E51; /* Màu cam đậm */
            }

            .login-form .form-control {
                border: 1px solid #FC6E51; /* Màu cam đậm */
            }

            .login-form .btn-primary {
                background-color: #FC6E51; /* Màu cam đậm */
                border-color: #FC6E51; /* Màu cam đậm */
            }

            .login-form .btn-primary:hover {
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

            <div class="login-form">
                <h2>Login</h2>
                <form action="login" method="post">
                    <div class="mb-3">
                        <label for="username" class="form-label">Username</label>
                        <input type="text" class="form-control" id="username" name="userName" required>
                    </div>
                    <div class="mb-3 position-relative">
                        <label for="password" class="form-label">Password</label>
                        <input type="password" class="form-control" id="password" name="password" required>
                        <span toggle="#password" class="fa fa-fw fa-eye toggle-password position-absolute" style="top: 50%; right: 10px; cursor: pointer; transform: translateY(60%); color: #FC6E51;"></span>
                    </div>

                    <div class="d-grid gap-2">
                        <button type="submit" class="btn btn-primary">Login</button>
                        <p class="error">${errorMessage}</p>
                </div>

                <div class="form-text d-flex justify-content-between">
                    <span>Don't have an account? <a href="register.jsp">Register Here</a></span>
                </div>
                <div class="form-text d-flex justify-content-between">
                    <span><a href="forgot-password.jsp">Forgot Password Here</a></span>
                </div>
                <c:if test="${not empty sessionScope.registersuccessmessage}">
                    <p class="error"><c:out value="${sessionScope.registersuccessmessage}" /></p>
                </c:if>
                <c:if test="${not empty sessionScope.message}">
                    <p class="error"><c:out value="${sessionScope.message}" /></p>
                </c:if>


            </form>
        </div>

        <jsp:include page="footer.jsp"></jsp:include>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
        <script>
            document.querySelector(".toggle-password").addEventListener("click", function () {
                const passwordInput = document.getElementById("password");
                const type = passwordInput.getAttribute("type") === "password" ? "text" : "password";
                passwordInput.setAttribute("type", type);
                this.classList.toggle("fa-eye-slash"); // Chuyển đổi biểu tượng mắt
            });
        </script>

    </body>
</html>
