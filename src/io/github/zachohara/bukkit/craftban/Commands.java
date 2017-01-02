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

import io.github.zachohara.bukkit.simpleplugin.command.CommandInstance;
import io.github.zachohara.bukkit.simpleplugin.command.CommandSet;
import io.github.zachohara.bukkit.simpleplugin.command.Implementation;
import io.github.zachohara.bukkit.simpleplugin.command.Properties;
import io.github.zachohara.bukkit.simpleplugin.command.Properties.Source;
import io.github.zachohara.bukkit.simpleplugin.command.Properties.Target;

/**
 * The {@code Commands} interface represents the set of commands supported by this plugin,
 * and contains a {@code Properties} object for each command.
 *
 * @author Zach Ohara
 * @see Properties
 */
public enum Commands implements CommandSet {

	BANCRAFT(new Properties(1, 1, Source.OP_ONLY, Target.NONE, new BanCraft())),
	BANSMELT(new Properties(BANCRAFT, new BanSmelt())),
	BANFUEL(new Properties(BANCRAFT, new BanFuel())),
	BANNEDCRAFTLIST(new Properties(0, 0, Source.ALL, Target.NONE, new BannedCraftList())),
	BANNEDSMELTLIST(new Properties(BANNEDCRAFTLIST, new BannedSmeltList())),
	BANNEDFUELLIST(new Properties(BANNEDCRAFTLIST, new BannedFuelList()));

	/**
	 * The {@code Properties} object specific to a single command.
	 */
	private Properties properties;

	/**
	 * Constructs a new {@code Commands} with the given {@code Properties} for this
	 * command.
	 *
	 * @param p the {@code Properties} for this command.
	 */
	private Commands(Properties p) {
		this.properties = p;
	}
	
	@Override
	public Properties getProperties() {
		return this.properties;
	}

	/**
	 * The implementation for the 'bancraft' command.
	 */
	private static class BanCraft extends Implementation {

		@Override
		public boolean doPlayerCommand(CommandInstance instance) {
			return MaterialUtil.toggleMaterialBan(instance, "crafting", "crafted");
		}

	}

	/**
	 * The implementation for the 'bansmelt' command.
	 */
	private static class BanSmelt extends Implementation {

		@Override
		public boolean doPlayerCommand(CommandInstance instance) {
			return MaterialUtil.toggleMaterialBan(instance, "smelting", "smelted");
		}

	}

	/**
	 * The implementation for the 'banfuel' command.
	 */
	private static class BanFuel extends Implementation {

		@Override
		public boolean doPlayerCommand(CommandInstance instance) {
			return MaterialUtil.toggleMaterialBan(instance, "smeltfueling", "used as fuel for smelting");
		}

	}

	/**
	 * The implementation for the 'bannedcraftlist' command.
	 */
	private static class BannedCraftList extends Implementation {

		@Override
		public boolean doPlayerCommand(CommandInstance instance) {
			return MaterialUtil.listBannedMaterials(instance, "crafting", "crafted");
		}

	}

	/**
	 * The implementation for the 'bannedsmeltlist' command.
	 */
	private static class BannedSmeltList extends Implementation {

		@Override
		public boolean doPlayerCommand(CommandInstance instance) {
			return MaterialUtil.listBannedMaterials(instance, "smelting", "smelted");
		}

	}

	/**
	 * The implementation for the 'bannedfuellist' command.
	 */
	private static class BannedFuelList extends Implementation {

		@Override
		public boolean doPlayerCommand(CommandInstance instance) {
			return MaterialUtil.listBannedMaterials(instance, "smeltfueling", "used as fuel for smelting");
		}

	}

}
