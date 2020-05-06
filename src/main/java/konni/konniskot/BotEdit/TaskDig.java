/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package konni.konniskot.BotEdit;

import java.util.HashSet;
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
public class TaskDig extends Task {

    public Location anker1;
    public Location anker2;
    public int X;
    public int Y;
    public int Z;
    public int X2;
    public int Y2;
    public int Z2;
    public int sliceskip;
    private static final HashSet<Material> gravityblocks = new HashSet<>();
    public int laserpickslot = 0;
    public int shovelslot = 1;
    public int sliceheight;

    public TaskDig(int x1, int y1, int z1, int x2, int y2, int z2, int sliceheight, int mode) {
        super(12);
        this.sliceheight = sliceheight;
        anker1 = new Location(x1, y1, z1).centerHorizontally();
        anker2 = new Location(x2, y2, z2).centerHorizontally();
        System.out.println("anker1 set to: " + anker1);
        System.out.println("anker2 set to: " + anker2);
        X = x1;
        Y = y1;
        Z = z1;
        X2 = x2;
        Y2 = y2;
        Z2 = z2;
        sliceskip = mode;
    }

    public void run() {
        System.out.println("starting TaskDig");
        System.out.println("sliceheight set to: " + sliceheight);
        try {
            ai.navigateTo(anker1);
        } catch (InterruptedException ex) {
           
        }
        for (int xachsenpunkt = X; xachsenpunkt > X2; xachsenpunkt = xachsenpunkt - sliceskip * 2) {
            for (int zachsenpunkt = Z; zachsenpunkt <= Z2; zachsenpunkt++) {
                try {
                    experiment(xachsenpunkt, zachsenpunkt);
                } catch (InterruptedException ex) {

                }
            }
            if (xachsenpunkt - sliceskip < X2) {
                break;
            }
            for (int zachsenpunkt = Z2; zachsenpunkt >= Z; zachsenpunkt--) {
                try {
                    experiment(xachsenpunkt - sliceskip, zachsenpunkt);
                } catch (InterruptedException ex) {

                }

            }

        }

        System.out.println("done");

    }
//25

    public void experiment(int x, int z) throws InterruptedException {
        Location walk = new Location(x, Y, z).centerHorizontally();
        Location laseraim = new Location(x, Y + 3, z);
        for (int r = 0; r < sliceheight; r++) {
           // Material mat = Main.self.getEnvironment().getBlockAt(x, Y + 2 + r, z).getType();
            //if (mat == Material.AIR || mat == Material.CAVE_AIR || mat == Material.VOID_AIR) {
             //   continue;
           // }
            if (Main.self.getLocation().distanceTo(walk) > 0.1) {
                ai.moveTo(walk);
            }
            Main.self.lookAt(0, -89);
            ai.tick(2);
            Main.self.placeBlock(laseraim, BlockFace.EAST);
        }
    }

    public void doSlice(int x, int z) throws InterruptedException {
        System.out.println("doslice");
        // Sindnochblöckedrüber
        // || checkGravityBlocks(x, z) == true
        while (KaiTools.checkIfBlocksAbove(x, Y, z, 64) == true) {
            while (checkGravityBlocks(x, z) == false) {
                Main.self.selectSlot(laserpickslot);
                Location laseraim = new Location(x, Y + 3, z);
                Main.self.placeBlock(laseraim, BlockFace.EAST);
            }
            while (checkGravityBlocks(x, z) == true) {
                Main.self.selectSlot(shovelslot);
                ai.breakBlock(KaiTools.Scan(Material.SAND, x, Y, z, 2, 3), 1);
            }
            System.out.println("slice");
        }

    }

    public boolean checkGravityBlocks(int x, int z) {
        if (KaiTools.Scan(Material.GRAVEL, x, Y, z, 2, 3) != null) {
            return true;
        } else {
            return false;
        }
    }

}
