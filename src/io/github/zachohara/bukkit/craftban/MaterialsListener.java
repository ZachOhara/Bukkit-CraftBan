/* Copyright (C) 2015 Zach Ohara
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.zachohara.bukkit.craftban;

import io.github.zachohara.bukkit.common.util.PlayerUtil;
import io.github.zachohara.bukkit.common.util.StringUtil;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MaterialsListener implements Listener {
	
	//private InventoryClickEvent lastEvent;
	
	@EventHandler
	public void onCraftItem(CraftItemEvent event) {
		MaterialsList banned = CraftBanPlugin.getBannedList("crafting");
		Material m = event.getRecipe().getResult().getType();
		if (banned.containsMaterial(m)) {
			reportBannedMaterial((Player) event.getWhoClicked(), m.name(), "craft");
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		Bukkit.getScheduler().runTask(CraftBanPlugin.getActivePlugin(),
				new ScheduledInventoryCheck(event));
	}
	
	private static boolean checkInventoryEvent(InventoryClickEvent event) {
		if (event.getInventory() instanceof FurnaceInventory) {
			FurnaceInventory furnace = (FurnaceInventory) event.getInventory();
			Player p = (Player) event.getWhoClicked();
			if (!checkFurnaceContents("smelting", furnace.getSmelting(), p, "smelt")) {
				removeOffendingItem(furnace, furnace.getSmelting(), p);
				return false;
			}
			if (!checkFurnaceContents("smeltfueling", furnace.getFuel(), p, "fuel a furnace with")) {
				removeOffendingItem(furnace, furnace.getFuel(), p);
				return false;
			}
		}
		return true;
	}
	
	private static boolean checkFurnaceContents(String banlist, ItemStack stack, Player clicker,
			String reportActivity) {
		MaterialsList banned = CraftBanPlugin.getBannedList(banlist);
		if (stack != null && banned.containsMaterial(stack.getType())) {
			reportBannedMaterial(clicker, stack.getType().name(), reportActivity);
			return false;
		}
		return true;
	}
	
	private static void removeOffendingItem(Inventory removefrom, ItemStack stack, Player returnto) {
		returnto.getInventory().addItem(stack);
		removefrom.remove(stack);
	}
	
	private static void reportBannedMaterial(Player clicker, String material, String reportActivity) {
		String playerReport = StringUtil.parseError("You cannot " + reportActivity
				+ " @name(" + material + ")!", null);
		String adminReport = StringUtil.parseString("@name(" + clicker.getName()
				+ ") tried to " + reportActivity + " @name(" + material + ")!", null);
		clicker.sendMessage(playerReport);
		PlayerUtil.sendAllAdmins(adminReport);
	}
	
	public static class ScheduledInventoryCheck implements Runnable {
		
		private InventoryClickEvent event;
		
		public ScheduledInventoryCheck(InventoryClickEvent event) {
			this.event = event;
		}
		
		@Override
		public void run() {
			checkInventoryEvent(this.event);
		}
		
	}
	
	//	public static void  checkFurnaceContents3(FurnaceInventory furnace, Player clicker) {
	//		MaterialsList smeltBans = CraftBanPlugin.getBannedList("smelting");
	//		ItemStack smeltStack = furnace.getSmelting();
	//		Material smelting = null;
	//		if (smeltStack != null) {
	//			smelting = furnace.getSmelting().getType();
	//		}
	//		if (smeltBans.containsMaterial(smelting)) {
	//			reportBannedMaterial(clicker, smelting, "smelted", "smelt");
	//			furnace.setSmelting(new ItemStack(Material.AIR, 0));
	//			clicker.getInventory().addItem(smeltStack);
	//		}
	//		MaterialsList fuelBans = CraftBanPlugin.getBannedList("smeltfueling");
	//		ItemStack fuelStack = furnace.getFuel();
	//		Material fuel = null;
	//		if (fuelStack != null) {
	//			fuel = fuelStack.getType();;
	//		}
	//		if (fuelBans.containsMaterial(fuel)) {
	//			reportBannedMaterial(clicker, fuel, "used as fuel", "smelt with");
	//			furnace.setFuel(new ItemStack(Material.AIR, 0));
	//			clicker.getInventory().addItem(fuelStack);
	//		}
	//	}
	//	
	//	public static void reportBannedMaterial(Player p, Material m, String activityPast, String activityPresent) {
	//		p.closeInventory();
	//		String error = StringUtil.parseError("@name(" + m.name() + ") cannot be " + activityPast, null);
	//		String report = StringUtil.parseError("@name(" + p.getName() + ") tried to " + activityPresent + "@name " + m.name(), null);
	//		p.sendMessage(error);
	//		PlayerUtil.sendAllAdmins(report);
	//	}
	
}
