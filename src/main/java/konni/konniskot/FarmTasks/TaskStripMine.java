/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package konni.konniskot.FarmTasks;

import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import konni.konniskot.InventoryUtil;
import konni.konniskot.KaiTools;
import konni.konniskot.Main;
import konni.konniskot.Task;
import static konni.konniskot.KaiTools.BFSScan;
import zedly.zbot.BlockFace;
import zedly.zbot.Location;
import zedly.zbot.Material;
import zedly.zbot.environment.Block;

/**
 *
 * @author Konstantin
 */
public class TaskStripMine extends Task {

    public int Y;
    private int laserrepeats = 20;
    public int step;
    private static final HashSet<Material> ores = new HashSet<>();
    private static final HashSet<Material> nonwalkables = new HashSet<>();

    public TaskStripMine() {
        super(28);

    }

    public void run() {
        step = 0;
        Location aktuell = Main.self.getLocation();
        int x = (int) aktuell.getX();
        Y = (int) aktuell.getY();
        int z = (int) aktuell.getZ();

        try {
            KaiTools.modeSelect("tall", ai);
            ai.tick();
            while (true) {
                doLeftSide();
                ai.tick();
                doRightSide();
                ai.tick();
                CheckAndDump();
                doStep();
                ai.tick();
            }

        } catch (InterruptedException ex) {
        }
        unregister();
    }

    public void doLeftSide() throws InterruptedException {
        Main.self.selectSlot(0);
        Main.self.lookAt(180, 0);
        for (int r = laserrepeats; r > 0; r--) {
            Main.self.lookAt(180, 0);
            Main.self.placeBlock(Main.self.getLocation(), BlockFace.EAST);
            ai.tick();
        }
    }

    public void doRightSide() throws InterruptedException {
        Main.self.selectSlot(0);
        Main.self.lookAt(180, 0);
        for (int r = laserrepeats; r > 0; r--) {
            Main.self.lookAt(0, 0);
            Main.self.placeBlock(Main.self.getLocation(), BlockFace.EAST);
            ai.tick();
        }
    }

    public void doStep() throws InterruptedException {
        ai.moveTo(Main.self.getLocation().getRelative(-1, 0, 0));
        ai.tick();
    }

    public Location checkNeightbourBlocks(int x, int y, int z) throws InterruptedException {

        Location locup = new Location(x, y + 1, z);
        Location locdown = new Location(x, y - 1, z);
        Location locxpos = new Location(x + 1, y, z);
        Location locxneg = new Location(x - 1, y, z);
        Location loczpos = new Location(x, y, z + 1);
        Location loczneg = new Location(x, y, z - 1);
        KaiTools.modeSelect("ore", ai);
        ai.tick();
        if (ores.contains(Main.self.getEnvironment().getBlockAt(locup).getType())) {
            return locup;
        }
        if (ores.contains(Main.self.getEnvironment().getBlockAt(locdown).getType())) {
            return locdown;
        }
        if (ores.contains(Main.self.getEnvironment().getBlockAt(locxpos).getType())) {
            return locxpos;
        }
        if (ores.contains(Main.self.getEnvironment().getBlockAt(locxneg).getType())) {
            return locxneg;
        }
        if (ores.contains(Main.self.getEnvironment().getBlockAt(loczpos).getType())) {
            return loczpos;
        }
        if (ores.contains(Main.self.getEnvironment().getBlockAt(loczneg).getType())) {
            return locxneg;
        }
        return null;
    }

    public void CheckAndDump() throws InterruptedException {
        int dumpeditemstacks = 0;
        System.out.println("CHECK AND DUMP");
        if (InventoryUtil.countFreeStorageSlots(true, false) < 3) {
            if (KaiTools.lookInInventoryAndMove(Material.CHEST, 37, ai) == false) {
                while (true) {
                    Main.self.sendChat("/msg Konni999 ERROR");
                    ai.tick(200);
                }
            }
            Location tempchest = Main.self.getLocation().getRelative(2, 0, 0);
            Main.self.selectSlot(1);
            Main.self.placeBlock(tempchest, BlockFace.UP);
            ai.tick();
            ai.openContainer(tempchest);
            ai.tick();
            for (int slot = 27; slot <= 62; slot++) {
                if (Main.self.getInventory().getSlot(slot) != null
                        && ores.contains(Main.self.getInventory().getSlot(slot).getType())) {
                    if (dumpeditemstacks < 26) {
                        ai.depositSlot(slot);
                        dumpeditemstacks++;
                    }
                }
            }
            ai.closeContainer();
            ai.tick();
            Main.self.sendChat("/cremove");
            ai.tick();
            Main.self.clickBlock(tempchest);
            ai.tick();
            Main.self.selectSlot(0);
        }
    }

    static {
        ores.add(Material.COAL_ORE);
        ores.add(Material.IRON_ORE);
        ores.add(Material.GOLD_ORE);
        ores.add(Material.REDSTONE_ORE);
        ores.add(Material.EMERALD_ORE);
        ores.add(Material.DIAMOND_ORE);
        ores.add(Material.STONE);
        ores.add(Material.GRANITE);
        ores.add(Material.ANDESITE);
        ores.add(Material.GRAVEL);
        ores.add(Material.DIORITE);
        ores.add(Material.DIRT);
    }
}
