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

import com.velocitypowered.api.command.RawCommand;
import com.velocitypowered.api.proxy.Player;
import fun.surviv.proxy.StaticCache;
import fun.surviv.proxy.SurvivalProxyPlugin;
import fun.surviv.proxy.serialization.ComponentSerializer;
import fun.surviv.proxy.utils.ChatUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * SurvivalProxy; fun.surviv.proxy.commands:WhisperCommand
 *
 * @author LuciferMorningstarDev - https://github.com/LuciferMorningstarDev
 * @since 18.08.2022
 */
public class WhisperCommand implements RawCommand {

    private SurvivalProxyPlugin plugin;

    public WhisperCommand(SurvivalProxyPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(final Invocation invocation) {
        if (!invocation.source().hasPermission("proxy.command.whisper")) {
            return;
        }
        if (!(invocation.source() instanceof Player)) {
            ChatUtil.reply(invocation.source(), "&7Der Command funktioniert nur als Spieler.");
            return;
        }

        String args[] = invocation.arguments().split("\\s+");

        if (args.length > 1) {
            Player sender = (Player) invocation.source();
            Player target = plugin.getServer().getPlayer(args[0]).get();

            if (target == null || !target.isOnlineMode()) {
                invocation.source().sendMessage(ComponentSerializer.sectionOnly.deserialize("§8[§5MSG§8] §cDieser Spieler existiert nicht oder ist offline."));
            }

            String message = "";
            for (int i = 1; i < args.length; ++i) {
                message = message + " " + args[i];
            }
            sender.sendMessage(ComponentSerializer.sectionOnly.deserialize("§8[§5MSG§8] §7Du -> " + target.getUsername() + " §8\u00bb§7 " + invocation.arguments()));
            target.sendMessage(ComponentSerializer.sectionOnly.deserialize("§8[§5MSG§8] §7" + sender.getUsername() + " -> Dir §8\u00bb§7 " + invocation.arguments()));

            StaticCache.reply.put(sender.getUniqueId(), target.getUniqueId());
            StaticCache.reply.put(target.getUniqueId(), sender.getUniqueId());

            return;
        }

        ChatUtil.reply(invocation.source(), "§8[§5MSG§8] §7Benutze &3/msg <Spieler> <Nachricht>");

    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(final Invocation invocation) {
        String args[] = invocation.arguments().split("\\s+");
        List<String> completions = new ArrayList<>();
        switch (args.length) {
            case 1: {
                List<String> available = new ArrayList<>();
                for (Player online : plugin.getServer().getAllPlayers()) {
                    available.add(online.getUsername());
                }
                ChatUtil.copyPartialMatches(args[0], available, completions);
                break;
            }
            default:
                break;
        }
        Collections.sort(completions);
        return CompletableFuture.completedFuture(completions);
    }

}
