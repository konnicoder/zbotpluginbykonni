/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package konni.konniskot;

import java.util.HashSet;
import zedly.zbot.BlockFace;
import zedly.zbot.Location;
import zedly.zbot.Material;

/**
 *
 * @author Konstantin
 */
public class TaskConvertPumpkins extends Task {

    private static final Location MINING_LOC = new Location(316, 138, -8718).centerHorizontally();
    private static final Location CHEST_LOC = new Location(315, 137, -8716).centerHorizontally();
    private static final Location PUMPKIN_TESS_LOC = new Location(296, 125, -8720).centerHorizontally();
    private static final HashSet<Material> products = new HashSet<>();
    public int shearsslot = 2;
    public int axeslot = 3;
    public int pumpkinslot = 1;

    public TaskConvertPumpkins() {
        super(100);
    }

    public void run() {
        while (true) {
            try {
                getItems();
                Main.self.selectSlot(pumpkinslot);

                while (Main.self.getInventory().getSlot(37) != null
                        && Main.self.getInventory().getSlot(37).getType() == Material.PUMPKIN
                        && Main.self.getInventory().getSlot(38) != null
                        && Main.self.getInventory().getSlot(38).getType() == Material.SHEARS) {

                    ai.tick();
                    convert();
                    System.out.println("converting");
                }

                ai.tick(5);
                System.out.println("dumping");
                dump();
            } catch (InterruptedException ex) {
            }

        }

    }

    public void convert() throws InterruptedException {
        Main.self.selectSlot(pumpkinslot);
        Main.self.placeBlock(MINING_LOC, BlockFace.UP);
        
        Main.self.selectSlot(shearsslot);
        
        Main.self.placeBlock(MINING_LOC, BlockFace.SOUTH);
       
        Main.self.selectSlot(axeslot);
        
        ai.breakBlock(MINING_LOC, 100);
        ai.tick();

    }

    public void dump() throws InterruptedException {
        ai.openContainer(CHEST_LOC);
        for (int i = 54; i <= 89; i++) {
            if (Main.self.getInventory().getSlot(i) != null
                    && products.contains(Main.self.getInventory().getSlot(i).getType())) {
                ai.depositSlot(i);
            }
        }
        ai.closeContainer();
    }

    public void getItems() throws InterruptedException {
        ai.openContainer(CHEST_LOC);
        ai.tick();
        if (Main.self.getInventory().getSlot(82) == null || Main.self.getInventory().getSlot(82).getType() != Material.PUMPKIN) {
            for (int i = 0; i < 53; i++) {
                if (Main.self.getInventory().getSlot(i) != null && Main.self.getInventory().getSlot(i).getType() == Material.PUMPKIN) {
                    ai.swapItems(i, 82);
                    break;
                }
            }
        }
        if (Main.self.getInventory().getSlot(83) == null || Main.self.getInventory().getSlot(83).getType() != Material.SHEARS) {
            for (int i = 0; i < 53; i++) {
                if (Main.self.getInventory().getSlot(i) != null && Main.self.getInventory().getSlot(i).getType() == Material.SHEARS) {
                    ai.swapItems(i, 83);
                    break;
                }
            }
        }
        ai.closeContainer();
    }

    static {
        products.add(Material.CARVED_PUMPKIN);
        products.add(Material.PUMPKIN_SEEDS);

    }
}
