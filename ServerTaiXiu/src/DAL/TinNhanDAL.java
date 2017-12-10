/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAL;

import DTO.LichSuNap;
import DTO.TinNhan;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author TD
 */
public class TinNhanDAL extends sqlConnect{
    public boolean createTinNhan(TinNhan t)
    {
        boolean check=false;
        try {
            String query="INSERT INTO TINNHAN(TINNHAN,IDNGUOIGUI,IDNGUOINHAN,THOIGIAN) VALUES(N'"+t.getTinnhan()+"',"+t.getIdNguoiGui()+","+t.getIdNguoiNhan()+",GETDATE())";
            Statement stamement=conn.createStatement();
            stamement.executeUpdate(query);
            check=true;
        } catch (SQLException ex) {
            check=false;
        }
        return check;
    }
    public String loadTinNhan(int nguoigui,int nguoinhan,String tenNgNhan)
    {
        String s="";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            //get 10 record
            String sql="SELECT TOP 10 * FROM TINNHAN WHERE (IDNGUOIGUI="+nguoigui+" AND IDNGUOINHAN="+nguoinhan;
            sql+=") OR (IDNGUOIGUI="+nguoinhan+" AND IDNGUOINHAN="+nguoigui+") ORDER BY THOIGIAN DESC";
            Statement statement = conn.createStatement();
            ResultSet rs=statement.executeQuery(sql);
            while(rs.next())
            {
                Date time=rs.getTime("THOIGIAN");
                if(rs.getInt("IDNGUOIGUI")==nguoigui)
                    s+="["+sdf.format(time)+"] Tôi: "+rs.getString("TINNHAN")+"\n";
                else s+="["+sdf.format(time)+"] "+tenNgNhan+": "+rs.getString("TINNHAN")+"\n";
            }
        } catch (SQLException ex) {
        }
        return s;
    }
}
