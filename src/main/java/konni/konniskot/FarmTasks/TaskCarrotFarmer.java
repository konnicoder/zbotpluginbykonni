/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package konni.konniskot.FarmTasks;

import java.util.logging.Level;
import java.util.logging.Logger;
import konni.konniskot.Main;
import konni.konniskot.Task;
import zedly.zbot.Location;
import static zedly.zbot.Material.CARROTS;
import zedly.zbot.block.data.AgeableBlock;
import zedly.zbot.block.data.BlockData;
import zedly.zbot.environment.Block;

/**
 *
 * @author Konstantin
 */
public class TaskCarrotFarmer extends Task {

    public TaskCarrotFarmer() {
        super(100);
    }

    public void run() {
        Location loc = new Location(1385, 64, -8927);

        BlockData bd = Main.self.getEnvironment().getBlockAt(loc).getData();
        AgeableBlock carrot = (AgeableBlock) bd;
        System.out.println(carrot.getAge());
        if (carrot.getAge() == 7) {
            try {
                ai.breakBlock(loc);
            } catch (InterruptedException ex) {

            }
        }
     
    }
}
