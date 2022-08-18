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

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.proxy.Player;
import fun.surviv.proxy.Constants;
import fun.surviv.proxy.SurvivalProxyPlugin;
import fun.surviv.proxy.serialization.ComponentSerializer;

/**
 * SurvivalProxy; fun.surviv.proxy.listener:PlayerConnectListener
 *
 * @author LuciferMorningstarDev - https://github.com/LuciferMorningstarDev
 * @since 18.08.2022
 */
public class PlayerConnectListener {

    private final SurvivalProxyPlugin plugin;

    public PlayerConnectListener(SurvivalProxyPlugin plugin) {
        this.plugin = plugin;
    }

    @Subscribe(order = PostOrder.FIRST)
    public void onPlayerConnect(LoginEvent event) {
        if (event.getPlayer() == null) {
            return;
        }
        Player player = event.getPlayer();

        if (plugin.getMaintenanceConfig().get().isActive() && !player.hasPermission("proxy.maintenance.join")) {
            player.disconnect(ComponentSerializer.etAndHEX.deserialize(Constants.HEADER + "\n&cAktuell sind wir im Wartungsmodus." + "\n&7" + plugin.getMaintenanceConfig().get().getReason() + "\n" + Constants.FOOTER));
            return;
        }

        // TODO: use event.setResult(ResultedEvent.ComponentResult.denied(...));
    }

}
