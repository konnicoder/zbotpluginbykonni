/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package konni.konniskot;

import java.util.logging.Level;
import java.util.logging.Logger;
import zedly.zbot.Location;
import zedly.zbot.Material;
import zedly.zbot.block.TileSign;

/**
 *
 * @author Konstantin
 */
public class TaskReadSign extends Task {

    private static final Location COAL_CHEST_WALK = new Location(299, 132, -8717).centerHorizontally();
    private static final Location COAL_CHEST_LOC = new Location(300, 132, -8717).centerHorizontally();

    public TaskReadSign() {
        super(100);
    }

    public void run() {
        try {
            ai.tick();
            ai.moveTo(COAL_CHEST_WALK);
            ai.tick();
            ai.openContainer(COAL_CHEST_LOC);
            ai.tick();
            for (int slot = 54; slot <= 89; slot++) {
                if (Main.self.getInventory().getSlot(slot) != null && Main.self.getInventory().getSlot(slot).getType() == Material.DRIED_KELP_BLOCK) {

                    ai.depositSlot(slot);

                }
            }
            ai.closeContainer();
        } catch (InterruptedException ex) {
        }
    }
}
