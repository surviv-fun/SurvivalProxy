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

package fun.surviv.proxy.configuration.defaults;

import fun.surviv.proxy.Constants;
import fun.surviv.proxy.configuration.types.TablistConfig;

/**
 * SurvivalProxy; fun.surviv.proxy.configuration.defaults:DefaultTablistConfig
 *
 * @author LuciferMorningstarDev - https://github.com/LuciferMorningstarDev
 * @since 24.08.2022
 */
public class DefaultTablistConfig extends TablistConfig {

    public DefaultTablistConfig(String version) {
        super(
                version,
                "\n &3&lsurviv.fun &8| &7Minecraft Server \n &7Server &8» &f{servername} \n &7Spieler §8» §f{online}§8/§f{max} \n",
                "\n &7Discord §8» &fsurviv.fun/discord \n\n &8» &f&l" + Constants.PREFIX +  " \n",
                "{prefix} {username-color}{username}"
        );
    }

}
