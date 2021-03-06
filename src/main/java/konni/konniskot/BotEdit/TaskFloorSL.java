/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package konni.konniskot.BotEdit;

import konni.konniskot.Main;
import konni.konniskot.Task;
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

    int blockslot = 0;
    int toolslot = 1;
    boolean zenchantmentsactive = false;

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
        System.out.println("Starting TaskFloorSL");
        Main.self.selectSlot(toolslot);
        try {
            for (int xachse = x1; xachse >= x2; xachse = xachse - 4) {
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
                    if (zenchantmentsactive == true) {
                        Main.self.selectSlot(toolslot);
                        Main.self.sneak(true);
                        Main.self.placeBlock(xachse, y1, zachse, BlockFace.EAST);
                        Main.self.sneak(false);
                    } else {
                        Main.self.selectSlot(toolslot);
                        if (Main.self.getEnvironment().getBlockAt(xachse, y1, zachse).getType() == Material.IRON_BLOCK) {
                            Location loc = new Location(xachse, y1, zachse);
                            Main.self.selectSlot(toolslot);
                            ai.mineBlock(loc);
                            ai.tick();
                            Main.self.selectSlot(blockslot);
                            Main.self.placeBlock(xachse, y1, zachse, BlockFace.EAST);
                        }
                    }

                    Location walk = new Location(xachse, y1 + 1, zachse).centerHorizontally();
                    ai.moveTo(walk);
                }
                ai.tick();

            }

        } catch (InterruptedException ex) {
        }
        unregister();
    }

}
