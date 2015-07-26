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

/**
 * A {@code MaterialsListener} is a {@code Listener} that listens for inventory clicks
 * and crafting, and determines if the given event should be blocked due to banned
 * materials being used.
 * 
 * @author Zach Ohara
 */
public class MaterialsListener implements Listener {
	
	/**
	 * Checks the situation involved in a {@code CraftItemEvent} to make sure the all the
	 * involved materials are allowed to be crafted.
	 * 
	 * @param event the {@code CraftItemEvent} triggered by a player crafting something.
	 */
	@EventHandler
	public void onCraftItem(CraftItemEvent event) {
		MaterialsList banned = CraftBanPlugin.getBannedList("crafting");
		Material m = event.getRecipe().getResult().getType();
		if (banned.containsMaterial(m)) {
			reportBannedMaterial((Player) event.getWhoClicked(), m.name(), "craft");
			event.setCancelled(true);
		}
	}
	
	/**
	 * Schedules a task for the next 'tick' that will check the contents of a furnace.
	 * The scheduling is necessary over checking now, because if the click event was a
	 * player placing an item into a furnace, the item will not be registered with the
	 * furnace until the next tick.
	 * 
	 * @param event the {@code InventoryClickEvent} triggered by a player clicking
	 * anywhere in their inventory.
	 */
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		Bukkit.getScheduler().runTask(CraftBanPlugin.getActivePlugin(),
				new ScheduledInventoryCheck(event));
	}
	
	/**
	 * Checks that an {@code InventoryClickEvent} does not use banned materials, and
	 * handles any consequences of an event that <em>does</em> use banned materials.
	 *  
	 * @param event the event to check.
	 * @return {@code true} if the event did not involve the use of any banned materials,
	 * or {@code false} if the event did use banned materials and the consequences were
	 * handles accordingly.
	 */
	private static boolean checkInventoryEvent(InventoryClickEvent event) {
		if (event.getInventory() instanceof FurnaceInventory) {
			FurnaceInventory furnace = (FurnaceInventory) event.getInventory();
			Player p = (Player) event.getWhoClicked();
			if (!checkItemStackContents("smelting", furnace.getSmelting(), p, "smelt")) {
				removeOffendingItem(furnace, furnace.getSmelting(), p);
				return false;
			}
			if (!checkItemStackContents("smeltfueling", furnace.getFuel(), p, "fuel a furnace with")) {
				removeOffendingItem(furnace, furnace.getFuel(), p);
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Checks that a given {@code ItemStack} is not banned from the given activity. If
	 * the {@code ItemStack} is banned, this method will report the misuse to the player
	 * who attempted the activity, and to all the admins on the server.
	 * 
	 * @param banlist the name of the activty to check the material against.
	 * @param stack the material to check.
	 * @param clicker the player that tried to do the activity.
	 * @param reportActivity the name of the activity, in a form that will be reported to
	 * the admins and to the player.
	 * @return {@code true} if no banned materials were found, or {@code false} if baanned
	 * materials were found, and appropriate messages were sent to the player and to the
	 * admins.
	 */
	private static boolean checkItemStackContents(String banlist, ItemStack stack, Player clicker,
			String reportActivity) {
		MaterialsList banned = CraftBanPlugin.getBannedList(banlist);
		if (stack != null && banned.containsMaterial(stack.getType())) {
			reportBannedMaterial(clicker, stack.getType().name(), reportActivity);
			return false;
		}
		return true;
	}
	
	/**
	 * Moves the given {@code ItemStack} out of any given inventory, and into the
	 * inventory of the given player.
	 * 
	 * @param removefrom the {@code Inventory} to remove the {@code ItemStack} from.
	 * @param stack the {@code ItemStack} to move.
	 * @param returnto the {@code Player} to move the {@code ItemStack} to.
	 */
	private static void removeOffendingItem(Inventory removefrom, ItemStack stack, Player returnto) {
		returnto.getInventory().addItem(stack);
		removefrom.remove(stack);
	}
	
	/**
	 * Reports a banned material being used for the given activity, by the given player.
	 * Appropriate messages will be sent to the offending player, and to the admins of
	 * the server.
	 * 
	 * @param clicker the {@code Player} that tried to use the given banned material.
	 * @param material the name of the material that was used.
	 * @param reportActivity the activity that the player attempted but was blocked from.
	 */
	private static void reportBannedMaterial(Player clicker, String material, String reportActivity) {
		String playerReport = StringUtil.parseError("You cannot " + reportActivity
				+ " @name(" + material + ")!", null);
		String adminReport = StringUtil.parseString("@name(" + clicker.getName()
				+ ") tried to " + reportActivity + " @name(" + material + ")!", null);
		clicker.sendMessage(playerReport);
		PlayerUtil.sendAllAdmins(adminReport);
	}
	
	/**
	 * A {@code ScheduledInventoryCheck} is constructed with any given
	 * {@code InventoryClickEvent}. When the {@link #run()} method is called, the event
	 * will be checked for compliance with the current list of banned materials, and
	 * any violation will be handled accordingly.
	 */ 
	public static class ScheduledInventoryCheck implements Runnable {
		
		/**
		 * The event to check when the {@link #run()} method is called.
		 */
		private InventoryClickEvent event;
		
		/**
		 * Constructs a new {@code ScheduledInventoryCheck} with the given event to check.
		 * 
		 * @param event the event to check when the {@link #run()} method is called.
		 */
		public ScheduledInventoryCheck(InventoryClickEvent event) {
			this.event = event;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void run() {
			checkInventoryEvent(this.event);
		}
		
	}
	
}
