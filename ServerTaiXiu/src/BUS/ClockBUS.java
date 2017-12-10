/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BUS;

import DTO.DatCuoc;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;

/**
 *
 * @author TD
 */
public class ClockBUS extends Thread{
    JLabel lblTime;
    MainBUS bus;
    public static int i=30;
    public static int j=0;
    public int getI() {
        return i;
    }
    public ClockBUS(MainBUS bus,JLabel lblTime){
        this.bus=bus;
        this.lblTime = lblTime;
    }
    @SuppressWarnings("static-access")
    public ClockBUS(int a){
      this.i = a;
    }
   
    @Override
        public void run()
        {
            
            while(i>0){
                //Gửi lệnh bắt đầu
                if(i==30)
                {
                    DatCuoc d=new DatCuoc();
                    d.Lenh="batdau";
                    bus.GuiChoTatCa(d);
                }
                //đếm thời gian
                i--;
                int minute = i/60;
                int Second = i -minute*60;
                String time  =  minute+":"+Second;
                if(Second<10){
                time =minute+":0"+Second;
            }
            lblTime.setText(time);
            try {
                    j++;
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {

                  }
            //Xử lý khi hết thời gian
            if(i==0)
            {
                Date d = new Date();
                SimpleDateFormat format= new SimpleDateFormat("hh:mm");
                int quayso=bus.QuaySo();
                if(bus.isTai) bus.form.areaServer.append("["+format.format(d)+"] "+"Kết quả đổ xúc sắc là Tài["+bus.xucsac1+","+bus.xucsac2+","+bus.xucsac3+"]: "+quayso+"\n");
                else if(bus.isXiu) bus.form.areaServer.append("["+format.format(d)+"] "+"Kết quả đổ xúc sắc là Xỉu["+bus.xucsac1+","+bus.xucsac2+","+bus.xucsac3+"]: "+quayso+"\n");
                else bus.form.areaServer.append("["+format.format(d)+"] "+"Kết quả đổ xúc sắc là Bộ 3 trùng["+bus.xucsac1+","+bus.xucsac2+","+bus.xucsac3+"]: "+quayso+"\n");
                
                DatCuoc dc=new DatCuoc();
                dc.Lenh="ketqua";
                dc.xucsac1=bus.xucsac1;
                dc.xucsac2=bus.xucsac2;
                dc.xucsac3=bus.xucsac3;
                bus.GuiChoTatCa(dc);
                reset();
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ClockBUS.class.getName()).log(Level.SEVERE, null, ex);
                    }
            }
            }
        }

        
   public void reset(){
   i =30;
   j=0;
   }
    public static String TimePlay(){
          int minute = j/60;
          int Second = j -minute*60;
          //String time  =  minute+":"+Second;
          String time  =  minute+":"+Second;
          if(Second<10){
          time =minute+":0"+Second;
          }
          return time;
    }
}
