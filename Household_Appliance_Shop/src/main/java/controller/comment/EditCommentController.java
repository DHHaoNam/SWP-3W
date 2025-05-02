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
public class EditCommentController extends HttpServlet {

    private CommentDAO commentDAO;

    public EditCommentController() {
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
            out.println("<title>Servlet EditCommentController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet EditCommentController at " + request.getContextPath() + "</h1>");
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
        String raw_commentId = request.getParameter("commentId");

        // **HungLHT** - Kiểm tra tham số commentId
        if (raw_commentId == null || raw_commentId.trim().isEmpty()) {
            response.getWriter().print(gson.toJson("Comment ID is required"));
            response.setStatus(400);
            return;
        }

        // **HungLHT** - Kiểm tra nội dung comment
        if (content == null || content.trim().isEmpty()) {
            response.getWriter().print(gson.toJson("Comment content cannot be empty"));
            response.setStatus(400);
            return;
        }

        Customer customer = (Customer) request.getSession().getAttribute("customer");
        if (customer == null) {
            response.getWriter().print(gson.toJson("You need to login to comment"));
            response.setStatus(400);
            return;
        }

        try {
            // **HungLHT** - Bọc trong try-catch để xử lý ngoại lệ NumberFormatException
            int commentId = Integer.parseInt(raw_commentId);

            // **HungLHT** - Kiểm tra xem comment có tồn tại không và thuộc về người dùng hiện tại
            Comment existingComment = commentDAO.getCommentById(commentId);
            if (existingComment == null) {
                response.getWriter().print(gson.toJson("Comment not found"));
                response.setStatus(404);
                return;
            }

            // **HungLHT** - Kiểm tra quyền sở hữu comment
            if (existingComment.getCustomerID() != customer.getCustomerID()) {
                response.getWriter().print(gson.toJson("You do not have permission to edit this comment"));
                response.setStatus(403);
                return;
            }

            Comment comment = new Comment();
            comment.setCommentID(commentId);
            comment.setContent(content);
            Date today = new Date();
            comment.setUpdatedAt(new Timestamp(today.getTime()));
            commentDAO.update(comment);
            response.getWriter().print(gson.toJson("Update comment successfully"));
        } catch (NumberFormatException e) {
            // **HungLHT** - Xử lý ngoại lệ khi commentId không phải số hợp lệ
            response.getWriter().print(gson.toJson("Invalid comment ID format"));
            response.setStatus(400);
        } catch (Exception e) {
            // **HungLHT** - Xử lý các ngoại lệ khác
            response.getWriter().print(gson.toJson("Error updating comment: " + e.getMessage()));
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
