/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package konni.konniskot.FarmTasks;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import konni.konniskot.InventoryUtil;
import konni.konniskot.KaiTools;
import konni.konniskot.Main;
import konni.konniskot.Task;
import net.minecraft.server.NBTTagCompound;
import zedly.zbot.BlockFace;
import zedly.zbot.EntityType;
import zedly.zbot.Location;
import zedly.zbot.Material;
import zedly.zbot.entity.Entity;
import zedly.zbot.entity.Item;
import zedly.zbot.inventory.ItemStack;

/**
 *
 * @author Konstantin
 */
public class TaskGetSand extends Task {

    double CenterPointX = 0;
    double CenterPointY = 0;
    double CenterPointZ = 0;

    int ScanAnker1X = 0;
    int ScanAnker1Y = 0;
    int ScanAnker1Z = 0;

    int ScanAnker2X = 0;
    int ScanAnker2Y = 0;
    int ScanAnker2Z = 0;
    public Location aktuell = null;
    int Y;

    public TaskGetSand() {
        super(100);
    }

    public void run() {
        System.out.println("TaskGetSand started");
        Y = Main.self.getLocation().getBlockY();
        Main.self.selectSlot(0);
        try {
            Location startpos = new Location(9610, Y, 7759);
            ai.moveTo(startpos);
            int Zachse;

            for (int Xachse = 9610; Xachse > 9568; Xachse = Xachse - 5) {

                for (Zachse = 7760; Zachse > 7720; Zachse--) {
                    if (checkToolHealth() == false) {
                        Main.self.sendChat("break because of low tool health");
                        break;
                    }

                    aktuell = Main.self.getLocation();
                    double X = aktuell.getX();
                    double Z = aktuell.getZ();

                    int x = (int) (X + 0.5);
                    int z = (int) (Z + 0.5);

                    if (InventoryUtil.countFreeStorageSlots(true, false) == 0) {
                        while (InventoryUtil.countFreeStorageSlots(true, false) == 0) {
                            emptyInventory();

                        }
                    }

                    while (KaiTools.Scan(Material.SAND, x, Y, z, 3, 3) != null) {
                        ai.breakBlock(KaiTools.Scan(Material.SAND, x, Y, z, 3, 3), 1);
                    }
                    while (KaiTools.Scan(Material.RED_SAND, x, Y, z, 3, 3) != null) {
                        ai.breakBlock(KaiTools.Scan(Material.RED_SAND, x, Y, z, 3, 3), 1);
                    }
                    Location walk = new Location(Xachse, Y, Zachse);
                    ai.moveTo(walk);
                    ai.tick();
                }
            }
        } catch (InterruptedException ex) {
        }
    }

    public boolean checkToolHealth() {
        ItemStack is = Main.self.getInventory().getItemInHand();
        if (is.getNbt() instanceof NBTTagCompound) {
            NBTTagCompound nbt = (NBTTagCompound) is.getNbt();
            int damage = nbt.getInteger("Damage");

            if (damage < 1300) {
                return true;
            }
        }
        Main.self.sendChat("tool health low");
        return false;
    }

    public void emptyInventory() throws InterruptedException {
        int ding = 0;
        Location org = Main.self.getLocation();

        while (true) {

            if (KaiTools.Scan(Material.SHULKER_BOX, org.getBlockX(), org.getBlockY(), org.getBlockZ(), 2, 4) != null) {

                break;
            }
            ai.tick(20);
            System.out.println("Waiting for shulkerbox");
            if (ding == 0) {
                Main.self.sendChat("ding Konni");
            }
            ding++;
        }

        Location shulker = KaiTools.Scan(Material.SHULKER_BOX, org.getBlockX(), org.getBlockY(), org.getBlockZ(), 2, 4);
        ai.openContainer(shulker);
        for (int slot = 27; slot <= 62; slot++) {
            if (Main.self.getInventory().getSlot(slot) != null
                    && Main.self.getInventory().getSlot(slot).getType() == Material.SAND
                    || Main.self.getInventory().getSlot(slot).getType() == Material.RED_SAND) {
                ai.depositSlot(slot);
            }
            if (InventoryUtil.countFreeStorageSlots(false, true) == 0) {
                break;
            }

        }
        ai.closeContainer();
    }

    public void ups() {
        Collection<Entity> items = Main.self.getEnvironment().getEntities();
        items.removeIf((e) -> {
            return !(e instanceof Item) || ((Item) e).getItemStack().getType() != Material.CHARCOAL;
        });

    }

    public void cancel() {
    }
}
