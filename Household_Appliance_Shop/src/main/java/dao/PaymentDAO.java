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
import model.PaymentMethod;

/**
 *
 * @author HP
 */
public class PaymentDAO {

    private DBContext dbcontext = new DBContext();

    public List<PaymentMethod> getAllPaymentMethods() {
        List<PaymentMethod> paymentMethods = new ArrayList<>();
        String sql = "SELECT * FROM PaymentMethod";

        try ( Connection conn = dbcontext.getConnection();  PreparedStatement ps = conn.prepareStatement(sql);  ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                paymentMethods.add(new PaymentMethod(
                        rs.getInt("paymentMethodID"),
                        rs.getString("methodName")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return paymentMethods;
    }
}
