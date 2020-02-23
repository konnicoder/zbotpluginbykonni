/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package konni.konniskot;

import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import zedly.zbot.BlockFace;
import zedly.zbot.EntityType;
import zedly.zbot.Location;
import zedly.zbot.Material;
import zedly.zbot.entity.Entity;

/**
 *
 * @author Konstantin
 */
public class TaskTest extends Task {
 
    private static final Location craftingbench = new Location(296, 125, -8720).centerHorizontally();
    private static final Location nuggetTesseract = new Location(295, 126, -8719).centerHorizontally();
    private static final Location ingotTesseract = new Location(294, 126, -8720).centerHorizontally();
   
    public TaskTest() {
        super(100);

    }

    public void run() {

        try {
            System.out.println("Tasktest start");
            ai.tick();
            KaiTools.CraftFullBlockSpeed(Material.GOLD_NUGGET, nuggetTesseract, ingotTesseract, craftingbench, ai);
        } catch (InterruptedException ex) {

        }

    }

}
