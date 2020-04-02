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

/**
 *
 * @author Konstantin
 */
public class TaskWallUp extends Task {

    int error = 0;

    public TaskWallUp() {
        super(15);
    }

    public void run() {
        try {
            wall();
        } catch (InterruptedException ex) {

        }
        System.out.println("done");
        unregister();
    }

    public boolean wall() throws InterruptedException {
        for (int y = Main.self.getLocation().getBlockY(); y < 96; y++) {
            while (true) {
                //System.out.println(error);
                if (error > 3) {
                    Main.self.sendChat("error detected");
                    return false;
                }
                trySetBlock();
                if (error > 0) {
                    error--;
                }
                if (trySetBlock() == false) {
                    if (trySetBlock() == false) {
                        if (StackUp(Main.self.getLocation()) == false) {
                            break;
                        }
                    }

                    ai.tick();
                    break;
                }

            }
        }
        return true;
    }

    public boolean trySetBlock() throws InterruptedException {
        while (InventoryUtil.count(Material.STONE, true, false) < 20) {
            System.out.println("STONE NEEDED");
            ai.tick(200);

        }

        if (testifset(Main.self.getLocation().getRelative(+1, 0, 0).centerHorizontally()) == true) {
            return true;
        }
        if (testifset(Main.self.getLocation().getRelative(-1, 0, 0).centerHorizontally()) == true) {
            return true;
        }
        if (testifset(Main.self.getLocation().getRelative(0, 0, +1).centerHorizontally()) == true) {
            return true;
        }
        if (testifset(Main.self.getLocation().getRelative(0, 0, -1).centerHorizontally()) == true) {
            return true;
        }

        return false;
    }

    public boolean testifset(Location locwalk) throws InterruptedException {
        if (Main.self.getEnvironment().getBlockAt(locwalk.getRelative(0, -1, 0)).getType() == Material.AIR && Main.self.getEnvironment().getBlockAt(locwalk.getRelative(0, -2, 0)).getType() == Material.STONE) {
            while (InventoryUtil.count(Material.STONE, true, false) <= 64) {
                System.out.println("Stone needed testifset");
                ai.tick();
                while (InventoryUtil.countFreeStorageSlots(true, false) > 1) {
                    ai.tick(40);
                }

            }

           
            placeBlockInv(locwalk.getRelative(0, -1, 0).centerHorizontally(),BlockFace.DOWN);
            ai.tick();

            ai.moveTo(locwalk.centerHorizontally());
            
            while (Main.self.getEnvironment().getBlockAt(locwalk.getRelative(0, -1, 0)).getType() == Material.AIR) {
                placeBlockInv(locwalk.getRelative(0, -1, 0).centerHorizontally(),BlockFace.DOWN);
                                       
            }
            

            return true;
        }
        return false;
    }

    public boolean placeBlockInv(Location loc, BlockFace face) throws InterruptedException {
        while (InventoryUtil.count(Material.STONE, true, false) <= 70) {
            ai.tick(200);
            Main.self.sendChat("/msg Konni999 Stone needed");
            while (InventoryUtil.countFreeStorageSlots(true, false) > 1) {
                ai.tick(40);
            }
        }
        if (KaiTools.testStackAvaliable(Material.STONE, 36) == false) {
            if (KaiTools.lookInInventoryAndMove(Material.STONE, 36, ai) == false) {
                System.out.println("cant find items");

                System.out.println(InventoryUtil.count(Material.STONE, true, false));
                while (InventoryUtil.count(Material.STONE, true, false) <= 20) {
                    ai.tick(90);
                    System.out.println("need stone");
                    ai.tick(90);
                    ai.tick(90);
                }
            }
        }

        Main.self.placeBlock(loc, BlockFace.DOWN);
        ai.tick();
        if (Main.self.getEnvironment().getBlockAt(loc).getType() == Material.AIR) {
            Main.self.placeBlock(loc, BlockFace.DOWN);
        }
        if (Main.self.getEnvironment().getBlockAt(loc).getType() == Material.AIR) {
            Main.self.placeBlock(loc, BlockFace.DOWN);
        }

        return true;

    }

    public boolean StackUp(Location loc) throws InterruptedException {
        error = error + 3;
        ai.moveTo(loc.getRelative(0, 1, 0));
        placeBlockInv(loc, BlockFace.UP);
        ai.tick(2);

        return true;
    }
}
