/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BUS;

import DTO.NguoiDung;
import GUI.MainForm;
import java.awt.List;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Random;
import javax.swing.JTextArea;
import DAL.*;
import DTO.DatCuoc;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;

/**
 *
 * @author TD
 */
public class MainBUS extends Thread{
    public MainForm form;
    private int port;
    private ServerSocket server;
    private Socket client;
    boolean isTai=false,isXiu=false;
    public Hashtable<String,ClientConnectBUS> DSUser;
    public int xucsac1,xucsac2,xucsac3;
    public NguoiDungDAL dal=new NguoiDungDAL();
    public TinNhanDAL dalTN=new TinNhanDAL();
    public ClockBUS clock;
    
    public MainBUS(MainForm form,int port)
    {
        this.form=form;
        this.port = port;
        try {
            dal.getConnect();
//            dalTN.getConnect();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MainBUS.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(MainBUS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void run()
    {
        
            
        try
        {
            server= new ServerSocket(port);
            form.areaServer.append("Server đang phục vụ...\n");
            DSUser =new Hashtable<String,ClientConnectBUS>();
            clock=new ClockBUS(this,form.labelTime);
            clock.start();     
        }catch(IOException e)
        {
            
        }
        try {
            while (true) {
                client=server.accept();
                new ClientConnectBUS(this,client);
                
            }
        } 
        catch (IOException e) {
                    e.printStackTrace();
        }
    }
    public int QuaySo()
    {
        Random r = new Random();
        xucsac1= 1+r.nextInt(6);
        xucsac2= 1+r.nextInt(6);
        xucsac3= 1+r.nextInt(6);
        int tong=xucsac1+xucsac2+xucsac3;
        if(xucsac1==xucsac2 && xucsac1==xucsac3)
        {
            isXiu =false;
            isTai=false;
        }
        else if(tong<=10)
        {
            isXiu =true;
            isTai=false;
        }
        else 
        {
            isXiu=false;
            isTai=true;
        }
        return tong;
    }
    public void GuiChoTatCa(DatCuoc dc)
    {
        Enumeration e = DSUser.keys();
        String name=null;
        while(e.hasMoreElements()){
            name=(String) e.nextElement();
            DSUser.get(name).GuiDuLieu(dc);
        }
    }
    public void GuiChoTatCa(String from,DatCuoc dc)
    {
        Enumeration e = DSUser.keys();
        String name=null;
        while(e.hasMoreElements()){
            name=(String) e.nextElement();
            if(name.compareTo(from)!=0) DSUser.get(name).GuiDuLieu(dc);
        }
    }
    public String LayDS()
    {
        Enumeration e = DSUser.keys();
        String name="";
        while(e. hasMoreElements()){
                name+=(String) e.nextElement()+"\n";
        }
        return name;
    }
    public void GuiTinNhan(String from,String to,String tinnhan)
    {
        Enumeration e = DSUser.keys();
        String name=null;
        while(e.hasMoreElements()){
            name=(String) e.nextElement();
            if(name.compareTo(to)==0) DSUser.get(name).GuiTinNhan(from, tinnhan);
        }
    }
}
