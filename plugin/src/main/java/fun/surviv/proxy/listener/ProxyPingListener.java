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

import com.google.common.collect.Lists;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.ServerPing;
import com.velocitypowered.api.util.Favicon;
import fun.surviv.proxy.Constants;
import fun.surviv.proxy.SurvivalProxyPlugin;
import fun.surviv.proxy.serialization.ComponentSerializer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

/**
 * SurvivalProxy; fun.surviv.proxy.listener:ProxyPingListener
 *
 * @author LuciferMorningstarDev - https://github.com/LuciferMorningstarDev
 * @since 18.08.2022
 */
public class ProxyPingListener {

    private final String versionStringified = "v1.19.2";
    private final SurvivalProxyPlugin plugin;

    static final String HEAD = Constants.CHAT_PREFIX + " &r[&l&9AKTUELL IN ENTWICKLUNG&r]";

    private int pingCount = 0;

    public ProxyPingListener(SurvivalProxyPlugin plugin) {
        this.plugin = plugin;
    }

    @Subscribe(order = PostOrder.LAST)
    public void onProxyPing(final ProxyPingEvent event) {
        final ServerPing ping = event.getPing();
        final ServerPing.Builder builder = ping.asBuilder();
        builder.onlinePlayers(ping.getPlayers().get().getOnline());

        if (plugin.getServer().getPlayerCount() > 0) {
            List<ServerPing.SamplePlayer> onlinePlayersSamples = Lists.newArrayList();
            for (Player player : plugin.getServer().getAllPlayers()) {
                if (player.hasPermission("proxy.ping.playerlist")) {
                    onlinePlayersSamples.add(new ServerPing.SamplePlayer(player.getUsername(), player.identity().uuid()));
                }
            }
            builder.samplePlayers(onlinePlayersSamples.toArray(new ServerPing.SamplePlayer[onlinePlayersSamples.size()]));
        } else {
            builder.samplePlayers(new ServerPing.SamplePlayer("Keine Spieler online.", UUID.randomUUID()));
        }

        if (plugin.getMaintenanceConfig().get().isActive()) {
            builder.maximumPlayers(0).version(new ServerPing.Version(ping.getVersion().getProtocol(), "SurvivFunMC" + versionStringified)).description(ComponentSerializer.etAndHEX.deserialize("&7Aktuell befinden wir uns im &cWartungsmodus&7." + "\n&7" + plugin.getMaintenanceConfig().get().getReason()));
            builder.favicon(new Favicon(favicon(new File("server-icon_red.png"))));
            event.setPing(builder.build());
            return;
        }

        builder.maximumPlayers(200).version(new ServerPing.Version(ping.getVersion().getProtocol(), "SurvivFunMC" + versionStringified));

        pingCount++;

        switch (pingCount) {
            case 0: {
                builder.description(ComponentSerializer.etAndHEX.deserialize(HEAD + "\n&r&3surviv.fun &7- Nichts für schwache &3Nerven&7."));
                builder.favicon(new Favicon(favicon(new File("server-icon.png"))));
                break;
            }
            case 1: {
                builder.description(ComponentSerializer.etAndHEX.deserialize(HEAD + "\n&r&3surviv.fun &7- Nimm dich vor dem \"&3Zombum&7\" in Acht!"));
                builder.favicon(new Favicon(favicon(new File("server-icon_green.png"))));
                break;
            }
            case 2: {
                builder.description(ComponentSerializer.etAndHEX.deserialize(HEAD + "\n&r&7Sind die &3Monster&7 zu stark, bist du zu schwach!"));
                builder.favicon(new Favicon(favicon(new File("server-icon_purple.png"))));
                break;
            }
            case 3: {
                builder.description(ComponentSerializer.etAndHEX.deserialize(HEAD + "\n&r&3surviv.fun &7- Ein gefährliches &3Spiel&7!"));
                builder.favicon(new Favicon(favicon(new File("server-icon_red.png"))));
                break;
            }
            default: {
                pingCount = 0;
                builder.description(ComponentSerializer.etAndHEX.deserialize(HEAD + "\n&r&3surviv.fun &7- Kämpfe bis zum letzten &3Atemzug&7!"));
                builder.favicon(new Favicon(favicon(new File("server-icon_blue.png"))));
                break;
            }
        }

        event.setPing(builder.build());
    }

    static String favicon(File file) {
        BufferedImage image;
        try {
            image = ImageIO.read(file);
        } catch (Exception ex) {
            return null;
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] imageBytes;
        try {
            ImageIO.write(image, "png", bos);
            imageBytes = bos.toByteArray();
            bos.close();
        } catch (Exception eee) {
            return null;
        }
        return "data:image/png;base64," + Base64.getEncoder().encodeToString(imageBytes);
    }

}
