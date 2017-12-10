/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package taixiu;

import GUI.GameForm;

/**
 *
 * @author TD
 */
public class TaiXiu {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        GameForm form= new GameForm("localhost");
        form.setVisible(true);
    }
    
}
