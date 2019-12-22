/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package konni.konniskot;

import java.util.logging.Level;
import java.util.logging.Logger;
import zedly.zbot.Location;
import zedly.zbot.Material;

/**
 *
 * @author Konstantin
 */
public class TaskCheckInventory extends Task {

    public Location aktuell = null;
    public Location Chest = null;

    public TaskCheckInventory() {
        super(100);
    }

    public void run() {
        try {

            if (checkForChest() == true) {
                ai.tick();

                ai.tick(3);
                if (checkInventoryDumped() == false) {                  
                        dumpInventory();                                                   
                } else {
                    takeIncentory();
                }
                ai.tick();

            } else {
                Main.self.sendChat("place a fucking chest you stupid fuck");
            }

        } catch (InterruptedException ex) {
        }
    }

    public boolean testChestempty() throws InterruptedException {
        ai.openContainer(Chest);
        ai.tick();
        if (InventoryUtil.countFreeStorageSlots(false, true) == 54) {
            ai.tick();
            ai.closeContainer();
            return true;
        }

        return false;
    }

    public boolean checkInventoryDumped() {
        if (InventoryUtil.countFreeStorageSlots(true, false) == 36) {
            return true;
        }
        return false;
    }

    public void dumpInventory() throws InterruptedException {

        ai.openContainer(Chest);
        ai.tick();
        for (int inventoryslot = 54; inventoryslot <= 89; inventoryslot++) {
            int Chestslot = inventoryslot - 54;
            ai.transferItem(inventoryslot, Chestslot);
            ai.tick();
            System.out.println("from: " + inventoryslot + "to: " + Chestslot);
        }
        ai.tick();
        ai.closeContainer();

        ai.tick();
        for (int sourceslot = 5; sourceslot <= 8; sourceslot++) {
            int targetslot = sourceslot + 4;
            ai.transferItem(sourceslot, targetslot);
        }

        ai.tick();
        ai.openContainer(Chest);
        for (int sourceslot = 54; sourceslot <= 57; sourceslot++) {
            int targetslot = sourceslot - 9;
            ai.transferItem(sourceslot, targetslot);

        }
        ai.closeContainer();
        ai.tick();

        //HOTBAR FEHLT
    }

    public void takeIncentory() throws InterruptedException {
        ai.openContainer(Chest);
        int Chestslot;

        //armour chest-inv
        for (int sourceslot = 45; sourceslot <= 48; sourceslot++) {
            int targetslot = sourceslot + 9;
            ai.transferItem(sourceslot, targetslot);

        }
        ai.closeContainer();

        //armour inv-slots
        for (int sourceslot = 5; sourceslot <= 8; sourceslot++) {
            int targetslot = sourceslot + 4;
            ai.transferItem(targetslot, sourceslot);

        }

        ai.openContainer(Chest);
        ai.tick();
        for (int inventoryslot = 54; inventoryslot <= 89; inventoryslot++) {
            Chestslot = inventoryslot - 54;
            ai.transferItem(Chestslot, inventoryslot);
            System.out.println("from: " + Chestslot + "to: " + inventoryslot);

        }

        ai.tick();
        ai.closeContainer();

    }

    public boolean checkForChest() {

        aktuell = Main.self.getLocation();
        double X = aktuell.getX();
        double Y = aktuell.getY();
        double Z = aktuell.getZ();

        int x = (int) (X + 0.5);
        int y = (int) (Y + 0.5);
        //System.out.println(y);
        int z = (int) (Z + 0.5);
        if (KaiTools.Scan(Material.CHEST, x, y, z, 1, 2) != null) {
            Chest = KaiTools.Scan(Material.CHEST, x, y, z, 1, 2);
            return true;
        }
        return false;
    }
}
