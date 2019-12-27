/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package konni.konniskot.BotEdit;

import java.util.logging.Level;
import java.util.logging.Logger;
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
public class TaskPyramidBFS extends Task {

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

    public TaskPyramidBFS(int x, int y, int z, int size) {
        super(100);
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
            getMaterialChest();
            //START LAYER
            Location startlocation = new Location(ScanAnker1X + 2, ScanAnker1Y, ScanAnker1Z - 1).centerHorizontally();
            ai.tick();
            ai.moveTo(startlocation);
            ai.tick();

            ai.tick(2);

            for (Y = CenterPointY; Y <= CenterPointY + (height - 1); Y++) {
                X = ScanAnker1X;

                Z = ScanAnker1Z;

                findAndPlace(buildblock, ScanAnker1X, ScanAnker1Y, ScanAnker1Z, BlockFace.UP);
                ai.tick(2);

                for (Z = ScanAnker1Z + 1; Z <= ScanAnker2Z; Z++) {
                    tryPlacePyramidBlock(X, Y, Z, BlockFace.SOUTH);
                }
                Z = ScanAnker2Z;

                for (X = ScanAnker1X - 1; X >= ScanAnker2X; X--) {

                    tryPlacePyramidBlock(X, Y, Z, BlockFace.WEST);
                }
                X = ScanAnker2X;

                for (Z = ScanAnker2Z - 1; Z >= ScanAnker1Z; Z--) {

                    tryPlacePyramidBlock(X, Y, Z, BlockFace.NORTH);
                }
                Z = ScanAnker1Z;

                for (X = ScanAnker2X + 1; X < ScanAnker1X; X++) {

                    tryPlacePyramidBlock(X, Y, Z, BlockFace.EAST);
                }
                X = ScanAnker2X;
                ai.tick();
                findAndPlace(buildblock, ScanAnker1X - 1, ScanAnker1Y, ScanAnker1Z + 1, BlockFace.UP);
                ai.tick(3);

                ScanAnker1X--;
                ScanAnker1Y++;
                ScanAnker1Z++;

                ScanAnker2X++;
                ScanAnker2Y++;
                ScanAnker2Z--;
            }
            Main.self.sendChat("penis");

        } catch (InterruptedException ex) {
        }
    }

    public void tryPlacePyramidBlock(int X, int Y, int Z, BlockFace bface) throws InterruptedException {
        Location loc = new Location(X, Y, Z);
        Location temploc;
        Main.self.selectSlot(1);
        if (Main.self.getEnvironment().getBlockAt(loc).getType().isAir()
                && (temploc = KaiTools.BFSScan((l) -> {
                    if (l.distanceTo(loc) < 1) {
                        return false;
                    }
                    try {

                        return !Main.self.getEnvironment().getBlockAt(loc.getRelative(0, -1, 0)).getType().isAir() && ai.navigateTo(l.centerHorizontally());
                    } catch (InterruptedException ex) {
                        return false;
                    }
                }, X, Y, Z, 2)) != null) {
            System.out.println("gehe zu "+temploc);
            ai.tick(20);
            findAndPlace(buildblock, X, Y, Z, bface);
            ai.tick(10);
        }

    }

    public void findAndPlace(Material mat, int xpos, int ypos, int zpos, BlockFace bface) throws InterruptedException {
        /*
        while (testStackAvaliable(mat, 37) == false) {

            if (searchininventory(mat) == false) {
                getMaterialsFromChest();
                System.out.println("get Mat from chest");
            } else {

                for (int slot = 9; slot <= 44; slot++) {
                    if (Main.self.getInventory().getSlot(slot) != null && Main.self.getInventory().getSlot(slot).getType() == mat) {
                        ai.swapItems(slot, 37);
                        System.out.println("item found and moved");
                    }
                }
                System.out.println("should be moved");
            }

        }
         */
        if (Main.self.getInventory().getSlot(37).getType() == mat) {
            Main.self.placeBlock(xpos, ypos, zpos, bface);
            System.out.println("bauloc: "xpos + " " + ypos + " " + zpos);

        } else {
            System.out.println("i am holding the wrong block");
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
        Materialchest = KaiTools.Scan(Material.CHEST, x, y, z, 2, 5);

    }

    public void getMaterialsFromChest() {
        int MatchestX = (int) Materialchest.getX();
        int MatchestY = (int) Materialchest.getY();
        int MatchestZ = (int) Materialchest.getZ();

//Location chestloc, Location chestwalk, Material mat
    }

}
