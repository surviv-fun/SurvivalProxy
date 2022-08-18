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

package fun.surviv.proxy.commands;

import com.google.common.collect.Lists;
import com.velocitypowered.api.command.RawCommand;
import fun.surviv.proxy.SurvivalProxyPlugin;
import fun.surviv.proxy.utils.ChatUtil;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * SurvivalProxy; fun.surviv.proxy.commands:MaintenanceCommand
 *
 * @author LuciferMorningstarDev - https://github.com/LuciferMorningstarDev
 * @since 18.08.2022
 */
public class MaintenanceCommand implements RawCommand {

    private SurvivalProxyPlugin plugin;

    public MaintenanceCommand(SurvivalProxyPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(final Invocation invocation) {
        if (!invocation.source().hasPermission("proxy.command.maintenance")) {
            return;
        }
        try {
            if (plugin.getMaintenanceConfig().get().isActive()) {
                plugin.getMaintenanceConfig().get().setActive(false);
                if (invocation.arguments() != null) {
                    plugin.getMaintenanceConfig().get().setReason(invocation.arguments());
                }
                plugin.getMaintenanceConfig().save(true);

                ChatUtil.reply(invocation.source(), "&7Du hast die &3Wartungen &cdeaktiviert.");
                return;
            }

            plugin.getMaintenanceConfig().get().setActive(true);
            if (invocation.arguments() != null) {
                plugin.getMaintenanceConfig().get().setReason(invocation.arguments());
            }
            plugin.getMaintenanceConfig().save(true);

            ChatUtil.reply(invocation.source(), "&7Du hast die &3Wartungen &caktiviert.");
        } catch (Exception exception) {
            exception.printStackTrace();
            ChatUtil.replyError(invocation.source());
        }

    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(final Invocation invocation) {
        return CompletableFuture.completedFuture(Collections.emptyList());
    }

}
