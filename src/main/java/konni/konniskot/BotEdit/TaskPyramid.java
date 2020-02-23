/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package konni.konniskot.BotEdit;

import java.util.logging.Level;
import java.util.logging.Logger;
import jdk.nashorn.internal.ir.ContinueNode;
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
public class TaskPyramid extends Task {

    public int CenterPointX;
    public int CenterPointY;
    public int CenterPointZ;

    public int ScanAnker1X;
    public int ScanAnker1Y;
    public int ScanAnker1Z;

    int ScanAnker2X;
    int ScanAnker2Y;
    int ScanAnker2Z;

    int height;
    public Material buildblock = Material.GOLD_BLOCK;
    public Material helpblock = Material.OAK_LEAVES;
    public Location walklocation;
    public Location Materialchest;
    public Location Materialchestwalk;

    public TaskPyramid(int x, int y, int z, int size) {
        super(25);
        CenterPointX = x;
        CenterPointY = y;
        CenterPointZ = z;
        height = size;
    }

    public void run() {
        System.out.println("building pyramid");

        ScanAnker1X = CenterPointX + (height - 1);
        ScanAnker1Y = CenterPointY;
        ScanAnker1Z = CenterPointZ - (height - 1);

        ScanAnker2X = CenterPointX - (height - 1);
        ScanAnker2Y = CenterPointY;
        ScanAnker2Z = CenterPointZ + (height - 1);

        int X = ScanAnker1X;
        int Y = CenterPointY;
        int Z = ScanAnker1Z;

        try {
            //START LAYER
            Main.self.selectSlot(1);
            Location startlocation = new Location(ScanAnker1X + 1, ScanAnker1Y, ScanAnker1Z - 1).centerHorizontally();
            ai.tick();
            System.out.println(ScanAnker1X + " " + ScanAnker1Y + " " + ScanAnker1Z);
            ai.navigateTo(startlocation);
            ai.tick();
            getMaterialChest();
            ai.tick();
            getMaterialsFromChest();
            ai.tick();
            ai.navigateTo(startlocation);
            ai.tick(2);

            for (Y = CenterPointY; Y <= CenterPointY + (height - 1); Y++) {
                X = ScanAnker1X;
                Y = ScanAnker1Y;
                Z = ScanAnker1Z;
                if (Main.self.getEnvironment().getBlockAt(ScanAnker1X, ScanAnker1Y, ScanAnker1Z).getType() == Material.AIR) {
                    findAndPlace(buildblock, ScanAnker1X, ScanAnker1Y, ScanAnker1Z, BlockFace.UP);
                }
                ai.tick(2);

                for (Z = ScanAnker1Z + 1; Z <= ScanAnker2Z; Z++) {
                    if (Main.self.getEnvironment().getBlockAt(X, Y, Z).getType() == Material.AIR) {
                        findAndPlace(buildblock, X, Y, Z, BlockFace.SOUTH);
                    }

                    walklocation = new Location(X + 1, Y, Z).centerHorizontally();
                    ai.tick();
                    ai.moveTo(walklocation);
                    ai.tick();
                }
                Z = ScanAnker2Z;

                for (X = ScanAnker1X - 1; X >= ScanAnker2X; X--) {
                    if (Main.self.getEnvironment().getBlockAt(X, Y, Z).getType() == Material.AIR) {
                        findAndPlace(buildblock, X, Y, Z, BlockFace.WEST);
                    }

                    ai.tick();
                    walklocation = new Location(X, Y, Z + 1).centerHorizontally();
                    ai.moveTo(walklocation);
                    ai.tick();
                }
                X = ScanAnker2X;

                for (Z = ScanAnker2Z - 1; Z >= ScanAnker1Z; Z--) {

                    ai.tick();
                    ai.moveTo(walklocation);
                    ai.tick();
                    if (Main.self.getEnvironment().getBlockAt(X, Y, Z).getType() == Material.AIR) {
                        findAndPlace(buildblock, X, Y, Z, BlockFace.NORTH);
                    }
                    ai.tick();

                    ai.tick();
                    walklocation = new Location(X - 1, Y, Z).centerHorizontally();
                    ai.tick();
                    ai.moveTo(walklocation);
                }
                Z = ScanAnker1Z;

                for (X = ScanAnker2X + 1; X < ScanAnker1X; X++) {
                    if (Main.self.getEnvironment().getBlockAt(X, Y, Z).getType() == Material.AIR) {
                        findAndPlace(buildblock, X, Y, Z, BlockFace.EAST);
                    }

                    ai.tick();
                    walklocation = new Location(X, Y, Z - 1).centerHorizontally();
                    ai.moveTo(walklocation);
                    ai.tick();
                }
                X = ScanAnker2X;
                ai.tick();
                if (Main.self.getEnvironment().getBlockAt(X, Y, Z).getType() == Material.AIR) {
                    findAndPlace(helpblock, ScanAnker1X - 1, ScanAnker1Y, ScanAnker1Z + 1, BlockFace.WEST);
                }
                ai.tick(3);

                ScanAnker1X--;
                ScanAnker1Y++;
                ScanAnker1Z++;

                ScanAnker2X++;
                ScanAnker2Y++;
                ScanAnker2Z--;
                ai.tick(3);
                getMaterialsFromChest();
                ai.tick(3);
            }

        } catch (InterruptedException ex) {
        }

    }

    public void findAndPlace(Material mat, int xpos, int ypos, int zpos, BlockFace bface) throws InterruptedException {
        // while (testStackAvaliable(mat, 37) == false) {
        if (testStackAvaliable(mat, 37) == false) {
            if (searchininventory(mat) == false) {
                System.out.println("inventory empty");
                getMaterialsFromChest();
                ai.tick(2);

            } else {

                for (int slot = 9; slot <= 44; slot++) {
                    if (Main.self.getInventory().getSlot(slot) != null && Main.self.getInventory().getSlot(slot).getType() == mat) {
                        ai.swapItems(slot, 37);
                        System.out.println("item found and moved");
                        break;
                    }
                }
                System.out.println("should be moved");
            }
        }
        int goldblocks = InventoryUtil.count(Material.GOLD_BLOCK, true, false);
        System.out.println(goldblocks);
        if (goldblocks <= 280) {
            Main.self.sendChat("/msg Konni999 goldblocks at" + goldblocks + ", ding Konni");
        }

        // if (InventoryUtil.countFreeStorageSlots(true, false)>10) {
        //    Main.self.sendChat("/msg Konni999 Goldblocks in inventory low");
        //}
        //}
        while (Main.self.getEnvironment().getBlockAt(xpos, ypos, zpos).getType() != Material.GOLD_BLOCK) {

            Main.self.placeBlock(xpos, ypos, zpos, bface);
            ai.tick();

        }
    }

    public boolean testStackAvaliable(Material mat, int slot) {
        if (Main.self.getInventory().getSlot(slot) != null && Main.self.getInventory().getSlot(slot).getType() == mat) {
            return true;
        } else {
            return false;
        }
    }

    public boolean searchininventory(Material mat) throws InterruptedException {
        System.out.println("search inv");
        for (int slot = 9; slot <= 44; slot++) {
            if (Main.self.getInventory().getSlot(slot) != null && Main.self.getInventory().getSlot(slot).getType() == mat) {
                //ai.transferItem(slot, 37);
                System.out.println("item found and moved");
                return true;
            }

        }
        return false;
    }

    public void getMaterialChest() {
        Location aktuell = new Location();
        aktuell = Main.self.getLocation();
        int x = (int) aktuell.getX();
        int y = (int) aktuell.getY();
        int z = (int) aktuell.getZ();
        Materialchest = KaiTools.Scan(Material.CHEST, x, y, z, 20, 20);
       // Materialchest = new Location(-1293, 185, -3691);
        Materialchestwalk = Materialchest.getRelative(-2, 0, 0).centerHorizontally();
        System.out.println("Materialchest set at: " + Materialchest);

    }

    public void getMaterialsFromChest() throws InterruptedException {

        ai.tick();

        ai.navigateTo(Materialchestwalk);
        ai.tick();
        if (ai.openContainer(Materialchest)) {
            ai.tick(3);
            emptyChest();
        }
        ai.tick(3);

        ai.tick(3);
        ai.closeContainer();
        ai.tick();

//Location chestloc, Location chestwalk, Material mat
    }

    public void emptyChest() throws InterruptedException {
        while (InventoryUtil.countFreeStorageSlots(true, false) > 1 && InventoryUtil.count(Material.GOLD_BLOCK, false, true) > 0) {
            for (int slot = 0; slot <= 53; slot++) {
                ai.withdrawSlot(slot);
            }
            break;

        }
    }

}
