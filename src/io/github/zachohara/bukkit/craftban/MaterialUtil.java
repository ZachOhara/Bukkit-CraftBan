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

import io.github.zachohara.bukkit.common.command.CommandInstance;

import org.bukkit.Material;

/**
 * The {@code MaterialUtil} class contains some static methods that simplify interactions
 * with the lists of banned materials.
 * 
 * @author Zach Ohara
 */
public final class MaterialUtil {
	
	/**
	 * The {@code MaterialUtil} class should not be instantiable.
	 */
	private MaterialUtil() {
		
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
		boolean success = MaterialUtil.banMaterial(purpose, material);
		if (success) {
			String name = Material.matchMaterial(material).name();
			instance.sendMessage("@name(" + name + ") was successfully banned from being " + reportPurpose);
		} else {
			instance.sendError("@name(" + material + ") could not be banned from being " + reportPurpose);
		}
		return true;
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
			return true;
		}
		return banned.addMaterial(material);
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
	public static boolean listBannedMaterials(CommandInstance instance, String purpose, String reportPurpose) {
		String report = "The following materials cannot be " +  reportPurpose + ": ";
		MaterialsList banned = CraftBanPlugin.getBannedList(purpose);
		if (banned == null) {
			instance.sendError("Could not retrieve the banned materials for " + purpose);
			return true;
		}
		for (String material : banned.listdata()) {
			report += "@name(" + material + "), ";
		}
		instance.sendMessage(report);
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
	
}
