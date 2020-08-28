/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package konni.konniskot.FarmTasks;

import konni.konniskot.KaiTools;
import konni.konniskot.Main;
import konni.konniskot.Task;
import zedly.zbot.BlockFace;
import zedly.zbot.Location;
import zedly.zbot.Material;

/**
 *
 * @author Konstantin
 */
public class TaskMineOres extends Task {

    private static final Location MINING_LOC = new Location(310, 138, -8691).centerHorizontally();
    private static final Location INPUT_CHEST_LOC = new Location(311, 137, -8692).centerHorizontally();
    private static final Location OUTPUT_CHEST_LOC = new Location(309, 137, -8692).centerHorizontally();
    public int pickaxeslot = 0;
    public int oreslot = 1;
    public Material oremat = Material.REDSTONE_ORE;
    public Material outputmat = Material.REDSTONE;

    public TaskMineOres() {
        super(100);
    }

    public void run() {
        try {
            while (true) {
                getItems();
                ai.tick();
                while (Main.self.getInventory().getSlot(37) != null
                        && Main.self.getInventory().getSlot(37).getType() == oremat) {
                    breakOre();
                }
                ai.tick();
                emptyInventory();
            }
        } catch (InterruptedException ex) {
        }
    }

    public void breakOre() throws InterruptedException {
        Main.self.selectSlot(pickaxeslot);
        while (KaiTools.checkToolHealth() == false) {
            System.out.println("TOOLHEALTH LOW");
            ai.tick(10);
        }
        ai.mineBlock(MINING_LOC);
        ai.tick();
        Main.self.selectSlot(oreslot);
        Main.self.placeBlock(MINING_LOC, BlockFace.UP);
    }

    public void emptyInventory() throws InterruptedException {
        ai.openContainer(OUTPUT_CHEST_LOC);
        for (int i = 54; i <= 89; i++) {
            if (Main.self.getInventory().getSlot(i) != null
                    && Main.self.getInventory().getSlot(i).getType() == outputmat) {
                ai.depositSlot(i);
            }
        }
        ai.closeContainer();
    }

    public void getItems() throws InterruptedException {
        ai.openContainer(INPUT_CHEST_LOC);
        ai.tick();
        if (Main.self.getInventory().getSlot(82) == null || Main.self.getInventory().getSlot(82).getType() != oremat) {
            for (int i = 0; i < 53; i++) {
                if (Main.self.getInventory().getSlot(i) != null && Main.self.getInventory().getSlot(i).getType() == oremat) {
                    ai.swapItems(i, 82);
                    break;
                }
            }
        }
        ai.closeContainer();
    }
}
