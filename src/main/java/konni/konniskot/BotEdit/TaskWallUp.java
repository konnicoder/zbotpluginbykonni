/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package konni.konniskot.BotEdit;

import java.util.logging.Level;
import java.util.logging.Logger;
import konni.konniskot.InventoryUtil;
import konni.konniskot.KaiTools;
import konni.konniskot.Main;
import konni.konniskot.Task;
import zedly.zbot.BlockFace;
import zedly.zbot.Location;
import zedly.zbot.Material;
import zedly.zbot.event.block.BlockChangeEvent;

/**
 *
 * @author Konstantin
 */
public class TaskWallUp extends Task {

    private static final Location RES_WALK_LOC = new Location(176, 139, -8690).centerHorizontally();
    private static final Location STONE_TESS_LOC = new Location(175, 140, -8690).centerHorizontally();

    int error = 0;
    int targetyheight;
    int debug = 0;
    String operator;
    Material wallmat = Material.OBSIDIAN;

    public TaskWallUp(int yheight, String user) {
        super(30);
        targetyheight = yheight;
        operator = user;
    }

    public void run() {

        System.out.println("Starting TaskWallUp");
        try {
            wall();
        } catch (InterruptedException ex) {
        }

        KaiTools.messageMaster(operator, "done");
        System.out.println("done");
        Main.self.sendChat("done, ding konni");
        unregister();
    }

    public boolean wall() throws InterruptedException {
        for (int y = Main.self.getLocation().getBlockY(); y < targetyheight + 1; y++) {
            while (true) {
                if (error > 3) {
                    KaiTools.messageMaster(operator, "error detected");
                    return false;
                }
                trySetBlock();
                if (error > 0) {
                    error--;
                }
                if (trySetBlock() == false) {

                    if (StackUp(Main.self.getLocation()) == false) {
                        break;
                    }

                    break;
                }
            }
        }
        return true;
    }

    public boolean trySetBlock() throws InterruptedException {

        if (getLocationAndPlace(Main.self.getLocation().getRelative(+1, 0, 0).centerHorizontally()) == true) {
            return true;
        }
        if (getLocationAndPlace(Main.self.getLocation().getRelative(-1, 0, 0).centerHorizontally()) == true) {
            return true;
        }
        if (getLocationAndPlace(Main.self.getLocation().getRelative(0, 0, +1).centerHorizontally()) == true) {
            return true;
        }
        if (getLocationAndPlace(Main.self.getLocation().getRelative(0, 0, -1).centerHorizontally()) == true) {
            return true;
        }
        //ab hier experiment
        if (getLocationAndPlace(Main.self.getLocation().getRelative(+1, 0, +1).centerHorizontally()) == true) {
            return true;
        }
        if (getLocationAndPlace(Main.self.getLocation().getRelative(+1, 0, -1).centerHorizontally()) == true) {
            return true;
        }
        if (getLocationAndPlace(Main.self.getLocation().getRelative(-1, 0, +1).centerHorizontally()) == true) {
            return true;
        }
        if (getLocationAndPlace(Main.self.getLocation().getRelative(-1, 0, -1).centerHorizontally()) == true) {
            return true;
        }

        return false;
    }

    public boolean getLocationAndPlace(Location locwalk) throws InterruptedException {

        if (Main.self.getEnvironment().getBlockAt(locwalk.getRelative(0, -1, 0)).getType() == Material.AIR
                && Main.self.getEnvironment().getBlockAt(locwalk.getRelative(0, -2, 0)).getType() == wallmat) {

            while (InventoryUtil.count(wallmat, true, false) <= 2) {
                KaiTools.messageMaster(operator, "Material neede testIfSet");
                System.out.println("Material needed testifset");
                ai.tick();
            }

            placeBlockInv(locwalk.getRelative(0, -1, 0).centerHorizontally(), BlockFace.DOWN);
            ai.tick();
            ai.moveTo(locwalk.centerHorizontally());

            return true;
        }
        return false;
    }

    public boolean placeBlockInv(Location loc, BlockFace face) throws InterruptedException {
        //System.out.println("placeBlockInv");
        testIfItemAvaliable();
        Main.self.placeBlock(loc, BlockFace.DOWN);

        BlockChangeEvent be = ai.waitForEvent(BlockChangeEvent.class, 1500);

        //System.out.println(be);
        while (Main.self.getEnvironment().getBlockAt(loc).getType() == Material.AIR) {
            ai.tick(5);
            if (Main.self.getEnvironment().getBlockAt(loc).getType() == wallmat) {
                System.out.println("break");
                break;
            }
            Main.self.placeBlock(loc, BlockFace.DOWN);
            ai.tick();

            System.out.println("whileplaceblock");
            debug++;
            System.out.println(debug);
            if (debug >= 15) {
                ai.tick(50);
                Main.self.placeBlock(loc, BlockFace.DOWN);
                debug = 0;
                KaiTools.messageMaster(operator, "debugged placement");
                System.out.println(debug);
                //getResources();
            }
        }
        debug = 0;
        return true;

    }

    public void testIfItemAvaliable() throws InterruptedException {
        if (InventoryUtil.count(wallmat, true, false) <= 70) {
            ai.tick(20);
            KaiTools.messageMaster(operator, "getting materials");
            System.out.println("getting materials placeblock");

            getResources();
            ai.tick(10);
        }
        while (KaiTools.testStackAvaliable(wallmat, 36) == false) {
            if (KaiTools.lookInInventoryAndMove(wallmat, 36, ai) == false) {
                System.out.println("cant find items");
                System.out.println(InventoryUtil.count(wallmat, true, false));
            }
        }

    }

    public boolean StackUp(Location loc) throws InterruptedException {
        if (Main.self.getLocation().getY() == targetyheight) {
            return false;
        }
        if (InventoryUtil.count(wallmat, true, false) <= 3) {
            getResources();
            ai.tick();
        }
        error = error + 3;
        ai.moveTo(loc.getRelative(0, 1, 0));
        ai.tick(2);
        placeBlockInv(loc, BlockFace.DOWN);
        ai.tick();

        return true;
    }

    public void getResources() throws InterruptedException {
        System.out.println("getResources");
        error = 5;
//        Main.self.sendChat("/sethome temp");
//        ai.tick(5);
//        Main.self.sendChat("/home res");
//        ai.tick(40);
//        if (Main.self.getLocation().distanceTo(RES_WALK_LOC) > 0.1) {
//            ai.moveTo(RES_WALK_LOC);
//            ai.tick(5);
//        }
//        while (InventoryUtil.countFreeStorageSlots(true, false) >= 1) {
//            Main.self.clickBlock(STONE_TESS_LOC);
//            ai.tick(10);
//        }
//        ai.tick(10);
//        Main.self.sendChat("/home temp");
//        ai.tick(40);

    }
}
