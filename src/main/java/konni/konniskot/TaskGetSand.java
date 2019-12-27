/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package konni.konniskot;

import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.server.NBTTagCompound;
import zedly.zbot.BlockFace;
import zedly.zbot.Location;
import zedly.zbot.Material;
import zedly.zbot.inventory.ItemStack;

/**
 *
 * @author Konstantin
 */
public class TaskGetSand extends Task {

    double CenterPointX = 0;
    double CenterPointY = 0;
    double CenterPointZ = 0;

    int ScanAnker1X = 0;
    int ScanAnker1Y = 0;
    int ScanAnker1Z = 0;

    int ScanAnker2X = 0;
    int ScanAnker2Y = 0;
    int ScanAnker2Z = 0;
    public Location aktuell = null;

    private static final Location SPAWN_SANDFARM_WALK = new Location(-19, 80, -46).centerHorizontally();
    private static final Location SPAWN_SANDFARM_SIGN = new Location(-19, 80, -47).centerHorizontally();
    private static final Location SANDFARM_LIFTSIGN_WALK = new Location(9613, 63, 7765).centerHorizontally();
    private static final Location SANDFARM_LIFTSIGN_1 = new Location(9612, 64, 7765).centerHorizontally();
    private static final Location SANDFARM_LIFTSIGN_2 = new Location(9612, 85, 7766).centerHorizontally();
    private static final Location SANDFARM_LIFTSIGN_3 = new Location(9612, 106, 7765).centerHorizontally();
    private static final Location SANDFARM_FLOOR_6_START = new Location(9612, 144, 7761).centerHorizontally();

    private static final Location SAND_TESSERACT_LOC = new Location(216, 138, -8665).centerHorizontally();
    private static final Location SAND_TESSERACT_WALK = new Location(216, 137, -8666).centerHorizontally();

    private static final Location waypoint0 = new Location(260, 137, -8711).centerHorizontally();
    private static final Location waypoint1 = new Location(260, 137, -8711).centerHorizontally();
    private static final Location waypoint2 = new Location(229, 137, -8711).centerHorizontally();

    public TaskGetSand() {
        super(100);

    }

    public void run() {
        try {
            if (Main.self.getLocation().distanceTo(SANDFARM_FLOOR_6_START) > 100) {
                gotoSandfarm();
            } else {
                ai.moveTo(SANDFARM_FLOOR_6_START);
            }
            System.out.println("get Sand");
            ai.tick(5);
            inSandfarm();
            ai.tick();
            Main.self.sendChat("Inventory full;");
            //ai.tick();
            //fillTesseract();
        } catch (InterruptedException ex) {

        }

    }

    public boolean checkToolHealth() {
        ItemStack is = Main.self.getInventory().getItemInHand();
        if (is.getNbt() instanceof NBTTagCompound) {
            NBTTagCompound nbt = (NBTTagCompound) is.getNbt();
            int damage = nbt.getInteger("Damage");

            if (damage < 1400) {
                return true;
            }
        }
        Main.self.sendChat("tool health low");
        return false;
    }

    public void fillTesseract() throws InterruptedException {
        Main.self.selectSlot(0);
        ai.tick();
        Main.self.sendChat("/home xp");
        ai.tick(20);
        ai.moveTo(waypoint1);
        ai.tick(5);
        ai.moveTo(waypoint2);
        ai.tick(3);
        ai.moveTo(SAND_TESSERACT_WALK);
        Main.self.sneak(true);
        ai.tick();
        Main.self.placeBlock(SAND_TESSERACT_LOC, BlockFace.EAST);
        ai.tick();
        Main.self.sneak(false);
    }

    public void gotoSandfarm() throws InterruptedException {

        Main.self.sendChat("/spawn");
        ai.tick(20);
        ai.moveTo(SPAWN_SANDFARM_WALK);
        Main.self.placeBlock(SPAWN_SANDFARM_SIGN, BlockFace.EAST);
        ai.tick(20);
        ai.moveTo(SANDFARM_LIFTSIGN_WALK);
        ai.tick(10);
        Main.self.placeBlock(SANDFARM_LIFTSIGN_1, BlockFace.EAST);
        ai.tick(10);
        ai.moveTo(Main.self.getLocation().getRelative(0, -1, 0));
        ai.tick(10);
        Main.self.placeBlock(SANDFARM_LIFTSIGN_2, BlockFace.EAST);
        ai.tick(10);
        ai.moveTo(Main.self.getLocation().getRelative(0, -1, 0));
        ai.tick(10);
        Main.self.placeBlock(SANDFARM_LIFTSIGN_3, BlockFace.EAST);
        ai.tick(10);
        ai.moveTo(Main.self.getLocation().getRelative(0, -1, 0));
        ai.tick(10);
        ai.moveTo(SANDFARM_FLOOR_6_START);
        ai.tick(10);
    }

    public void inSandfarm() {
        System.out.println("Starting farm Sand");
        Main.self.selectSlot(2);
        try {
            int Zachse;
            while (InventoryUtil.countFreeStorageSlots(true, false) > 0 && checkToolHealth()==true) {

                for (int Xachse = 9610; Xachse > 9570; Xachse = Xachse - 5) {

                    for (Zachse = 7759; Zachse > 7720; Zachse--) {

                        aktuell = Main.self.getLocation();
                        double X = aktuell.getX();
                        double Y = aktuell.getY();
                        double Z = aktuell.getZ();

                        int x = (int) (X + 0.5);
                        int y = (int) (Y + 0.5);
                        //System.out.println(y);
                        int z = (int) (Z + 0.5);
                        if (InventoryUtil.countFreeStorageSlots(true, false) == 0) {
                            break;
                        }

                        while (KaiTools.Scan(Material.SAND, x, y, z, 3, 3) != null) {
                            ai.breakBlock(KaiTools.Scan(Material.SAND, x, y, z, 3, 3), 1);

                        }
                        while (KaiTools.Scan(Material.RED_SAND, x, y, z, 3, 3) != null) {
                            ai.breakBlock(KaiTools.Scan(Material.RED_SAND, x, y, z, 3, 3), 1);

                        }
                        Location walk = new Location(Xachse, Y, Zachse);
                        ai.moveTo(walk);
                        //WALK
                    }
                }
            }
            System.out.println("Inventory full");

        } catch (InterruptedException ex) {
        }
    }

    public void cancel() {
    }
}
