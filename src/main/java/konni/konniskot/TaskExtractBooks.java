/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package konni.konniskot;

import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.server.NBTTagCompound;
import zedly.zbot.BlockFace;
import zedly.zbot.Location;
import zedly.zbot.Material;
import zedly.zbot.inventory.ItemStack;

/**
 *
 * @author Konstantin
 */
public class TaskExtractBooks extends Task {
    
    private static final Location BOOK_MINING_LOC = new Location(185, 144, -8768).centerHorizontally();
    private static final Location BOOK_MINING_WALK = new Location(184, 143, -8768).centerHorizontally();
    
    private static final Location TOOL_MENDING_LOC = new Location(295, 137, -8705).centerHorizontally();
    
    private static final Location checkpoint1 = new Location(241, 137, -8711).centerHorizontally();
    private static final Location checkpoint2 = new Location(185, 137, -8713).centerHorizontally();
    
    private static final Location BOOK_SHELF_TESS_LOC = new Location(176, 144, -8775).centerHorizontally();
    private static final Location BOOK_SHELF_TESS_WALK = new Location(176, 143, -8774).centerHorizontally();
    
    private static final Location BOOK_TESS_LOC = new Location(174, 144, -8775).centerHorizontally();
    private static final Location BOOK_TESS_WALK = new Location(174, 143, -8774).centerHorizontally();
    
    private static final Location trashchest = new Location(298, 137, -8701).centerHorizontally();
    private static final Location trashablegen = new Location(299, 137, -8701).centerHorizontally();
    
    private static final HashSet<Material> TRASH_MATERIALS = new HashSet<>();
    
    public TaskExtractBooks() {
        super(100);
    }
    
    public void run() {
        try {
            if (Main.self.getLocation().distanceTo(BOOK_MINING_WALK) > 20) {
                Main.self.sendChat("/home xp");
                ai.moveTo(checkpoint1);
                ai.tick();
                ai.moveTo(checkpoint2);
            }
            ai.tick();
            ai.moveTo(BOOK_MINING_WALK);
            ai.tick();
            Main.self.selectSlot(3);
            depositBooks();
            ai.moveTo(BOOK_MINING_WALK);
            
            while (true) {
                
                while (testStackAvaliable(Material.BOOKSHELF, 37) && checkToolHealth() == true) {
                    mineBookShelves();
                }
                System.out.println("no stack found");
                if (checkToolHealth() == false) {
                    healtool();

                    //repair tool
                }
                if (testStackAvaliable(Material.BOOKSHELF, 37) == false) {
                    System.out.println("Test stack");
                    if (searchInInventory() == true) {
                        System.out.println("item im inventar vorhanden");
                    } else {
                        
                        depositBooks();
                    }
                    
                }
                System.out.println("done");
            }
            
        } catch (InterruptedException ex) {
        }
        
    }
    
    public boolean searchInInventory() throws InterruptedException {
        System.out.println("search inv");
        for (int slot = 9; slot <= 44; slot++) {
            if (Main.self.getInventory().getSlot(slot) != null && Main.self.getInventory().getSlot(slot).getType() == Material.BOOKSHELF) {
                ai.transferItem(slot, 37);
                System.out.println("item found and moved");
                return true;
            }
            
        }
        return false;
    }
    
    public boolean testStackAvaliable(Material mat, int slot) {
        if (Main.self.getInventory().getSlot(slot) != null && Main.self.getInventory().getSlot(slot).getType() == mat) {
            return true;
        } else {
            return false;
        }
    }
    
    public boolean checkinv() throws InterruptedException {
        for (int slot = 9; slot <= 44; slot++) {
            if (Main.self.getInventory().getSlot(slot) != null && Main.self.getInventory().getSlot(slot).getType() == Material.BOOKSHELF) {
                ai.tick();
                ai.transferItem(slot, 37);
                ai.tick();
                System.out.println("item found and moved");
                ai.tick(5);
                break;
                
            }
            
        }
        return false;
    }
    
    public void healtool() throws InterruptedException {
        System.out.println("healtool");
        if (Main.self.getLocation().distanceTo(TOOL_MENDING_LOC) > 20) {
            ai.moveTo(checkpoint2);
            ai.tick(3);
            ai.moveTo(checkpoint1);
            ai.tick(3);
            ai.moveTo(TOOL_MENDING_LOC);
        }
        Main.self.selectSlot(3);
        while (checkToolFullHealth() == false) {
            if (Main.self.getLocation().distanceTo(TOOL_MENDING_LOC) > 0.1) {
                ai.moveTo(TOOL_MENDING_LOC);
                
            }
            ai.tick(10);
        }
        ai.moveTo(trashablegen);
        ai.tick(20);
        System.out.println("Waitdone-deposit Trash");
        dumpTrash();
        ai.tick();
        
        Main.self.sendChat("/home xp");
        ai.tick(5);
        
        ai.moveTo(checkpoint1);
        ai.tick(5);
        ai.moveTo(checkpoint2);
        
        ai.tick(5);
        ai.moveTo(BOOK_MINING_WALK);
        ai.tick();
        
    }
    
    private boolean dumpTrash() throws InterruptedException {
        if (InventoryUtil.findItem((i) -> i != null && TRASH_MATERIALS.contains(i.getType())) == -1) {
            return true;
        }
        
        if (!ai.openContainer(trashchest)) {
            System.err.println("Can't open disposal");
            ai.tick(50);
            return false;
        }
        
        int staticOffset = Main.self.getInventory().getStaticOffset();
        boolean hasTrash;
        do {
            hasTrash = false;
            for (int i = staticOffset; i < staticOffset + 36; i++) {
                if (Main.self.getInventory().getSlot(i) != null
                        && TRASH_MATERIALS.contains(Main.self.getInventory().getSlot(i).getType())) {
                    ai.depositSlot(i);
                    hasTrash = true;
                }
            }
        } while (hasTrash);
        
        ai.closeContainer();
        
        return true;
    }
    
    public void depositBooks() throws InterruptedException {
        ai.moveTo(BOOK_TESS_WALK);
        Main.self.sneak(true);
        ai.tick();
        Main.self.placeBlock(BOOK_TESS_LOC, BlockFace.EAST);
        ai.tick();
        Main.self.sneak(false);
        ai.tick();
        ai.moveTo(BOOK_SHELF_TESS_WALK);
        ai.tick();
        int freeslots = InventoryUtil.countFreeStorageSlots(true, false);
        int takeslots = (freeslots / 3) - 2;
        for (int clicks = 1; clicks <= takeslots; clicks++) {            
            ai.clickBlock(BOOK_SHELF_TESS_LOC);
            ai.tick();
        }
        ai.moveTo(BOOK_MINING_WALK);
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
        return false;
    }
    
    public boolean checkToolFullHealth() {
        ItemStack is = Main.self.getInventory().getItemInHand();
        if (is.getNbt() instanceof NBTTagCompound) {
            NBTTagCompound nbt = (NBTTagCompound) is.getNbt();
            int damage = nbt.getInteger("Damage");
            
            if (damage < 10) {
                return true;
            }
        }
        return false;
    }
    
    public void mineBookShelves() throws InterruptedException {
        Main.self.selectSlot(3);
        ai.breakBlock(BOOK_MINING_LOC, 250);
        Main.self.selectSlot(1);
        Main.self.placeBlock(BOOK_MINING_LOC, BlockFace.UP);
        Main.self.selectSlot(3);
    }
    
    static {
        TRASH_MATERIALS.add(Material.ROTTEN_FLESH);
        TRASH_MATERIALS.add(Material.GOLD_NUGGET);
        TRASH_MATERIALS.add(Material.GOLD_INGOT);
        TRASH_MATERIALS.add(Material.GOLDEN_SWORD);
        TRASH_MATERIALS.add(Material.CHICKEN);
        TRASH_MATERIALS.add(Material.FEATHER);
    }
    
}
