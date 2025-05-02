/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.product;

import dao.CustomerDAO_temp;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import model.Customer;

/**
 *
 * @author TRUNG NHAN
 */
public class CustomerController_temp extends HttpServlet {

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
            out.println("<title>Servlet CustomerController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CustomerController at " + request.getContextPath() + "</h1>");
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

        switch (action) {
            case "/admin-edit-account":
                editForm(request, response);
                break;
            case "/admin-delete-account":
                deleteCustomer(request, response);
                break;
            case "/admin-toggle-status":
                toggleStatus(request, response);
                break;
            case "/admin-update-account":
                updateCustomer(request, response);
                break;
            case "/admin-view-account":
                viewDetail(request, response);
                break;
            case "/admin-add-account":
                addCustomer(request, response);
                break;
            case "/admin-search-account":
                searchCustomer(request, response);
                break;
            default:
                adminCustomer(request, response);
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
    CustomerDAO_temp cusdao = new CustomerDAO_temp();

    private void adminCustomer(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response)
            throws IOException, ServletException {
        try {
            List<Customer> customers = cusdao.getAllCustomers();
            request.setAttribute("customers", customers);
            request.getRequestDispatcher("admin_account_crud.jsp").forward(request, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void editForm(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response)
            throws IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        Customer customer = cusdao.getCustomerById(id);
        request.setAttribute("customer", customer);
        request.getRequestDispatcher("admin_edit_account.jsp").forward(request, response);
    }

    private void updateCustomer(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response)
            throws IOException, ServletException {
        try {
            int id = Integer.parseInt(request.getParameter("id").trim());
            String name = request.getParameter("name").trim();
            String username = request.getParameter("username").trim();
            String password_temp = request.getParameter("password").trim();
            String password = cusdao.hashPasswordMD5(password_temp);
            String email = request.getParameter("email").trim();
            String phone = request.getParameter("phone_number").trim();
            String registrationDateStr = request.getParameter("registrationDate").trim();
            boolean status = request.getParameter("status").trim().equals("1");

            // Chuyển đổi ngày đăng ký
            LocalDate registrationDate = LocalDate.parse(registrationDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            // Kiểm tra nếu username đã tồn tại (trừ khách hàng đang cập nhật)
            if (cusdao.isUsernameTaken(username, id)) {
                System.out.println("DEBUG: Username '" + username + "' is already taken!");
                request.setAttribute("error", "Username '" + username + "' is already taken. Please choose another one.");
                request.getRequestDispatcher("admin_edit_account.jsp").forward(request, response);
                return;
            }

            // Tiến hành cập nhật thông tin khách hàng
            boolean success = cusdao.updateCustomer(new Customer(id, name, email, phone, username, password, registrationDate, status));

            if (success) {
                request.setAttribute("message", "Account updated successfully!");
            } else {
                request.setAttribute("error", "Failed to update account.");
            }

            request.getRequestDispatcher("admin_edit_account.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "An error occurred: " + e.getMessage());
            request.getRequestDispatcher("admin_edit_account.jsp").forward(request, response);
        }
    }

    private void viewDetail(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response)
            throws IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        try {
            Customer customer = cusdao.getCustomerById(id);
            request.setAttribute("customer", customer);
            request.getRequestDispatcher("admin_view_account.jsp").forward(request, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addCustomer(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response)
            throws IOException, ServletException {
        String name = request.getParameter("name");
        String username = request.getParameter("username");
        String password_temp = request.getParameter("password");
        String password = cusdao.hashPasswordMD5(password_temp);
        String email = request.getParameter("email");
        String phone = request.getParameter("phone_number");
        String registrationDateStr = request.getParameter("registrationDate"); // Lấy ngày dưới dạng String
        int status = 1; // Mặc định là Active

        // Kiểm tra số điện thoại
        if (!phone.matches("\\d{10}")) { // Chỉ nhận đúng 10 chữ số
            request.setAttribute("error", "Phone number must be exactly 10 digits.");
            request.getRequestDispatcher("admin_add_account.jsp").forward(request, response);
            return;
        }

        try {
            // Chuyển đổi từ String sang LocalDate
            LocalDate registrationDate = LocalDate.parse(registrationDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            boolean success = cusdao.insertCustomer(new Customer(name, email, phone, username, password, registrationDate, true));

            if (success) {
                request.setAttribute("message", "Account added successfully!");
            } else {
                request.setAttribute("error", "Failed to add account.");
            }

            // Load lại trang add account
            request.getRequestDispatcher("admin_add_account.jsp").forward(request, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteCustomer(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response)
            throws IOException, ServletException {

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            cusdao.deleteCustomer(id);
            response.sendRedirect("CustomerController_temp");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void searchCustomer(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response)
            throws IOException, ServletException {
        String keyword = request.getParameter("search").trim();

        try {
            if (keyword == null || keyword.trim().isEmpty()) {
                request.setAttribute("error", "Vui long nhap tu khoa tim kiem");
                request.getRequestDispatcher("admin_account_crud.jsp").forward(request, response);
                return;
            }
            List<Customer> list = cusdao.searchCustomerByPhone(keyword);

            if (list.isEmpty()) {
                request.setAttribute("error", "Khong tim thay khach hang hop le");
            } else {
                request.setAttribute("customers", list);
            }
            request.getRequestDispatcher("admin_account_crud.jsp").forward(request, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void toggleStatus(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        Customer customer = cusdao.getCustomerById(id);

        if (customer != null) {
            boolean newStatus = !customer.getStatus(); // Đảo trạng thái hiện tại
            customer.setStatus(newStatus);
            cusdao.updateCustomerStatus(id, newStatus); // Cập nhật trạng thái trong DB
        }

        response.sendRedirect("CustomerController_temp"); // Load lại trang danh sách
    }

}



