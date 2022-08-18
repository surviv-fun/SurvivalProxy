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

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * SurvivalProxy; fun.surviv.proxy.commands:ReplyCommand
 *
 * @author LuciferMorningstarDev - https://github.com/LuciferMorningstarDev
 * @since 18.08.2022
 */
public class ReplyCommand implements RawCommand {

    private SurvivalProxyPlugin plugin;

    public ReplyCommand(SurvivalProxyPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(final Invocation invocation) {
        if (!invocation.source().hasPermission("proxy.command.reply")) {
            return;
        }
        if (!(invocation.source() instanceof Player)) {
            ChatUtil.reply(invocation.source(), "&7Der Command funktioniert nur als Spieler.");
            return;
        }
        String args[] = invocation.arguments().split("\\s+");

        if (args.length > 0) {

            Player sender = (Player) invocation.source();
            if (!StaticCache.reply.containsKey(sender.getUniqueId())) {
                invocation.source().sendMessage(ComponentSerializer.sectionOnly.deserialize("§8[§5MSG§8] §cDu hast noch keinem Nutzer geschrieben."));
                return;
            }

            Player target = plugin.getServer().getPlayer(StaticCache.reply.get(sender.getUniqueId())).get();
            if (target == null || !target.isOnlineMode()) {
                invocation.source().sendMessage(ComponentSerializer.sectionOnly.deserialize("§8[§5MSG§8] §cDer letzte Nutzer ist nun offline."));
                return;
            }

            sender.sendMessage(ComponentSerializer.sectionOnly.deserialize("§8[§5MSG§8] §7Du -> " + target.getUsername() + " §8\u00bb§7 " + invocation.arguments()));
            target.sendMessage(ComponentSerializer.sectionOnly.deserialize("§8[§5MSG§8] §7" + sender.getUsername() + " -> Dir §8\u00bb§7 " + invocation.arguments()));

            StaticCache.reply.put(sender.getUniqueId(), target.getUniqueId());
            StaticCache.reply.put(target.getUniqueId(), sender.getUniqueId());

            return;
        }

        ChatUtil.reply(invocation.source(), "§8[§5MSG§8] §7Benutze &3/r <Nachricht>");
    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(final Invocation invocation) {
        return CompletableFuture.completedFuture(Collections.emptyList());
    }

}
