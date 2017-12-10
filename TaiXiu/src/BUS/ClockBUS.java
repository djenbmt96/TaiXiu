/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BUS;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;

/**
 *
 * @author TD
 */
public class ClockBUS extends Thread{
    JLabel lblTime;
    public static int i=30;
    public static int j=0;

    public void setI(int i) {
        ClockBUS.i = i;
    }
    public ClockBUS(JLabel lblTime){
        
        this.lblTime = lblTime;
    }
    @SuppressWarnings("static-access")
    public ClockBUS(int a){
      this.i = a;
    }
   
    @Override
        public void run()
        {
            while(true)
            {
            while(i>0){
                i--;
                int minute = i/60;
                int Second = i -minute*60;
                String time  =  minute+":"+Second;
                if(Second<10){
                time =minute+":0"+Second;
            }
             // System.out.println(j);
            lblTime.setText(time);
            try {
                    j++;
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {

                  }
            }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ClockBUS.class.getName()).log(Level.SEVERE, null, ex);
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
