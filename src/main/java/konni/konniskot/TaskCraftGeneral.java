/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package konni.konniskot;

import zedly.zbot.Location;

public class TaskCraftGeneral extends Task {

    private static Location Tesseract1;
    private static final Location Tesseract1 = new Location(299, 137, -8712).centerHorizontally();

    public TaskCraftGeneral(Location loc1, Location Loc2, int amount) {
        super(100);
    }

    public void run() {

    }
}
