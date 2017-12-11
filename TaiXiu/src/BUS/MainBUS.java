/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BUS;

import DTO.DatCuoc;
import DTO.LichSuNap;
import DTO.TinNhan;
import GUI.GameForm;
import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author TD
 */
public class MainBUS extends Thread{
    public GameForm form;
    private String ip;
    private int port;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Socket client;
    private DatCuoc DCin;
    public ClockBUS clock;
    
    public MainBUS(GameForm form,String ip,int port)
    {
        this.form=form;
        this.ip=ip;
        this.port=port;
        clock=new ClockBUS(this.form.labelThoiGian);
        try {
            client= new Socket(ip,port);
            out=new ObjectOutputStream(client.getOutputStream());
            in=new ObjectInputStream(client.getInputStream());
            this.start();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(form,"Không có phản hồi từ máy chủ");
        }
    }
    public void run()
    {
        while(true)
        {
            try{
                DCin=NhanDuLieu();
                XuLy();
            }catch(IOException e)
            {
            } 
        }
    }
    public boolean GuiDuLieu(DatCuoc d)
    {
        try{
            out.writeObject(d);
            out.flush();
            return true;
        }catch(IOException e){
            return false;
        }
    }
    public DatCuoc NhanDuLieu() throws IOException
    {
        DatCuoc s=new DatCuoc();
            try {
                s=(DatCuoc) in.readObject();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(MainBUS.class.getName()).log(Level.SEVERE, null, ex);
            }
            return s;
    }
    public void DongKetNoi()
    {
        try {
            out.close();
            in.close();
            client.close();
        } catch (IOException ex) {
            Logger.getLogger(MainBUS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void XuLy() {
        switch(DCin.Lenh)
        {
            case "dangnhap":
            {
                form.TabbedPane.setEnabledAt(1,true);
                form.TabbedPane.setSelectedIndex(1);
                form.TabbedPane.setEnabledAt(0,false);
                form.labelUser.setText(DCin.tentaikhoan);
                form.labelTien.setText(String.valueOf(DCin.Tien));
                
                //Lấy thời gian chạy
                DatCuoc dc=new DatCuoc();
                dc.Lenh="laythoigian";
                GuiDuLieu(dc);
                break;
            }
            case "!dangnhap":
            {
                JOptionPane.showMessageDialog(form, "đăng nhập thất bại, vui lòng kiểm tra lại.","Lỗi đăng nhập",JOptionPane.WARNING_MESSAGE);
                break;
            }
            case "userdatontai":
            {
                JOptionPane.showMessageDialog(form, "Tài khoản này đang đăng nhập.","Lỗi đăng nhập",JOptionPane.WARNING_MESSAGE);
                break;
            }
            case "laythoigian":
            {
                clock.setI(DCin.xucsac1);
                clock.start();
                break;
            }
            case "batdau":
            {
                clock.reset();
                break;
            }
            case "ketqua":
            {
                ImageIcon image1 = new ImageIcon("src/Image/"+DCin.xucsac1+".png");
                ImageIcon image2 = new ImageIcon("src/Image/"+DCin.xucsac2+".png");
                ImageIcon image3 = new ImageIcon("src/Image/"+DCin.xucsac3+".png");
                form.xucsac1.setIcon(image1);
                form.xucsac2.setIcon(image2);
                form.xucsac3.setIcon(image3);
                if(DCin.xucsac1==DCin.xucsac2 && DCin.xucsac1==DCin.xucsac3)
                {
                    form.labelXiu.setForeground(Color.black);
                    form.labelTai.setForeground(Color.black);
                    DatCuoc d = new DatCuoc();
                    d.Lenh="thua";
                    d.Tien=Double.parseDouble(form.labelTienTai.getText())+Double.parseDouble(form.labelTienXiu.getText());
                    GuiDuLieu(d);
                    form.labelTienTai.setText("0");
                    form.labelTienXiu.setText("0");
                }
                else if((DCin.xucsac1+DCin.xucsac2+DCin.xucsac3)<=10)
                {
                    form.labelXiu.setForeground(Color.red);
                    form.labelTai.setForeground(Color.black);
                    if(form.labelTienTai.getText()!="0")
                    {
                        form.labelThoiGian.setText("Bạn thua!");
                        DatCuoc d = new DatCuoc();
                        d.Lenh="thua";
                        d.Tien=Double.parseDouble(form.labelTienTai.getText());
                        GuiDuLieu(d);
                        form.labelTienTai.setText("0");
                    }
                    else if(form.labelTienXiu.getText()!="0")
                    {
                        form.labelThoiGian.setText("Bạn thắng!");
                        DatCuoc d = new DatCuoc();
                        d.Lenh="thang";
                        d.Tien=Double.parseDouble(form.labelTienXiu.getText());
                        GuiDuLieu(d);
                        form.labelTienXiu.setText("0");
                        Double tien= Double.parseDouble(form.labelTien.getText());
                        tien+=d.Tien*2;
                        form.labelTien.setText(tien.toString());
                    }
                }
                else
                {
                    form.labelXiu.setForeground(Color.black);
                    form.labelTai.setForeground(Color.red);
                    if(form.labelTienXiu.getText()!="0")
                    {
                        form.labelThoiGian.setText("Bạn thua!");
                        DatCuoc d = new DatCuoc();
                        d.Lenh="thua";
                        d.Tien=Double.parseDouble(form.labelTienXiu.getText());
                        GuiDuLieu(d);
                        form.labelTienXiu.setText("0");
                    }
                    else if(form.labelTienTai.getText()!="0")
                    {
                        form.labelThoiGian.setText("Bạn thắng!");
                        DatCuoc d = new DatCuoc();
                        d.Lenh="thang";
                        d.Tien=Double.parseDouble(form.labelTienTai.getText());
                        GuiDuLieu(d);
                        form.labelTienTai.setText("0");
                        Double tien= Double.parseDouble(form.labelTien.getText());
                        tien+=d.Tien*2;
                        form.labelTien.setText(tien.toString());
                    }
                }
                break;
            }
            case "tinnhantong":
            {
                Date d = new Date();
                SimpleDateFormat format= new SimpleDateFormat("hh:mm");
                form.areaChat.append("["+format.format(d)+"] "+DCin.tentaikhoan+": "+DCin.tinnhan+"\n");
                break;
            }
            case "dangky":
            {
                JOptionPane.showMessageDialog(form,"Đăng ký thành công. Enjoy!");
                break;
            }
            case "!dangky":
            {
                form.labelDKCanhBao.setText("Xảy ra lỗi. Có thể username bị trùng");
                break;
            }
            case "napthe":
            {
                JOptionPane.showMessageDialog(form, "Nạp thành công");
                form.labelCanhBaoNapThe.setText("");
                form.labelTien.setText(String.valueOf(DCin.Tien));
                break;
            }
            case "!napthe":
            {
                JOptionPane.showMessageDialog(form, "Nạp thất bại");
                form.labelCanhBaoNapThe.setText("");
                break;
            }
            case "thedasudung":
            {
                form.labelCanhBaoNapThe.setText("Thẻ đã được sử dụng");
                break;
            }
            case "xemlichsunap":
            {
                ArrayList<LichSuNap> list=DCin.lichsu;
                DefaultTableModel model=new DefaultTableModel();
                if(model.getRowCount() == 0)
                {
                    Vector header = new Vector();
                    header.add("Ngày");
                    header.add("Mã thẻ");
                    header.add("Seri");
                    header.add("Mệnh giá");
                    model=new DefaultTableModel(header,0);
                }
                for(int i=0;i<list.size();i++)
                {
                    Vector row = new Vector();
                    row.add(list.get(i).ngay);
                    row.add(list.get(i).mathe);
                    row.add(list.get(i).seri);
                    row.add(list.get(i).menhgia);
                    model.addRow(row);
                }
                form.tableLichSuNap.setModel(model);
                break;
            }
            case "dsnguoichoi":
            {
                DefaultListModel model=new DefaultListModel();
                String chuoi=DCin.ten;
                String[] catchuoi=chuoi.split("\n");
                for(String s:catchuoi)
                {
                    if(!form.labelUser.getText().equals(s))
                    model.addElement(s);
                }
                form.listNguoiChoi.setModel(model);
                break;
            }
            case "tinnhancanhan":
            {
                Date d = new Date();
                SimpleDateFormat format= new SimpleDateFormat("hh:mm");
                if(form.labelTNten.getText().equals(DCin.ten))
                {
                    form.areaTN.append("["+format.format(d)+"] "+DCin.ten+": "+DCin.tinnhan+"\n");
                }
                else
                {
                    form.labelTNten.setText(DCin.ten);
                    form.areaTN.setText("["+format.format(d)+"] "+DCin.ten+": "+DCin.tinnhan+"\n");
                }
                break;
            }
            case "sainguoinhan":
            {
                JOptionPane.showMessageDialog(form,"Người nhận không hợp lệ.");
                break;
            }
            case "returntinnhan":
            {
                String tinnhan=DCin.tinnhan;
                String[] catchuoi=tinnhan.split("\n");
                int len=catchuoi.length;
                for(int i=len-1;i>=0;i--)
                {
                    form.areaTN.append(catchuoi[i]+"\n");
                }
                break;
            }
            case "dongServer":
            {
                JOptionPane.showMessageDialog(form, "Mất kết nối từ server, Server đã bị tắt","Thông báo",JOptionPane.OK_OPTION);
                break;
            }
            default: break;
        }
    }
    public void DangNhap(String u,String p)
    {
        DatCuoc dc = new DatCuoc();
        dc.Lenh="dangnhap";
        dc.tentaikhoan=u;
        dc.matkhau=p;
        GuiDuLieu(dc);
    }

    public void Thoat() {
        if(client!=null)
        {
        DatCuoc dc= new DatCuoc();
        dc.Lenh="thoat";
        GuiDuLieu(dc);
        DongKetNoi();
        }
    }

    public void GuiTinNhan(String s) {
        DatCuoc dc= new DatCuoc();
        dc.Lenh="tinnhantong";
        dc.tinnhan=s;
        GuiDuLieu(dc);
    }

    public void DangKy(String u, String p, String ho, String ten, String sdt, Date ngsinh) {
        DatCuoc dc= new DatCuoc();
        dc.Lenh="dangky";
        dc.tentaikhoan=u;
        dc.matkhau=p;
        dc.ho=ho;
        dc.ten=ten;
        dc.sdt=sdt;
        dc.ngsinh=ngsinh;
        GuiDuLieu(dc);
    }

    public void NapThe(String mathe, String seri) {
        DatCuoc dc = new DatCuoc();
        dc.Lenh="napthe";
        dc.mathe=mathe;
        dc.seri=seri;
        GuiDuLieu(dc);
    }

    public void XemLichSuNap() {
        DatCuoc dc = new DatCuoc();
        dc.Lenh="xemlichsunap";
        GuiDuLieu(dc);
    }

    public void GuiTinNhanCaNhan(String nguoinhan, String tinnhan) {
        DatCuoc dc = new DatCuoc();
        dc.Lenh="tinnhancanhan";
        dc.ten=nguoinhan;
        dc.tinnhan=tinnhan;
        GuiDuLieu(dc);
    }

    public void LayTinNhan(String nguoinhan) {
        DatCuoc dc = new DatCuoc();
        dc.Lenh="laytinnhan";
        dc.ten=nguoinhan;
        GuiDuLieu(dc);
    }
}
