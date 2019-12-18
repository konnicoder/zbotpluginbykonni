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
public class TaskCraftKelpBlocks extends Task {

    private static final Location KELP_INPUT_WALK = new Location(255, 137, -8603).centerHorizontally();
    private static final Location KELP_INPUT_TESS = new Location(256, 138, -8603).centerHorizontally();
    private static final Location KELP_CRAFTING_WALK = new Location(255, 137, -8602).centerHorizontally();
    private static final Location KELP_CRAFTING_LOC = new Location(257, 138, -8602).centerHorizontally();
    private static final Location KELP_OUTPUT_WALK = new Location(255, 137, -8604).centerHorizontally();
    private static final Location KELP_OUTPUT_TESS = new Location(256, 138, -8604).centerHorizontally();
    private static final Location KELP_OUTPUT_CHEST = new Location(255, 137, -8601).centerHorizontally();

    private static final Location waypoint1 = new Location(229, 137, -8711).centerHorizontally();
    private static final Location waypoint2 = new Location(229, 137, -8631).centerHorizontally();
    
    public TaskCraftKelpBlocks() {
        super(100);
    }

    public void run() {

        try {
            if (Main.self.getLocation().distanceTo(KELP_CRAFTING_LOC) > 10) {
                Main.self.sendChat("/home xp");
                ai.tick(5);
                ai.moveTo(waypoint1);
                ai.tick(5);
                ai.moveTo(waypoint2);
                ai.tick(5);
            }
            while (true) {
                ai.moveTo(KELP_INPUT_WALK);
                ai.tick();
                ai.openContainer(KELP_OUTPUT_CHEST);
                //  if (InventoryUtil.count(Material.DRIED_KELP, false, true) > 640) {
                if (InventoryUtil.countFullStacks(Material.DRIED_KELP,0, 53) >= 9) {
                    int slot = 0;
                    while (InventoryUtil.count(Material.DRIED_KELP, true, false) < 576 && slot <= 53) {

                        if (Main.self.getInventory().getSlot(slot) != null && Main.self.getInventory().getSlot(slot).getType() == Material.DRIED_KELP && Main.self.getInventory().getSlot(slot).getAmount() == 64) {
                            System.out.println("Withdraw slot");
                            ai.withdrawSlot(slot);
                        }
                        slot++;
                        System.out.println(slot);

                    }
                    ai.closeContainer();
                    ai.tick();
                    craftBlock(Material.DRIED_KELP, KELP_CRAFTING_WALK, KELP_CRAFTING_LOC, KELP_INPUT_TESS, KELP_INPUT_WALK, KELP_OUTPUT_TESS, KELP_OUTPUT_WALK);
                    ai.tick();
                    Main.self.sneak(true);
                    ai.tick();
                    Main.self.placeBlock(KELP_INPUT_TESS, BlockFace.EAST);
                    ai.tick();
                    Main.self.sneak(false);

                } else {
                    ai.closeContainer();
                    System.out.println("not enough kelp");
                    ai.tick(300);
                    System.out.println("waiting 300 ticks");
                }

            }
        } catch (InterruptedException ex) {
        }
        unregister();
    }

    public void craftBlock(Material matin, Location walkcraft, Location craftingbench, Location inputtess, Location walkinput, Location outputtess, Location walkoutput) throws InterruptedException {
        ai.moveTo(walkinput);
        ai.tick();
        while (InventoryUtil.count(matin, true, false) < 9 * 64) {
            Main.self.clickBlock(inputtess);
            ai.tick(3);
        }
        ai.moveTo(walkcraft);

        ai.openContainer(craftingbench);
        int staticOffset = Main.self.getInventory().getStaticOffset();
        for (int crafting = 1; crafting <= 9; crafting++) {
            for (int local = staticOffset; local < staticOffset + 36; local++) {
                if (Main.self.getInventory().getSlot(local) != null
                        && Main.self.getInventory().getSlot(local).getType() == matin) {
                    ai.transferItem(local, crafting);
                    //System.out.println("MIAUUUU " + local + " " + crafting);

                    ai.tick(2);
                    break;
                }
            }
        }
        Main.self.getInventory().click(0, 1, 0);
        ai.closeContainer();
        ai.moveTo(walkoutput);
        ai.tick(3);
        Main.self.sneak(true);
        ai.tick();
        Main.self.placeBlock(outputtess, BlockFace.EAST);
        ai.tick();
        Main.self.sneak(false);
    }

}
