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
    
    int toolslot = 0;
    int chestslot = 7;
    boolean chestmode = true;
    public Location aktuell = null;
    int Y;
    
    public TaskGetSand() {
        super(30);
    }
    
    public void run() {
        System.out.println("TaskGetSand started");
        Y = Main.self.getLocation().getBlockY();
        Main.self.selectSlot(toolslot);
        try {
            Location startpos = new Location(9610, Y, 7759);
            ai.moveTo(startpos);
            int Zachse;
            
            for (int Xachse = 9610; Xachse > 9568; Xachse = Xachse - 5) {
                if (skipEmptySlice(Xachse) == true) {
                    continue;
                }
                for (Zachse = 7760; Zachse > 7720; Zachse--) {
                    if (toolslot == 3) {
                        System.out.println("break because of low tool health");
                        break;
                    }
                    if (checkToolHealth() == false) {
                        toolslot++;
                        Main.self.sendChat("/msg Konni999 toolslot now: " + toolslot);
                        Main.self.selectSlot(toolslot);

                        //System.out.println("break because of low tool health");
                        //Main.self.sendChat("break because of low tool health");
                    }
                    Main.self.selectSlot(toolslot);
                    aktuell = Main.self.getLocation();
                    double X = aktuell.getX();
                    double Z = aktuell.getZ();
                    
                    int x = (int) (X + 0.5);
                    int z = (int) (Z + 0.5);
                    
                    if (InventoryUtil.countFreeStorageSlots(true, false) == 0) {
                        if (chestmode == true) {
                            emptyInChest();
                        } else {
                            while (InventoryUtil.countFreeStorageSlots(true, false) == 0) {
                                emptyInventory();
                            }
                        }
                        
                    }
                    
                    digSand(x, z);
                    
                    Location temp = Main.self.getLocation();
                    ai.moveTo(temp.getRelative(-2, 0, 0));
                    ai.tick();
                    ai.moveTo(temp.getRelative(+2, 0, 0));
                    ai.tick();
                    Location walk = new Location(Xachse, Y, Zachse).centerHorizontally();
                    ai.moveTo(walk);
                    ai.tick();
                }
            }
            //emptyInventory();
        } catch (InterruptedException ex) {
        }
        unregister();
    }
    
    public void digSand(int x, int z) throws InterruptedException {
        Main.self.selectSlot(toolslot);
        while (KaiTools.Scan(Material.SAND, x, Y, z, 3, 3) != null) {
            Location dig = KaiTools.Scan(Material.SAND, x, Y, z, 3, 3);
            if (dig != null) {
                ai.breakBlock(dig, 1);
            }
            
            ai.tick();
        }
        while (KaiTools.Scan(Material.RED_SAND, x, Y, z, 3, 3) != null) {
            Location dig = KaiTools.Scan(Material.RED_SAND, x, Y, z, 3, 3);
            if (dig != null) {
                ai.breakBlock(dig, 1);
            }
            
            ai.tick();
        }
    }
    
    public boolean skipEmptySlice(int xachse) {
        if (Main.self.getEnvironment().getBlockAt(xachse, Main.self.getLocation().getBlockY(), 7721).getType() == Material.AIR) {
            return true;
        }
        
        return false;
    }
    
    public void emptyInChest() throws InterruptedException {
        Location tempchest = Main.self.getLocation().getRelative(0, 0, 2);
        Main.self.selectSlot(chestslot);
        Main.self.placeBlock(tempchest, BlockFace.UP);
        ai.tick();
        ai.openContainer(tempchest);
        ai.tick();
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
        ai.tick();
        Main.self.sendChat("/cremove");
        ai.tick();
        Main.self.clickBlock(tempchest);
        ai.tick();
        
    }
    
    public boolean checkToolHealth() {
        ItemStack is = Main.self.getInventory().getItemInHand();
        if (is.getNbt() instanceof NBTTagCompound) {
            NBTTagCompound nbt = (NBTTagCompound) is.getNbt();
            int damage = nbt.getInteger("Damage");
            
            if (damage < 1400) {
                return true;
            }
        }
        //Main.self.sendChat("tool health low");
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
