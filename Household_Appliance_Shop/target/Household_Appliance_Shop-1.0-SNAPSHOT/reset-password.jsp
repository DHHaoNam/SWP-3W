<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">

    <head>
        <meta charset="UTF-8">
        <title>Đặt lại mật khẩu</title>

        <!-- Bootstrap -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet" />
        <link rel="stylesheet" href="./CSS/HeaderAndFooter_CSS.css" />

        <style>
            body {
                display: flex;
                flex-direction: column;
                min-height: 100vh;
                margin: 0;
            }

            .reset-form {
                flex: 1;
                min-width: 500px;
                margin: 100px auto;
                padding: 20px;
                background-color: #ffe3d5;
                border-radius: 8px;
                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            }

            .reset-form h2 {
                color: #FC6E51;
            }

            .reset-form .form-label {
                color: #FC6E51;
            }

            .reset-form .form-control {
                border: 1px solid #FC6E51;
            }

            .reset-form .btn-primary {
                background-color: #FC6E51;
                border-color: #FC6E51;
            }

            .reset-form .btn-primary:hover {
                background-color: #d85a3f;
                border-color: #d85a3f;
            }

            .error {
                color: #d85a3f;
                margin-top: 10px;
                font-size: 14px;
            }
        </style>
    </head>

    <body>

        <jsp:include page="header.jsp"></jsp:include>

            <div class="reset-form">
                <h2>Reset Password</h2>
                <form action="ResetPasswordServlet" method="post" onsubmit="return validatePasswordMatch();">
                    <div class="mb-3">
                        <label for="newPassword" class="form-label">New Password:</label>
                        <input type="password" name="newPassword" id="newPassword" class="form-control" required minlength="6">
                    </div>
                    <div class="mb-3">
                        <label for="confirmPassword" class="form-label">Confirm Password:</label>
                        <input type="password" name="confirmPassword" id="confirmPassword" class="form-control" required minlength="6">
                    </div>
                    <div class="d-grid gap-2">
                        <button type="submit" class="btn btn-primary">Confirm</button>
                    </div>
                    <p class="error" id="passwordError"></p>
                <c:if test="${not empty message}">
                    <p class="error"><c:out value="${message}" /></p>
                </c:if>
            </form>
        </div>

        <jsp:include page="footer.jsp"></jsp:include>

        <script>
            function validatePasswordMatch() {
                const pw1 = document.getElementById("newPassword").value;
                const pw2 = document.getElementById("confirmPassword").value;
                const errorEl = document.getElementById("passwordError");

                if (pw1 !== pw2) {
                    errorEl.textContent = "Mật khẩu không khớp!";
                    return false;
                }
                errorEl.textContent = "";
                return true;
            }
        </script>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
    </body>

</html>
