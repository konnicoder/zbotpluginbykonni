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
public class TaskFloorSL extends Task {

    int x1;
    int y1;
    int z1;

    int x2;
    int y2;
    int z2;

    int blockslot;
    int toolslot;

    public TaskFloorSL(int x1, int y1, int z1, int x2, int y2, int z2) {
        super(30);
        this.x1 = x1;
        this.y1 = y1;
        this.z1 = z1;
        this.x2 = x2;
        this.y2 = y2;
        this.z2 = z2;

    }

    public void run() {
        System.out.println("Starting TaskFloor");
        Main.self.selectSlot(1);
        try {
            for (int xachse = x1; xachse > x2; xachse = xachse - 4) {
                Location walk1 = new Location(xachse, y1 + 1, z1).centerHorizontally();
                ai.navigateTo(walk1);

                //erste zeile
                for (int zachse = z1; zachse < z2; zachse = zachse + 4) {
                    if (Main.self.getInventory().getSlot(36) == null || Main.self.getInventory().getSlot(36).getType() != Material.SEA_LANTERN) {
                        for (int i = 9; i < 44; i++) {
                            if (Main.self.getInventory().getSlot(i) != null && Main.self.getInventory().getSlot(i).getType() == Material.SEA_LANTERN) {
                                ai.swapItems(i, 36);
                                break;
                            }
                        }
                    }
                    Main.self.sneak(true);
                    Main.self.placeBlock(xachse, y1, zachse, BlockFace.EAST);
                    Main.self.sneak(false);
                    Location walk = new Location(xachse, y1 + 1, zachse).centerHorizontally();
                    ai.moveTo(walk);
                }
                ai.tick();

            }

        } catch (InterruptedException ex) {
        }
    }
}
