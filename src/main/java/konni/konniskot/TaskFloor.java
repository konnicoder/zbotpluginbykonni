/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package konni.konniskot;

import java.util.logging.Level;
import java.util.logging.Logger;
import zedly.zbot.BlockFace;
import zedly.zbot.Location;
import zedly.zbot.Material;

/**
 *
 * @author Konstantin
 */
public class TaskFloor extends Task {

    int x1 = 521;
    int y1 = 11;
    int z1 = -8091;

    int x2 = 507;
    int y2 = 11;
    int z2 = 8073;
    int mode;
    int ironblockslot;
    int sealanternslot;

    public TaskFloor() {
        super(100);
    }

    public void run() {
        System.out.println("Starting TaskFloor");
        Main.self.selectSlot(0);
        try {
            for (int xachse = x1; xachse > x2; xachse = xachse - 4) {

                //erste zeile
                mode = 1;
                for (int zachse = z1; zachse < z2; zachse++) {
                    System.out.println("1");
                    if (mode == 1 || mode == 2 || mode == 3) {
                        findAndPlace(Material.IRON_BLOCK, xachse, y1, zachse);
                    }
                    if (mode == 4) {
                        findAndPlace(Material.SEA_LANTERN, xachse, y1, zachse);
                    }
                    Location walk = new Location(xachse, y1 + 1, zachse);
                    ai.moveTo(walk);

                    mode++;
                    if (mode == 4) {
                        mode = 1;
                    }
                    ai.tick(5);
                }
                Location walkstart = new Location(xachse, y1, z1);
                ai.navigateTo(walkstart);
                for (int zachse = z1; zachse < z2; zachse++) {
                    System.out.println("2");
                    Location walkstart1 = new Location(xachse - 1, y1, z1);
                    ai.navigateTo(walkstart1);
                    findAndPlace(Material.IRON_BLOCK, xachse - 1, y1, zachse);
                    Location walk = new Location(xachse - 1, y1 + 1, zachse);
                    ai.moveTo(walk);
                }

                //MODE
                mode = 3;
                Location walkstart2 = new Location(xachse - 2, y1, z1);
                ai.navigateTo(walkstart2);
                for (int zachse = z1; zachse < z2; zachse++) {

                    if (mode == 1 || mode == 2 || mode == 3) {
                        findAndPlace(Material.IRON_BLOCK, xachse - 2, y1, zachse);
                    }
                    if (mode == 4) {
                        findAndPlace(Material.SEA_LANTERN, xachse - 2, y1, zachse);
                    }
                    Location walk = new Location(xachse - 2, y1 + 1, zachse);
                    ai.moveTo(walk);

                    mode++;
                    if (mode == 4) {
                        mode = 1;
                    }
                }
                for (int zachse = z1; zachse < z2; zachse++) {
                    Location walkstart3 = new Location(xachse - 3, y1, z1);
                    ai.navigateTo(walkstart3);
                    findAndPlace(Material.IRON_BLOCK, xachse - 3, y1, zachse);
                    Location walk = new Location(xachse - 3, y1 + 1, zachse);
                    ai.moveTo(walk);
                }

            }

        } catch (InterruptedException ex) {
        }

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
