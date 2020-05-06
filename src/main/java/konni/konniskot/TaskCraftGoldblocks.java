/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package konni.konniskot;

import konni.konniskot.KaiTools;
import konni.konniskot.Task;
import konni.konniskot.Tesseract;
import zedly.zbot.BlockFace;
import zedly.zbot.Location;
import zedly.zbot.Material;

/**
 *
 * @author Konstantin
 */
public class TaskCraftGoldblocks extends Task {

    private static final Location craftingbench = new Location(295, 130, -8720).centerHorizontally();
    private static final Location walk = new Location(295, 131, -8721).centerHorizontally();
    private static final Tesseract goldblockTesseract = new Tesseract(299, 132, -8721);
    private static final Tesseract ingotTesseract = new Tesseract(295, 132, -8721);
    private boolean verbose;
    private int repeats;

    public TaskCraftGoldblocks() {
        super(100);
        
    }

    public void run() {
        try {
            ai.moveTo(walk);
            ai.tick();

            KaiTools.CraftFullBlockSuper("gold_block", Material.GOLD_INGOT, ingotTesseract, goldblockTesseract, craftingbench, ai);

        } catch (InterruptedException guenther) {
        }
    }

}
