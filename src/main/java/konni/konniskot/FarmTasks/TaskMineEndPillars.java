/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package konni.konniskot.FarmTasks;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import konni.konniskot.InventoryUtil;
import konni.konniskot.KaiTools;
import konni.konniskot.Main;
import konni.konniskot.Task;
import net.minecraft.server.NBTTagCompound;
import zedly.zbot.BlockFace;
import zedly.zbot.Location;
import zedly.zbot.Material;
import zedly.zbot.inventory.ItemStack;

/**
 *
 * @author Konstantin
 */
public class TaskMineEndPillars extends Task {

    int Y;
    private List<Location> obsloc = new ArrayList<>();
    boolean abbruch;
    String operator;
    Material Blocktomine = Material.OBSIDIAN;

    public TaskMineEndPillars(String user) {
        super(20);
        operator = user;
    }

    public void run() {
        abbruch = false;
        Y = Main.self.getLocation().getBlockY();
        try {
            while (true) {
                ToolHealth();
                scanForObsidian();
                ai.tick();
                mineBlocksFromList();
                ai.tick();
                obsloc.clear();
                Y = Y - 1;
                if (InventoryUtil.countFreeStorageSlots(true, false) <= 1) {
                    KaiTools.messageMaster(operator, "Inventory full");
                    gravity();
                    emptyInventory();

                }
                if (Y == 1) {
                    break;
                }
                if (abbruch == true) {
                    break;
                }
                removeNether();
                ai.tick();
                gravity();
                System.out.println("freeslots: " + InventoryUtil.countFreeStorageSlots(true, false));
            }
            gravity();
        } catch (InterruptedException ex) {
        }
        //Main.self.sendChat("done, ding Konni");
        unregister();
    }

    public void removeNether() throws InterruptedException {

        while (true) {

            gravity();
            Location loc = Main.self.getLocation();
            if (KaiTools.Scan(Material.NETHERRACK, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), 2, 3) == null) {
                break;

            } else {
                if (KaiTools.Scan(Material.NETHERRACK, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), 2, 4) != null) {
                    ai.breakBlock(KaiTools.Scan(Material.NETHERRACK, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), 2, 4), 0);
                }
            }

        }

    }

    public void mineBlocksFromList() throws InterruptedException {
        int breaks = 0;
        while (obsloc.isEmpty() == false) {
            Main.self.lookAt(0, 90);
            mineBlock();
            obsloc.remove(0);
            //System.out.println("obsloc size: " + obsloc.size());
            breaks++;
            if (breaks >= 6) {
                removeNether();
                breaks = 0;
            }
        }
        gravity();

    }

    public void mineBlock() throws InterruptedException {

        ai.moveTo(obsloc.get(0).getRelative(0, 1, 0).centerHorizontally());

        ai.tick();
        if (KaiTools.testStackAvaliable(Material.NETHERRACK, 38) == false) {
            if (KaiTools.lookInInventoryAndMove(Material.NETHERRACK, 38, ai) == false) {
                KaiTools.messageMaster(operator, "No Netherrack found");
                abbruch = true;
            }
        }
        Main.self.selectSlot(1);
        if (KaiTools.checkToolHealth() == false) {
            System.out.println("tool-health low");
            ai.tick();
            abbruch = true;
        }
        Main.self.selectSlot(1);
        Main.self.sneak(true);
        Main.self.placeBlock(obsloc.get(0), BlockFace.EAST);
        Main.self.sneak(false);
        ai.tick();
        Main.self.selectSlot(0);
        if (KaiTools.checkToolHealth() == false) {
            System.out.println("tool-health low");
            ai.tick();
            abbruch = true;
        }
        Main.self.selectSlot(0);
        if (obsloc.get(0) != null) {
            ai.breakBlock(obsloc.get(0), 0);
            ai.tick();
            if (Main.self.getEnvironment().getBlockAt(obsloc.get(0)).getType() == Material.NETHERRACK) {
                ai.breakBlock(obsloc.get(0), 0);
            }
        }

    }

    public void gravity() throws InterruptedException {
        while (true) {
            if (Main.self.getEnvironment().getBlockAt(Main.self.getLocation().getRelative(0, -1, 0)).getType() == Material.AIR) {
                ai.moveTo(Main.self.getLocation().getRelative(0, -1, 0).centerHorizontally());

            } else {
                break;
            }
        }
    }

    public void ToolHealth() {
        ItemStack is = Main.self.getInventory().getItemInHand();
        if (is.getNbt() instanceof NBTTagCompound) {
            NBTTagCompound nbt = (NBTTagCompound) is.getNbt();
            int damage = nbt.getInteger("Damage");
            //System.out.println("tooldamage:" + damage);

            if (damage > 1200) {

            }
            System.out.println("Tooldamage: " + damage);
        }

    }

    public void scanForObsidian() throws InterruptedException {
        gravity();
        int viewrange = 10;
        Location loc = Main.self.getLocation();
        int orgx = loc.getBlockX();
        int orgy = loc.getBlockY();
        int orgz = loc.getBlockZ();
        for (int x = orgx + viewrange; x >= orgx - viewrange; x--) {
            for (int z = orgz - viewrange; z <= orgz + viewrange; z++) {
                Location checkloc = new Location(x, Y - 1, z);
                if (Main.self.getEnvironment().getBlockAt(checkloc).getType() == Blocktomine) {
                    obsloc.add(checkloc);
                }

            }
        }
    }

    public void emptyInventory() throws InterruptedException {
        gravity();
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
            if (Main.self.getInventory().getSlot(slot) != null && Main.self.getInventory().getSlot(slot).getType() == Blocktomine) {
                ai.depositSlot(slot);
            }
            if (InventoryUtil.countFreeStorageSlots(false, true) == 0) {
                break;
            }

        }
        ai.closeContainer();
    }
}
