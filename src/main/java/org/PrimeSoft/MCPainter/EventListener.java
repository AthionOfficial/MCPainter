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
package org.PrimeSoft.MCPainter;

import org.PrimeSoft.MCPainter.utils.VersionChecker;
import org.PrimeSoft.MCPainter.Configuration.ConfigProvider;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginDescriptionFile;

/**
 *
 * @author SBPrime
 */
public class EventListener implements Listener {

    private MCPainterMain m_parent;

    public EventListener(MCPainterMain parent) {
        m_parent = parent;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        if (!PermissionManager.isAllowed(player, PermissionManager.Perms.AnnounceVersion)) {
            return;
        }

        if (!ConfigProvider.isConfigUpdated()) {
            MCPainterMain.say(player, ChatColor.BLUE + MCPainterMain.getPrefix()
                    + "Please update your config file!");
        }

        new Thread(new Runnable() {
            public void run() {
                if (ConfigProvider.getCheckUpdate()) {
                    PluginDescriptionFile desc = m_parent.getDescription();
                    MCPainterMain.say(player, ChatColor.BLUE + MCPainterMain.getPrefix()
                            + VersionChecker.CheckVersion(desc.getVersion()));
                }
            }
        }).start();
    }


    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        m_parent.removePlayer(event.getPlayer().getName());
    }
}
