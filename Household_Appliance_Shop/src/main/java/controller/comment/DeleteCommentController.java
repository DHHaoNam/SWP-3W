/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.comment;

import com.google.gson.Gson;
import dao.CommentDAO;
import java.io.IOException;
import java.io.PrintWriter;
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
public class DeleteCommentController extends HttpServlet {

    private CommentDAO commentDAO;

    public DeleteCommentController() {
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
            out.println("<title>Servlet DeleteCommentController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet DeleteCommentController at " + request.getContextPath() + "</h1>");
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
        String raw_commentId = request.getParameter("commentId");
        Customer customer = (Customer) request.getSession().getAttribute("customer");
        if (customer == null) {
            response.getWriter().print(gson.toJson("You need to login to delete comment"));
            response.setStatus(400);
            return;
        }
        Comment comment = new Comment();
        comment.setCommentID(Integer.parseInt(raw_commentId));
        comment.setDeleted(true);
        commentDAO.updateDeleted(comment);
        response.getWriter().print(gson.toJson("Delete comment successfully"));
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

        try {
            // Lấy commentId từ request
            String raw_commentId = request.getParameter("commentId");

            // Kiểm tra commentId có tồn tại không
            if (raw_commentId == null || raw_commentId.trim().isEmpty()) {
                response.getWriter().print(gson.toJson("Comment ID is required"));
                response.setStatus(400);
                return;
            }

            // Kiểm tra người dùng đã đăng nhập chưa
            Customer customer = (Customer) request.getSession().getAttribute("customer");
            if (customer == null) {
                response.getWriter().print(gson.toJson("You need to login to delete comment"));
                response.setStatus(401);
                return;
            }

            int commentId = Integer.parseInt(raw_commentId);

            // Kiểm tra comment có tồn tại không và người dùng có quyền xóa không
            Comment existingComment = commentDAO.getCommentById(commentId);
            if (existingComment == null) {
                response.getWriter().print(gson.toJson("Comment not found"));
                response.setStatus(404);
                return;
            }

            // Kiểm tra quyền sở hữu comment
            if (existingComment.getCustomer().getCustomerID() != customer.getCustomerID()) {
                response.getWriter().print(gson.toJson("You don't have permission to delete this comment"));
                response.setStatus(403);
                return;
            }

            // Thực hiện xóa comment
            Comment comment = new Comment();
            comment.setCommentID(commentId);
            comment.setDeleted(true);
            try {
                commentDAO.updateDeleted(comment);
                response.getWriter().print(gson.toJson("Delete comment successfully"));
                response.setStatus(200);
            } catch (Exception e) {
                response.getWriter().print(gson.toJson("Failed to delete comment"));
                response.setStatus(500);
                e.printStackTrace();
            }
        } catch (NumberFormatException e) {
            response.getWriter().print(gson.toJson("Invalid comment ID format"));
            response.setStatus(400);
        } catch (Exception e) {
            response.getWriter().print(gson.toJson("Error: " + e.getMessage()));
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
