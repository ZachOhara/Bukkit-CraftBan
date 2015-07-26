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

import org.bukkit.Material;

import io.github.zachohara.bukkit.common.command.CommandInstance;
import io.github.zachohara.bukkit.common.persistence.PersistentList;
import io.github.zachohara.bukkit.common.plugin.CommonPlugin;


public class MaterialsList extends PersistentList<Integer> {
	
	public MaterialsList(CommonPlugin owner, String filename) {
		super(owner, filename);
	}
	
	@SuppressWarnings("deprecation")
	public boolean addMaterial(String name) {
		Material m = Material.matchMaterial(name);
		if (m == null) {
			return false;
		}
		this.addSafe(m.getId());
		return true;
	}
	
	@SuppressWarnings("deprecation")
	public boolean containsMaterial(String name) {
		Material m = Material.matchMaterial(name);
		if (m == null) {
			return false;
		}
		return this.contains(m.getId());
	}
	
	@SuppressWarnings("deprecation")
	public boolean containsMaterial(Material m) {
		return m != null && this.contains(m.getId());
	}
	
	@SuppressWarnings("deprecation")
	public static boolean listBannedMaterials(CommandInstance instance, String purpose, String reportPurpose) {
		String report = "The following materials cannot be " +  reportPurpose + ": ";
		MaterialsList banned = CraftBanPlugin.getBannedList(purpose);
		if (banned == null) {
			instance.sendError("Could not retrieve the banned materials for " + purpose);
			return true;
		}
		for (int i : banned.listdata()) {
			String name = Material.getMaterial(i).name();
			report += "@name(" + name + "), ";
		}
		instance.sendMessage(report);
		return true;
	}
	
	public static boolean banMaterial(CommandInstance instance, String purpose, String reportPurpose) {
		String material = instance.getArguments()[0];
		boolean success = banMaterial(purpose, material);
		if (success) {
			String name = Material.matchMaterial(material).name();
			instance.sendMessage("@name(" + name + ") was successfully banned from being " + reportPurpose);
		} else {
			instance.sendError("@name(" + material + ") could not be banned from being " + reportPurpose);
		}
		return true;
	}
	
	public static boolean isMaterialBanned(String purpose, String material) {
		MaterialsList banned = CraftBanPlugin.getBannedList(purpose);
		if (banned == null) {
			return false;
		}
		return banned.containsMaterial(material);
	}
	
	public static boolean banMaterial(String purpose, String material) {
		MaterialsList banned = CraftBanPlugin.getBannedList(purpose);
		if (banned == null) {
			return false;
		}
		return banned.addMaterial(material);
	}
	
}
