/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package konni.konniskot;

import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
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
            for (int xachse = 0; xachse < 100; xachse = xachse + 3) {
            }

        } catch (InterruptedException ex) {
        }

//Block testblock= Main.self.getEnvironment().getBlockAt(BFSScan(blabla));
//KaiTools.isVisible(testblock);
    }

    public void doTunnel(int xcoord, int ycoord, int zcoord) throws InterruptedException {
        int x = xcoord;
        int y = ycoord;
        int z = zcoord;
        while (true) {
            if (checkForLava(x, y, z) == false) {
                KaiTools.modeSelect("tall", ai);
                ai.tick();
                Main.self.breakBlock(x, y + 1, z + 1);
            } else {
                break;
            }

        }
    }

    public boolean checkForLava(int x, int y, int z) {
        int x1 = x + 2;
        int y1 = y + 3;
        int z1 = z - 1;

        int x2 = x - 1;
        int y2 = y - 1;
        int z2 = z + 1;

        if (KaiTools.ScanArea(Material.LAVA, x1, y1, z1, x2, y2, z2) != null) {
            return true;
        }
        return false;
    }

    public void checkNeightbourBlocks(int x, int y, int z) throws InterruptedException {
        
        Location locup = new Location(x, y+1, z);
        Location locdown = new Location(x, y-1, z);
        Location locxpos = new Location(x+1,y,z);
        Location locxneg = new Location(x-1,y,z);
        Location loczpos = new Location(x,y,z+1);
        Location loczneg = new Location(x,y,z-1);
        KaiTools.modeSelect("ore", ai);
        ai.tick();
        if(ores.contains(Main.self.getEnvironment().getBlockAt(locup).getType())){
        ai.breakBlock(locup, 200);    
        }
        if(ores.contains(Main.self.getEnvironment().getBlockAt(locdown).getType())){
        ai.breakBlock(locdown, 200);    
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
