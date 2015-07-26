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

import java.util.HashMap;
import java.util.Map;

import io.github.zachohara.bukkit.common.command.CommandExecutables;
import io.github.zachohara.bukkit.common.command.CommandRules;
import io.github.zachohara.bukkit.common.plugin.CommonPlugin;

/**
 * The {@code CraftBanPlugin} class is the entry point for plugin.
 * 
 * @author Zach Ohara
 */
public class CraftBanPlugin extends CommonPlugin {
	
	/**
	 * The currently-active instance of {@code CraftBanPlugin} that is being run on the
	 * server.
	 */
	private static CraftBanPlugin activePlugin;
	
	/**
	 * The collection of banned materials, sorted by banned purpose in the map. Every map
	 * entry pair consists of a string representing a purpose (eg. "crafting", "smelting",
	 * etc.) and a {@code MaterialsList} of materials that are banned from that purpose.
	 */
	private static Map<String, MaterialsList> bannedMaterialsMap;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onEnable() {
		super.onEnable();
		activePlugin = this;
		this.populateBannedMaterialsMap();
		this.getServer().getPluginManager().registerEvents(new MaterialsListener(), this);
	}
	
	/**
	 * Gets a reference to the {@code CraftBanPlugin} that is being used on the server.
	 * 
	 * @return the active {@code CraftBanPlugin} instance.
	 * @see #activePlugin
	 */
	public static CraftBanPlugin getActivePlugin() {
		return CraftBanPlugin.activePlugin;
	}
	
	/**
	 * Returns the list of materials that are banned from the given purpose.
	 * 
	 * @param purpose the purpose to query for.
	 * @return the materials that are banned from the given purpose.
	 * @see #bannedMaterialsMap
	 */
	public static MaterialsList getBannedList(String purpose) {
		return CraftBanPlugin.bannedMaterialsMap.get(purpose);
	}
	
	/**
	 * Populates the map of banned materials.
	 * 
	 * @see #bannedMaterialsMap
	 */
	private void populateBannedMaterialsMap() {
		bannedMaterialsMap = new HashMap<String, MaterialsList>();
		bannedMaterialsMap.put("crafting", new MaterialsList(this, "banned_crafting.dat"));
		bannedMaterialsMap.put("smelting", new MaterialsList(this, "banned_smelting.dat"));
		bannedMaterialsMap.put("smeltfueling", new MaterialsList(this, "banned_smelt_fueling.dat"));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<? extends CommandRules> getCommandRuleSet() {
		return Rules.class;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<? extends CommandExecutables> getCommandExecutableSet() {
		return Executables.class;
	}
	
}
