/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package konni.konniskot;

import java.util.ConcurrentModificationException;
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
    private static final Location nuggetTesseract = new Location(297, 132, -8717).centerHorizontally();
    private static final Location ingotTesseract = new Location(295, 132, -8721).centerHorizontally();
    private boolean verbose;

    public TaskCraftGoldingots(boolean verbose) {
        super(100);
        this.verbose = verbose;
    }

    public void run() {

        try {
            ai.moveTo(walk);
            ai.tick();
            KaiTools.CraftFullBlockSuper("gold_ingot_from_nuggets",Material.GOLD_NUGGET, nuggetTesseract, ingotTesseract, craftingbench, ai);
          //  while (true) {
            //    KaiTools.CraftFullBlock(Material.GOLD_NUGGET, nuggetTesseract, ingotTesseract, craftingbench, ai);
            //}

        } catch (InterruptedException guenther) {
        }
        unregister();
    }

    public void cancel() {

        System.out.println("miauuu");

    }
}
