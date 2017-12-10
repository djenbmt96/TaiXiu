/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAL;

import DTO.LichSuNap;
import DTO.NguoiDung;
import DTO.TheCao;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author TD
 */
public class NguoiDungDAL extends sqlConnect{
    public DTO.NguoiDung nguoidung;
    public TheCao thenap;
    public boolean getDangNhap(String u,String p)
    {
        
        boolean check=false;
        try{
            String sql="SELECT * FROM NGUOIDUNG WHERE TENTAIKHOAN='"+u+"' AND MATKHAU='"+p+"'";
            Statement statement = conn.createStatement();
            ResultSet rs=statement.executeQuery(sql);
            if(rs.next())
            {
                nguoidung=new NguoiDung();
                nguoidung.id=rs.getInt("ID");
                nguoidung.tentaikhoan=u;
                nguoidung.matkhau=rs.getString("MATKHAU");
                nguoidung.ho=rs.getString("HO");
                nguoidung.ten=rs.getString("TEN");
                nguoidung.sdt =rs.getString("SDT");
                nguoidung.ngaysinh=rs.getDate("NGAYSINH");
                nguoidung.diachi=rs.getString("DIACHI");
                nguoidung.ngaydangky=rs.getDate("NGAYDANGKY");
                nguoidung.tien=rs.getDouble("TIEN");
                if(rs.getBoolean("TINHTRANGKHOA")) nguoidung.khoa=true;
                else nguoidung.khoa=false;
                check=true;
            }
        }catch(Exception e)
        {
            check=false;
        }
        return check;
    }
    public boolean updateNguoiDung(NguoiDung ng)
    {
        boolean check=false;
        try {
            int khoa=1;
            if (ng.khoa) khoa=1; else khoa=0;
            String sql="UPDATE NGUOIDUNG SET MATKHAU='"+ng.matkhau;
            sql+="',HO=N'"+ng.ho+"',TEN=N'"+ng.ten+"',SDT='"+ng.sdt;
            sql+="',NGAYSINH='"+ng.ngaysinh+"',";
            sql+="TIEN="+ng.tien+",TINHTRANGKHOA="+khoa;
            sql+=" WHERE ID="+ng.id;
            Statement stamement = conn.createStatement();
            stamement.executeUpdate(sql);
            check=true;
        } catch (Exception ex) {
            check=false;
        }
        return check;
    }
    public boolean createNguoiDung(NguoiDung ng)
    {
        boolean check=false;
        try {
            String ngsinh="";
            if(ng.ngaysinh!=null)
            {
            Calendar cal = Calendar.getInstance();
            cal.setTime(ng.ngaysinh);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            int year = cal.get(Calendar.YEAR);
            ngsinh=year+"-"+month+"-"+day;
            }
            
            String sql="INSERT INTO NGUOIDUNG(TENTAIKHOAN,MATKHAU,HO,TEN,SDT,NGAYSINH,NGAYDANGKY) VALUES('";
            sql+=ng.tentaikhoan+"','"+ng.matkhau+"',N'"+ng.ho+"',N'"+ng.ten+"','"+ng.sdt+"','"+ngsinh+"',GETDATE())";
            Statement stamement = conn.createStatement();
            stamement.executeUpdate(sql);
            check=true;
        } catch (Exception ex) {
            check=false;
        }
        return check;
    }
    public boolean napthe(String mathe,String seri)
    {
        boolean check=false;
        try{
            String sql="SELECT * FROM THECAO WHERE SOTK='"+mathe+"' AND SERI='"+seri+"'";
            Statement statement = conn.createStatement();
            ResultSet rs=statement.executeQuery(sql);
            if(rs.next())
            {
                thenap=new TheCao();
                thenap.setId(rs.getInt("ID"));
                thenap.setMenhgia(rs.getInt("MENHGIA"));
                if(rs.getBoolean("DANAP")) thenap.setDanap(true);
                else thenap.setDanap(false);
                check=true;
            }
        }catch(Exception e)
        {
            check=false;
        }
        return check;
    }
    public boolean luulichsunap(int idNguoiDung,int idTheCao)
    {
        boolean check=false;
        try{
            String sql="INSERT INTO LICHSUNAP(IDNGUOIDUNG,IDTHECAO,THOIGIAN) VALUES("+idNguoiDung+","+idTheCao+",GETDATE())";
            Statement statement = conn.createStatement();
            statement.executeUpdate(sql);
            check=true;
        }catch(Exception e)
        {
            check=false;
        }
        return check;
    }
    public ArrayList<LichSuNap> xemlichsunap(int idnguoidung)
    {
        ArrayList<LichSuNap> list=new ArrayList<>();
        try{
            String sql="SELECT L.THOIGIAN, T.SOTK, T.SERI, T.MENHGIA FROM LICHSUNAP L,";
            sql+=" THECAO T WHERE L.IDTHECAO=T.ID AND L.IDNGUOIDUNG="+idnguoidung;
            Statement statement = conn.createStatement();
            ResultSet rs=statement.executeQuery(sql);
            while(rs.next())
            {
                LichSuNap l=new LichSuNap();
                l.ngay=rs.getDate("THOIGIAN");
                l.menhgia=rs.getInt("MENHGIA");
                l.mathe=rs.getString("SOTK");
                l.seri=rs.getString("SERI");
                list.add(l);
            }
        }catch(Exception e)
        {
        }
        return list;
    }
    public int getID(String username)
    {
        int id=-1;
        try{
            String sql="SELECT ID FROM NGUOIDUNG WHERE TENTAIKHOAN='"+username+"'";
            Statement statement = conn.createStatement();
            ResultSet rs=statement.executeQuery(sql);
            if(rs.next())
            {
                id=rs.getInt("ID");
            }
        }catch(Exception e)
        {
        }
        return id;
    }
}
