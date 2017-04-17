/*
 * The MIT License
 *
 * Copyright 2014 SBPrime.
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

import java.awt.image.BufferedImage;
import org.PrimeSoft.MCPainter.blocksplacer.BlockLoger;
import org.PrimeSoft.MCPainter.Configuration.ConfigProvider;
import org.PrimeSoft.MCPainter.Drawing.Filters.FilterManager;
import org.PrimeSoft.MCPainter.Drawing.ImageHelper;
import org.PrimeSoft.MCPainter.Help;
import org.PrimeSoft.MCPainter.MapDrawer.MapHelper;
import org.PrimeSoft.MCPainter.PermissionManager;
import org.PrimeSoft.MCPainter.MCPainterMain;
import org.PrimeSoft.MCPainter.worldEdit.ICuboidSelection;
import org.PrimeSoft.MCPainter.worldEdit.IEditSession;
import org.PrimeSoft.MCPainter.worldEdit.ILocalPlayer;
import org.PrimeSoft.MCPainter.worldEdit.ILocalSession;
import org.PrimeSoft.MCPainter.worldEdit.IWorldEdit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

/**
 * @author SBPrime
 */
public class HdImageCommand {

	public HdImageCommand(MapHelper mapHelper) {
	}

	public void Execute(MCPainterMain sender, Player player, IWorldEdit worldEdit, String[] args) {
		if (args.length != 2) {
			Help.ShowHelp(player, Commands.COMMAND_IMAGEHD);
			return;
		}

		String url = args[1];
		final ICuboidSelection selection = worldEdit.getSelection(player);
		if (selection == null) {
			MCPainterMain.say(player, ChatColor.RED + "No selection.");
			return;
		}

		sender.getServer().getScheduler().runTaskAsynchronously(sender,
				new CommandThread(this, sender, player, url, worldEdit, selection));
	}

	private class CommandThread implements Runnable {

		private final ICuboidSelection m_selection;
		private final String m_url;
		private final Player m_player;
		private final MCPainterMain m_sender;
		private final IEditSession m_session;
		private final ILocalSession m_lSession;
		private final BlockFace m_rotation;

		private CommandThread(HdImageCommand command, MCPainterMain sender, Player player,
				String url, IWorldEdit worldEdit, ICuboidSelection selection) {
			m_sender = sender;
			m_player = player;
			m_url = url;
			m_selection = selection;
			ILocalPlayer localPlayer = worldEdit.wrapPlayer(player);
			m_rotation = calcHeading(localPlayer.getYaw());
			m_lSession = worldEdit.getSession(player);
			m_session = m_lSession.createEditSession(localPlayer);
		}

		public void run() {
			FilterManager fm = FilterManager.getFilterManager(m_player);
			ConfigProvider.getCommandPrice("imagehd");
			fm.getPrice();

			MCPainterMain.say(m_player, "Loading image...");
			BufferedImage img = ImageHelper.downloadImage(m_url);
			if (img == null) {
				MCPainterMain.say(m_player, ChatColor.RED + "Error downloading image " + ChatColor.WHITE + m_url);
				return;
			}

			img = fm.applyFilters(img, null);
			int imgH = img.getHeight();
			int imgW = img.getWidth();

			if (!PermissionManager.checkImage(m_player, imgW, imgH)) {
				return;
			}

			Location minPoint = m_selection.getMinimumPoint();
			Location maxPoint = m_selection.getMaximumPoint();
			int l = m_selection.getLength();
			int w = m_selection.getWidth();
			int h = m_selection.getHeight();
			int kx, kz;

			if (w > 1 && l > 1) {
				MCPainterMain.say(m_player, ChatColor.RED + "Invalid selection area.");
				return;
			} else if (w > l) {
				kx = 1;
				kz = 0;
			} else {
				kx = 0;
				kz = 1;
			}

			int bHeight = imgH / 128 + (imgH % 128 != 0 ? 1 : 0);
			int bWidth = imgW / 128 + (imgW % 128 != 0 ? 1 : 0);
			if (h < bHeight || (w < bWidth && l < bWidth)) {
				MCPainterMain.say(m_player, ChatColor.RED + "The selection is to smal, required: " + bWidth + "x" + bHeight);
				return;
			}

			Location pos = new Location(minPoint.getWorld(), minPoint.getBlockX(), maxPoint.getBlockY(), minPoint.getBlockZ());
			if (m_rotation == BlockFace.NORTH
					|| m_rotation == BlockFace.EAST) {
				pos = pos.add(kx * (bWidth - 1), 0, kz * (bWidth - 1));
				kx *= -1;
				kz *= -1;
			}
			MCPainterMain.say(m_player, "Drawing image...");
			BlockLoger loger = new BlockLoger(m_player, m_lSession, m_session, m_sender);

			for (int py = 0; py < bHeight; py++) {
				Location tmp = pos.clone();
				for (int px = 0; px < bWidth; px++) {
					tmp = tmp.add(kx, 0, kz);
				}
				pos = pos.add(0, -1, 0);
			}

			loger.logMessage("Drawing image done.");
			loger.logEndSession();
			loger.flush();

		}
	}

	private BlockFace calcHeading(double yaw) {
		yaw = (yaw + 360) % 360;
		if (yaw < 45) {
			return BlockFace.NORTH;
		} else if (yaw < 135) {
			return BlockFace.EAST;
		} else if (yaw < 225) {
			return BlockFace.SOUTH;
		} else if (yaw < 315) {
			return BlockFace.WEST;
		} else {
			return BlockFace.NORTH;
		}
	}
}

