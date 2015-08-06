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

import io.github.zachohara.bukkit.simpleplugin.command.CommandSet;
import io.github.zachohara.bukkit.simpleplugin.plugin.SimplePlugin;

/**
 * The {@code CraftBanPlugin} class is the entry point for plugin.
 *
 * @author Zach Ohara
 */
public class CraftBanPlugin extends SimplePlugin {

	/**
	 * The currently-active instance of {@code CraftBanPlugin} that is being run on the
	 * server.
	 */
	private static CraftBanPlugin activePlugin;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onEnable() {
		super.onEnable();
		CraftBanPlugin.activePlugin = this;
		MaterialUtil.populateBannedMaterialsMap(this);
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
	 * {@inheritDoc}
	 */
	@Override
	public Class<? extends CommandSet> getCommandSet() {
		return Commands.class;
	}

}
