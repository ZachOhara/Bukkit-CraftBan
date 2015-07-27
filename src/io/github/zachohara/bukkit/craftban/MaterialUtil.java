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
	 * @param purpose the activity that the returned list of materials is not allowed for
	 * ("crafting" for materials that can't be crafted, etc.).
	 * @param reportPurpose the activity that the returned list of materials is not allowed
	 * for, but given in a form that will be reported to the user ("craft" instead of
	 * "crafting", etc).
	 * @return {@code true} if the operation was successful; {@code false} otherwise.
	 */
	public static boolean toggleMaterialBan(CommandInstance instance, String purpose, String reportPurpose) {
		String input = instance.getArguments()[0];
		Material material = Material.matchMaterial(input);
		String materialName = null;
		if (material != null) {
			materialName = material.name();
		}
		int result = MaterialUtil.toggleMaterialBan(purpose, materialName);
		if (result == 0) {
			instance.sendError("@name(" + input + ") could not be banned from being " + reportPurpose);
		} else if (result == 1) {
			instance.sendMessage("@name(" + materialName + ") was successfully banned from being "
					+ reportPurpose);
		} else if (result == 2) {
			instance.sendMessage("@name(" + materialName + ") was successfuly un-banned from being "
					+ reportPurpose);
		}
		return true;
	}

	/**
	 * Bans a given material from being used for the given purpose, or unbans the material
	 * if it is already banned when this method is called.
	 *
	 * @param purpose the activity that the banned material is not allowed for ("crafting"
	 * for materials that can't be crafted, etc.).
	 * @param material the name of the material that should be banned or unbanned from the
	 * given purpose.
	 * @return 0 if the operation fails, 1 if the material was previously not banned and
	 * now is banned, or 2 if the material was previously banned and now is not.
	 */
	public static int toggleMaterialBan(String purpose, String material) {
		MaterialsList banned = CraftBanPlugin.getBannedList(purpose);
		if (banned == null) {
			return 0;
		}
		if (banned.removeMaterial(material)) {
			return 2;
		} else if (banned.addMaterial(material)) {
			return 1;
		}
		return 0;
	}

	/**
	 * Retrieves the list of materials, color it correctly, and then return it to the
	 * sender of the given command.
	 *
	 * @param instance the {@code CommandInstance} that should be responded to.
	 * @param purpose the activity that the returned list of materials is not allowed for
	 * ("crafting" for materials that can't be crafted, etc.).
	 * @param reportPurpose the activity that the returned list of materials is not allowed
	 * for, but given in a form that will be reported to the user ("craft" instead of
	 * "crafting", etc).
	 * @return {@code true} if the operation was successful; {@code false} otherwise.
	 */
	public static boolean listBannedMaterials(CommandInstance instance, String purpose, String reportPurpose) {
		String report = "The following materials cannot be " + reportPurpose + ": ";
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
