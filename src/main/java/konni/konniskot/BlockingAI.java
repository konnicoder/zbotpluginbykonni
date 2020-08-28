/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package konni.konniskot;

import edu.kit.informatik.AStar;
import edu.kit.informatik.GeometricPath;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

import zedly.zbot.event.Event;
import zedly.zbot.Location;
import zedly.zbot.BlockFace;
import zedly.zbot.EntityType;
import zedly.zbot.Material;
import zedly.zbot.PotionEffect;
import zedly.zbot.entity.Entity;
import zedly.zbot.event.EventHandler;
import zedly.zbot.event.Listener;
import zedly.zbot.event.PlayerFinishEatingEvent;
import zedly.zbot.event.SlotUpdateEvent;
import zedly.zbot.event.TransactionResponseEvent;
import zedly.zbot.event.WindowOpenFinishEvent;
import zedly.zbot.inventory.Enchantment;
import zedly.zbot.inventory.ItemStack;
import zedly.zbot.util.Vector;

/**
 *
 * @author Dennis
 */
public class BlockingAI implements Runnable {

    private final double stepResolution = 0.4;
    private final Object lock = "";
    private final Object timestopLock = "kek";
    private boolean timeStop = false;
    boolean halt;

    public void run() {
        synchronized (lock) {
            lock.notifyAll();
        }
        if (timeStop) {
            try {
                synchronized (timestopLock) {
                    timestopLock.wait();
                }
            } catch (InterruptedException ex) {
            }
        }
    }

    public boolean moveTo(Location target) throws InterruptedException {
        List<Location> nodes;
        Location oldLoc = Main.self.getLocation();
        if (oldLoc.distanceTo(target) <= 1) {
            nodes = new LinkedList<>();
            nodes.add(target);
        } else {
            GeometricPath path = AStar.getPath(target);
            if (path == null) {
                return false;
            }
            nodes = path.getLocations();
        }
        followPath(nodes);
        return true;
    }
    
   

    public void eat(int usedHand) throws InterruptedException {
        Main.self.eatHeldItem(usedHand);
        waitForEvent(PlayerFinishEatingEvent.class, (e) -> {
            return e.getPlayer() ==Main.self;
        }, 10000);
    }

    public boolean mineBlock(Location loc) throws InterruptedException {
        ItemStack item = Main.self.getInventory().getItemInHand();
        if (item == null) {
            Main.self.sendChat("beschwerdich");
            return false;
        }
        int efficiencyLevel = item.getEnchantments().getOrDefault(Enchantment.EFFICIENCY, 0);
        //int efficiencyLevel = 5;
        int efficiency = (efficiencyLevel * efficiencyLevel) + 1;
        int hastelevel = Main.self.getPotionEffects().getOrDefault(PotionEffect.HASTE, 0);
        int picktype = 0;
        if (item.getType() == Material.DIAMOND_PICKAXE
                || item.getType() == Material.DIAMOND_AXE
                || item.getType() == Material.DIAMOND_SHOVEL) {
            picktype = 8;
        }
        double hardness = Main.self.getEnvironment().getBlockAt(loc).getType().getHardness();
        int breakspeed = (picktype + (efficiency)) * (1 + (20 * hastelevel) / 100);
        int ticks = (int) (((hardness * 1.5) * 1000)) / breakspeed;
        if (breakspeed >= hardness * 30) {
            breakBlock(loc,1);
            return true;
        } else {
            breakBlock(loc, ticks);
            return true;
        }
    }

    public boolean moveToDynamic(Location target, int speed) throws InterruptedException {
        List<Location> nodes;
        Location oldLoc = Main.self.getLocation();
        if (oldLoc.distanceTo(target) <= 1) {
            nodes = new LinkedList<>();
            nodes.add(target);
        } else {
            GeometricPath path = AStar.getPath(target);
            if (path == null) {
                return false;
            }
            nodes = path.getLocations();
        }
        followPathDynamic(nodes, speed);
        return true;
    }

    public boolean navigateTo(Location target) throws InterruptedException {
        List<Location> nodes;
        while (Main.self.getLocation().distanceSquareTo(target) > 1) {
            Location oldLoc = Main.self.getLocation();
            GeometricPath path = AStar.getPath(target, true);
            if (path == null) {
                return false;
            }
            nodes = path.getLocations();
            followPath(nodes);
        }
        Location oldLoc = Main.self.getLocation();
        nodes = new LinkedList<>();
        nodes.add(target);
        followPath(nodes);
        return true;
    }

    public void followPath(GeometricPath path) throws InterruptedException {
        followPath(path.getLocations());
    }

    public void followPath(List<Location> nodes) throws InterruptedException {
        Location oldLoc = Main.self.getLocation();
        double yaw = oldLoc.getYaw();
        for (Location loc : nodes) {
            Vector direction = Main.self.getLocation().vectorTo(loc);
            if (direction.getHorizontalLength() != 0) {
                yaw = direction.getYaw();
            }
            int steps = (int) Math.floor(direction.getLength() / stepResolution);
            direction = direction.normalize();
            for (int i = 0; i < steps; i++) {
                Main.self.moveTo(oldLoc.getRelative(direction.multiply(i * stepResolution)).withYawPitch(180 / Math.PI * yaw, oldLoc.getPitchTo(loc)));
                tick();
            }
            Main.self.moveTo(loc.withYawPitch(180 / Math.PI * yaw, oldLoc.getPitchTo(loc)));
            tick();
            oldLoc = loc;
        }
    }

    public void followPathSpeedometer(List<Location> nodes, int tickdelay) throws InterruptedException {
        Location oldLoc = Main.self.getLocation();
        double yaw = oldLoc.getYaw();
        for (Location loc : nodes) {
            Vector direction = Main.self.getLocation().vectorTo(loc);
            if (direction.getHorizontalLength() != 0) {
                yaw = direction.getYaw();
            }
            int steps = (int) Math.floor(direction.getLength() / stepResolution);
            direction = direction.normalize();
            for (int i = 0; i < steps; i++) {
                Main.self.moveTo(oldLoc.getRelative(direction.multiply(i * stepResolution)).withYawPitch(180 / Math.PI * yaw, oldLoc.getPitchTo(loc)));
                tick();
            }
            Main.self.moveTo(loc.withYawPitch(180 / Math.PI * yaw, oldLoc.getPitchTo(loc)));
            tick(tickdelay);
            oldLoc = loc;
        }
    }

    public void followPathDynamic(List<Location> nodes, int tickdelay) throws InterruptedException {
        halt = false;
        Location oldLoc = Main.self.getLocation();
        double yaw = oldLoc.getYaw();
        for (Location loc : nodes) {
            Vector direction = Main.self.getLocation().vectorTo(loc);
            if (direction.getHorizontalLength() != 0) {
                yaw = direction.getYaw();
            }
            int steps = (int) Math.floor(direction.getLength() / 0.1);
            direction = direction.normalize();
            for (int i = 0; i < steps; i++) {
                Main.self.moveTo(oldLoc.getRelative(direction.multiply(i * 0.1)).withYawPitch(180 / Math.PI * yaw, oldLoc.getPitchTo(loc)));
                tick(tickdelay);
                if (getPlayerLoc() != nodes) {
                    System.out.println("HAAAALT");
                    halt = true;
                    break;
                }
            }
            Main.self.moveTo(loc.withYawPitch(180 / Math.PI * yaw, oldLoc.getPitchTo(loc)));
            tick(tickdelay);
            oldLoc = loc;
            if (halt == true) {
                break;
            }
        }
    }

    private Location getPlayerLoc() {
        for (Entity ent : Main.self.getEnvironment().getEntities()) {
            if (ent.getType() != EntityType.PLAYER) {
                continue;
            }
            if (ent.getType() == EntityType.PLAYER) {
                Location target = ent.getLocation();
                return target;
            }

        }

        return null;
    }

    public void stopWalk() {

        halt = true;
    }

    public void breakBlock(Location loc, int millis) throws InterruptedException {
        CallbackLock cLock = new CallbackLock();
        Main.self.breakBlock(loc, millis, () -> {
            cLock.finish();
        });
        int i = 0;
        while (true) {
            tick();
            if (++i % 3 == 0) {
                Main.self.swingArm(false);
            }
            if (cLock.isFinished()) {
                return;
            }
        }
    }

    public void breakBlock(Location loc) throws InterruptedException {
        breakBlock(loc, 1000);
    }

    public void clickBlock(Location loc) throws InterruptedException {
        Main.self.clickBlock(loc);
        tick();
    }

    public void tick() throws InterruptedException {
        timeStop = false;
        synchronized (timestopLock) {
            timestopLock.notifyAll();
        }
        synchronized (lock) {
            lock.wait();
        }
    }

    public void tick(int ticks) throws InterruptedException {
        for (int i = 0; i < ticks; i++) {
            tick();
        }
    }

    public void timeStop() throws InterruptedException {
        timeStop = true;
        tick();
    }

    public <T extends Event> T waitForEvent(final Class<T> eventClass, Predicate<T> eventFilter, int timeoutMillis) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        final Event[] event = new Event[1];

        final BlockingAI ai = this;
        final AtomicBoolean detected = new AtomicBoolean();
        detected.set(false);

        Main.self.registerEvents(new Listener() {
            @EventHandler
            public void listen(T hue) {
                if (eventClass.isInstance(hue) && eventFilter.test(hue)) {
                    detected.set(true);
                    event[0] = hue;
                    Main.self.unregisterEvents(this);
                    synchronized (lock) {
                        lock.notifyAll();
                    }
                }
            }
        });

        while (true) {
            synchronized (lock) {
                lock.wait();
            }
            if (detected.get()) {
                return (T) event[0];
            } else if (System.currentTimeMillis() - startTime > timeoutMillis) {
                return null;
            }
        }
    }

    public boolean openContainer(int x, int y, int z) throws InterruptedException {
        Main.self.placeBlock(x, y, z, BlockFace.NORTH);
        if (waitForEvent(WindowOpenFinishEvent.class, 5000) == null) {
            return false;
        }
        while (waitForEvent(SlotUpdateEvent.class, 250) != null) {
        }
        return true;
    }

    public boolean openContainer(Location loc) throws InterruptedException {
        return openContainer(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }

    public boolean closeContainer() throws InterruptedException {
        //final int expectedSlot = Main.self.getInventory().getSelectedSlot() + 9;
        Main.self.closeWindow();
        if (Main.self.getInventory().changed()) {
            boolean closed = waitForEvent(SlotUpdateEvent.class, 5000) != null;
            return closed;
        }
        tick();
        return true;
    }

    public int withdrawSlot(int sourceSlot) throws InterruptedException {
        int destSlot = InventoryUtil.findFreeStorageSlot(true);
        if (destSlot == -1) {
            return 3;
        }
        return transferItem(sourceSlot, destSlot);
    }

    public int depositSlot(int sourceSlot) throws InterruptedException {
        int destSlot = InventoryUtil.findFreeStorageSlot(false);
        if (destSlot == -1) {
            return 3;
        }
        return transferItem(sourceSlot, destSlot);
    }

    public int swapItems(int sourceSlot, int destSlot) throws InterruptedException {
        if (!clickSlot(sourceSlot, 0, 0)) {
            return 1;
        }
        if (!clickSlot(destSlot, 0, 0)) {
            return 2;
        }
        if (!clickSlot(sourceSlot, 0, 0)) {
            return 3;
        }
        return 0;
    }

    public int transferItem(int sourceSlot, int destSlot) throws InterruptedException {
        if (!clickSlot(sourceSlot, 0, 0)) {
            return 1;
        }
        if (!clickSlot(destSlot, 0, 0)) {
            return 2;
        }
        return 0;
    }

    public boolean clickSlot(int slot, int mode, int button) throws InterruptedException {
        AtomicBoolean confirm = new AtomicBoolean(false);
        Main.self.getInventory().click(slot, mode, button);
        waitForEvent(TransactionResponseEvent.class, (e) -> {
            confirm.set(((TransactionResponseEvent) e).getStatus() == 1);
            return true;
        }, 15000);
        return confirm.get();
    }

    public <T extends Event> T waitForEvent(final Class<T> eventClass, int timeoutMillis) throws InterruptedException {
        return waitForEvent(eventClass, (e) -> true, timeoutMillis);
    }

    private class CallbackLock {

        private boolean finished = false;

        public synchronized boolean isFinished() {
            return finished;
        }

        public synchronized void finish() {
            finished = true;
        }
    }

}
