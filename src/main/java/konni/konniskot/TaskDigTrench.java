/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package konni.konniskot;

import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import zedly.zbot.Location;
import zedly.zbot.Material;

/**
 *
 * @author Konstantin
 */
public class TaskDigTrench extends Task {
private static final HashSet<Material> blockstomine = new HashSet<>();
    public Location aktuell = null;

    public TaskDigTrench() {
        super(100);
    }

    public void run() {
    try {
        digTrench();
    } catch (InterruptedException ex) {
    
    }
    }

    public void digTrench() throws InterruptedException {

        
        while(true){
        aktuell = Main.self.getLocation();
        int X = (int) aktuell.getX();
        int Y = -3812;
        int Z = (int) aktuell.getZ();
        
        
        while (KaiTools.Scan(Material.SAND, X, Y, Z, 1, 3)!=null){
        Main.self.selectSlot(0);
        ai.breakBlock(KaiTools.Scan(Material.SAND, X, Y, Z, 1, 4),100);
        }
        while (KaiTools.Scan(Material.SANDSTONE, X, Y, Z, 1, 3)!=null){
        Main.self.selectSlot(1);  
        ai.breakBlock(KaiTools.Scan(Material.SANDSTONE, X, Y, Z, 1, 4),100);
        }
        
        Location walk = new Location(X-1,Y,Z);
        ai.moveTo(walk);
        
        }
    }
}
