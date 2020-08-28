/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package konni.konniskot.BotEdit;

import java.util.ArrayList;
import static java.util.Collections.list;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
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
import zedly.zbot.event.block.BlockChangeEvent;
import zedly.zbot.inventory.ItemStack;

/**
 *
 * @author Konstantin
 */
public class TaskFillFloor extends Task {

    private List<Location> scannedblocks = new ArrayList<>();
    private List<Location> scanborder = new ArrayList<>();
    private List<Location> placinglocations = new ArrayList<>();

    public TaskFillFloor() {
        super(20);
    }

    public void run() {
        Location start = findStartingLocation();
        System.out.println(start);
        BFSScan(start);
        System.out.println(placinglocations.size());
        int neededblocks = placinglocations.size();
        if (neededblocks <= InventoryUtil.count(Material.OBSIDIAN, true, false)) {
            try {
                placeObsidian();
            } catch (InterruptedException ex) {

            }
        } else {
            Main.self.sendChat("not enough obsidian");
        }
        System.out.println("done");
        unregister();
    }

    public Location findStartingLocation() {
        Location org = Main.self.getLocation();
        if (Main.self.getEnvironment().getBlockAt(org.getRelative(1, -1, 0)).getType() == Material.AIR) {
            return org.getRelative(1, -1, 0);
        }
        if (Main.self.getEnvironment().getBlockAt(org.getRelative(-1, -1, 0)).getType() == Material.AIR) {
            return org.getRelative(-1, -1, 0);
        }
        if (Main.self.getEnvironment().getBlockAt(org.getRelative(0, -1, 1)).getType() == Material.AIR) {
            return org.getRelative(0, -1, 1);
        }
        if (Main.self.getEnvironment().getBlockAt(org.getRelative(0, -1, -1)).getType() == Material.AIR) {
            return org.getRelative(0, -1, -1);
        }

        return null;
    }

    public void placeObsidian() throws InterruptedException {
        while (placinglocations.isEmpty() == false) {
            Location closest = placinglocations.get(0);
            for (Location loc : placinglocations) {
                if (loc.distanceSquareTo(Main.self.getLocation()) < closest.distanceSquareTo(Main.self.getLocation())) {
                    closest = loc;
                }
            }

            Location place = closest;
            ai.moveTo(place.getRelative(0, 1, 0));
            placeBlockInv(place, BlockFace.DOWN);
            ai.tick();
            placinglocations.remove(closest);
        }


    }

    public boolean placeBlockInv(Location loc, BlockFace face) throws InterruptedException {
        int debug = 0;
        //System.out.println("placeBlockInv");
        testIfItemAvaliable();
        Main.self.placeBlock(loc, BlockFace.DOWN);

        BlockChangeEvent be = ai.waitForEvent(BlockChangeEvent.class, 1500);

        //System.out.println(be);
        while (Main.self.getEnvironment().getBlockAt(loc).getType() == Material.AIR) {
            ai.tick(5);
            if (Main.self.getEnvironment().getBlockAt(loc).getType() == Material.OBSIDIAN) {
                System.out.println("break");
                break;
            }
            Main.self.placeBlock(loc, BlockFace.DOWN);
            ai.tick();

            System.out.println("whileplaceblock");
            debug++;
            System.out.println(debug);
            if (debug >= 15) {
                ai.tick(50);
                Main.self.placeBlock(loc, BlockFace.DOWN);
                debug = 0;
                KaiTools.messageMaster("Konni999", "debugged placement");
                System.out.println(debug);
                //getResources();
            }
        }
        debug = 0;
        return true;

    }

    public void testIfItemAvaliable() throws InterruptedException {
        if (InventoryUtil.count(Material.OBSIDIAN, true, false) <= 70) {
            ai.tick(20);
            KaiTools.messageMaster("Konni", "getting materials");
            System.out.println("getting materials placeblock");

            // getResources();
            ai.tick(10);
        }
        while (KaiTools.testStackAvaliable(Material.OBSIDIAN, 36) == false) {
            if (KaiTools.lookInInventoryAndMove(Material.OBSIDIAN, 36, ai) == false) {
                System.out.println("cant find items");
                System.out.println(InventoryUtil.count(Material.OBSIDIAN, true, false));
            }
        }

    }

    public void BFSScan(Location miau) {
        HashSet<Location> searched_blocks = new HashSet<>();
        final BlockFace[] searchdirections = {BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};

        LinkedList<Location> scanborder = new LinkedList<>();

        Location origin = miau;
        searched_blocks.add(origin);
        scanborder.add(origin);

        while (!scanborder.isEmpty()) {
            Location loc = scanborder.removeFirst();
            if (Main.self.getEnvironment().getBlockAt(loc).getType() == Material.AIR) {
                placinglocations.add(loc);

            }

            for (BlockFace face : searchdirections) {
                Location relatives = loc.getRelative(face.getDirection());
                if (searched_blocks.contains(relatives)) {
                    continue;
                }
                if (Main.self.getEnvironment().getBlockAt(relatives).getType() == Material.OBSIDIAN) {
                    continue;

                }
                if (Main.self.getEnvironment().getBlockAt(relatives).getType() == Material.GOLD_BLOCK) {
                    continue;

                }

                searched_blocks.add(relatives);
                scanborder.add(relatives);
            }
            if (placinglocations.size() > 2500) {
                Main.self.sendChat("Error with floorscan");
                break;
            }
        }

    }

}
