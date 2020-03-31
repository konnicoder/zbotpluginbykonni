/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package konni.konniskot;

import java.util.function.Predicate;
import zedly.zbot.Location;
import zedly.zbot.Material;
import zedly.zbot.environment.Block;
import zedly.zbot.util.Vector;

/**
 *
 * @author Konstantin
 */
public class TaskSee extends Task {

    int x;
    int y;
    int z;

    public TaskSee(int dx, int dy, int dz) {
        super(100);
        x = dx;
        y = dy;
        z = dz;
    }

    public void run() {

        Location dest = new Location(x, y, z);

        Location origin = Main.self.getLocation();
        Block testblock = Main.self.getEnvironment().getBlockAt(dest);
        Vector aim = origin.vectorTo(dest);

        int originx = origin.getBlockX();
        int originy = origin.getBlockY();
        int originz = origin.getBlockZ();
        System.out.println(KaiTools.rayCastNew(origin, aim, 30, (block) -> {
            return block.getType() != Material.AIR;
        }));

    }
}
