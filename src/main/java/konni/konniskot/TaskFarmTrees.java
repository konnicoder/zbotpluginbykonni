/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package konni.konniskot;

import net.minecraft.server.NBTTagCompound;
import zedly.zbot.inventory.ItemStack;

/**
 *
 * @author Konstantin
 */
public class TaskFarmTrees extends Task {

    public TaskFarmTrees() {
        super(100);
    }

    public void run() {

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

}
