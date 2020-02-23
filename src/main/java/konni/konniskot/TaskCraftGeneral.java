/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package konni.konniskot;

import zedly.zbot.Location;

public class TaskCraftGeneral extends Task {

    private static Location Tesseract1;
    private static Location Tesseract2;

    public TaskCraftGeneral(Location loc1, Location loc2, int amount) {
        super(100);
        Tesseract1 = loc1;
        Tesseract2 = loc2;
    }

    public void run() {

    }
}
