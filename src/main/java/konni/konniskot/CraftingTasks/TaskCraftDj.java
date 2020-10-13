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
public class TaskCraftDj extends Task {

    private static final Location craftingbench = new Location().centerHorizontally();
    private static final Location nuggetTesseract = new Location().centerHorizontally();
    private static final Location ingotTesseract = new Location().centerHorizontally();

    public TaskCraftDj() {
        super(100);
    }

    public void run() {
        System.out.println("starting TastCraftDj");
        try {
            ai.tick();
            KaiTools.CraftFullBlockSuper("gold_ingot_from_nuggets", Material.GOLD_NUGGET, nuggetTesseract, ingotTesseract, craftingbench, ai);
        } catch (InterruptedException ex) {
        }

    }
}
