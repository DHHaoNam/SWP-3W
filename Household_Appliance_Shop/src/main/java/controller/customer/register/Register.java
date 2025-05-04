/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.customer.register;

import controller.customer.profile.EmailSender;
import dao.CustomerDAO;
import jakarta.mail.MessagingException;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Customer;

/**
 *
 * @author Nam
 */
public class Register extends HttpServlet {

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
            out.println("<title>Servlet Register</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Register at " + request.getContextPath() + "</h1>");
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
        request.getRequestDispatcher("register.jsp").forward(request, response);
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();

        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String userName = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        CustomerDAO customerDAO = new CustomerDAO();
        boolean hasError = false;

        try {
            if (!password.equals(confirmPassword)) {
                session.setAttribute("errorConfirm", "The re-entered password does not match!");
                hasError = true;
            }

            if (customerDAO.checkUserExists(userName)) {
                session.setAttribute("errorName", "Username already exists!");
                hasError = true;
            }

            if (customerDAO.checkEmailExists(email)) {
                session.setAttribute("errorEmail", "Email already exists!");
                hasError = true;
            }

            String passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$";
            if (!password.matches(passwordPattern)) {
                session.setAttribute("errorPassword", "The password must contain at least one letter, one number, one special character, and be at least 8 characters long!");
                hasError = true;
            }

            if (!phone.matches("\\d+")) {
                session.setAttribute("errorPhoneFormat", "The phone number can only contain digits!");
                hasError = true;
            }

            if (customerDAO.checkPhoneExists(phone)) {
                session.setAttribute("errorPhone", "The phone number is already in use!");
                hasError = true;
            }

            if (hasError) {
                response.sendRedirect("register.jsp");
                return;
            }

            // Nếu không lỗi, gửi mã xác thực OTP
            String code = String.valueOf((int) (Math.random() * 900000) + 100000); // 6 chữ số

            try {
                EmailSender.sendEmail(email, "Account Verification Code", "Your verification code is: " + code);

                // Lưu thông tin vào session để xử lý sau khi xác thực
                Customer customer = new Customer();
                customer.setFullName(fullName);
                customer.setEmail(email);
                customer.setPhone(phone);
                customer.setUserName(userName);
                customer.setPassword(password); // nên mã hóa sau

                session.setAttribute("tempCustomer", customer);
                session.setAttribute("verificationCode", code);
                session.setAttribute("emailToVerify", email);

                request.setAttribute("message", "A verification code has been sent to your email.");
                request.getRequestDispatcher("verify-email.jsp").forward(request, response);
            } catch (MessagingException ex) {
                Logger.getLogger(Register.class.getName()).log(Level.SEVERE, null, ex);
                session.setAttribute("errorGeneral", "Lỗi gửi email xác thực. Vui lòng thử lại.");
                response.sendRedirect("register.jsp");
            }

        } catch (SQLException e) {
            throw new ServletException("Lỗi database: " + e.getMessage());
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
