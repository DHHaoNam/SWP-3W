/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import DB.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.Role;

/**
 *
 * @author TRUNG NHAN
 */
public class RoleDAO {

    private DBContext dbcontext = new DBContext();

    public List<Role> getAllRoles() {
        List<Role> list = new ArrayList<>();
        String sql = "SELECT * FROM Role";

        try ( Connection conn = dbcontext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Role(rs.getInt("roleID"), rs.getString("roleName")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public Role getRoleById(int id) {
        String sql = "SELECT * FROM Role WHERE roleID = ?";
        try ( Connection conn = dbcontext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try ( ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Role(rs.getInt("roleID"), rs.getString("roleName"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}



