/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.product;

import dao.AttributeDAO;
import dao.CategoryDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Attribute;
import model.Category;

/**
 *
 * @author TRUNG NHAN
 */
public class CategoryController extends HttpServlet {

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
            out.println("<title>Servlet CategoryController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CategoryController at " + request.getContextPath() + "</h1>");
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
        try {
            String action = request.getServletPath();
            response.getWriter().println("CategoryController is working!");
            switch (action) {
                case "/new-category":
                    showNewForm(request, response);
                    break;
                case "/insert-category":
                    addCategory(request, response);
                    break;
                case "/newedit-category":
                    neweditCategory(request, response);
                    break;
                case "/delete-category":
                    deleteCategory(request, response);
                    break;
                case "/edit-category":
                    editCategory(request, response);
                    break;
                case "/add-attribute":
                    attribute_showNewForm(request, response);
                    break;
                case "/insert-attribute":
                    addAttribute(request, response);
                    break;
                case "/newedit-attribute":
                    attribute_showEditForm(request, response);
                    break;
                case "/edit-attribute":
                    editAttribute(request, response);
                    break;
                case "/delete-attribute":
                    deleteAttribute(request, response);
                    break;
                default:
                    adminCategory(request, response);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException ex) {
            Logger.getLogger(CategoryController.class.getName()).log(Level.SEVERE, null, ex);
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
    CategoryDAO cdao = new CategoryDAO();
    AttributeDAO adao = new AttributeDAO();

    private void adminCategory(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response)
            throws IOException, ServletException {
        List<Category> cList = cdao.selectAllCategories();
        request.setAttribute("categories", cList);
        request.getRequestDispatcher("admin_category_crud.jsp").forward(request, response);
    }

    private void showNewForm(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response)
            throws IOException, ServletException {
        response.sendRedirect("admin_add_category.jsp");

    }

    private void addCategory(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String name = request.getParameter("name").trim();

        try {
            if (cdao.isCategoryExists(name)) {
                // Nếu danh mục đã tồn tại, chuyển hướng về trang thêm danh mục và hiển thị lỗi
                request.setAttribute("errorMessage", "Category name already exists!");
                request.getRequestDispatcher("/admin_add_category.jsp").forward(request, response);
            } else {
                cdao.insertCategory(new Category(name));
                response.sendRedirect(request.getContextPath() + "/CategoryController");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error occurred.");
        }
    }

    private void neweditCategory(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response)
            throws IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        try {
            Category category = cdao.selectCategory(id);
            request.setAttribute("category", category);
            request.getRequestDispatcher("admin_edit_category.jsp").forward(request, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void editCategory(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, SQLException {
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name").trim();

        try {
            if (cdao.isCategoryExistsForUpdate(id, name)) {
                // Nếu tên đã tồn tại, quay lại form chỉnh sửa với thông báo lỗi
                request.setAttribute("errorMessage", "Category name already exists!");
                request.setAttribute("category", new Category(id, name)); // Giữ lại thông tin cũ
                request.getRequestDispatcher("/admin_edit_category.jsp").forward(request, response);
            } else {
                cdao.updateCategory(new Category(id, name));
                response.sendRedirect(request.getContextPath() + "/CategoryController");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error occurred.");
        }
    }

    private void deleteCategory(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            try {
                cdao.deleteCategory(id);
                response.sendRedirect(request.getContextPath() + "/CategoryController");

            } catch (SQLException e) {
                e.printStackTrace();

                List<Category> cList = cdao.selectAllCategories();
                request.setAttribute("categories", cList);
                request.setAttribute("errorMessage", "Category cannot be deleted because there is related data!");

                request.getRequestDispatcher("/admin_category_crud.jsp").forward(request, response);
            }

        } catch (NumberFormatException e) {
            // Trường hợp id không hợp lệ
            e.printStackTrace();
            request.setAttribute("errorMessage", "Invalid category ID format!");
            request.getRequestDispatcher("/admin_category_crud.jsp").forward(request, response);
        }
    }

    private void attribute_showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        List<Attribute> attributes = adao.getAttributeByCategoryID(id);
        request.setAttribute("attributes", attributes);
        request.setAttribute("categoryID", id);
        request.getRequestDispatcher("admin_add_attribute.jsp").forward(request, response);
    }

    private void addAttribute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String attributeName = request.getParameter("attributeName").trim();
        int categoryID = Integer.parseInt(request.getParameter("categoryID"));

        try {
            if (adao.isAttributeInCategory(attributeName, categoryID)) {
                request.setAttribute("error", "duplicate");
            } else {
                int attributeID = adao.insertAttribute(attributeName);
                adao.linkAttributeToCategory(attributeID, categoryID);
                request.setAttribute("success", "added");
            }

            List<Attribute> attributes = adao.getAttributeByCategoryID(categoryID);
            request.setAttribute("attributes", attributes);
            request.setAttribute("categoryID", categoryID);
            request.getRequestDispatcher("admin_add_attribute.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "exception");
            List<Attribute> attributes = adao.getAttributeByCategoryID(categoryID);
            request.setAttribute("attributes", attributes);
            request.setAttribute("categoryID", categoryID);
            request.getRequestDispatcher("admin_add_attribute.jsp").forward(request, response);
        }
    }

    private void attribute_showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, SQLException {
        try {
            int attributeID = Integer.parseInt(request.getParameter("id"));
            int categoryID = Integer.parseInt(request.getParameter("categoryID"));

            Attribute attribute = adao.getAttributeByID(attributeID);
            request.setAttribute("attribute", attribute);
            request.setAttribute("categoryID", categoryID);

            request.getRequestDispatcher("admin_edit_attribute.jsp").forward(request, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void editAttribute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int attributeID = Integer.parseInt(request.getParameter("attributeID"));
            int categoryID = Integer.parseInt(request.getParameter("categoryID"));
            String attributeName = request.getParameter("attributeName");

            // Kiểm tra trùng tên trong cùng category
            boolean isDuplicate = adao.isDuplicateNameInCategory(attributeName, attributeID, categoryID);
            if (isDuplicate) {
                request.setAttribute("error", "duplicate");
                request.setAttribute("categoryID", categoryID);
                request.setAttribute("attribute", new Attribute(attributeID, attributeName));
                request.getRequestDispatcher("admin_edit_attribute.jsp").forward(request, response);
                return;
            }

            // Cập nhật thuộc tính
            adao.updateAttribute(attributeID, attributeName);

            // Chuyển hướng về trang admin_add_attribute.jsp sau khi thành công
            List<Attribute> attributes = adao.getAttributeByCategoryID(categoryID);
            request.setAttribute("attributes", attributes);
            request.setAttribute("categoryID", categoryID);
            request.setAttribute("success", "updated");

            // Chuyển hướng về trang add attribute
            request.getRequestDispatcher("admin_add_attribute.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while processing your request.");
        }
    }

    private void deleteAttribute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String attrIDRaw = request.getParameter("id");
        String catIDRaw = request.getParameter("categoryID");
        try {
            int attributeID = Integer.parseInt(attrIDRaw);
            int categoryID = Integer.parseInt(catIDRaw);
            boolean deletedLink = adao.deleteCategoryAttributeByAttributeID(attributeID);
            boolean deleted = false;
            if (deletedLink) {
                deleted = adao.deleteAttribute(attributeID);
            }
            List<Attribute> attributes = adao.getAttributeByCategoryID(categoryID);
            request.setAttribute("attributes", attributes);
            request.setAttribute("categoryID", categoryID);

            if (!deletedLink) {
                request.setAttribute("error", "link_not_found_or_failed");
            } else if (!deleted) {
                request.setAttribute("error", "foreign_key_constraint");
            } else {
                request.setAttribute("success", "deleted");
            }

            request.getRequestDispatcher("admin_add_attribute.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "exception");
            request.getRequestDispatcher("admin_add_attribute.jsp").forward(request, response);
        }
    }

}



