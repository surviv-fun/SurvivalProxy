/*
 * Copyright (c) LuciferMorningstarDev <contact@lucifer-morningstar.dev>
 * Copyright (c) surviv.fun <contact@surviv.fun>
 * Copyright (C) surviv.fun team and contributors
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
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package fun.surviv.proxy.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;
import fun.surviv.proxy.StaticCache;
import fun.surviv.proxy.SurvivalProxyPlugin;
import fun.surviv.proxy.serialization.ComponentSerializer;

/**
 * SurvivalProxy; fun.surviv.proxy.listener:GlobalChatListener
 *
 * @author LuciferMorningstarDev - https://github.com/LuciferMorningstarDev
 * @since 18.08.2022
 */
public class GlobalChatListener {

    private final SurvivalProxyPlugin plugin;

    public GlobalChatListener(SurvivalProxyPlugin plugin) {
        this.plugin = plugin;
    }

    @Subscribe
    public void handlePlayerChat(PlayerChatEvent event) {
        // TODO: FORGET THE FXXXNG CHAT EVENT IN A PROXY XD

        // Player player = event.getPlayer();

        // if(player.hasPermission("proxy.teamchat") && (event.getMessage().toLowerCase().startsWith("@team ") || event.getMessage().toLowerCase().startsWith("@team"))) {
        //     for(Player current : plugin.getServer().getAllPlayers()) {
        //         if(current.hasPermission("proxy.teamchat")) {
        //             current.sendMessage(ComponentSerializer.etAndHEX.deserialize("&7&l[&r&3TEAM&8CHAT&7&l]&r ").append(ComponentSerializer.etAndHEX.deserialize("&7" + player.getUsername() + " &r")).append(ComponentSerializer.etAndHEX.deserialize(event.getMessage())));
        //         }
        //     }
        // }

        // if(StaticCache.globalChat.contains(player.getUniqueId())) {
        //     // TODO: broadcast global chat message
        // }

    }

}
