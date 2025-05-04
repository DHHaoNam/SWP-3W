/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.voucher;

import dao.CategoryDAO;
import dao.VoucherDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.Category;
import model.Voucher;
import java.sql.Timestamp;

/**
 *
 * @author D
 */
public class VoucherController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        VoucherDAO dao = new VoucherDAO();

        String action = request.getParameter("action");

        if (action != null && action.equals("edit")) {
            showEditForm(request, response);
        } else if (action != null && action.equals("updateVoucher")) {
            updateVoucher(request, response);
        } else if (action != null && action.equals("add")) {
            showAddForm(request, response);
        } else if (action != null && action.equals("addVoucher")) {
            addVoucher(request, response);
        } else if (action != null && action.equals("delete")) {
            deleteVoucher(request, response);
        } else {
            // Lấy tham số từ request
            String voucherID = request.getParameter("voucherID");
            String status = request.getParameter("status");
            String categoryID = request.getParameter("categoryID");
            String sort = request.getParameter("sort");

            // Lấy danh sách voucher (có lọc nếu có tham số)
            List<Voucher> vList = dao.filterVoucher(voucherID, status, categoryID, sort);

            // Lấy danh sách category để hiển thị trong dropdown
            List<Category> categoryList = dao.getAllCategory();

            // Đặt danh sách vào request attribute để hiển thị trên JSP
            request.setAttribute("listV", vList);
            request.setAttribute("listC", categoryList);

            // Chuyển hướng đến JSP
            request.getRequestDispatcher("voucher-management.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Servlet for managing vouchers";
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        VoucherDAO dao = new VoucherDAO();
        int voucherID = Integer.parseInt(request.getParameter("id"));
        Voucher voucher = dao.getVoucherById(voucherID);
        List<Category> categories = dao.getAllCategory();

        request.setAttribute("voucher", voucher);
        request.setAttribute("categories", categories);
        request.getRequestDispatcher("admin_edit_voucher.jsp").forward(request, response);
    }

    private void updateVoucher(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        VoucherDAO dao = new VoucherDAO();

        int voucherID = Integer.parseInt(request.getParameter("voucherID"));
        String title = request.getParameter("title");
        double discount = Double.parseDouble(request.getParameter("discount"));
        int categoryID = Integer.parseInt(request.getParameter("categoryID"));
        String status = request.getParameter("status");

        // Lấy startTime và endTime dạng String từ form
        String startTimeStr = request.getParameter("startTime").replace("T", " ") + ":00";
        String endTimeStr = request.getParameter("endTime").replace("T", " ") + ":00";

        try {
            Timestamp startTime = Timestamp.valueOf(startTimeStr);
            Timestamp endTime = Timestamp.valueOf(endTimeStr);

            // ✅ Bắt lỗi logic: startTime phải < endTime
            if (!startTime.before(endTime)) {
                Voucher voucher = dao.getVoucherById(voucherID);
                List<Category> categories = dao.getAllCategory();

                request.setAttribute("error", "Start time must be before end time.");
                request.setAttribute("voucher", voucher);
                request.setAttribute("categories", categories);
                request.getRequestDispatcher("admin_edit_voucher.jsp").forward(request, response);
                return;
            }

            // Nếu hợp lệ thì cập nhật
            dao.updateVoucher(voucherID, title, discount, categoryID, status, startTime, endTime);
            response.sendRedirect("VoucherController");

        } catch (IllegalArgumentException e) {
            // Bắt lỗi sai định dạng ngày tháng (hiếm khi xảy ra nếu form đúng)
            Voucher voucher = dao.getVoucherById(voucherID);
            List<Category> categories = dao.getAllCategory();

            request.setAttribute("error", "Invalid date format.");
            request.setAttribute("voucher", voucher);
            request.setAttribute("categories", categories);
            request.getRequestDispatcher("admin_edit_voucher.jsp").forward(request, response);
        }
    }

    private void showAddForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        VoucherDAO dao = new VoucherDAO();
        List<Category> categories = dao.getAllCategory();

        request.setAttribute("categories", categories);
        request.getRequestDispatcher("admin_add_voucher.jsp").forward(request, response);
    }

    private void addVoucher(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        VoucherDAO dao = new VoucherDAO();

        // Lấy tham số từ form
        String title = request.getParameter("title");
        double discount = Double.parseDouble(request.getParameter("discount"));
        int categoryID = Integer.parseInt(request.getParameter("categoryID"));
        String status = request.getParameter("status");

        // Lấy startTime và endTime dạng String từ form
        String startTimeStr = request.getParameter("startTime").replace("T", " ") + ":00";
        String endTimeStr = request.getParameter("endTime").replace("T", " ") + ":00";

        // Chuyển String sang Timestamp
        Timestamp startTime = Timestamp.valueOf(startTimeStr);
        Timestamp endTime = Timestamp.valueOf(endTimeStr);

        // ✅ Kiểm tra điều kiện: startTime phải < endTime
        if (startTime.after(endTime) || startTime.equals(endTime)) {
            request.setAttribute("error", "Start time must be before end time.");
            request.setAttribute("categories", new CategoryDAO().selectAllCategories());

            // Giữ lại dữ liệu đã nhập để tránh người dùng phải nhập lại
            request.setAttribute("title", title);
            request.setAttribute("discount", discount);
            request.setAttribute("categoryID", categoryID);
            request.setAttribute("status", status);
            request.setAttribute("startTime", request.getParameter("startTime"));
            request.setAttribute("endTime", request.getParameter("endTime"));

            request.getRequestDispatcher("admin_add_voucher.jsp").forward(request, response);
            return;
        }

        // Nếu không có lỗi, thêm voucher mới
        dao.addVoucher(title, discount, categoryID, status, startTime, endTime);

        // Chuyển hướng về danh sách voucher
        response.sendRedirect("VoucherController");
    }

    private void deleteVoucher(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Sửa lại tham số từ "id" thành "voucherID"
            int voucherID = Integer.parseInt(request.getParameter("voucherID"));
            VoucherDAO dao = new VoucherDAO();
            dao.deleteVoucher(voucherID);
        } catch (NumberFormatException e) {
            System.out.println("Invalid voucher ID: " + e.getMessage());
        }

        response.sendRedirect("VoucherController");
    }

}
