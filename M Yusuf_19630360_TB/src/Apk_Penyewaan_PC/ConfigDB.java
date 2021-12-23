/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Apk_Penyewaan_PC;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author myusu
 */
public class ConfigDB {
    
    private String url = "jdbc:mysql://localhost:3306/db_warnet";
    private String user = "root";
    private String pass = "";
    
    public ConfigDB(){}
    
    
    //==== Create Connection to Database
    public Connection getConnect() throws SQLException {
        try {
            Driver myDriver = new com.mysql.jdbc.Driver();
            DriverManager.registerDriver(myDriver);
            System.out.println("Connection Successfully");
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return DriverManager.getConnection(url,user,pass);
    }
    
    //========= DINAMIS METHODS ==============
    //*********************** Fields
    public String getFieldArray(String[] Fields){
        String hasil = "";
        int detection = Fields.length - 1;
        try {
            for (int i = 0; i < Fields.length; i++) {
                if(i==detection){
                    hasil = hasil + Fields[i];
                }else{
                    hasil  = hasil + Fields[i]+",";
                }   
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return "("+hasil+")";
    }
    
    //*********************** Values
    public String getValueArray(String[] Values){
        String hasil = "";
        int detection = Values.length - 1;
        
        try {
            for (int i = 0; i < Values.length; i++) {
                if(i==detection){
                    hasil = hasil +"'"+Values[i]+"'";
                }else{
                    hasil = hasil +"'"+ Values[i]+"',";
                }
            } 
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return "("+hasil+")";   
    }
    
    //======= SIMPAN DATA
    public void saveTable(String Table, String[] Fields, String[] Values){
        try {
            String SQL = "INSERT INTO "+Table+""+getFieldArray(Fields)+" VALUES "+getValueArray(Values);
            Statement command = getConnect().createStatement();
            command.executeUpdate(SQL);
            command.close();
            getConnect().close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
    
    //======= LOGIN ====
    public void login(String Table, String data1, String data2){
        try {
            String SQl = "SELECT * FROM "+Table+" WHERE username="+"'"+data1+"'"+" AND password="+"'"+data2+"'";
            Statement command = getConnect().createStatement();
            ResultSet rs = command.executeQuery(SQl);
            
            rs.next();
            if(rs.getRow() == 1){
                JOptionPane.showMessageDialog(null, "Berhasil Login");
                new FormMenu().setVisible(true);
            }else{
                JOptionPane.showMessageDialog(null, "Username dan Password Salah");
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
    
    
    
}
