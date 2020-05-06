/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package konni.konniskot;

import java.util.ConcurrentModificationException;
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
public class TaskCraftGoldingots extends Task {

    private static final Location craftingbench = new Location(295, 130, -8720).centerHorizontally();
    private static final Location walk = new Location(297, 130, -8718).centerHorizontally();
    private static final Tesseract nuggetTesseract1 = new Tesseract(297, 132, -8717);
    private static final Tesseract nuggetTesseract2 = new Tesseract(295, 132, -8717);
    private static final Tesseract nuggetTesseract3 = new Tesseract(293, 132, -8717);
    private static final Tesseract nuggetTesseractsmelter = new Tesseract(303, 131, -8719);
    private static final Tesseract ingotTesseractfinal = new Tesseract(295, 132, -8721);
    private static final Tesseract ingotTesseractdropper = new Tesseract(300, 132, -8718);

    private boolean verbose;
    private int repeats;

    public TaskCraftGoldingots(boolean verbose) {
        super(100);
        this.verbose = verbose;
    }

    public void run() {

        try {
            KaiTools.CraftFullBlockSuper("gold_ingot_from_nuggets", Material.GOLD_NUGGET, nuggetTesseract1, ingotTesseractfinal, craftingbench, ai);
//           while(true){
//            KaiTools.CraftFullBlockSpeed(Material.GOLD_NUGGET, nuggetTesseract1.getLocation(), ingotTesseractfinal.getLocation(), craftingbench, ai);
//           }
        } catch (InterruptedException ex) {
        
        }
    

    
        
        
    }

    public void cancel() {

        System.out.println("miauuu");

    }
}
