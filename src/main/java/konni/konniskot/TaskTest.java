/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package konni.konniskot;

import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import zedly.zbot.EntityType;
import zedly.zbot.Location;
import zedly.zbot.Material;
import zedly.zbot.entity.Entity;

/**
 *
 * @author Konstantin
 */
public class TaskTest extends Task {
   private static final Location SKEL_ATTACK_LOC = new Location(234, 10, -8784).centerHorizontally();
   
   
    public TaskTest() {
        super(100);

    }

    public void run() {

       
            try {
                ai.tick();
                ai.moveTo(SKEL_ATTACK_LOC);
                ai.tick();
                Main.self.selectSlot(0);
                while (true){
                ai.tick();
                    tryAttack(SKEL_ATTACK_LOC, SKEL_ATTACK_LOC);
                }
            } catch (InterruptedException ex) {
               
            
            
        
        
        }
        
        
        
    }    

    private boolean tryAttack(Location home, Location attackLoc) throws InterruptedException {
        Entity skeleton = tryGetEnemy(attackLoc);
        if (skeleton != null) {
            
            Main.self.attackEntity(skeleton);
            ai.tick(3);
            return true;
        }
        return false;
    }

    private boolean tryAttackWither(Location home, Location attackLoc) throws InterruptedException {
        Entity witherskelleton = tryGetEnemyWither(attackLoc);
        if (witherskelleton != null) {
            ai.moveTo(home);
            ai.tick();
            Main.self.attackEntity(witherskelleton);
            ai.tick(5);
            return true;
        }
        return false;
    }

    private Entity tryGetEnemy(Location attackLoc) throws InterruptedException {
        try {
            int annoyingEntities = 0;
            Entity nearestAnnoying = null;
            for (Entity ent : Main.self.getEnvironment().getEntities()) {
                if (ent.getType() != EntityType.SKELETON) {
                    continue;
                }
                if (ent.getLocation().distanceTo(attackLoc) < 4) {
                    annoyingEntities++;
                    if (nearestAnnoying == null
                            || ent.getLocation().distanceTo(attackLoc)
                            < nearestAnnoying.getLocation().distanceTo(attackLoc)) {
                        nearestAnnoying = ent;
                    }
                }
            }
            if (nearestAnnoying != null) {
                return nearestAnnoying;
            }
        } catch (ConcurrentModificationException ex) {
            System.err.println("CME in getEntities() :( :(");
        }
        return null;
    }

    private Entity tryGetEnemyWither(Location attackLoc) throws InterruptedException {
        try {
            int annoyingEntities = 0;
            Entity nearestAnnoying = null;
            for (Entity ent : Main.self.getEnvironment().getEntities()) {
                if (ent.getType() != EntityType.WITHER_SKELETON) {
                    continue;
                }
                if (ent.getLocation().distanceTo(attackLoc) < 4) {
                    annoyingEntities++;
                    if (nearestAnnoying == null
                            || ent.getLocation().distanceTo(attackLoc)
                            < nearestAnnoying.getLocation().distanceTo(attackLoc)) {
                        nearestAnnoying = ent;
                    }
                }
            }
            if (nearestAnnoying != null) {
                return nearestAnnoying;
            }
        } catch (ConcurrentModificationException ex) {
            System.err.println("CME in getEntities() :( :(");
        }
        return null;
    }

}
