/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.product;

import dao.FeedbackDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.CommentDTO;
import model.Product;

/**
 *
 * @author TRUNG NHAN
 */
public class FeedbackController extends HttpServlet {

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
            out.println("<title>Servlet FeedbackController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet FeedbackController at " + request.getContextPath() + "</h1>");
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
        String action = request.getServletPath();
        try {
            switch (action) {
                case "/viewFeedback":
                    adminViewFeedback(request, response);
                    break;
                case "/admin-delete-feedback":
                    adminDeleteFeedback(request, response);
                    break;
                default:
                    adminFeedback(request, response);
                    break;
            }
        } catch (IOException e) {
            e.getStackTrace();
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
        doGet(request, response);
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
    FeedbackDAO fdao = new FeedbackDAO();

    private void adminFeedback(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Product> productList = fdao.getProductsWithComments();
            request.setAttribute("products", productList);
        } catch (Exception e) {
            System.err.println("Error loading feedback products: " + e.getMessage());
            request.setAttribute("errorMessage", "Failed to load feedback products.");
        }
        request.getRequestDispatcher("admin_feedback_crud.jsp").forward(request, response);
    }

    private void adminViewFeedback(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int productId = Integer.parseInt(request.getParameter("id"));
            if (productId <= 0) {
                throw new IllegalArgumentException("Invalid product ID");
            }

            List<CommentDTO> comments = fdao.getCommentByProduct(productId);
            request.setAttribute("comments", comments);
            request.setAttribute("productID", productId);

        } catch (NumberFormatException e) {
            System.err.println("Invalid product ID format: " + e.getMessage());
            request.setAttribute("errorMessage", "Invalid product ID.");
        } catch (IllegalArgumentException e) {
            System.err.println("Illegal argument: " + e.getMessage());
            request.setAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            System.err.println("Error loading feedback: " + e.getMessage());
            request.setAttribute("errorMessage", "Error loading feedback.");
        }

        request.getRequestDispatcher("admin_view_feedback.jsp").forward(request, response);
    }

    private void adminDeleteFeedback(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String productID = request.getParameter("productID");
        try {
            int feedbackID = Integer.parseInt(request.getParameter("fid"));

            FeedbackDAO feedbackDAO = new FeedbackDAO();
            feedbackDAO.softDeleteFeedback(feedbackID);

        } catch (NumberFormatException e) {
            System.err.println("Invalid feedback ID format: " + e.getMessage());
            request.setAttribute("errorMessage", "Invalid feedback ID.");
        } catch (Exception e) {
            System.err.println("Unexpected error during feedback deletion: " + e.getMessage());
            request.setAttribute("errorMessage", "Failed to delete feedback.");
        }

        response.sendRedirect("FeedbackController?productID=" + productID);
    }

}
