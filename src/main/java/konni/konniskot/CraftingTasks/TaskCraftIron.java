/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package konni.konniskot.CraftingTasks;

import java.util.logging.Level;
import java.util.logging.Logger;
import konni.konniskot.KaiTools;
import konni.konniskot.Task;
import zedly.zbot.Location;
import zedly.zbot.Material;

/**
 *
 * @author Konstantin
 */
public class TaskCraftIron extends Task {

    private static final Location craftingbench_LOC = new Location(256, 137, -8530).centerHorizontally();
    private static final Location IRON_INGOT_TESS_LOC = new Location(257, 137, -8531).centerHorizontally();
    private static final Location IRON_BLOCK_TESS_LOC = new Location(256, 137, -8531).centerHorizontally();

    public TaskCraftIron() {
        super(100);
    }

    public void run() {

        try {
            KaiTools.CraftFullBlockSuper("iron_block", Material.IRON_INGOT, IRON_INGOT_TESS_LOC, IRON_BLOCK_TESS_LOC, craftingbench_LOC, ai);
        } catch (InterruptedException ex) {
        }
        unregister();
    }
}
