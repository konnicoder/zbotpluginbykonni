/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package konni.konniskot.FarmTasks;

import java.util.ConcurrentModificationException;
import java.util.logging.Level;
import java.util.logging.Logger;
import konni.konniskot.Main;
import konni.konniskot.Task;
import zedly.zbot.EntityType;
import zedly.zbot.Location;
import zedly.zbot.entity.Entity;

/**
 *
 * @author Konstantin
 */
public class TaskFarmPigmen extends Task {

    private static final Location walk1 = new Location(-864, 76, -155).centerHorizontally();
    private static final Location at1 = new Location(-864, 77, -153).centerHorizontally();
    private static final Location at11 = new Location(-864, 77, -152).centerHorizontally();

    private static final Location walk2 = new Location(-866, 76, -155).centerHorizontally();
    private static final Location at2 = new Location(-866, 77, -153).centerHorizontally();
    private static final Location at22 = new Location(-866, 77, -152).centerHorizontally();

    int miau;

    public TaskFarmPigmen(int delay) {
        super(110);
        miau = delay;
    }

    public void run() {

        try {
            while(true){
            side1();
            ai.tick(miau);
            
            side2();
            ai.tick(miau);
            
            }        
                    
        } catch (InterruptedException ex) {

        }

    }

    public void side1() throws InterruptedException {
        if(Main.self.getLocation().distanceTo(walk1)>0.1){
        ai.moveTo(walk1);
        ai.tick();
        }
        tryAttack(at1);
        ai.tick(8);
        tryAttack(at11);
        ai.tick(8);
        tryAttack(at1);
        ai.tick(4);
    }
    
    public void side2() throws InterruptedException {
        if(Main.self.getLocation().distanceTo(walk2)>0.1){
        ai.moveTo(walk2);
        ai.tick();
        }
        tryAttack(at2);
        ai.tick(8);
        tryAttack(at22);
        ai.tick(8);
        tryAttack(at2);
        ai.tick(4);
    }
    
    
    private Entity tryGetEnemy(Location attackLoc) throws InterruptedException {
        try {
            int annoyingEntities = 0;
            Entity nearestAnnoying = null;
            for (Entity ent : Main.self.getEnvironment().getEntities()) {
                if (ent.getType() != EntityType.CHICKEN
                        && ent.getType() != EntityType.ZOMBIE_PIGMAN) {
                    continue;
                }

                if (ent.getLocation().distanceTo(attackLoc) < 0.5) {
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

    private boolean tryAttack(Location attackLoc) throws InterruptedException {
        Entity skeleton = tryGetEnemy(attackLoc);
        if (skeleton != null) {

            Main.self.attackEntity(skeleton);
            ai.tick(5);
            return true;
        }
        return false;
    }
}
