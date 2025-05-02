/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.customer.profile;

import dao.OrderDAO;
import dao.OrderHistoryDAO;
import dao.ProductDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.OrderDetail;

/**
 *
 * @author Nam
 */
public class CancelOrder extends HttpServlet {

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
            out.println("<title>Servlet CancelOrder</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CancelOrder at " + request.getContextPath() + "</h1>");
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
        String orderIdStr = request.getParameter("orderId");
        String reason = request.getParameter("reason");
        try {
            int orderId = Integer.parseInt(orderIdStr);
            OrderHistoryDAO orderHistoryDAO = new OrderHistoryDAO();
            OrderDAO orderDAO = new OrderDAO();
            ProductDAO productDAO = new ProductDAO();

            // Lấy danh sách sản phẩm trong đơn hàng
            List<OrderDetail> orderDetails = orderDAO.getOrderDetailsByOrderId(orderId);

            // Hoàn trả số lượng sản phẩm vào kho
            for (OrderDetail detail : orderDetails) {
                productDAO.incrementProductQuantity(detail.getProductID(), detail.getQuantity());
            }

            // Hủy đơn hàng và kiểm tra kết quả
            boolean isCancelled = orderHistoryDAO.cancelOrder(orderId, reason); // Lý do có thể không cần ở đây nếu chỉ hủy

            if (isCancelled) {
                response.sendRedirect("listOrders?status=5"); // Chuyển hướng đến danh sách đơn hàng đã hủy
            } else {
                response.sendRedirect("listOrders?status=1"); // Chuyển hướng về danh sách đơn hàng đang chờ xử lý
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("listOrders?error=invalid_id"); // Trường hợp lỗi ID không hợp lệ
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
        String orderIdStr = request.getParameter("orderId");
        String reason = request.getParameter("reason");

        if (orderIdStr != null && reason != null && !reason.trim().isEmpty()) {
            int orderId = Integer.parseInt(orderIdStr);

            // Gọi DAO cập nhật trạng thái hủy và lưu lý do
            OrderHistoryDAO orderHistoryDAO = new OrderHistoryDAO();
            boolean isCancelled = orderHistoryDAO.cancelOrder(orderId, reason); // Lý do hủy

            if (isCancelled) {
                // Hoàn trả số lượng sản phẩm vào kho
                OrderDAO orderDAO = new OrderDAO();
                ProductDAO productDAO = new ProductDAO();
                List<OrderDetail> orderDetails = orderDAO.getOrderDetailsByOrderId(orderId);

                for (OrderDetail detail : orderDetails) {
                    productDAO.incrementProductQuantity(detail.getProductID(), detail.getQuantity());
                }

                response.sendRedirect("listOrders?status=5"); // Chuyển sang trang đơn hàng đã hủy
            } else {
                response.getWriter().println("Không thể hủy đơn hàng. Vui lòng thử lại.");
            }
        } else {
            response.getWriter().println("Thiếu thông tin orderId hoặc lý do hủy.");
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
