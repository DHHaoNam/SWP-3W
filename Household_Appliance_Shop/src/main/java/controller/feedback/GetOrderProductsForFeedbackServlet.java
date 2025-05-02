/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.feedback;

import dao.FeedbackDAO;
import dao.OrderDAO;
import dao.ProductDAO;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Customer;
import model.OrderDetail;
import model.Product;

/**
 *
 * @author HP
 */
public class GetOrderProductsForFeedbackServlet extends HttpServlet {

    private final OrderDAO orderDAO;
    private final ProductDAO productDAO;
    private final FeedbackDAO feedbackDAO;

    public GetOrderProductsForFeedbackServlet() {
        orderDAO = new OrderDAO();
        productDAO = new ProductDAO();
        feedbackDAO = new FeedbackDAO();
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet GetOrderProductsForFeedbackServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet GetOrderProductsForFeedbackServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String orderId = request.getParameter("orderId");

        if (orderId == null || orderId.isEmpty()) {
            response.getWriter().write("<div class='alert alert-danger'>Order ID is required</div>");
            return;
        }

        HttpSession session = request.getSession();
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            response.getWriter().write("<div class='alert alert-danger'>You must be logged in to provide feedback</div>");
            return;
        }

        try ( PrintWriter out = response.getWriter()) {
            int orderIdNum = Integer.parseInt(orderId);
            List<OrderDetail> orderDetails = orderDAO.getOrderDetailsByOrderID(orderIdNum);

            if (orderDetails.isEmpty()) {
                out.println("<div class='alert alert-info'>No products found in this order.</div>");
                return;
            }

            out.println("<div class='row'>");

            int productCounter = 0;
            for (OrderDetail detail : orderDetails) {
                Product product = productDAO.selectProduct(detail.getProductID());
                if (product == null) {
                    continue;
                }

                boolean hasGivenFeedback = feedbackDAO.hasCustomerFeedbackForProduct(
                        customer.getCustomerID(), product.getProductID());

                out.println("<div class='col-md-6 mb-4'>");
                out.println("<div class='card h-100'>");
                out.println("<div class='card-header'>");
                out.println("<div class='d-flex align-items-center'>");
                out.println("<img src='" + product.getImage() + "' class='img-fluid' style='width: 60px; height: 60px; object-fit: cover;' alt='" + product.getProductName() + "'>");
                out.println("<div class='ms-3'>");
                out.println("<h5 class='mb-0'>" + product.getProductName() + "</h5>");
                out.println("<small class='text-muted'>Quantity: " + detail.getQuantity() + "</small>");
                out.println("</div></div></div>");

                out.println("<div class='card-body'>");
                if (hasGivenFeedback) {
                    out.println("<div class='alert alert-success'>");
                    out.println("<i class='fas fa-check-circle me-2'></i> You have already provided feedback for this product.");
                    out.println("</div>");
                } else {
                    out.println("<form id='feedback-form-" + productCounter + "' class='feedback-form'>");
                    out.println("<input type='hidden' name='productId' value='" + product.getProductID() + "'>");
                    out.println("<input type='hidden' name='orderDetailId' value='" + detail.getOrderDetailID() + "'>");
                    out.println("<input type='hidden' name='rating' value=''>");

                    out.println("<div class='mb-3'>");
                    out.println("<label class='form-label'>Rate this product:</label>");
                    out.println("<div class='star-rating' style='font-size: 1.8rem; color: grey;'>");
                    for (int i = 1; i <= 5; i++) {
                        out.println("<span class='star' data-value='" + i + "'><i class='fas fa-star'></i></span>");
                    }
                    out.println("</div></div>");

                    out.println("<div class='mb-3'>");
                    out.println("<label for='comment-" + productCounter + "' class='form-label'>Your comments:</label>");
                    out.println("<textarea class='form-control' id='comment-" + productCounter + "' name='comment' rows='3' placeholder='Share your thoughts on this product...'></textarea>");
                    out.println("</div>");

                    out.println("<button type='submit' class='btn btn-primary w-100'>Submit Feedback</button>");
                    out.println("</form>");
                }

                out.println("</div></div></div>");
                productCounter++;
            }

            out.println("</div>"); // End row
        } catch (NumberFormatException e) {
            response.getWriter().write("<div class='alert alert-danger'>Invalid order ID</div>");
        } catch (Exception e) {
            response.getWriter().write("<div class='alert alert-danger'>Error: " + e.getMessage() + "</div>");
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendError(jakarta.servlet.http.HttpServletResponse.SC_METHOD_NOT_ALLOWED, "POST method not supported");
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
