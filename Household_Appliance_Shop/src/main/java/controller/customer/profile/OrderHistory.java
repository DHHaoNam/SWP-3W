/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.customer.profile;

import dao.OrderHistoryDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import model.CancelReason;
import model.Customer;
import model.OrderInfo;

/**
 *
 * @author Nam
 */
public class OrderHistory extends HttpServlet {

    private final OrderHistoryDAO orderDAO = new OrderHistoryDAO();

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
            out.println("<title>Servlet OrderHistory</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet OrderHistory at " + request.getContextPath() + "</h1>");
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
        HttpSession session = request.getSession();
        Customer customer = (Customer) session.getAttribute("customer");

        if (customer == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        String statusParam = request.getParameter("status");
        int orderStatus = 1; // Mặc định "Chờ xác nhận"
        if (statusParam != null) {
            try {
                orderStatus = Integer.parseInt(statusParam);
            } catch (NumberFormatException e) {
                orderStatus = 1;
            }
        }
        int customerID = customer.getCustomerID();
        List<OrderInfo> orders = orderDAO.getOrdersByStatus(customerID, orderStatus);

        // Lấy lý do hủy cho các đơn hàng
        for (OrderInfo order : orders) {
            CancelReason cancelReason = orderDAO.getCancelReasonByOrderID(order.getOrderID());
            request.setAttribute("cancelReason_" + order.getOrderID(), cancelReason);
        }

        request.setAttribute("orders", orders);
        request.setAttribute("currentStatus", orderStatus);
        request.getRequestDispatcher("orderhistorymanagement.jsp").forward(request, response);
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
        processRequest(request, response);
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
