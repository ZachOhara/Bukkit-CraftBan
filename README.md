# Bukkit CraftBan

Bukkit CraftBan is a plugin for a Bukkit server that adds the ability for server owners and operators to selectively disallow players from using certain materials in crafting or smelting proceses. It is last confirmed to work on Bukkit version 1.7.9-R2. A full list of commands that this plugin adds is included below.

This plugin uses my Bukkit Common Library. The server *must* have that plugin installed for this plugin to work. The Bukkit Common Library can be downloaded from [GitHub](http://github.com/zachohara/bukkit-common)

Along with all of the source code, in the root folder of this repository you'll find [detailed documentation](javadoc) and a compiled .jar version of the project.

I may or may not support this software in the future, but feel free to send a pull request if you think you have a way to improve it. There is no warranty on this software, and I am absolutely not going to do full-time tech support for it, but I will try to be as helpful as I can if you're having problems. Send me an email, or create a new issue.

This entire repository is made available under the GNU General Public License v3.0. A full copy of this license is available as the [LICENSE](LICENSE) file in this repository, or at [gnu.org/licenses](http://www.gnu.org/licenses/).

## Installation

Download the "Location Manager v___.jar" from the root folder of this repository, or check out the [releases page](https://github.com/ZachOhara/Bukkit-CraftBan/releases) and download the latest version. Drop either file into the 'plugins' folder on your server.

## Added Commands:

### BanCraft

Usage: `/bancraft <material>`

This command will make it impossible for players to produce the specified material using an in-game crafting table or workbench. It is usable only by server owners and server operators. If the material is already banned when the command is run, the material will be un-banned.

### BanSmelt

Usage: `/bansmelt <material>`

This command will make it impossible for players to use the specified material in a furnace in order to produce something else. It is usable only by server owners and server operators. If the material is already banned when the command is run, the material will be un-banned.

### BanFuel

Usage: `/banfuel <material>`

This command will make it impossible for players to use the specified material as fuel in any smelting operation. It is usable only by server owners and server operators. If the material is already banned when the command is run, the material will be un-banned.

### BannedCraftList

Usage: `/bannedcraftlist` or `/bancraftlist` or `/craftbanlist`

This command will provide a list of all the materials that are currently blocked from being crafted.

### BannedSmeltList

Usage: `/bannedsmeltlist` or `/bansmeltlist` or `/smeltbanlist`

This command will provide a list of all the materials that are currently blocked from being smelted into something else.

### BannedFuelList

Usage: `/bannedfuellist` or `/banfuellist` of `/fuelbanlist`

This command will provide a list of all the materials that are currently blocked from being used as fuel in a furnace.
