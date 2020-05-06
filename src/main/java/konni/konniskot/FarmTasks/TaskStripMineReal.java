/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package konni.konniskot.FarmTasks;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import konni.konniskot.InventoryUtil;
import konni.konniskot.KaiTools;
import konni.konniskot.Main;
import konni.konniskot.Task;
import zedly.zbot.BlockFace;
import zedly.zbot.Location;
import zedly.zbot.Material;

/**
 *
 * @author Konstantin
 */
public class TaskStripMineReal extends Task {

    private static final HashSet<Material> ores = new HashSet<>();
    private static final HashSet<Material> nonusables = new HashSet<>();
    private static final HashSet<Material> shovelables = new HashSet<>();
    private List<Location> checkloc = new ArrayList<>();
    private List<Location> oreloc = new ArrayList<>();
    private List<Location> brotkrümel = new ArrayList<>();
    private List<Location> branchstart = new ArrayList<>();
    private Location ORE_CHEST_LOC;
    private Location TRASH_CHEST_LOC;
    private Location TORCH_STORE_LOC;
    private Location origin;
    private static Location XP_FARM_LOC;
    private int originy;
    private int originz;
    private int startdis;
    private int torchcounter;
    private int pickaxeslot = 0;
    private int shovelslot = 1;
    int torchslot = 2;
    int bridgeslot = 3;

    public TaskStripMineReal(int startdistortion) {
        super(100);
        startdis = startdistortion;
    }

    //TO DO: MainTunnle; Defender, Krümel am Wegeinganmg; healtool
    public void run() {
        try {
            KaiTools.modeSelect("ore", ai);
            ai.tick();
            torchcounter = 0;
            //doBranchTunnle();
            ai.tick();
            doStripMine();
        } catch (InterruptedException ex) {
        }
    }

    public void doStripMine() throws InterruptedException {
        int x = (Main.self.getLocation().getBlockX()) + (startdis);
        int y = Main.self.getLocation().getBlockY();
        int z = Main.self.getLocation().getBlockZ();

        for (int X = x; X < 10000; X = X + 3) {
            System.out.println("starting branch");
            Location startbranch = new Location(X, originy, originz);
            storeItems();
            ai.tick();
            ai.moveTo(startbranch);
            ai.tick();
            branchstart.add(Main.self.getLocation());
            ai.tick();
            //DOMAINTUNNLE
            if (checkIfTunnelIsAlreadyMined() == false) {
                doBranchTunnle();
                ai.tick();
                FollowKrümelReverse();
                ai.tick();
                brotkrümel.clear();
            }

        }
    }

    public boolean checkIfTunnelIsAlreadyMined() {
        Location now = Main.self.getLocation();
        if (Main.self.getEnvironment().getBlockAt(now.getRelative(0, 0, 1)).getType() == Material.AIR
                && Main.self.getEnvironment().getBlockAt(now.getRelative(0, 1, 1)).getType() == Material.AIR
                && Main.self.getEnvironment().getBlockAt(now.getRelative(0, 2, 1)).getType() == Material.AIR) {
            System.out.println("already mined");
            return true;
        }
        return false;
    }

    public void doMainTunnle() {

    }

    public void FollowKrümelReverse() throws InterruptedException {
        System.out.println("FollowKrümelReverse()");
        if (brotkrümel.isEmpty() == false) {

            for (int i = brotkrümel.size() - 1; i >= 0; i--) {
                ai.moveTo(brotkrümel.get(i));
                ai.tick();
            }
        }
    }

    public void FollowKrümel() throws InterruptedException {
        System.out.println("FollowKrümel()");
        if (brotkrümel.isEmpty() == false) {

            for (int i = 0; i <= brotkrümel.size() - 1; i++) {
                ai.moveTo(brotkrümel.get(i));
                ai.tick();
            }
        }

    }

    public void doBranchTunnle() throws InterruptedException {
        int branchlength = 200;

        for (int length = branchlength; length > 0; length--) {
            breakSlice();
            checkFloorForOres(Main.self.getLocation());
            cleverWalk(Main.self.getLocation().getRelative(0, 0, 1));
            scanAndMineOres();
            System.out.println("torchcounter: " + torchcounter);
            if (InventoryUtil.countFreeStorageSlots(true, false) < 2) {
                storeItemsInChest();
            }
            if (checkForLava(Main.self.getLocation()) == true) {
                System.out.println("lava detected");
                break;
            }
            if (checkForWater(Main.self.getLocation()) == true) {
                System.out.println("water detected");
                break;
            }
            System.out.println("length" + length);

        }
    }
    
    public void storeItemsInChest(){
    
    
    } 

    

    public void checkFloorForOres(Location loc) throws InterruptedException {
        if (ores.contains(Main.self.getEnvironment().getBlockAt(Main.self.getLocation().getRelative(0, -1, 1)))) {
            mineDLC(Main.self.getLocation().getRelative(0, -1, 1));
        }
    }

    public void breakSlice() throws InterruptedException {
        //KaiTools.modeSelect("normal", ai);
        //breakmiddleblock

        checkloc.add(Main.self.getLocation().getRelative(0, 2, 1));
        checkloc.add(Main.self.getLocation().getRelative(0, 1, 1));
        checkloc.add(Main.self.getLocation().getRelative(0, 0, 1));
        //checkloc.add(Main.self.getLocation().getRelative(0, -1, 1));
        torchcounter++;
        ai.tick();
        mineBlocks();
    }

    public void scanAndMineOres() throws InterruptedException {
        checkloc.add(Main.self.getLocation().getRelative(0, +3, 0));

        checkloc.add(Main.self.getLocation().getRelative(-1, 0, 0));
        checkloc.add(Main.self.getLocation().getRelative(+1, 0, 0));

        checkloc.add(Main.self.getLocation().getRelative(-1, 1, 0));
        checkloc.add(Main.self.getLocation().getRelative(+1, 1, 0));

        checkloc.add(Main.self.getLocation().getRelative(-1, 2, 0));
        checkloc.add(Main.self.getLocation().getRelative(+1, 2, 0));
        //System.out.println("size" + checkloc.size());
        while (checkloc.isEmpty() == false) {
            if (ores.contains(Main.self.getEnvironment().getBlockAt(checkloc.get(0)).getType())) {
                oreloc.add(checkloc.get(0));

            }
            checkloc.remove(0);
            // System.out.println("size" + checkloc.size());
            //System.out.println("sizeore" + oreloc.size());
            mineOres();

        }
    }

    public void mineBlocks() throws InterruptedException {
        while (checkloc.isEmpty() == false) {
            if (Main.self.getEnvironment().getBlockAt(checkloc.get(0)).getType() != Material.AIR
                    && Main.self.getEnvironment().getBlockAt(checkloc.get(0)).getType() != Material.CAVE_AIR
                    && Main.self.getEnvironment().getBlockAt(checkloc.get(0)).getType() != Material.VOID_AIR) {
                if (ores.contains(Main.self.getEnvironment().getBlockAt(checkloc.get(0)).getType())) {
                    oreloc.add(checkloc.get(0));
                } else {
                    if (Main.self.getEnvironment().getBlockAt(checkloc.get(0)).getType() != Material.AIR) {
                        mineDLC(checkloc.get(0));
                    }
                }
            }
            checkloc.remove(0);
            ai.tick();
        }
        mineOres();
    }

    public void mineOres() throws InterruptedException {
        while (oreloc.isEmpty() == false) {
            //System.out.println("orelist");

            mineDLC(oreloc.get(0));

            oreloc.remove(0);
            ai.tick();
        }
    }

    public void rightClick() {
        Main.self.placeBlock(Main.self.getLocation(), BlockFace.UP);
    }

    public void storeItems() throws InterruptedException {
        ai.moveTo(origin);
        ai.tick();
        ai.openContainer(ORE_CHEST_LOC);
        for (int slot = 54; slot < 80; slot++) {
            if (Main.self.getInventory().getSlot(slot) != null
                    && ores.contains(Main.self.getInventory().getSlot(slot).getType())) {
                ai.depositSlot(slot);
            }
        }
        ai.closeContainer();
        ai.tick();
        ai.openContainer(TRASH_CHEST_LOC);
        for (int slot = 54; slot < 80; slot++) {
            if (Main.self.getInventory().getSlot(slot) != null
                    && nonusables.contains(Main.self.getInventory().getSlot(slot).getType())) {
                ai.depositSlot(slot);
            }
        }
        ai.closeContainer();
    }

    public void cleverWalk(Location loc) throws InterruptedException {
        Location groundblock = loc.getRelative(0, -1, 0);
        if (Main.self.getEnvironment().getBlockAt(groundblock).isSolid() == false) {
            Main.self.selectSlot(bridgeslot);
            placeBlockClever(Material.STONE, groundblock, bridgeslot);
            ai.tick();
            Main.self.placeBlock(groundblock, BlockFace.NORTH);
        }
        while (Main.self.getEnvironment().getBlockAt(Main.self.getLocation().getRelative(0, 0, 1)).getType() == Material.GRAVEL) {
            mineDLC(Main.self.getLocation().getRelative(0, 0, 1));
            ai.tick(3);
        }
        ai.tick();
        if (torchcounter == 10) {
            System.out.println("placing toch");
            brotkrümel.add(Main.self.getLocation());
            ai.tick();
            Main.self.selectSlot(torchslot);
            if (Main.self.getEnvironment().getBlockAt(Main.self.getLocation()).getType() == Material.AIR) {
                placeBlockClever(Material.TORCH, Main.self.getLocation().getRelative(0, 0, -1), 38);
                Main.self.selectSlot(pickaxeslot);
            }
            torchcounter = 0;
        }
        ai.moveTo(loc.centerHorizontally());

    }

    public boolean checkForLava(Location loc) {
        int cx = loc.getBlockX();
        int cy = loc.getBlockY();
        int cz = loc.getBlockZ();

        if (KaiTools.ScanAboveFloor(Material.LAVA, cx, cy, cz, 4, 2) == null) {
            return false;
        }
        return true;
    }

    public boolean checkForWater(Location loc) {
        int cx = loc.getBlockX();
        int cy = loc.getBlockY();
        int cz = loc.getBlockZ();

        if (KaiTools.ScanAboveFloor(Material.WATER, cx, cy, cz, 4, 2) == null) {
            return false;
        }
        return true;
    }

    public boolean mineDLC(Location loc) throws InterruptedException {
        //Main.self.lookAt(loc.getYaw(), loc.getPitch());
        if (shovelables.contains(Main.self.getEnvironment().getBlockAt(loc).getType())) {
            Main.self.selectSlot(shovelslot);
            ai.tick();
            if (KaiTools.checkToolHealth() == false) {
                while (KaiTools.checkToolHealth() == false) {
                    System.out.println("TOOL HEALTH LOW");
                    ai.tick(200);
                }
            }

        } else {
            Main.self.selectSlot(pickaxeslot);
            if (KaiTools.checkToolHealth() == false) {
                while (KaiTools.checkToolHealth() == false) {
                    System.out.println("TOOL HEALTH LOW");
                    ai.tick(200);
                }
            }
        }
        ai.mineBlock(loc);
        return true;
    }

    public boolean placeBlockClever(Material mat, Location loc, int slot) throws InterruptedException {

        if (KaiTools.testStackAvaliable(mat, slot) == false) {
            if (KaiTools.lookInInventoryAndMove(mat, slot, ai) == false) {
                
                while(true){
                Main.self.sendChat("MATERIAL MISSING");
                ai.tick(200);
                }
            }
        }
        Main.self.placeBlock(loc, BlockFace.UP);
        return true;
    }

    

    

    

    public void healtool() throws InterruptedException {
        Main.self.sendChat("/home xp");
        ai.tick(5);
        ai.moveTo(XP_FARM_LOC);
        ai.tick(3);
        while (KaiTools.checkToolFullHealth() == false) {
            if (Main.self.getLocation().distanceTo(XP_FARM_LOC) > 0.1) {
                ai.moveTo(XP_FARM_LOC);
            }
            ai.tick(10);
        }

    }

    static {
        shovelables.add(Material.CLAY);
        shovelables.add(Material.COARSE_DIRT);
        //CONCRETEPOWDER MISSING
        shovelables.add(Material.DIRT);
        shovelables.add(Material.FARMLAND);
        shovelables.add(Material.GRASS_BLOCK);
        shovelables.add(Material.GRAVEL);
        shovelables.add(Material.MYCELIUM);
        shovelables.add(Material.PODZOL);
        shovelables.add(Material.RED_SAND);
        shovelables.add(Material.SAND);
        shovelables.add(Material.SOUL_SAND);
        ores.add(Material.COAL_ORE);
        ores.add(Material.IRON_ORE);
        ores.add(Material.GOLD_ORE);
        ores.add(Material.LAPIS_ORE);
        ores.add(Material.REDSTONE_ORE);
        ores.add(Material.EMERALD_ORE);
        ores.add(Material.DIAMOND_ORE);
        nonusables.add(Material.STONE);
        nonusables.add(Material.DIRT);
        nonusables.add(Material.ANDESITE);
        nonusables.add(Material.DIORITE);
        nonusables.add(Material.GRANITE);
        nonusables.add(Material.GRAVEL);

    }
}
