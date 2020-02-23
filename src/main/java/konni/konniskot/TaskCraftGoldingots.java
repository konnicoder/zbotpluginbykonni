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

    private static final Location startpunkt = new Location(299, 137, -8712).centerHorizontally();
    private static final Location craftingbench = new Location(301, 138, -8712).centerHorizontally();
    private static final Location nuggetholen = new Location(299, 137, -8713).centerHorizontally();
    private static final Location nuggetTesseract = new Location(300, 138, -8713).centerHorizontally();
    private static final Location ingotablegen = new Location(299, 137, -8714).centerHorizontally();
    private static final Location ingotTesseract = new Location(300, 138, -8714).centerHorizontally();
    private boolean verbose;

    public TaskCraftGoldingots(boolean verbose) {
        super(100);
        this.verbose = verbose;
    }

    public void run() {

        try {
            ai.moveTo(nuggetholen);
            ai.tick();

            while (true) {
                KaiTools.CraftFullBlockSpeed(Material.GOLD_NUGGET, nuggetTesseract, ingotTesseract, craftingbench, ai);
            }

        } catch (InterruptedException guenther) {
        }
        unregister();
    }

    public void cancel() {

        System.out.println("miauuu");

    }
}
