/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.cartController;

import dao.AddressDAO;
import dao.CartDAO;
import dao.CustomerDAO;
import dao.OrderDAO;
import dao.PaymentDAO;
import dao.ProductDAO;
import dao.VoucherDAO;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import model.Address;
import model.Cart;
import model.Customer;

import model.OrderDetail;
import model.OrderInfo;
import model.PaymentMethod;
import model.Product;
import model.Voucher;

/**
 *
 * @author HP
 */
public class Checkout extends HttpServlet {

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
            out.println("<title>Servlet Checkout</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Checkout at " + request.getContextPath() + "</h1>");
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

        OrderDAO orderDAO = new OrderDAO();
        CartDAO cartDAO = new CartDAO();
        PaymentDAO paymentDAO = new PaymentDAO();
        AddressDAO addressDAO = new AddressDAO();
        CustomerDAO customerDAO = new CustomerDAO();
        ProductDAO productDAO = new ProductDAO();
        // Lấy danh sách sản phẩm trong giỏ hàng
        List<Cart> cartItems = cartDAO.getcart(customer.getCustomerID());

        //List<DeliveryOption> deliveryOptions = deliveryDAO.getAllDeliveryOptions();
        List<PaymentMethod> paymentMethods = paymentDAO.getAllPaymentMethods();

        Address defaultAddress = addressDAO.getDefaultAddress(customer.getCustomerID());

        double total = 0;
        for (Cart item : cartItems) {
            total += item.getQuantity() * item.getProduct().getPrice();
        }

        // Gửi dữ liệu đến trang JSP
        request.setAttribute("cartItems", cartItems);
        //request.setAttribute("deliveryOptions", deliveryOptions);
        request.setAttribute("paymentMethods", paymentMethods);
        request.setAttribute("defaultAddress", defaultAddress);
        request.setAttribute("customer", customer);
        request.setAttribute("total", total);

        RequestDispatcher dispatcher = request.getRequestDispatcher("checkout.jsp");
        dispatcher.forward(request, response);
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
        HttpSession session = request.getSession();
        Customer customer = (Customer) session.getAttribute("customer");

        if (customer == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String action = request.getParameter("action"); // xác định action là apply voucher hay checkout
        String voucherCode = request.getParameter("voucherCode");
        request.setAttribute("voucherCode", voucherCode); // giữ lại khi reload

        CartDAO cartDAO = new CartDAO();
        OrderDAO orderDAO = new OrderDAO();
        ProductDAO productDAO = new ProductDAO();
        PaymentDAO paymentDAO = new PaymentDAO();
        AddressDAO addressDAO = new AddressDAO();

        List<Cart> cartItems = cartDAO.getcart(customer.getCustomerID());

        if (cartItems == null || cartItems.isEmpty()) {
            request.setAttribute("errorMessage", "Your cart is empty!");
            request.getRequestDispatcher("cart.jsp").forward(request, response);
            return;
        }
        double total = calculateTotal(cartItems);
        double discount = 0.0;
        if ("applyVoucher".equals(action)) {

            if (voucherCode != null && !voucherCode.trim().isEmpty()) {
                VoucherDAO voucherDAO = new VoucherDAO();
                Voucher voucher = voucherDAO.getVoucherByTitle(voucherCode.trim());
                if (voucher != null) {
                    discount = voucher.getDiscount() / 100;

                    double discountedTotal = total - (total * discount);
                    request.setAttribute("discountedTotal", discountedTotal);
                    request.setAttribute("successMessage", "Discount code applied successfully!");
                } else {
                    request.setAttribute("errorMessage", "Voucher is invalid or expired!");
                }
            }

            // Trả về lại trang checkout với dữ liệu cần thiết
            request.setAttribute("cartItems", cartItems);
            request.setAttribute("paymentMethods", paymentDAO.getAllPaymentMethods());
            request.setAttribute("defaultAddress", addressDAO.getDefaultAddress(customer.getCustomerID()));
            request.setAttribute("customer", customer);
            request.setAttribute("total", total); // Tổng gốc
            request.getRequestDispatcher("checkout.jsp").forward(request, response);
            return;
        }

        // Bắt đầu xử lý đặt hàng
        try {
            int paymentMethodID = parseIntSafe(request.getParameter("paymentMethodID"));
            String deliveryAddress = request.getParameter("deliveryAddress");

            if (paymentMethodID == -1 || deliveryAddress == null || deliveryAddress.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Please enter all the required shipping information!");
                request.getRequestDispatcher("checkout.jsp").forward(request, response);
                return;
            }

            double finalTotal = (request.getAttribute("discountedTotal") != null)
                    ? (double) request.getAttribute("discountedTotal")
                    : total;

            // Debugging the final total before placing the order
            // Tạo order
            OrderInfo order = new OrderInfo();
            order.setCustomerID(customer.getCustomerID());
            order.setOrderStatus(1);
            order.setOrderDate(new java.sql.Date(System.currentTimeMillis()));
            order.setManagerID(1);
            order.setPaymentMethodID(paymentMethodID);
            order.setTotalPrice(finalTotal);
            order.setDeliveryAddress(deliveryAddress);
            order.setFullName(customer.getFullName());
            order.setPhone(customer.getPhone());

            int orderID = orderDAO.createOrder(order);

            if (orderID == -1) {
                request.setAttribute("errorMessage", "Error creating order. Please try again!");
                request.getRequestDispatcher("checkout.jsp").forward(request, response);
                return;
            }

            boolean added = orderDAO.addOrderItem(orderID, customer.getCustomerID(), discount);

            if (!added) {
                request.setAttribute("errorMessage", "Cannot add the product to the order!");
                request.getRequestDispatcher("checkout.jsp").forward(request, response);
                return;
            }

            // Cập nhật tồn kho
            for (Cart item : cartItems) {
                boolean updated = productDAO.updateStock(item.getProduct().getProductID(), item.getQuantity());
                if (!updated) {
                    request.setAttribute("errorMessage", "Failed to update stock for product ID: " + item.getProduct().getProductID());
                    request.getRequestDispatcher("checkout.jsp").forward(request, response);
                    return;
                }
            }

            cartDAO.clearCart(customer.getCustomerID());
            session.setAttribute("successMessage", "Order successful!   ");
            response.sendRedirect("listOrders");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An unknown error has occurred!");
            request.getRequestDispatcher("checkout.jsp").forward(request, response);
        }
    }

    private double calculateTotal(List<Cart> cartItems) {
        double total = 0;
        for (Cart item : cartItems) {
            total += item.getQuantity() * item.getProduct().getPrice();
        }
        return total; // Trả về tổng giá trị dưới dạng double
    }

    private int parseIntSafe(String param) {
        try {
            return (param != null && !param.trim().isEmpty()) ? Integer.parseInt(param) : -1;
        } catch (NumberFormatException e) {
            return -1;
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
