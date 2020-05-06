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
import zedly.zbot.BlockFace;
import zedly.zbot.EntityType;
import zedly.zbot.Location;
import zedly.zbot.Material;
import zedly.zbot.entity.Entity;

/**
 *
 * @author Konstantin
 */
public class TaskTest extends Task {

    Location loc;

    public TaskTest(int x, int y, int z) {
        super(100);
        loc = new Location(x, y, z);
    }

    public void run() {

        try {
            ai.tick();
            ai.mineBlock(loc);

        } catch (InterruptedException ex) {

        }

    }

}
