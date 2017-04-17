/*
 * The MIT License
 *
 * Copyright 2012 SBPrime.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.PrimeSoft.MCPainter.Commands;

import org.PrimeSoft.MCPainter.Drawing.Blocks.IBlockProvider;
import org.PrimeSoft.MCPainter.Drawing.Blocks.IDrawableElement;
import org.PrimeSoft.MCPainter.Drawing.IColorMap;
import org.PrimeSoft.MCPainter.Help;
import org.PrimeSoft.MCPainter.MCPainterMain;
import org.PrimeSoft.MCPainter.worldEdit.IWorldEdit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

/**
 * @author SBPrime
 */
public class BlockCommand {
	/**
	 * Blocks provider
	 */
	private final IBlockProvider m_blockProvider;

	public BlockCommand(MCPainterMain plugin) {
		m_blockProvider = plugin.getBlockProvider();
	}

	public void Execte(MCPainterMain sender, final Player player, IWorldEdit worldEdit,
			final IColorMap colorMap, String[] args) {
		if (args.length < 1 || args.length > 2) {
			Help.ShowHelp(player, Commands.COMMAND_BLOCK);
			return;
		}

		Material blockMaterial = null;
		int matId = -1;
		String blockName = null;

		if (args.length == 1) {
			ItemStack inHand = player.getInventory().getItemInMainHand();
			MaterialData tmp = inHand.getData();

			blockMaterial = tmp.getItemType();
			tmp.getData();
		} else {
			String[] materialParts = args[1].split(":");
			if (materialParts.length == 0 || materialParts[0] == null
					|| materialParts[0].length() == 0) {
				ItemStack inHand = player.getInventory().getItemInMainHand();
				MaterialData tmp = inHand.getData();

				blockMaterial = tmp.getItemType();
				if (materialParts.length > 1) {
					try {
						Integer.parseInt(materialParts[1]);
					} catch (NumberFormatException e) {
						tmp.getData();
					}
				} else {
					tmp.getData();
				}
			} else {
				try {
					matId = Integer.parseInt(materialParts[0]);
				} catch (NumberFormatException e) {
					matId = -1;
				}

				if (matId == -1) {
					blockName = materialParts[0].toUpperCase();
				} else {
					blockMaterial = Material.getMaterial(matId);
				}

				if (materialParts.length > 1) {
					try {
						Integer.parseInt(materialParts[1]);
					} catch (NumberFormatException e) {
					}
				} else {
				}
			}

			if (blockMaterial == null && matId == -1 && blockName == null) {
				MCPainterMain.say(player, ChatColor.RED + "Unknown material");
				return;
			}
		}

		if (blockMaterial != null) {
			blockName = blockMaterial.toString();
		}
		String name = (blockName != null ? blockName : matId).toString();
		MCPainterMain.say(player, "Drawing block " + name + "...");

		final IDrawableElement element = (blockName != null) ? m_blockProvider.getBlock(blockName) : m_blockProvider.getBlock(matId);

		if (element == null) {
			MCPainterMain.say(player, ChatColor.RED + "Block " + name + " not supported");
			return;
		}

	}

}