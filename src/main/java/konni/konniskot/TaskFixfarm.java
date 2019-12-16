/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package konni.konniskot;

import java.util.logging.Level;
import java.util.logging.Logger;
import zedly.zbot.BlockFace;
import zedly.zbot.Location;
import zedly.zbot.Material;

import zedly.zbot.inventory.ItemStack;

/**
 *
 * @author Konstantin
 */
public class TaskFixfarm extends Task {

    private static final Location SHULKER_WALK = new Location(290, 137, -8721).centerHorizontally();
    private static final Location BUTTON_SHULKER_LOC = new Location(289, 137, -8721).centerHorizontally();
    private static final Location SHULKER_OUTPUT_LOC = new Location(290, 138, -8723).centerHorizontally();
    private static final Location COAL_CHEST_WALK = new Location(300, 132, -8716).centerHorizontally();
    private static final Location COAL_CHEST_LOC = new Location(300, 132, -8717).centerHorizontally();

    private static final Location testlocation1 = new Location(295, 134, -8714).centerHorizontally();
    private static final Location testlocationwalk = new Location(294, 133, -8714).centerHorizontally();
    private static final Location testfurnace1walk = new Location(303, 131, -8718).centerHorizontally();
    private static final Location testfurnace1loc = new Location(301, 130, -8718).centerHorizontally();

    private int furnaceX = 301;
    private int furnaceY = 130;
    private int furnaceZ = -8718;

    public TaskFixfarm() {
        super(100);
    }

    public void run() {

        try {
            

            /* ai.moveTo(SHULKER_WALK);
            ai.tick();
            ai.openContainer(SHULKER_OUTPUT_LOC);
            int x = count(Material.GOLD_NUGGET , false, true);
            Main.self.sendChat("There are " + x +" goldnuggets in this container" );
                
           
            if(count(Material.GOLDEN_SWORD , false, true)>0 || count(Material.GOLD_INGOT , false, true)>0 || count(Material.FEATHER , false, true)>0) {
            System.out.println("ERROR");
            Main.self.sendChat("/msg Konni999 Error at the Shulkeroutput, ding konni");
            ai.tick(5);
            }
             */
 /*    
          ai.tick();
            if(TestHopperfilllevel(testlocationwalk, testlocation1, x)){
            Main.self.sendChat("ok so this is not working");
            Main.self.sendChat("Failure at: "+ testlocation1);
        }
            
           ai.tick();
           ai.moveTo(testfurnace1walk);
           ai.openContainer(testfurnace1loc);
           if(Main.self.getInventory().getSlot(0).getType() != Material.GOLDEN_SWORD){
           Main.self.sendChat("ERROR at: "+ testfurnace1loc);
           
           }
           ai.closeContainer();
          Main.self.sendChat("Test done");  
             */
            furnaceX = 301;
            furnaceY = 130;
            furnaceZ = -8718;
            checkFurnace();
        } catch (InterruptedException ex) {

        }
    }

    public static int count(Material mat, boolean testStatic, boolean testExternal) {
        int count = 0;
        int staticOffset = Main.self.getInventory().getStaticOffset();
        if (testExternal) {
            for (int i = 0; i <= staticOffset; i++) {
                ItemStack is = Main.self.getInventory().getSlot(i);
                if (is != null && is.getType() == mat) {
                    count += is.getAmount();

                }
            }
        }
        if (testStatic) {
            for (int i = staticOffset; i < staticOffset + 36; i++) {
                ItemStack is = Main.self.getInventory().getSlot(i);
                if (is != null && is.getType() == mat) {
                    count += is.getAmount();

                }
            }
        }
        return count;
    }

    public boolean TestHopperfilllevel(Location walkloc, Location checkloc, int needemptyslots) throws InterruptedException {
        int freestorage = 0;
        ai.moveTo(walkloc);
        ai.tick();
        ai.openContainer(checkloc);
        freestorage = InventoryUtil.countFreeStorageSlots(false, true);
        System.out.println(freestorage);

        Main.self.sendChat("hi" + freestorage);
        ai.closeContainer();
        if (freestorage < needemptyslots) {
            return true;
        } else {
            return false;
        }

    }

    private Location getFurnaceLoc() {
        return new Location(furnaceX, furnaceY, furnaceZ).centerHorizontally();
    }

    private Location getFurnaceWalk() {
        return new Location(furnaceX + 2, furnaceY + 1, furnaceZ).centerHorizontally();
    }

    public void checkFurnace() throws InterruptedException {

        int x = 1;
        while (x <= 5) {
            ai.moveTo(getFurnaceWalk());
            ai.tick();
            if (ai.openContainer(getFurnaceLoc())) {
                ai.tick();
                if (Main.self.getInventory().getSlot(0) != null && Main.self.getInventory().getSlot(0).getType() != Material.GOLDEN_SWORD) {
                    Main.self.sendChat("ERROR at: " + getFurnaceLoc());
                } else {
                    System.out.println("Furnace ok");
                }
                ai.tick();
                ai.closeContainer();
                ai.tick();
                furnaceZ++;      
                System.out.println(furnaceZ);
            } else {
                System.out.println("Error while checking the Furnace");
            }
            x++;
            ai.tick(3);
        }
    }

}
