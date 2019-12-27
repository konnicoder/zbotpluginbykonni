/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package konni.konniskot.BotEdit;

import java.util.logging.Level;
import java.util.logging.Logger;
import jdk.nashorn.internal.ir.ContinueNode;
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

    public TaskPyramid(int x, int y, int z, int size) {
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

            //START LAYER
            Location startlocation = new Location(ScanAnker1X + 2, ScanAnker1Y, ScanAnker1Z - 1).centerHorizontally();
            ai.tick();
            ai.moveTo(startlocation);
            ai.tick();

            ai.tick(2);

            for (Y = CenterPointY; Y <= CenterPointY + (height - 1); Y++) {
                X = ScanAnker1X;
                Y = ScanAnker1Y;
                Z = ScanAnker1Z;

                findAndPlace(buildblock, ScanAnker1X, ScanAnker1Y, ScanAnker1Z, BlockFace.UP);
                ai.tick(2);

                for (Z = ScanAnker1Z + 1; Z <= ScanAnker2Z; Z++) {

                    findAndPlace(buildblock, X, Y, Z, BlockFace.SOUTH);
                    walklocation = new Location(X, Y + 1, Z).centerHorizontally();
                    ai.tick();
                    ai.moveTo(walklocation);
                    ai.tick();
                }
                Z = ScanAnker2Z;

                for (X = ScanAnker1X - 1; X >= ScanAnker2X; X--) {

                    findAndPlace(buildblock, X, Y, Z, BlockFace.WEST);
                    walklocation = new Location(X, Y + 1, Z).centerHorizontally();
                    ai.moveTo(walklocation);
                    ai.tick();
                }
                X = ScanAnker2X;

                for (Z = ScanAnker2Z - 1; Z >= ScanAnker1Z; Z--) {

                    walklocation = new Location(X, Y + 1, Z).centerHorizontally();
                    ai.tick();
                    ai.moveTo(walklocation);
                    ai.tick();
                    findAndPlace(buildblock, X, Y, Z, BlockFace.NORTH);
                }
                Z = ScanAnker1Z;

                for (X = ScanAnker2X + 1; X < ScanAnker1X; X++) {

                    findAndPlace(buildblock, X, Y, Z, BlockFace.EAST);
                    walklocation = new Location(X, Y + 1, Z).centerHorizontally();
                    ai.moveTo(walklocation);
                    ai.tick();
                }
                X = ScanAnker2X;
                ai.tick();
                findAndPlace(helpblock, ScanAnker1X - 1, ScanAnker1Y, ScanAnker1Z + 1, BlockFace.WEST);
                ai.tick(3);

                ScanAnker1X--;
                ScanAnker1Y++;
                ScanAnker1Z++;

                ScanAnker2X++;
                ScanAnker2Y++;
                ScanAnker2Z--;
            }

        } catch (InterruptedException ex) {
        }

    }

    public void findAndPlace(Material mat, int xpos, int ypos, int zpos, BlockFace bface) throws InterruptedException {
        while (testStackAvaliable(mat, 37) == false) {
            if (testStackAvaliable(mat, 37) == false) {
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
        }
        if (Main.self.getInventory().getSlot(37).getType() == mat) {
            Main.self.placeBlock(xpos, ypos, zpos, bface);

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
