/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package konni.konniskot;

import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import zedly.zbot.BlockFace;
import zedly.zbot.Location;
import zedly.zbot.Material;

/**
 *
 * @author Konstantin
 */
public class TaskStripMine extends Task {

    public int Y;
    private static final HashSet<Material> ores = new HashSet<>();

    public TaskStripMine() {
        super(100);

    }

    public void run() {

        Location aktuell = Main.self.getLocation();
        int x = (int) aktuell.getX();
        Y = (int) aktuell.getY();
        int z = (int) aktuell.getZ();

        try {
            ai.tick();
            if (checkForLava(x, Y, z) == true) {
                Main.self.sendChat("Lava detected");
            }
        } catch (InterruptedException ex) {
        }

//         while (true) {
//                KaiTools.modeSelect("tall", ai);
//                System.out.println("done tall");
//                ai.tick(20);
//                KaiTools.modeSelect("ore", ai);
//                System.out.println("done ore");
//                ai.tick(20);
//            }
//        
//
//        }
//        try {
//            
//            
//            Location aktuell = Main.self.getLocation();
//            int x = (int) aktuell.getX();
//            Y = (int) aktuell.getY();
//            int z = (int) aktuell.getZ();
//
//            for (int X = x; x < x + 1000; x++) {
//                
//                
//                Location walk = new Location(x, Y, z).centerHorizontally();
//
//                ai.moveTo(walk);
//
//                ai.tick();
//                doRightSide(X, z);
//                ai.tick();
//                doLeftSide(X, z);
//                ai.tick();
//                
//                
//            }
//        } catch (InterruptedException ex) {
//
//        }
    }

    public boolean checkForLava(int x, int y, int z) {
        int x1 = x + 2;
        int y1 = y + 3;
        int z1 = z - 1;

        int x2 = x - 1;
        int y2 = y;
        int z2 = z + 1;

        if (KaiTools.ScanArea(Material.LAVA, x1, y1, z1, x2, y2, z2) != null) {
            return true;
        }
        return false;
    }

    public void doRightSide(int x, int z) throws InterruptedException {

        for (int i = 0; i < 15; i++) {

            Main.self.lookAt(0, 0);
            ai.tick();

            Main.self.placeBlock(Main.self.getLocation(), BlockFace.EAST);
            ai.tick();
        }
    }

    public void doLeftSide(int x, int z) throws InterruptedException {
        for (int i = 0; i < 15; i++) {

            Main.self.lookAt(180, 0);
            ai.tick();
            Main.self.placeBlock(Main.self.getLocation(), BlockFace.EAST);
            ai.tick();
        }
    }

    static {
        ores.add(Material.COAL_ORE);
        ores.add(Material.IRON_ORE);
        ores.add(Material.GOLD_ORE);
        ores.add(Material.REDSTONE_ORE);
        ores.add(Material.EMERALD_ORE);
        ores.add(Material.DIAMOND_ORE);
    }
}
