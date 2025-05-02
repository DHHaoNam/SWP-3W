/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package conntroller.order;

import dao.CustomerDAO_temp;
import dao.OrderDAO;
import dao.ProductDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;
import model.Customer;
import model.OrderDetail;
import model.OrderInfo;
import model.PaymentMethod;
import model.Product;

/**
 *
 * @author ADMIN
 */
public class OrderController extends HttpServlet {

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
            out.println("<title>Servlet OrderController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet OrderController at " + request.getContextPath() + "</h1>");
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
        String action = request.getServletPath();

        try {
            switch (action) {

                case "/updateOrderStatus":
                    updateOrderStatus(request, response);
                    break;
                case "/viewOrderDetails":
                    viewOrderDetails(request, response);
                    break;
                case "/viewOrderDetails_customer":
                    viewOrderDetails_customer(request, response);
                    break;
                case "/searchByPhone":
                    searchOrdersByPhone(request, response);
                    break;
                case "/deleteOrder":
                    deleteOrder(request, response);
                    break;
                default:
                    listAllOrders(request, response);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    // Hiển thị danh sách tất cả đơn hàng
    private void listAllOrders(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        CustomerDAO_temp cusDAO = new CustomerDAO_temp();
        OrderDAO orderDAO = new OrderDAO();

        List<OrderInfo> listOrders;
        List<PaymentMethod> payment;
        List<Customer> customer;

        String statusParam = request.getParameter("orderStatus");
        Integer status = null;

        if (statusParam != null && !statusParam.isEmpty()) {
            status = Integer.parseInt(statusParam);
        }

        if (status == null) {
            listOrders = orderDAO.selectAllOrder(); // Lấy tất cả đơn hàng
        } else {
            listOrders = orderDAO.getOrdersByStatus(status); // Lấy đơn hàng theo trạng thái
        }

        customer = cusDAO.getAllCustomers();
        payment = orderDAO.getAllPaymentMethods();

        request.setAttribute("payment", payment);
        request.setAttribute("customer", customer);

        request.setAttribute("listOrders", listOrders);
        request.setAttribute("selectedStatus", status);
        request.getRequestDispatcher("admin_orders.jsp").forward(request, response);
    }

    private void updateOrderStatus(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        OrderDAO orderDAO = new OrderDAO();
        try {
            int orderId = Integer.parseInt(request.getParameter("id"));
            int newStatus = Integer.parseInt(request.getParameter("status"));

            if (newStatus < 1 || newStatus > 5) {
                throw new IllegalArgumentException("Invalid status");
            }

            // Cập nhật trạng thái đơn hàng
            boolean isUpdated = orderDAO.updateOrderStatus(orderId, newStatus);

            if (isUpdated) {
                response.sendRedirect("listAdminOrders");
            } else {
                request.setAttribute("error", "Failed to update order status");
                request.getRequestDispatcher("listAdminOrders.jsp").forward(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid order ID or status");
            e.printStackTrace();
            request.getRequestDispatcher("listAdminOrders.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "An unexpected error occurred");
            e.printStackTrace();
            request.getRequestDispatcher("listAdminOrders.jsp").forward(request, response);
        }

    }

    // Hiển thị chi tiết đơn hàng
    private void viewOrderDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int orderID = Integer.parseInt(request.getParameter("id"));

        OrderDAO ordersDAO = new OrderDAO();
        CustomerDAO_temp customerDAO = new CustomerDAO_temp();
        ProductDAO productDAO = new ProductDAO();

        OrderInfo order = ordersDAO.selectOrder(orderID);
        Customer customer = customerDAO.selectCustomer(order.getCustomerID());

        List<Product> product = productDAO.selectAllProducts();
        List<OrderDetail> orderDetails = ordersDAO.getOrderDetailsByOrderID(orderID);

//        int userRole = (int) request.getSession().getAttribute("managerRole");
        request.setAttribute("orderDetails", orderDetails);
        request.setAttribute("product", product);
        request.setAttribute("OrderInfo", order);
        request.setAttribute("Customer", customer);

        request.getRequestDispatcher("admin_view_orders.jsp").forward(request, response);

    }

    private void viewOrderDetails_customer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int orderID = Integer.parseInt(request.getParameter("id"));

        OrderDAO ordersDAO = new OrderDAO();
        CustomerDAO_temp customerDAO = new CustomerDAO_temp();
        ProductDAO productDAO = new ProductDAO();

        OrderInfo order = ordersDAO.selectOrder(orderID);
        Customer customer = customerDAO.selectCustomer(order.getCustomerID());

        List<Product> product = productDAO.selectAllProducts();
        List<OrderDetail> orderDetails = ordersDAO.getOrderDetailsByOrderID(orderID);

//        int userRole = (int) request.getSession().getAttribute("managerRole");
        request.setAttribute("orderDetails", orderDetails);
        request.setAttribute("product", product);
        request.setAttribute("OrderInfo", order);
        request.setAttribute("Customer", customer);

        request.getRequestDispatcher("customer_view_orders.jsp").forward(request, response);

    }

    // Tìm kiếm theo số điện thoại
    private void searchOrdersByPhone(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String phoneSearch = request.getParameter("phone");
        OrderDAO ordersDAO = new OrderDAO();
        List<OrderInfo> orders = ordersDAO.searchByPhone(phoneSearch);

        // Giả sử bạn có các DAO để lấy danh sách Customer, Delivery và Payment
        CustomerDAO_temp customerDAO = new CustomerDAO_temp();
        List<Customer> customers = customerDAO.getAllCustomers(); // hoặc lấy khách hàng liên quan
        List<PaymentMethod> payments = ordersDAO.getAllPaymentMethods();

        request.setAttribute("listOrders", orders);
        request.setAttribute("customer", customers);  // Lưu ý: attribute "customer" dùng trong JSP
        request.setAttribute("payment", payments);

        // Nếu không tìm thấy đơn hàng nào, set thông báo lỗi
        if (orders.isEmpty()) {
            request.setAttribute("error", "No orders found for this phone number.");
        }

        response.sendRedirect("listAdminOrders");
    }

    // Xóa order
    private void deleteOrder(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        int orderID = Integer.parseInt(request.getParameter("id"));
        OrderDAO ordersDAO = new OrderDAO();
        boolean isDeleted = ordersDAO.deleteOrder(orderID);

        if (isDeleted) {
            response.sendRedirect("listAdminOrders"); // Đảm bảo listAdminOrders gọi lại listAllOrders()
        } else {
            response.sendRedirect("listAdminOrders?error=Order deletion failed");
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
