/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BUS;

import DAL.TheCaoDAL;
import DTO.TheCao;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.AbstractList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author TD
 */
public class TheCaoBUS {
    DAL.TheCaoDAL dal= new TheCaoDAL();
    public TheCaoBUS()
    {
//        try {
//            dal.getConnect();
//        } catch (ClassNotFoundException ex) {
//            Logger.getLogger(TheCaoBUS.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (SQLException ex) {
//            Logger.getLogger(TheCaoBUS.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
    public void createTheCao(int n,int menhgia)
    {
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int year = cal.get(Calendar.YEAR);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        String mathe="";
        mathe += String.valueOf(month);
        mathe += String.valueOf(year);
        mathe += String.valueOf(day);
        mathe += String.valueOf(hour);
        mathe += String.valueOf(minute);
        mathe += String.valueOf(second);
        
        for(int i = 0;i<n;i++)
        {
            //Tạo mã thẻ
            String a=mathe;
            Random r = new Random();
            a+=String.valueOf(i);
            for(int j=1;j<=4;j++)
            { 
                int se = r.nextInt(10);
                a+=String.valueOf(se);
            }
            //Tạo seri
            String chuoi = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
            StringBuilder salt = new StringBuilder();
            Random rnd = new Random();
            while (salt.length() < 15) { // length of the random string.
                int index = (int) (rnd.nextFloat() * chuoi.length());
                salt.append(chuoi.charAt(index));
            }
            String seri = salt.toString();
            //Thêm vào database
            TheCao th=new TheCao();
            th.setMathe(a);
            th.setSeri(seri);
            th.setMenhgia(menhgia);
            dal.createTheCao(th);
        }
    }
    public boolean updateTheCao(TheCao th)
    {
        return dal.updateTheCao(th);
    }
    public DefaultTableModel loadDS()
    {
        DefaultTableModel model=new DefaultTableModel();
        if(model.getRowCount() == 0)
        {
            Vector header = new Vector();
            header.add("Mã thẻ");
            header.add("Seri");
            header.add("Mệnh giá");
            header.add("Đã nạp");
            model=new DefaultTableModel(header,0);
        }
        ResultSet rs=dal.loadDS();
        try {
            while(rs.next())
            {
                Vector row = new Vector();
                row.add(rs.getString("SOTK"));
                row.add(rs.getString("SERI"));
                row.add(rs.getInt("MENHGIA"));
                row.add(rs.getBoolean("DANAP"));
                model.addRow(row);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TheCaoBUS.class.getName()).log(Level.SEVERE, null, ex);
        }
        return model;
    }
}
