/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package konni.konniskot.BotEdit;

import java.util.logging.Level;
import java.util.logging.Logger;
import konni.konniskot.InventoryUtil;
import konni.konniskot.Main;
import konni.konniskot.Task;
import zedly.zbot.BlockFace;
import zedly.zbot.Location;
import zedly.zbot.Material;

/**
 *
 * @author Konstantin
 */
public class TaskFloorPerimiter extends Task {

    int x1;
    int y1;
    int z1;

    int x2;
    int y2;
    int z2;

    int blockslot;

    public TaskFloorPerimiter(int x1, int y1, int z1, int x2, int y2, int z2) {
        super(10);
        this.x1 = x1;
        this.y1 = y1;
        this.z1 = z1;
        this.x2 = x2;
        this.y2 = y2;
        this.z2 = z2;
    }

    public void run() {
        System.out.println("Starting TaskFloor");
        Main.self.selectSlot(0);
        try {
            for (int xachse = x1; xachse > x2; xachse--) {
                Location walk1 = new Location(xachse, y1 + 1, z1).centerHorizontally();
                ai.navigateTo(walk1);

                //erste zeile
                for (int zachse = z1; zachse < z2; zachse++) {
                    int blocksleft = InventoryUtil.count(Material.IRON_BLOCK, true, false);
                    System.out.println(blocksleft);
                    if (Main.self.getInventory().getSlot(36) == null || Main.self.getInventory().getSlot(36).getType() != Material.IRON_BLOCK) {
                        for (int i = 9; i < 44; i++) {
                            if (Main.self.getInventory().getSlot(i) != null && Main.self.getInventory().getSlot(i).getType() == Material.IRON_BLOCK) {
                                ai.swapItems(i, 36);
                                ai.tick(4);
                                break;
                            }
                        }
                    }

                    Main.self.placeBlock(xachse, y1, zachse, BlockFace.EAST);
                    Location walk = new Location(xachse, y1 + 1, zachse).centerHorizontally();
                    ai.moveTo(walk);
                }
                ai.tick();

            }

        } catch (InterruptedException ex) {
        }
        unregister();
    }

    public boolean findAndPlace(Material mat, int x, int y, int z) throws InterruptedException {
        if (Main.self.getInventory().getSlot(36) != null && Main.self.getInventory().getSlot(36).getType() == mat) {
            Main.self.placeBlock(x, y, z, BlockFace.UP);
            return true;
        } else {
            System.out.println("scanning for items in inventory");
            for (int i = 9; i < 44; i++) {
                if (Main.self.getInventory().getSlot(36) != null && Main.self.getInventory().getSlot(i).getType() == mat) {
                    ai.swapItems(i, 36);
                    break;
                }
            }
            Main.self.placeBlock(x, y, z, BlockFace.UP);
            return true;
        }

    }
}
