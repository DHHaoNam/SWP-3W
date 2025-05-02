<%-- 
    Document   : food_detail
    Created on : Oct 17, 2024, 7:41:02 PM
    Author     : admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title></title>
        <!-- Bootstrap -->
        <!-- Font Awesome -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css" integrity="sha512-Kc323vGBEqzTmouAECnVceyQqyqdsSiqLQISBL29aUW4U/M7pSPA/gEUZQqv1cwx4OnYxTxve5UMg5GT6L4JJg==" crossorigin="anonymous" referrerpolicy="no-referrer" />
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
        <!-- Custom CSS -->
        <link rel="stylesheet" href="./CSS/Style.css">
        <link rel="stylesheet" href="./CSS/HeaderAndFooter_CSS.css">
        <link rel="stylesheet" href="./CSS/home.css">
        <script src="./Script/header-script.js"></script>
        <!-- jQuery CDN -->
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

        <!-- SweetAlert2 CDN -->
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

        <style>
            /* General Body Styling */
            body {
                font-family: 'Arial', sans-serif;
                background-color: #fffdf8;
                color: #333;
            }

            mg-100px {
                margin: 100px;
            }


            /* Food Detail Page Styling */
            .food-detail-page {
                padding: 50px 0;
            }

            .food-img {
                max-width: 100%;
                border-radius: 15px;
                box-shadow: 0 6px 15px rgba(0, 0, 0, 0.1);
                transition: transform 0.3s ease;
            }

            .food-img:hover {
                transform: scale(1.05);
            }

            .food-name {
                font-size: 34px;
                font-weight: bold;
                color: #FC6E51;
                text-transform: uppercase;
            }

            .food-price {
                font-size: 26px;
                font-weight: bold;
                color: #d9534f;
                margin-top: 10px;
            }

            .food-desc {
                font-size: 16px;
                color: #777;
                line-height: 1.7;
                margin: 20px 0;
            }

            .quantity {
                width: 80px;
                text-align: center;
                border-radius: 8px;
                border: 1px solid #ddd;
                padding: 5px;
            }

            .btn-cart {
                background-color: #FF7F50;
                border: none;
                color: #fff;
                font-size: 16px;
                font-weight: bold;
                padding: 12px 25px;
                border-radius: 25px;
                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
                transition: background-color 0.3s ease, transform 0.2s ease;
            }

            .btn-cart:hover {
                background-color: #FF6347;
                transform: translateY(-2px);
            }

            /* Custom Back Button */
            .btn-back {
                background-color: #ddd;
                color: #333;
                font-size: 16px;
                padding: 10px 20px;
                border-radius: 25px;
                text-decoration: none;
                display: inline-block;
                transition: background-color 0.3s ease, color 0.3s ease;
            }

            .btn-back:hover {
                background-color: #bbb;
                color: #fff;
            }

            /* Footer Styling */
            .footer {
                background-color: #333;
                color: #f1f1f1;
                padding: 40px 0;
                font-size: 14px;
            }

            .footer strong {
                color: #FC6E51;
            }

            .footer p {
                margin: 0;
                font-size: 14px;
            }

            .feedback-container {
                max-height: 600px;
                overflow-y: auto;
                border: 1px solid #ccc;
                padding: 10px;
                margin-bottom: 20px;
            }

            .feedback-item {
                border: 1px solid #ddd;
                padding: 12px;
                margin-bottom: 10px;
                border-radius: 6px;
                position: relative;
            }

            .feedback-actions {
                position: absolute;
                top: 10px;
                right: 10px;
            }

            .stars {
                color: gold;
                font-size: 20px;
            }

            .rating-summary {
                font-size: 16px;
                margin-bottom: 15px;
                color: #555;
            }

            .rating-count {
                font-weight: bold;
                color: #000;
            }

            .comment-container {
                max-height: 600px;
                overflow-y: auto;
                border: 1px solid #ccc;
                padding: 10px;
                margin-bottom: 20px;
                border-radius: 6px;
            }

            .comment {
                position: relative;
                padding: 15px;
                margin-bottom: 15px;
                border: 1px solid #ddd;
                border-radius: 6px;
                background-color: #f9f9f9;
                box-shadow: 0 1px 3px rgba(0,0,0,0.05);
            }

            .comment-header {
                display: flex;
                justify-content: space-between;
                align-items: flex-start;
                margin-bottom: 10px;
                padding-bottom: 5px;
                border-bottom: 1px solid #eee;
            }

            .timestamp {
                font-size: 12px;
                color: #777;
                position: relative;
                display: block;
                margin-top: 5px;
                margin-bottom: 10px;
                text-align: right;
            }

            .comment-actions {
                position: relative;
                display: flex;
                gap: 5px;
            }

            .comment-content {
                margin: 10px 0;
                line-height: 1.5;
                word-break: break-word;
                padding: 5px 0;
            }

            .comment-form {
                margin-bottom: 20px;
                display: flex;
                align-items: center;
                gap: 10px;
            }

            .comment-form input[type="text"] {
                flex: 1;
                padding: 10px 15px;
                border: 1px solid #ddd;
                border-radius: 25px;
                font-size: 14px;
            }

            .comment-form button {
                background-color: #FF7F50;
                color: white;
                border: none;
                padding: 10px 20px;
                border-radius: 25px;
                cursor: pointer;
                font-weight: bold;
                transition: background-color 0.3s;
            }

            .comment-form button:hover {
                background-color: #FF6347;
            }

            .comment-actions .icon-btn {
                background: none;
                border: none;
                cursor: pointer;
                font-size: 16px;
                margin-left: 8px;
                padding: 5px;
                border-radius: 4px;
                transition: background-color 0.2s ease;
            }

            .icon-btn:hover {
                background-color: #f8f9fa;
            }

            .icon-btn.edit {
                color: #007bff;
            }

            .icon-btn.delete {
                color: #dc3545;
            }

            /* No Feedback Message */
            .no-feedback {
                text-align: center;
                padding: 30px;
                color: #6c757d;
                background-color: #f8f9fa;
                border-radius: 8px;
                margin: 20px 0;
            }

            /* Purchase Required Message */
            .purchase-required {
                background-color: #e9ecef;
                border-radius: 8px;
                padding: 15px;
                margin-top: 20px;
                color: #6c757d;
                text-align: center;
            }

            .no-comments {
                text-align: center;
                padding: 30px;
                color: #6c757d;
                background-color: #f8f9fa;
                border-radius: 8px;
                margin: 20px 0;
            }
        </style>
    </head>

    <body>
        <!-- Header -->
        <jsp:include page="header.jsp"></jsp:include>

            <div class="container my-5">
                <div class="row">
                    <!-- Food Image -->
                    <div class="col-md-6">
                        <img src="${details.image}"  class="food-img">
                </div>

                <!-- Food Details -->
                <div class="col-md-6 margin-left">
                    <h2 class="food-name">${details.productName}</h2>
                    <p class="food-price"><fmt:formatNumber value="${details.price}" pattern="#,###đ"/></p>

                    <!-- Quantity and Add to Cart -->
                    <form action="ProductDetailController" method="post"> 
                        <div class="form-group">

                            <span>In stock: ${details.stock_Quantity}</span>
                        </div>
                        <div class="form-group">
                            <label for="quantity">Quantity:</label>
                            <input type="number" id="stock_Quantity" name="stock_Quantity" class="form-control quantity" min="1" value="1">
                        </div>
                        <div class="form-group">
                            <label for="description">Description:</label>
                            <p id="description" name="description" class="form-control" rows="5" readonly>
                                ${details.description}
                            </p>
                        </div>

                        <!-- Thông số kỹ thuật -->
                        <div class="form-group mt-4">
                            <h5>Attribute:</h5>
                            <table class="table table-bordered table-striped mt-3">
                                <tbody>
                                    <c:forEach var="item" items="${pa}">
                                        <tr>
                                            <th style="width: 30%;">${item.attributeName}</th>
                                            <td>${item.value}</td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>





                        <input type="hidden" name="productID" value="${details.productID}"> 
                        <c:if test="${details.stock_Quantity > 0}">
                            <p>Status: In Stock</p>
                            <button type="submit" class="btn btn-cart mt-3 text-white">Add to Cart</button>
                        </c:if>
                        <c:if test="${details.stock_Quantity <= 0}">
                            <p>Status: Out of Stock</p>
                        </c:if>
                        <%
                            String errorMessage = (String) session.getAttribute("errorMessage");
                            if (errorMessage != null) {
                        %>
                        <script>
                            alert("<%= errorMessage%>");
                        </script>
                        <%
                                session.removeAttribute("errorMessage"); // Xóa lỗi sau khi hiển thị
                            }
                        %>

                    </form>

                </div>
            </div>
            <hr/>
            <h3>Feedbacks</h3>
            <div class="rating-summary">
                Average Rating: 
                <span class="stars">
                    <c:forEach begin="1" end="5" var="star">
                        <c:choose>
                            <c:when test="${star <= avgRating}">
                                <i class="fas fa-star"></i>
                            </c:when>
                            <c:when test="${star > avgRating && star <= avgRating + 0.5}">
                                <i class="fas fa-star-half-alt"></i>
                            </c:when>
                            <c:otherwise>
                                <i class="far fa-star"></i>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </span>
                <span class="rating-count">${feedbacks.size()} ratings</span>
            </div>
            <div class="feedback-container">
                <c:if test="${feedbacks.isEmpty() eq false}">
                    <c:forEach var="f" items="${feedbacks}">
                        <div class="feedback-item">
                            <div class="feedback-actions">
                                <c:if test="${sessionScope.customer.customerID eq f.customerID}">
                                    <button class="icon-btn edit" onclick="editFeedback('${f.feedbackID}');">
                                        <i class="fas fa-edit"></i>
                                    </button>
                                    <button class="icon-btn delete" onclick="deleteFeedback('${f.feedbackID}');">
                                        <i class="fas fa-trash"></i>
                                    </button>
                                </c:if>
                            </div>
                            <strong>${f.customer.fullName}</strong>
                            <div class="stars">
                                <c:forEach begin="1" end="5" var="star">
                                    <c:choose>
                                        <c:when test="${star <= f.rating}">
                                            <i class="fas fa-star"></i>
                                        </c:when>
                                        <c:otherwise>
                                            <i class="far fa-star"></i>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </div>
                            <div class="mt-2">
                                ${fn:escapeXml(f.comment)}
                            </div>
                        </div>
                    </c:forEach>
                </c:if>
                <c:if test="${feedbacks.isEmpty() eq true}">
                    <p>No feedback found</p>
                </c:if>
            </div>
            <h2>Leave Your Feedback</h2>
            <div class="feedback-form">
                <c:choose>
                    <c:when test="${sessionScope.customer ne null}">
                        <c:choose>
                            <c:when test="${canLeaveFeedback eq true}">
                                <form id="feedback-form">
                                    <input type="hidden" name="productId" value="${details.productID}">
                                    <div class="mb-3">
                                        <label for="rating" class="form-label">Rating:</label>
                                        <div class="star-rating">
                                            <span class="star" data-value="1"><i class="fas fa-star"></i></span>
                                            <span class="star" data-value="2"><i class="fas fa-star"></i></span>
                                            <span class="star" data-value="3"><i class="fas fa-star"></i></span>
                                            <span class="star" data-value="4"><i class="fas fa-star"></i></span>
                                            <span class="star" data-value="5"><i class="fas fa-star"></i></span>
                                        </div>
                                        <input type="hidden" id="rating" name="rating" value="">
                                    </div>
                                    <div class="mb-3">
                                        <label for="comment" class="form-label">Comment:</label>
                                        <textarea id="comment" name="comment" class="form-control" rows="5" cols="50"></textarea>
                                    </div>
                                    <button type="submit" id="submitFeedback" class="btn btn-primary">Submit Feedback</button>
                                </form>
                            </c:when>
                            <c:when test="${hasPurchased eq false}">
                                <div class="alert alert-info">
                                    You need to purchase this product before leaving feedback.
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="alert alert-info">
                                    You have already provided feedback for this product.
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </c:when>
                    <c:otherwise>
                        <div class="alert alert-warning">
                            Please <a href="login.jsp">log in</a> to leave feedback.
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
            <a class="btn btn-cart mt-3 bg-gradient-secondary text-white" href="home">Back to Home</a>
            <h2>Comments</h2>

            <!-- Form to add comment -->
            <div class="comment-form"> 
                <input id="content" type="text" name="content" placeholder="Write a comment..." required>
                <input type="hidden" id="productID" value="${details.productID}"> 
                <button type="button" id="submitComment" onclick="submitComment()"> 
                    <i class="fas fa-paper-plane"></i> Add Comment 
                </button>
            </div>

            <div class="comment-container">
                <c:if test="${comments.isEmpty() eq false}">
                    <c:forEach var="c" items="${comments}">
                        <div class="comment" id="comment-${c.commentID}">
                            <!-- Show customer name and comment details -->
                            <div class="comment-header"> 
                                <strong>${c.customer.fullName}</strong>

                                <!-- Show edit and delete button only for the comment owner -->
                                <c:if test="${c.customerID eq sessionScope.customer.customerID}">
                                    <div class="comment-actions"> 
                                        <button class="icon-btn edit" onclick="startEdit('${c.commentID}', '${c.content}');">
                                            <i class="fas fa-edit"></i> 
                                        </button>
                                        <button class="icon-btn delete" onclick="deleteComment('${c.commentID}');">
                                            <i class="fas fa-trash"></i> 
                                        </button>
                                    </div>
                                </c:if>
                            </div>

                            <!-- Show comment content -->
                            <div id="commentText-${c.commentID}" class="comment-content">
                                ${c.content}
                            </div>

                            <!-- Formatted Created At (Timestamp to Date) -->
                            <span class="timestamp"> 
                                <i class="far fa-clock"></i> 
                                <fmt:formatDate value="${c.createdAt}" pattern="yyyy-MM-dd HH:mm:ss" />

                                <!-- Show Changed At if updated -->
                                <c:if test="${not empty c.updatedAt and c.createdAt != c.updatedAt}">
                                    <br><i class="fas fa-edit"></i> 
                                    <fmt:formatDate value="${c.updatedAt}" pattern="yyyy-MM-dd HH:mm:ss" />
                                </c:if>
                            </span>
                        </div>
                    </c:forEach>    
                </c:if>
                <c:if test="${comments.isEmpty() eq true}">
                    <div class="no-comments"> 
                        <i class="far fa-comment-dots fa-2x mb-3"></i> 
                        <p>No comments found</p> 
                    </div>
                </c:if>
            </div>
        </div>
        <script src="https://kit.fontawesome.com/a076d05399.js" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
        <script>
                                            document.addEventListener("DOMContentLoaded", function () {
                                                let stockQuantity = ${details.stock_Quantity};
                                                let quantityInput = document.getElementById("stock_Quantity");
                                                let addToCartButton = document.querySelector(".btn-cart");
                                                // Giới hạn số lượng nhập vào
                                                quantityInput.addEventListener("input", function () {
                                                    let selectedQuantity = parseInt(quantityInput.value) || 1;
                                                    if (selectedQuantity > stockQuantity) {
                                                        alert("Quantity exceeds available stock! Only " + stockQuantity + " items left.");
                                                        quantityInput.value = stockQuantity;
                                                    } else if (selectedQuantity < 1) {
                                                        quantityInput.value = 1;
                                                    }
                                                });
                                                // Kiểm tra trước khi submit form
                                                document.querySelector("form").addEventListener("submit", function (event) {
                                                    let selectedQuantity = parseInt(quantityInput.value) || 1;
                                                    if (selectedQuantity > stockQuantity) {
                                                        event.preventDefault();
                                                        alert("Quantity exceeds available stock! Only " + stockQuantity + " items left.");
                                                    }
                                                });
                                                // Vô hiệu hóa nút "Thêm vào giỏ hàng" nếu số lượng tồn kho = 0
                                                if (stockQuantity <= 0) {
                                                    addToCartButton.disabled = true;
                                                }
                                            });
                                            document.addEventListener("DOMContentLoaded", function () {
                                                let textarea = document.getElementById("description");
                                                // Xóa khoảng trắng thừa
                                                let content = textarea.value.trim();
                                                // Thay dấu ',' và ';' bằng xuống dòng
                                                content = content.replaceAll(";", "\n");
                                                // Cập nhật lại nội dung đã format vào textarea
                                                textarea.value = content;
                                                // Tự động điều chỉnh số dòng
                                                let lineCount = content.split("\n").length;
                                                textarea.rows = lineCount > 5 ? lineCount : 5;
                                            });
        </script>

        <script>
            const stars = document.querySelectorAll('.star');
            const ratingInput = document.getElementById('rating');
            stars.forEach(star => {
                star.addEventListener('click', () => {
                    const rating = star.getAttribute('data-value');
                    ratingInput.value = rating;
                    highlightStars(rating);
                });
            });
            function highlightStars(rating) {
                stars.forEach(star => {
                    if (star.getAttribute('data-value') <= rating) {
                        star.classList.add('selected');
                    } else {
                        star.classList.remove('selected');
                    }
                });
            }

            function validateRating() {
                if (ratingInput.value == "0") {
                    alert("Please select a star rating before submitting.");
                    return false;
                }
                return true;
            }
        </script>

        <script>
            function submitFeedback(event) {
                let rating = $('#rating').val();
                let comment = $('#comment').val();
                if (!rating || !comment) {
                    Swal.fire({
                        title: 'Error!',
                        text: 'Please input all field in this feedback',
                        icon: 'warning',
                        confirmButtonText: 'Ok',
                        allowEnter: false,
                        allowOutsideClick: false
                    });
                    return;
                }

                if (rating <= 0) {
                    Swal.fire({
                        title: 'Error!',
                        text: 'Please select star for feedback',
                        icon: 'warning',
                        confirmButtonText: 'Ok',
                        allowEnter: false,
                        allowOutsideClick: false
                    });
                    return;
                }

                if (comment.length > 500 || comment.trim().length <= 0) {
                    Swal.fire({
                        title: 'Error!',
                        text: 'Please input comment',
                        icon: 'warning',
                        confirmButtonText: 'Ok',
                        allowEnter: false,
                        allowOutsideClick: false
                    });
                    return;
                }

                $.ajax({
                    url: 'feedback',
                    type: 'POST',
                    data: {
                        rating: $('#rating').val(),
                        comment: $('#comment').val(),
                        productId: $('#productID').val()
                    },
                    success: function (response) {
                        Swal.fire({
                            title: 'Thank you!',
                            text: 'Your feedback has been submitted.',
                            icon: 'success',
                            confirmButtonText: 'OK',
                            allowEnter: false,
                            allowOutsideClick: false
                        }).then((result) => {
                            if (result.isConfirmed) {
                                location.reload();
                            }
                        });
                    },
                    error: function (xhr, status, error) {
                        Swal.fire({
                            title: 'Error!',
                            text: xhr.responseJSON,
                            icon: 'warning',
                            confirmButtonText: 'Retry',
                            allowEnter: false,
                            allowOutsideClick: false
                        });
                    }
                });
            }

            function submitComment(event) {
                let content = $('#content').val();
                if (!content) {
                    Swal.fire({
                        title: 'Error!',
                        text: 'Please input all field in this comment',
                        icon: 'warning',
                        confirmButtonText: 'Ok',
                        allowEnter: false,
                        allowOutsideClick: false
                    });
                    return;
                }

                if (content.length > 255 || content.trim().length <= 0) {
                    Swal.fire({
                        title: 'Error!',
                        text: 'Please input comment',
                        icon: 'warning',
                        confirmButtonText: 'Ok',
                        allowEnter: false,
                        allowOutsideClick: false
                    });
                    return;
                }

                $.ajax({
                    url: 'comment',
                    type: 'POST',
                    data: {
                        content: $('#content').val(),
                        productId: $('#productID').val()
                    },
                    success: function (response) {
                        Swal.fire({
                            title: 'Sucess!',
                            text: 'You comment sucessfully!',
                            icon: 'success',
                            confirmButtonText: 'OK',
                            allowEnter: false,
                            allowOutsideClick: false
                        }).then((result) => {
                            if (result.isConfirmed) {
                                location.reload();
                            }
                        });
                    },
                    error: function (xhr, status, error) {
                        Swal.fire({
                            title: 'Error!',
                            text: xhr.responseJSON,
                            icon: 'warning',
                            confirmButtonText: 'Retry',
                            allowEnter: false,
                            allowOutsideClick: false
                        });
                    }
                });
            }

            function updateComment(event) {
                let content = $('#editContent').val();
                if (!content) {
                    Swal.fire({
                        title: 'Error!',
                        text: 'Please input all field in this comment',
                        icon: 'warning',
                        confirmButtonText: 'Ok',
                        allowEnter: false,
                        allowOutsideClick: false
                    });
                    return;
                }

                if (content.length > 255 || content.trim().length <= 0) {
                    Swal.fire({
                        title: 'Error!',
                        text: 'Please input comment',
                        icon: 'warning',
                        confirmButtonText: 'Ok',
                        allowEnter: false,
                        allowOutsideClick: false
                    });
                    return;
                }

                $.ajax({
                    url: 'edit-comment',
                    type: 'POST',
                    data: {
                        content: content,
                        commentId: $('#commentId').val()
                    },
                    success: function (response) {
                        Swal.fire({
                            title: 'Sucess!',
                            text: 'You update comment sucessfully!',
                            icon: 'success',
                            confirmButtonText: 'OK',
                            allowEnter: false,
                            allowOutsideClick: false
                        }).then((result) => {
                            if (result.isConfirmed) {
                                location.reload();
                            }
                        });
                    },
                    error: function (xhr, status, error) {
                        Swal.fire({
                            title: 'Error!',
                            text: xhr.responseJSON,
                            icon: 'warning',
                            confirmButtonText: 'Retry',
                            allowEnter: false,
                            allowOutsideClick: false
                        });
                    }
                });
            }

            function deleteComment(deleteCommentId) {
                Swal.fire({
                    title: 'Warning',
                    text: "Do you want to delete this comment ?",
                    icon: 'warning',
                    showCancelButton: true,
                    confirmButtonColor: '#3085d6',
                    cancelButtonColor: '#d33',
                    confirmButtonText: 'Yes',
                    cancelButtonText: 'No',
                    allowEnter: false,
                    allowOutsideClick: false
                }).then((result) => {
                    if (result.isConfirmed) {
                        $.ajax({
                            url: 'delete-comment?commentId=' + deleteCommentId,
                            type: 'GET',
                            data: {
                            },
                            success: function (response) {
                                Swal.fire({
                                    title: 'Sucess!',
                                    text: 'Delete comment sucessfully!',
                                    icon: 'success',
                                    confirmButtonText: 'OK',
                                    allowEnter: false,
                                    allowOutsideClick: false
                                }).then((result) => {
                                    if (result.isConfirmed) {
                                        location.reload();
                                    }
                                });
                            },
                            error: function (xhr, status, error) {
                                Swal.fire({
                                    title: 'Error!',
                                    text: xhr.responseJSON,
                                    icon: 'warning',
                                    confirmButtonText: 'Retry',
                                    allowEnter: false,
                                    allowOutsideClick: false
                                });
                            }
                        });
                    }
                });
            }
        </script>

        <script>
            function startEdit(id, text) {
                var htmlContent = '<textarea id="editContent" class="swal2-textarea" placeholder="Your comment...">' +
                        text +
                        '</textarea>' +
                        '<input type="hidden" id="commentId" value="' + id + '">';

                Swal.fire({
                    title: 'Edit Your Comment',
                    html: htmlContent,
                    showCancelButton: true,
                    confirmButtonText: 'Update',
                    cancelButtonText: 'Cancel',
                    confirmButtonColor: '#3085d6',
                    cancelButtonColor: '#d33',
                    preConfirm: () => {
                        const editedContent = document.getElementById('editContent').value;
                        if (!editedContent.trim()) {
                            Swal.showValidationMessage('Comment cannot be empty');
                            return false;
                        }
                        return true;
                    }
                }).then((result) => {
                    if (result.isConfirmed) {
                        const editedContent = document.getElementById('editContent').value;
                        const commentIdToEdit = document.getElementById('commentId').value;

                        console.log("Sending comment update with ID:", commentIdToEdit);

                        // Gửi request AJAX để cập nhật bình luận
                        $.ajax({
                            url: 'edit-comment',
                            type: 'POST',
                            data: {
                                content: editedContent,
                                commentId: commentIdToEdit
                            },
                            success: function (response) {
                                Swal.fire({
                                    icon: 'success',
                                    title: 'Success!',
                                    text: 'Your comment has been updated.',
                                    showConfirmButton: false,
                                    timer: 1500
                                }).then(() => {
                                    location.reload();
                                });
                            },
                            error: function (xhr) {
                                Swal.fire({
                                    icon: 'error',
                                    title: 'Error!',
                                    text: xhr.responseText || 'Failed to update your comment.',
                                    confirmButtonText: 'OK'
                                });
                            }
                        });
                    }
                });
            }

            // Hàm để xóa bình luận
            function deleteComment(commentId) {
                Swal.fire({
                    title: 'Are you sure?',
                    text: "Do you want to delete your comment?",
                    icon: 'warning',
                    showCancelButton: true,
                    confirmButtonColor: '#3085d6',
                    cancelButtonColor: '#d33',
                    confirmButtonText: 'Yes, delete it!'
                }).then((result) => {
                    if (result.isConfirmed) {
                        // Gửi request AJAX để xóa bình luận
                        $.ajax({
                            url: 'delete-comment',
                            type: 'POST',
                            data: {
                                commentId: commentId
                            },
                            success: function (response) {
                                Swal.fire({
                                    icon: 'success',
                                    title: 'Deleted!',
                                    text: 'Your comment has been deleted.',
                                    showConfirmButton: false,
                                    timer: 1500
                                }).then(() => {
                                    location.reload();
                                });
                            },
                            error: function (xhr) {
                                Swal.fire({
                                    icon: 'error',
                                    title: 'Error!',
                                    text: xhr.responseText || 'Failed to delete your comment.',
                                    confirmButtonText: 'OK'
                                });
                            }
                        });
                    }
                });
            }
        </script>

        <script>
            function editFeedback(feedbackId) {
                // Debug để xem hàm có được gọi không và giá trị feedbackId là gì
                console.log("editFeedback called with ID:", feedbackId);

                // Load feedback data
                $.ajax({
                    url: 'feedback?action=get&feedbackId=' + feedbackId,
                    type: 'GET',
                    dataType: 'json', // Chỉ định kiểu dữ liệu trả về là json
                    success: function (feedback) {
                        console.log("Feedback data received:", feedback);

                        // Populate edit form
                        Swal.fire({
                            title: 'Edit Your Feedback',
                            html:
                                    '<div class="star-rating edit-stars" style="justify-content: center; font-size: 24px; margin-bottom: 15px;">' +
                                    '<span class="star" data-value="1"><i class="fas fa-star"></i></span>' +
                                    '<span class="star" data-value="2"><i class="fas fa-star"></i></span>' +
                                    '<span class="star" data-value="3"><i class="fas fa-star"></i></span>' +
                                    '<span class="star" data-value="4"><i class="fas fa-star"></i></span>' +
                                    '<span class="star" data-value="5"><i class="fas fa-star"></i></span>' +
                                    '</div>' +
                                    '<input id="edit-rating" type="hidden" value="' + feedback.rating + '">' +
                                    '<textarea id="edit-comment" class="swal2-textarea" placeholder="Your comments...">' + feedback.comment + '</textarea>',
                            showCancelButton: true,
                            confirmButtonText: 'Update',
                            confirmButtonColor: '#3085d6',
                            cancelButtonColor: '#d33',
                            didOpen: () => {
                                // Initialize stars in the modal
                                const stars = document.querySelectorAll('.edit-stars .star');
                                const ratingInput = document.getElementById('edit-rating');

                                // Set initial stars
                                for (let i = 0; i < stars.length; i++) {
                                    if (i < feedback.rating) {
                                        stars[i].classList.add('selected');
                                    }

                                    stars[i].addEventListener('click', function () {
                                        const value = this.getAttribute('data-value');
                                        ratingInput.value = value;

                                        // Update visual
                                        document.querySelectorAll('.edit-stars .star').forEach(s => s.classList.remove('selected'));
                                        for (let j = 0; j < value; j++) {
                                            stars[j].classList.add('selected');
                                        }
                                    });
                                }
                            }
                        }).then((result) => {
                            if (result.isConfirmed) {
                                const updatedRating = document.getElementById('edit-rating').value;
                                const updatedComment = document.getElementById('edit-comment').value;

                                console.log("Sending feedback update with ID:", feedbackId);

                                // Submit update
                                $.ajax({
                                    url: 'feedback',
                                    type: 'POST',
                                    data: {
                                        action: 'update',
                                        feedbackId: feedbackId,
                                        rating: updatedRating,
                                        comment: updatedComment
                                    },
                                    success: function (response) {
                                        Swal.fire({
                                            icon: 'success',
                                            title: 'Feedback Updated',
                                            showConfirmButton: false,
                                            timer: 1500
                                        }).then(() => {
                                            location.reload();
                                        });
                                    },
                                    error: function (xhr) {
                                        Swal.fire({
                                            icon: 'error',
                                            title: 'Error',
                                            text: xhr.responseText || 'Failed to update your feedback'
                                        });
                                    }
                                });
                            }
                        });
                    },
                    error: function (xhr) {
                        console.error("Error loading feedback:", xhr);
                        Swal.fire({
                            icon: 'error',
                            title: 'Error',
                            text: 'Could not load feedback data'
                        });
                    }
                });
            }

            // Delete feedback function
            function deleteFeedback(feedbackId) {
                Swal.fire({
                    title: 'Are you sure?',
                    text: "You won't be able to revert this!",
                    icon: 'warning',
                    showCancelButton: true,
                    confirmButtonColor: '#3085d6',
                    cancelButtonColor: '#d33',
                    confirmButtonText: 'Yes, delete it!'
                }).then((result) => {
                    if (result.isConfirmed) {
                        $.ajax({
                            url: 'feedback',
                            type: 'POST',
                            data: {
                                action: 'delete',
                                feedbackId: feedbackId
                            },
                            success: function (response) {
                                Swal.fire(
                                        'Deleted!',
                                        'Your feedback has been deleted.',
                                        'success'
                                        ).then(() => {
                                    location.reload();
                                });
                            },
                            error: function (xhr) {
                                Swal.fire(
                                        'Error!',
                                        'There was a problem deleting your feedback: ' + xhr.responseText,
                                        'error'
                                        );
                            }
                        });
                    }
                });
            }
        </script>

        <!-- Footer -->
        <jsp:include page="footer.jsp"></jsp:include>

        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

    </body>

</html>

<script>
            // Hàm để chỉnh sửa bình luận
            function startEdit(commentId, commentContent) {
                var htmlContent = '<textarea id="editContent" class="swal2-textarea" placeholder="Your comment...">' +
                        commentContent +
                        '</textarea>' +
                        '<input type="hidden" id="commentId" value="' + commentId + '">';

                Swal.fire({
                    title: 'Edit Your Comment',
                    html: htmlContent,
                    showCancelButton: true,
                    confirmButtonText: 'Update',
                    cancelButtonText: 'Cancel',
                    confirmButtonColor: '#3085d6',
                    cancelButtonColor: '#d33',
                    preConfirm: () => {
                        const editedContent = document.getElementById('editContent').value;
                        if (!editedContent.trim()) {
                            Swal.showValidationMessage('Comment cannot be empty');
                            return false;
                        }
                        return true;
                    }
                }).then((result) => {
                    if (result.isConfirmed) {
                        const editedContent = document.getElementById('editContent').value;
                        const commentIdToEdit = document.getElementById('commentId').value;

                        console.log("Sending comment update with ID:", commentIdToEdit);

                        // Gửi request AJAX để cập nhật bình luận
                        $.ajax({
                            url: 'edit-comment',
                            type: 'POST',
                            data: {
                                content: editedContent,
                                commentId: commentIdToEdit
                            },
                            success: function (response) {
                                Swal.fire({
                                    icon: 'success',
                                    title: 'Success!',
                                    text: 'Your comment has been updated.',
                                    showConfirmButton: false,
                                    timer: 1500
                                }).then(() => {
                                    location.reload();
                                });
                            },
                            error: function (xhr) {
                                Swal.fire({
                                    icon: 'error',
                                    title: 'Error!',
                                    text: xhr.responseText || 'Failed to update your comment.',
                                    confirmButtonText: 'OK'
                                });
                            }
                        });
                    }
                });
            }

            // Hàm để xóa bình luận
            function deleteComment(commentId) {
                Swal.fire({
                    title: 'Are you sure?',
                    text: "Do you want to delete your comment?",
                    icon: 'warning',
                    showCancelButton: true,
                    confirmButtonColor: '#3085d6',
                    cancelButtonColor: '#d33',
                    confirmButtonText: 'Yes, delete it!'
                }).then((result) => {
                    if (result.isConfirmed) {
                        // Gửi request AJAX để xóa bình luận
                        $.ajax({
                            url: 'delete-comment',
                            type: 'POST',
                            data: {
                                commentId: commentId
                            },
                            success: function (response) {
                                Swal.fire({
                                    icon: 'success',
                                    title: 'Deleted!',
                                    text: 'Your comment has been deleted.',
                                    showConfirmButton: false,
                                    timer: 1500
                                }).then(() => {
                                    location.reload();
                                });
                            },
                            error: function (xhr) {
                                Swal.fire({
                                    icon: 'error',
                                    title: 'Error!',
                                    text: xhr.responseText || 'Failed to delete your comment.',
                                    confirmButtonText: 'OK'
                                });
                            }
                        });
                    }
                });
            }
</script>
