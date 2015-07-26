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

import io.github.zachohara.bukkit.common.command.CommandExecutables;
import io.github.zachohara.bukkit.common.command.CommandInstance;
import io.github.zachohara.bukkit.common.command.Implementation;

/**
 * The {@code Executables} interface represents the set of commands supported by this
 * plugin, and contains an executable object for each command that acts as the main
 * procedure for the command.
 * 
 * @author Zach Ohara
 */
public enum Executables implements CommandExecutables {
	
	BANCRAFT(new BanCraft()),
	BANSMELT(new BanSmelt()),
	BANFUEL(new BanFuel()),
	BANNEDCRAFTLIST(new BannedCraftList()),
	BANNEDSMELTLIST(new BannedSmeltList()),
	BANNEDFUELLIST(new BannedFuelList());
	
	/**
	 * The subclass of {@code Implementation} that contains an implementation for the
	 * command.
	 */
	private Implementation implement;
	
	/**
	 * Constructs a new constant with the given implementation.
	 * 
	 * @param implement the implementation of the command.
	 */
	private Executables(Implementation implement) {
		this.implement = implement;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Implementation getImplementation() {
		return this.implement;
	}
	
	/**
	 * The implementation for the 'bancraft' command.
	 */
	private static class BanCraft extends Implementation {
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public String getName() {
			return "bancraft";
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean doPlayerCommand(CommandInstance instance) {
			return MaterialsList.banMaterial(instance, "crafting", "crafted");
		}
		
	}
	
	/**
	 * The implementation for the 'bansmelt' command.
	 */
	private static class BanSmelt extends Implementation {
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public String getName() {
			return "bansmelt";
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean doPlayerCommand(CommandInstance instance) {
			return MaterialsList.banMaterial(instance, "smelting", "smelted");
		}
		
	}
	
	/**
	 * The implementation for the 'banfuel' command.
	 */
	private static class BanFuel extends Implementation {
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public String getName() {
			return "banfuel";
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean doPlayerCommand(CommandInstance instance) {
			return MaterialsList.banMaterial(instance, "smeltfueling", "used as fuel for smelting");
		}
		
	}
	
	/**
	 * The implementation for the 'bannedcraftlist' command.
	 */
	private static class BannedCraftList extends Implementation {
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public String getName() {
			return "bannedcraftlist";
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean doPlayerCommand(CommandInstance instance) {
			return MaterialsList.listBannedMaterials(instance, "crafting", "crafted");
		}
		
	}
	
	/**
	 * The implementation for the 'bannedsmeltlist' command.
	 */
	private static class BannedSmeltList extends Implementation {
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public String getName() {
			return "bannedsmeltlist";
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean doPlayerCommand(CommandInstance instance) {
			return MaterialsList.listBannedMaterials(instance, "smelting", "smelted");
		}
		
	}
	
	/**
	 * The implementation for the 'bannedfuellist' command.
	 */
	private static class BannedFuelList extends Implementation {
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public String getName() {
			return "bannedfuellist";
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean doPlayerCommand(CommandInstance instance) {
			return MaterialsList.listBannedMaterials(instance, "smeltfueling", "used as fuel for smelting");
		}
		
	}
	
}
