/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Apk_Penyewaan_PC;

import static com.sun.javafx.tk.Toolkit.getToolkit;
import java.awt.event.KeyEvent;
import java.io.File;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

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
    public void saveTable(String Table, String[]Fields, String[] Values){
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
    
    //======== SIMPAN DATA AUTO_INC
    //======= SIMPAN DATA
    
    
    //======= UPDATE DATA
     public String getDoubleArrays(String[] Fields, String[] Values){
        String hasil = "";
        int detection = Fields.length - 1;
        try {
            for (int i = 0; i < Fields.length; i++) {
                if(i==detection){
                    hasil = hasil +Fields[i]+"='"+Values[i]+"'";
                }else{
                    hasil = hasil +Fields[i]+"='"+Values[i]+"',";
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.toString());
        }
        return ""+hasil+"";
    }
     
    public void updateData(String Table, String PrimaryKey, String Key, String[] Fields, String[] Values){
        try {
            String SQLUpdate = "UPDATE "+Table+" SET "+getDoubleArrays(Fields, Values)+" WHERE "+PrimaryKey+"='"+Key+"'";
            Statement Command = getConnect().createStatement();
            Command.executeUpdate(SQLUpdate);
            Command.close();
            getConnect().close();
            JOptionPane.showMessageDialog(null, "Updated Data Successfully");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.toString());
        }
    }
    
    public void deleteDinamis(String Table, String Primary, String KodeFilm){
        try {
            String SQLHapus = "DELETE FROM "+Table+" WHERE "+Primary+" = '"+KodeFilm+"'";
            Statement commandDelete = getConnect().createStatement();
            commandDelete.executeUpdate(SQLHapus);
            commandDelete.close();
            getConnect().close();
            JOptionPane.showMessageDialog(null, "Data Deleted Successfully");
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }
    
    //==== DOUBLE KEY CHECK ===========================================
    public boolean getDupKey(String table, String Primary, String Isi){
        boolean hasil = false;
        //checking        
        try {
            String SQLcari = "SELECT * FROM "+table+" WHERE "+Primary+" = '"+Isi+"'";
            //            
            Statement CekData = getConnect().createStatement();
            //Get Data            
            ResultSet PK = CekData.executeQuery(SQLcari);
            
            if(PK.next()){
                hasil = true;
            }else {
                hasil = false;
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return hasil;
    }
    
    //======= LOGIN ====
    public void login(String Table, String data1, String data2){
        try {
            String SQl = "SELECT * FROM "+Table+" WHERE username="+"'"+data1+"'"+" AND password="+"'"+data2+"'";
            Statement command = getConnect().createStatement();
            ResultSet rs = command.executeQuery(SQl);
            
            rs.next();
            if(rs.getRow() == 1){
                JOptionPane.showMessageDialog(null, "Berhasil Login", "Login", JOptionPane.INFORMATION_MESSAGE);
                new FormMenu().setVisible(true);
            }else{
                JOptionPane.showMessageDialog(null, "Anda belum terdaftar sebagai admin");
                new FormLogin().setVisible(true);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
    
    
    //================================================================== METHODS TABLE

    public void setTitleColumn(JTable Table, String[] JudulKolom){
        try {
            DefaultTableModel model = new DefaultTableModel();
            Table.setModel(model);
            model.getDataVector().removeAllElements();
            model.fireTableDataChanged();
            
            for (int i = 0; i < JudulKolom.length; i++) {
                model.addColumn(JudulKolom[i]);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
    
    //-----------------------------------------------
    public void setWidhtTitColumn(JTable Table, int[] WidthColumnTo){
        try {
            TableColumn ColumnTo = new TableColumn();
            Table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
            
            for (int i = 0; i < WidthColumnTo.length; i++) {
                ColumnTo = Table.getColumnModel().getColumn(i);
                ColumnTo.setPreferredWidth(WidthColumnTo[i]);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
    
    //---------------------- OBJECT CONVERTION 
    public Object[][] TableFills(String SQL, int nColumns){
        Object[][] data = null;
        
        try {
            Statement command = getConnect().createStatement();
            ResultSet dataset = command.executeQuery(SQL);
            dataset.last();
            int nRows = dataset.getRow();
            dataset.beforeFirst();
            
            int j = 0;
            data = new Object[nRows][nColumns];
            while (dataset.next()) {                
                for (int i = 0; i < nColumns; i++) {
                    data[j][i] = dataset.getString(i+1);
                }
                j++;
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        
        return data;
    }
    
    //---------------------- CATCH TITLE
    public void setShowTable(JTable Table, String[] Title, String SQL){
        try {
            Table.setModel(new DefaultTableModel(TableFills(SQL, Title.length), Title));
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
    
     //---------------------- DATA SEARCH
    public void search(JTable Table, String[] TitleColumn, String SQLSearch){
        try {
            Table.setModel(new DefaultTableModel(TableFills(SQLSearch, TitleColumn.length),TitleColumn));
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
    
    //----------------------- PRINT DATA SHOW REPORT
        public void ReportShow(String laporanFile, String SQL){
        try {
            File file = new File(laporanFile);
            JasperDesign jasDes = JRXmlLoader.load(file);

             JRDesignQuery sqlQuery = new JRDesignQuery();
             sqlQuery.setText(SQL);
             jasDes.setQuery(sqlQuery);

             JasperReport JR = JasperCompileManager.compileReport(jasDes);
             JasperPrint JP = JasperFillManager.fillReport(JR,null,getConnect()); 
             JasperViewer.viewReport(JP);
           } catch (Exception e) {
              JOptionPane.showMessageDialog(null,e.toString());       

        }
     }
        
      
     //================== ADDITION METHODS
     public void FilterInputType(KeyEvent evt){
         char x = evt.getKeyChar();
        if(Character.isAlphabetic(x)){
            new JFrame().getToolkit().beep();
            JOptionPane.showMessageDialog(null, "Masukkan angka 0 -9");
            evt.consume();
        }
     }
     
     //==================== LAPORAN DATA
     public void reportData(String Folder, String File, String Table){
         try {
            int Pilih = JOptionPane.showConfirmDialog(null,"Apakah anda ingin mencetak Laporan Data Operator?","Print",JOptionPane.OK_CANCEL_OPTION);
            if(Pilih == JOptionPane.OK_OPTION){
                new ConfigDB().ReportShow("src/Laporan_"+Folder+"/report_"+File+".jrxml", "SELECT * FROM "+Table);
            }
         } catch (Exception e) {
             System.out.println(e.toString());
         }
     }
}
