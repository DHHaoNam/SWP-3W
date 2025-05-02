/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.feedback;

import com.google.gson.Gson;
import dao.FeedbackDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Customer;
import model.Feedback;

/**
 *
 * @author HP
 */
public class FeedbackControllers extends HttpServlet {

    private final FeedbackDAO feedbackDAO;

    public FeedbackControllers() {
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
            out.println("<title>Servlet FeedbackControllers</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet FeedbackControllers at " + request.getContextPath() + "</h1>");
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
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Gson gson = new Gson();

        String action = request.getParameter("action");
        if (action != null && action.equals("get")) {
            String feedbackId = request.getParameter("feedbackId");
            Customer customer = (Customer) request.getSession().getAttribute("customer");

            if (customer == null) {
                response.getWriter().print(gson.toJson("You need to login to get feedback"));
                response.setStatus(401);
                return;
            }

            try {
                // Get the feedback
                Feedback feedback = feedbackDAO.getFeedbackById(Integer.parseInt(feedbackId));

                if (feedback == null) {
                    response.getWriter().print(gson.toJson("Feedback not found"));
                    response.setStatus(404);
                    return;
                }

                // Check if the feedback belongs to the current customer
                if (feedback.getCustomerID() != customer.getCustomerID()) {
                    response.getWriter().print(gson.toJson("You can only access your own feedback"));
                    response.setStatus(403);
                    return;
                }

                response.getWriter().print(gson.toJson(feedback));

            } catch (NumberFormatException e) {
                response.getWriter().print(gson.toJson("Invalid feedback ID"));
                response.setStatus(400);
            }
        } else {
            response.getWriter().print(gson.toJson("Invalid action"));
            response.setStatus(400);
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
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Gson gson = new Gson();

        String action = request.getParameter("action");
        if (action != null) {
            switch (action) {
                case "delete":
                    deleteFeedback(request, response, gson);
                    break;
                case "update":
                    updateFeedback(request, response, gson);
                    break;
                default:
                    createFeedback(request, response, gson);
                    break;
            }
        } else {
            createFeedback(request, response, gson);
        }
    }

    private void createFeedback(HttpServletRequest request, HttpServletResponse response, Gson gson) throws IOException {
        String raw_rating = request.getParameter("rating");
        String comment = request.getParameter("comment");
        String productId = request.getParameter("productId");
        Customer customer = (Customer) request.getSession().getAttribute("customer");
        String orderDetailIdStr = request.getParameter("orderDetailId"); // Get orderDetailId from request
        if (customer == null) {
            response.getWriter().print(gson.toJson("You need to login to feedback"));
            response.setStatus(400);
            return;
        }

        // Check if customer has already given feedback for this product
        if (feedbackDAO.hasCustomerFeedbackForProduct(customer.getCustomerID(), Integer.parseInt(productId))) {
            response.getWriter().print(gson.toJson("You have already provided feedback for this product"));
            response.setStatus(400);
            return;
        }

        Feedback feedback = new Feedback();
        feedback.setRating(Integer.parseInt(raw_rating));
        feedback.setComment(comment);
        feedback.setProductID(Integer.parseInt(productId));
        feedback.setCustomerID(customer.getCustomerID());
        feedback.setDeleted(false);
        Date today = new Date();
        java.sql.Date sqlDate = new java.sql.Date(today.getTime());
        feedback.setCreateAt(sqlDate);

        if (orderDetailIdStr != null) {
            feedback.setOrderDetailID(Integer.parseInt(orderDetailIdStr));
        } else {
            // Handle the case where orderDetailId is not provided
            response.getWriter().print(gson.toJson("Order detail ID is required"));
            response.setStatus(400);
            return;
        }
        feedbackDAO.insert(feedback);

        response.getWriter().print(gson.toJson("Feedback successfully"));
    }

    private void updateFeedback(HttpServletRequest request, HttpServletResponse response, Gson gson) throws IOException {
        String feedbackId = request.getParameter("feedbackId");
        String rating = request.getParameter("rating");
        String comment = request.getParameter("comment");
        Customer customer = (Customer) request.getSession().getAttribute("customer");

        if (customer == null) {
            response.getWriter().print(gson.toJson("You need to login to update feedback"));
            response.setStatus(401);
            return;
        }

        try {
            // Get the feedback to make sure it belongs to the current customer
            Feedback feedback = feedbackDAO.getFeedbackById(Integer.parseInt(feedbackId));

            if (feedback == null) {
                response.getWriter().print(gson.toJson("Feedback not found"));
                response.setStatus(404);
                return;
            }

            if (feedback.getCustomerID() != customer.getCustomerID()) {
                response.getWriter().print(gson.toJson("You can only update your own feedback"));
                response.setStatus(403);
                return;
            }

            // Update the feedback
            feedback.setRating(Integer.parseInt(rating));
            feedback.setComment(comment);

            feedbackDAO.update(feedback);
            response.getWriter().print(gson.toJson("Feedback updated successfully"));

        } catch (NumberFormatException e) {
            response.getWriter().print(gson.toJson("Invalid feedback ID"));
            response.setStatus(400);
        }
    }

    private void deleteFeedback(HttpServletRequest request, HttpServletResponse response, Gson gson) throws IOException {
        String feedbackId = request.getParameter("feedbackId");
        Customer customer = (Customer) request.getSession().getAttribute("customer");

        if (customer == null) {
            response.getWriter().print(gson.toJson("You need to login to delete feedback"));
            response.setStatus(401);
            return;
        }

        try {
            // Get the feedback to make sure it belongs to the current customer
            Feedback feedback = feedbackDAO.getFeedbackById(Integer.parseInt(feedbackId));

            if (feedback == null) {
                response.getWriter().print(gson.toJson("Feedback not found"));
                response.setStatus(404);
                return;
            }

            if (feedback.getCustomerID() != customer.getCustomerID()) {
                response.getWriter().print(gson.toJson("You can only delete your own feedback"));
                response.setStatus(403);
                return;
            }

            // Soft delete the feedback
            feedbackDAO.delete(Integer.parseInt(feedbackId));
            response.getWriter().print(gson.toJson("Feedback deleted successfully"));

        } catch (NumberFormatException e) {
            response.getWriter().print(gson.toJson("Invalid feedback ID"));
            response.setStatus(400);
        }
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
