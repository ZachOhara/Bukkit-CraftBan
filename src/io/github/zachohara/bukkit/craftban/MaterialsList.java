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

/**
 * A {@code MaterialsList} is a {@code PersistentList} that stores an abstract set of
 * block or item materials. This class also contains some static methods that act as easy
 * interfaces between the list and the client code.
 * 
 * @author Zach Ohara
 */
public class MaterialsList extends PersistentList<Integer> {
	
	/**
	 * Constructs a new {@code MaterialsList} with the given plugin as an owner, and the
	 * filename to store that data to. This constructor will create a new {@code ArrayList}
	 * object that will contain the data.
	 * 
	 * @param owner the plugin that created this object.
	 * @param filename the filename to store the object as.
	 */
	public MaterialsList(CommonPlugin owner, String filename) {
		super(owner, filename);
	}
	
	/**
	 * Adds the material with the given name to the list, and returns the success of the
	 * operation. If no material exists with the given name, this method will return
	 * {@code false}.
	 * 
	 * @param name the name of the material to add.
	 * @return the success of the operation.
	 */
	@SuppressWarnings("deprecation")
	public boolean addMaterial(String name) {
		Material m = Material.matchMaterial(name);
		if (m == null) {
			return false;
		}
		this.addSafe(m.getId());
		return true;
	}
	
	/**
	 * Determines if the list contains the material with the given name. If no material
	 * exists with the given name, this method will return {@code false}.
	 * 
	 * @param name the name of the material to test for.
	 * @return {@code true} if the given material is in the list; {@code false} otherwise.
	 */
	public boolean containsMaterial(String name) {
		Material m = Material.matchMaterial(name);
		if (m == null) {
			return false;
		}
		return this.containsMaterial(m);
	}
	
	/**
	 * Determines if the list contains the given material.
	 * 
	 * @param m the material to test for.
	 * @return {@code true} if the given material is in the list; {@code false} otherwise.
	 */
	@SuppressWarnings("deprecation")
	public boolean containsMaterial(Material m) {
		return m != null && this.contains(m.getId());
	}
	
	/**
	 * Retrieves the list of materials, color it correctly, and then return it to the
	 * sender of the given command.
	 * 
	 * @param instance the {@code CommandInstance} that should be responded to.
	 * @param purpose the activity that the returned list of materials is not allowed
	 * for ("crafting" for materials that can't be crafted, etc.).
	 * @param reportPurpose the activity that the returned list of materials is not
	 * allowed for, but given in a form that will be reported to the user ("craft"
	 * instead of "crafting", etc).
	 * @return {@code true} if the operation was successful; {@code false} otherwise.
	 */
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
	
	/**
	 * Bans the given material from the given purpose, and reports the sucess of the
	 * operation to the given {@code CommandInstance}.
	 * 
	 * @param instance the {@code CommandInstance} that should be responded to.
	 * @param purpose the activity that the returned list of materials is not allowed
	 * for ("crafting" for materials that can't be crafted, etc.).
	 * @param reportPurpose the activity that the returned list of materials is not
	 * allowed for, but given in a form that will be reported to the user ("craft"
	 * instead of "crafting", etc).
	 * @return {@code true} if the operation was successful; {@code false} otherwise.
	 */
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
	
	/**
	 * Determines if the given material is banned from being used for the given purpose.
	 * 
	 * @param purpose the activity that the material should be tested for.
	 * @param material the name of the material that should tested.
	 * @return {@code true} if the given material is banned from the given purpose;
	 * {@code false} otherwise.
	 */
	public static boolean isMaterialBanned(String purpose, String material) {
		MaterialsList banned = CraftBanPlugin.getBannedList(purpose);
		if (banned == null) {
			return false;
		}
		return banned.containsMaterial(material);
	}
	
	/**
	 * Bans a given material from being used for the given purpose.
	 * 
	 * @param purpose the activity that the now-banned material is not allowed for
	 * ("crafting" for materials that can't be crafted, etc.).
	 * @param material the name of the material that should be banned from the given
	 * purpose.
	 * @return {@code true} if the operation was successful; {@code false} otherwise.
	 */
	public static boolean banMaterial(String purpose, String material) {
		MaterialsList banned = CraftBanPlugin.getBannedList(purpose);
		if (banned == null) {
			return false;
		}
		return banned.addMaterial(material);
	}
	
}
