/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package konni.konniskot;

import zedly.zbot.BlockFace;
import zedly.zbot.Location;
import zedly.zbot.Material;

/**
 *
 * @author Konstantin
 */
public class TaskCraftGoldblocks extends Task {

    private static final Location startpunkt = new Location(299, 137, -8712).centerHorizontally();
    private static final Location craftingbench = new Location(301, 138, -8712).centerHorizontally();
    private static final Location ingotholen = new Location(299, 137, -8714).centerHorizontally();
    private static final Location ingotTesseract = new Location(300, 138, -8714).centerHorizontally();
    private static final Location goldblockTesseract = new Location(300, 138, -8715).centerHorizontally();
    private static final Location goldblockablegen = new Location(299, 137, -8715).centerHorizontally();

    public TaskCraftGoldblocks() {
        super(100);
    }

    public void run() {
        try {
            ai.moveTo(ingotholen);
            ai.tick();
            while (true) {
                KaiTools.CraftFullBlockSpeed(Material.GOLD_INGOT, ingotTesseract, goldblockTesseract, craftingbench, ai);
            }
        } catch (InterruptedException siglinde) {
        }
    }

}
