/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import DB.DBContext;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.Manager;

/**
 *
 * @author Nam
 */
public class ManagerDAO {

    private DBContext dbcontext = new DBContext();

    public String hashPasswordMD5(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] digest = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Lỗi mã hóa MD5", e);
        }
    }

    public Manager login(String userName, String password) throws SQLException {
        String query = "SELECT * FROM Manager WHERE userName = ? AND password = ?";
        try ( Connection conn = dbcontext.getConnection();  PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, userName);
            stmt.setString(2, hashPasswordMD5(password));
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Manager(
                        rs.getInt("managerID"),
                        rs.getString("fullName"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("userName"),
                        rs.getString("password"),
                        rs.getDate("registrationDate").toLocalDate(),
                        rs.getBoolean("status"),
                        rs.getInt("roleID")
                );
            }
        }
        return null;
    }

    public boolean changePassword(int managerID, String oldPassword, String newPassword) throws SQLException {
        String sql = "SELECT password FROM Manager WHERE managerID = ?";

        try ( Connection conn = dbcontext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, managerID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");
                String hashedOldPassword = hashPasswordMD5(oldPassword); // Băm mật khẩu cũ trước khi so sánh

                if (!storedPassword.equals(hashedOldPassword)) {
                    return false; // Sai mật khẩu cũ
                }
            } else {
                return false; // Không tìm thấy người dùng
            }
        }

        // Nếu mật khẩu đúng, cập nhật mật khẩu mới
        String updateSql = "UPDATE Manager SET password = ? WHERE managerID = ?";
        try ( Connection conn = dbcontext.getConnection();  PreparedStatement ps = conn.prepareStatement(updateSql)) {
            ps.setString(1, hashPasswordMD5(newPassword)); // Băm mật khẩu mới trước khi lưu
            ps.setInt(2, managerID);
            return ps.executeUpdate() > 0;
        }
    }

    // hàm này của nhân
    public Manager getManagerById(int managerID) {
        String sql = "SELECT * FROM Manager WHERE managerID = ?";
        try ( Connection connection = dbcontext.getConnection();  PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, managerID);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractManagerFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Manager> getAllManagers() {
        List<Manager> list = new ArrayList<>();
        String sql = "SELECT * FROM Manager";
        try ( Connection conn = dbcontext.getConnection();  Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(extractManagerFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean isUsernameTaken(String username, int managerId) {
        String query = "SELECT COUNT(*) FROM Manager WHERE username = ? AND managerID <> ?";
        try ( Connection conn = dbcontext.getConnection();  PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setInt(2, managerId);
            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean insertManager(Manager m) {
        String sql = "INSERT INTO Manager (fullName, email, phone, userName, password, roleID, registrationDate, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try ( Connection conn = dbcontext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            setManagerPreparedStatement(stmt, m);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateManager(Manager m) {
        String sql = "UPDATE Manager SET fullName=?, email=?, phone=?, userName=?, password=?, roleID=?, registrationDate=?, status=? WHERE managerID=?";
        try ( Connection conn = dbcontext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            setManagerPreparedStatement(stmt, m);
            stmt.setInt(9, m.getManagerID());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteManager(int id) {
        String sql = "DELETE FROM Manager WHERE managerID = ?";
        try ( Connection conn = dbcontext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Manager> searchManagerByPhone(String keyword) {
        List<Manager> list = new ArrayList<>();
        String sql = "SELECT * FROM Manager WHERE phone LIKE ?";
        try ( Connection conn = dbcontext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + keyword + "%");
            try ( ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(extractManagerFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updateManagerStatus(int id, boolean status) {
        String sql = "UPDATE Manager SET status = ? WHERE managerID = ?";
        try ( Connection conn = dbcontext.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, status);
            stmt.setInt(2, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Manager extractManagerFromResultSet(ResultSet rs) throws SQLException {
        Manager m = new Manager();
        m.setManagerID(rs.getInt("managerID"));
        m.setFullName(rs.getString("fullName"));
        m.setEmail(rs.getString("email"));
        m.setPhone(rs.getString("phone"));
        m.setUserName(rs.getString("userName"));
        m.setPassword(rs.getString("password"));
        m.setRoleID(rs.getInt("roleID"));
        Date date = rs.getDate("registrationDate");
        if (date != null) {
            m.setRegistrationDate(date.toLocalDate());
        }
        m.setStatus(rs.getBoolean("status"));
        return m;
    }

    private void setManagerPreparedStatement(PreparedStatement stmt, Manager m) throws SQLException {
        stmt.setString(1, m.getFullName());
        stmt.setString(2, m.getEmail());
        stmt.setString(3, m.getPhone());
        stmt.setString(4, m.getUserName());
        stmt.setString(5, m.getPassword());
        stmt.setInt(6, m.getRoleID());
        stmt.setDate(7, Date.valueOf(m.getRegistrationDate()));
        stmt.setBoolean(8, m.isStatus());
    }
}
