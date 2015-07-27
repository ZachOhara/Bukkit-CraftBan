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

import io.github.zachohara.bukkit.common.persistence.PersistentList;
import io.github.zachohara.bukkit.common.plugin.CommonPlugin;

/**
 * A {@code MaterialsList} is a {@code PersistentList} that stores an abstract set of
 * block or item materials. This class also contains some static methods that act as easy
 * interfaces between the list and the client code.
 * 
 * @author Zach Ohara
 */
public class MaterialsList extends PersistentList<String> {
	
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
	public boolean addMaterial(String name) {
		Material m = Material.matchMaterial(name);
		if (m == null) {
			return false;
		}
		this.addSafe(m.name());
		return true;
	}
	
	/**
	 * Removes the material with the given name from the list, and returns the sucess of the operation.
	 * 
	 * @param name the name of the material to remove.
	 * @return the success of the operation.
	 */
	public boolean removeMaterial(String name) {
		Material m = Material.matchMaterial(name);
		return this.remove(m.name());
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
	public boolean containsMaterial(Material m) {
		return m != null && this.contains(m.name());
	}
	
}
