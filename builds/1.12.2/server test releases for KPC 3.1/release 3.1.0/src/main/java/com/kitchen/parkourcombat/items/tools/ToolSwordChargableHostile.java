package com.kitchen.parkourcombat.items.tools;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class ToolSwordChargableHostile extends ToolSwordChargable {

	public ToolSwordChargableHostile(String name, ToolMaterial material) {
		super(name, material);
		// TODO Auto-generated constructor stub
	}
	
	@Override
    public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced) {
        tooltip.add("Attack enderman or endermites with this item to return it to full strength!");
    }
	
	@Override
	public void repair(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
		if (target instanceof EntityMob) {
			stack.damageItem(-4, attacker);
		} else {
			attacker.attackEntityFrom(DamageSource.MAGIC, 10);
		}
	}
	
}
