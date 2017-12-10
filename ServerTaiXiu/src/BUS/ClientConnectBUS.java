/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BUS;

import DTO.DatCuoc;
import DTO.LichSuNap;
import DTO.NguoiDung;
import DTO.TheCao;
import DTO.TinNhan;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;

/**
 *
 * @author TD
 */
public class ClientConnectBUS extends Thread{
    public Socket client;
    public MainBUS server;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    boolean run=true;
    private String username;
    public NguoiDung thongtin=new NguoiDung();
    public ClientConnectBUS(MainBUS server,Socket client)
    {
        try {
            this.server=server;
            this.client= client;
            out=new ObjectOutputStream(this.client.getOutputStream());
            in= new ObjectInputStream(this.client.getInputStream());
            thongtin=new NguoiDung();
            start();
                    } catch (IOException ex) {
            Logger.getLogger(ClientConnectBUS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void run()
    {
        while(run)
        {
            try {
                DatCuoc dc = NhanDuLieu();
                String lenh=dc.getLenh();
                switch(lenh)
                {
                    case "dangnhap":
                    {
                        try {
                            username=dc.tentaikhoan;
                            String p=dc.matkhau;
                            if(server.dal.getDangNhap(username, p))
                            {
                                server.form.areaServer.append("Người chơi "+username+" đã kết nối.\n");
                                server.form.areaNguoiChoi.append(username+"\n");
                                server.DSUser.put(username, this);
                                thongtin=server.dal.nguoidung;
                                DatCuoc dc1= new DatCuoc();
                                dc1.Lenh="dangnhap";
                                dc1.tentaikhoan=username;
                                dc1.Tien=thongtin.tien;
                                GuiDuLieu(dc1);
                                
                                //Update danh sách người chơi
                                DatCuoc d=new DatCuoc();
                                d.Lenh="dsnguoichoi";
                                d.ten=server.LayDS();
                                server.GuiChoTatCa(d);
                            }
                            else
                            {
                                DatCuoc dc2=new DatCuoc();
                                dc2.Lenh="!dangnhap";
                                GuiDuLieu(dc2);
                            }
                        }catch(Exception e){}
                        break;
                    }
                    case "thoat":
                    {
                        Close();
                        break;
                    }
                    case "thua":
                    {
                        thongtin.tien-=dc.Tien;
                        server.dal.updateNguoiDung(thongtin);
                        break;
                    }
                    case "thang":
                    {
                        thongtin.tien+=dc.Tien;
                        server.dal.updateNguoiDung(thongtin);
                        break;
                    }
                    case "tinnhantong":
                    {
                        dc.tentaikhoan=username;
                        server.GuiChoTatCa(username, dc);
                        break;
                    }
                    case "laythoigian":
                    {
                        dc.xucsac1=server.clock.getI();
                        GuiDuLieu(dc);
                        break;
                    }
                    case "dangky":
                    {
                        NguoiDung ng = new NguoiDung();
                        ng.tentaikhoan=dc.tentaikhoan;
                        ng.matkhau=dc.matkhau;
                        ng.ho=dc.ho;
                        ng.ten=dc.ten;
                        ng.sdt=dc.sdt;
                        ng.ngaysinh=dc.ngsinh;
                        if(server.dal.createNguoiDung(ng))
                        {
                            DatCuoc d=new DatCuoc();
                            d.Lenh="dangky";
                            GuiDuLieu(d);
                        }
                        else
                        {
                            DatCuoc d=new DatCuoc();
                            d.Lenh="!dangky";
                            GuiDuLieu(d);
                        }
                        break;
                    }
                    case "napthe":
                    {
                        if(server.dal.napthe(dc.mathe, dc.seri))
                        {
                            if(server.dal.thenap.isDanap())
                            {
                                DatCuoc d=new DatCuoc();
                                d.Lenh="thedasudung";
                                GuiDuLieu(d);
                            }
                            else
                            {
                                thongtin.tien+=(double)server.dal.thenap.getMenhgia();
                                server.dal.updateNguoiDung(thongtin);
                                //update the cao
                                TheCao th=new TheCao();
                                th=server.dal.thenap;
                                th.setDanap(true);
                                server.form.busthecao.updateTheCao(th);
                                //thêm lịch sử nạp
                                server.dal.luulichsunap(thongtin.id,th.getId());
                                
                                DatCuoc d=new DatCuoc();
                                d.Lenh="napthe";
                                d.Tien=thongtin.tien;
                                GuiDuLieu(d);
                            }
                        }
                        else
                        {
                            DatCuoc d=new DatCuoc();
                            d.Lenh="!napthe";
                            GuiDuLieu(d);
                        }
                        break;
                    }
                    case "xemlichsunap":
                    {
                        ArrayList<LichSuNap> list=server.dal.xemlichsunap(thongtin.id);
                        DatCuoc d=new DatCuoc();
                        d.Lenh="xemlichsunap";
                        d.lichsu=list;
                        GuiDuLieu(d);
                        break;
                    }
                    case "tinnhancanhan":
                    {
                        //kiểm tra người nhận có tồn tại ko
                        int id=server.dal.getID(dc.ten);
                        if(id==-1)
                        {
                            DatCuoc d=new DatCuoc();
                            d.Lenh="sainguoinhan";
                            GuiDuLieu(d);
                        }
                        else
                        {
                            //Lưu tin nhắn vào DB
                            TinNhan tn =new TinNhan();
                            tn.setTinnhan(dc.tinnhan);
                            tn.setIdNguoiGui(thongtin.id);
                            tn.setIdNguoiNhan(id);
                            server.dalTN.createTinNhan(tn);
                            //Gửi đến client người nhận
                            server.GuiTinNhan(thongtin.tentaikhoan, dc.ten, dc.tinnhan);
                        }
                        break;
                    }
                    case "laytinnhan":
                    {
                        //kiểm tra người nhận có tồn tại ko
                        int id=server.dal.getID(dc.ten);
                        if(id==-1)
                        {
                            DatCuoc d=new DatCuoc();
                            d.Lenh="sainguoinhan";
                            GuiDuLieu(d);
                        }
                        else
                        {
                            //Load tin nhan tu DB
                            String s=server.dalTN.loadTinNhan(thongtin.id, id,dc.ten);
                            if(!s.equals(""))
                            {
                                DatCuoc d =new DatCuoc();
                                d.Lenh="returntinnhan";
                                d.tinnhan=s;
                                GuiDuLieu(d);
                            }
                        }
                        break;
                    }
                    default: break;
                }
                
            } catch (IOException ex) {
                Logger.getLogger(ClientConnectBUS.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    public void GuiDuLieu(DatCuoc dc)
    {
        try {
            out.reset();
            out.writeObject(dc);
            out.flush();
        } catch (IOException ex) {
            Logger.getLogger(ClientConnectBUS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private DatCuoc NhanDuLieu() throws IOException
    {
        DatCuoc dc=null;
        try {
            dc=(DatCuoc)in.readObject();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClientConnectBUS.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dc;
    }
    private void Close() throws IOException
    {
        
        run=false;
        if(username!=null)
        {
            server.form.areaServer.append("Người chơi "+username+" đã thoát.\n");
            server.form.areaNguoiChoi.setText(server.form.areaNguoiChoi.getText().replace(username+"\n", ""));
            server.DSUser.remove(this.username);
        }
        DatCuoc d=new DatCuoc();
        d.Lenh="dsnguoichoi";
        d.ten=server.LayDS();
        server.GuiChoTatCa(d);
        in.close();
        out.close();
        client.close();
    }

    private void XuLyTai() {
        
    }
    public void GuiTinNhan(String from,String tinnhan)
    {
        DatCuoc dc=new DatCuoc();
        dc.Lenh="tinnhancanhan";
        dc.tinnhan=tinnhan;
        dc.ten=from;
        GuiDuLieu(dc);
    }
}
