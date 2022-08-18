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

package fun.surviv.proxy;

import fun.surviv.proxy.serialization.ComponentSerializer;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;

/**
 * SurvivalProxy; fun.surviv.proxy:Constants
 *
 * @author LuciferMorningstarDev - https://github.com/LuciferMorningstarDev
 * @since 18.08.2022
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {

    public static final String ID = "survival-proxy";

    public static final boolean DEBUG_DAMAGE = false;
    public static final boolean DEBUG_SPAWN = false;

    public static final String CONFIG_PATH_NAME = "configuration/";
    public static final String DATA_PATH_NAME = "data/";
    public static final String ACCENT_COLOR = "&3";
    public static final String PREFIX = "&r[&9Survival&3Proxy&r]";
    public static final String CHAT_PREFIX = PREFIX + " &3»&r ";
    public static Component CHAT_PREFIX_COMPONENT = ComponentSerializer.etAndHEX.deserialize(CHAT_PREFIX).clickEvent(ClickEvent.openUrl("https://surviv.fun/")).hoverEvent(HoverEvent.showText(ComponentSerializer.etOnly.deserialize("&7Open Website")));
    public static final String HEADER = "&r&6============= " + PREFIX + " =============&6\n";
    public static final String FOOTER = "\n&6============= " + PREFIX + " =============&6&r";

    public static Component PREFIX_COMPONENT = ComponentSerializer.etAndHEX.deserialize(PREFIX).clickEvent(ClickEvent.openUrl("https://surviv.fun/")).hoverEvent(HoverEvent.showText(ComponentSerializer.etOnly.deserialize("&7Open Website")));

}
