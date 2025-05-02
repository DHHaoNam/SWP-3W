/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.comment;

import com.google.gson.Gson;
import dao.CommentDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Date;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Comment;
import model.Customer;

/**
 *
 * @author HP
 */
public class InsertCommentController extends HttpServlet {

    private CommentDAO commentDAO;

    public InsertCommentController() {
        commentDAO = new CommentDAO();
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
            out.println("<title>Servlet InsertCommentController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet InsertCommentController at " + request.getContextPath() + "</h1>");
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
        processRequest(request, response);
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
        String content = request.getParameter("content");
        String raw_ProductId = request.getParameter("productId");

        // Xử lý trường hợp productId bị null
        if (raw_ProductId == null || raw_ProductId.isEmpty()) {
            // Thử lấy productId từ URL hoặc request attribute
            raw_ProductId = request.getParameter("productID"); // Kiểm tra tham số khác có thể chứa productId

            if (raw_ProductId == null || raw_ProductId.isEmpty()) {
                // Nếu vẫn không tìm thấy, trả về lỗi
                response.getWriter().print(gson.toJson("Product ID is required for commenting"));
                response.setStatus(400);
                return;
            }
        }

        Customer customer = (Customer) request.getSession().getAttribute("customer");
        if (customer == null) {
            response.getWriter().print(gson.toJson("You need to login to comment"));
            response.setStatus(400);
            return;
        }

        if (content == null || content.trim().isEmpty()) {
            response.getWriter().print(gson.toJson("Comment content cannot be empty"));
            response.setStatus(400);
            return;
        }

        try {
            int productId = Integer.parseInt(raw_ProductId);

            Comment comment = new Comment();
            comment.setContent(content);
            comment.setProductID(productId);
            comment.setCustomerID(customer.getCustomerID());
            comment.setDeleted(false);
            Date today = new Date();
            comment.setCreatedAt(new Timestamp(today.getTime()));
            comment.setUpdatedAt(new Timestamp(today.getTime()));
            commentDAO.insert(comment);
            response.getWriter().print(gson.toJson("Comment successfully"));
        } catch (NumberFormatException e) {
            response.getWriter().print(gson.toJson("Invalid product ID format"));
            response.setStatus(400);
        } catch (Exception e) {
            response.getWriter().print(gson.toJson("Error processing your comment: " + e.getMessage()));
            response.setStatus(500);
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
