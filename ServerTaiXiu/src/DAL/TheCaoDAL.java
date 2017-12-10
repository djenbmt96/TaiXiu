/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAL;

import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import DTO.TheCao;
import java.sql.ResultSet;

/**
 *
 * @author TD
 */
public class TheCaoDAL extends sqlConnect{
    public boolean createTheCao(TheCao th)
    {
        boolean check=false;
        try {
            String query="INSERT INTO THECAO(SOTK,SERI,MENHGIA) VALUES('"+th.getMathe()+"','"+th.getSeri()+"',"+th.getMenhgia()+")";
            Statement stamement=conn.createStatement();
            stamement.executeUpdate(query);
            check=true;
        } catch (SQLException ex) {
            check=false;
        }
        return check;
    }
    public ResultSet loadDS()
    {
        ResultSet rs=null;
        try {
            Statement statement = conn.createStatement();
            String sql=("select * from THECAO");
            rs=statement.executeQuery(sql);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "không có kết nối");
        }
        return rs;
    }
    public boolean updateTheCao(TheCao th)
    {
        boolean check=false;
        try {
            int danap=1;
            if (th.isDanap()) danap=1; else danap=0;
            String sql="UPDATE THECAO SET DANAP="+danap;
            sql+=" WHERE ID="+th.getId();
            Statement stamement = conn.createStatement();
            stamement.executeUpdate(sql);
            check=true;
        } catch (Exception ex) {
            check=false;
        }
        return check;
    }
}
