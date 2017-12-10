/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author TD
 */
public class DatCuoc implements Serializable{

    public double getTien() {
        return Tien;
    }

    public void setTien(double Tien) {
        this.Tien = Tien;
    }

    public String getLenh() {
        return Lenh;
    }

    public void setLenh(String Lenh) {
        this.Lenh = Lenh;
    }

    public boolean isTai() {
        return Tai;
    }

    public void setTai(boolean Tai) {
        this.Tai = Tai;
    }
    public double Tien=0;
    public String Lenh="";
    public boolean Tai=false;
    public String tentaikhoan="",matkhau="";
    public String ho,ten,sdt,mathe,seri;
    public ArrayList<LichSuNap> lichsu;

    public ArrayList<LichSuNap> getLichsu() {
        return lichsu;
    }

    public void setLichsu(ArrayList<LichSuNap> lichsu) {
        this.lichsu = lichsu;
    }
    public String getMathe() {
        return mathe;
    }

    public void setMathe(String mathe) {
        this.mathe = mathe;
    }

    public String getSeri() {
        return seri;
    }

    public void setSeri(String seri) {
        this.seri = seri;
    }

    public String getHo() {
        return ho;
    }

    public void setHo(String ho) {
        this.ho = ho;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public Date getNgsinh() {
        return ngsinh;
    }

    public void setNgsinh(Date ngsinh) {
        this.ngsinh = ngsinh;
    }
    public Date ngsinh;

    public String getTinnhan() {
        return tinnhan;
    }

    public void setTinnhan(String tinnhan) {
        this.tinnhan = tinnhan;
    }
    public String tinnhan="";

    public int getXucsac1() {
        return xucsac1;
    }

    public void setXucsac1(int xucsac1) {
        this.xucsac1 = xucsac1;
    }

    public int getXucsac2() {
        return xucsac2;
    }

    public void setXucsac2(int xucsac2) {
        this.xucsac2 = xucsac2;
    }

    public int getXucsac3() {
        return xucsac3;
    }

    public void setXucsac3(int xucsac3) {
        this.xucsac3 = xucsac3;
    }
    public int xucsac1=0,xucsac2=0,xucsac3=0;

    public String getTentaikhoan() {
        return tentaikhoan;
    }

    public void setTentaikhoan(String tentaikhoan) {
        this.tentaikhoan = tentaikhoan;
    }

    public String getMatkhau() {
        return matkhau;
    }

    public void setMatkhau(String matkhau) {
        this.matkhau = matkhau;
    }
    
}
