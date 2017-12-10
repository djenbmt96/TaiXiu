/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author TD
 */
public class sqlConnect {
    public static Connection conn=null;
    public void getConnect() throws ClassNotFoundException, SQLException
    {
        Class.forName("net.sourceforge.jtds.jdbc.Driver");
        String url="jdbc:jtds:sqlserver://localhost:1433/TAIXIU;instance=MSSQLSERVER;user=sa;password=sa";
        conn= DriverManager.getConnection(url);
    }
    public void closeConnect() throws SQLException
    {
        conn.close();
    }
}
