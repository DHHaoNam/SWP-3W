<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">

    <head>
        <meta charset="UTF-8">
        <title>Quên mật khẩu</title>

        <!-- Bootstrap và FontAwesome -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css" />
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet" />

        <!-- Header/Footer CSS nếu có -->
        <link rel="stylesheet" href="./CSS/HeaderAndFooter_CSS.css" />
        <link rel="stylesheet" href="./CSS/home.css" />

        <style>
            body {
                display: flex;
                flex-direction: column;
                min-height: 100vh;
                margin: 0;
            }

            .forgot-form {
                flex: 1;
                min-width: 500px;
                margin: 100px auto;
                padding: 20px;
                background-color: #ffe3d5;
                border-radius: 8px;
                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            }

            .forgot-form h2 {
                margin-top: 15px;
                color: #FC6E51;
            }

            .forgot-form .form-label {
                color: #FC6E51;
            }

            .forgot-form .form-control {
                border: 1px solid #FC6E51;
            }

            .forgot-form .btn-primary {
                background-color: #FC6E51;
                border-color: #FC6E51;
            }

            .forgot-form .btn-primary:hover {
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

            <div class="forgot-form">
                <h2>Forgot Password</h2>
                <form action="ForgotPasswordServlet" method="post">
                    <div class="mb-3">
                        <label for="email" class="form-label">Your Email:</label>
                        <input type="email" name="email" class="form-control" required>
                    </div>
                    <div class="d-grid gap-2">
                        <button type="submit" class="btn btn-primary">Send OTP</button>
                    </div>

                <c:if test="${not empty message}">
                    <p class="error">${message}</p>
                </c:if>
            </form>
        </div>

        <jsp:include page="footer.jsp"></jsp:include>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
    </body>

</html>
