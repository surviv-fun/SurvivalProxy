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

package fun.surviv.proxy.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * SurvivalProxy; fun.surviv.proxy.utils:Resources
 *
 * @author LuciferMorningstarDev - https://github.com/LuciferMorningstarDev
 * @since 18.08.2022
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Resources {

    public static void saveResource(File folder, String resourcePath, boolean overwrite) {
        if (!resourcePath.startsWith("/")) {
            resourcePath = "/" + resourcePath;
        }
        File file = new File(folder, resourcePath);
        if (!file.exists() || overwrite) {
            try (InputStream in = Resources.class.getResourceAsStream(resourcePath)) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Read File content to String
     *
     * @param resourcePath
     * @return content
     */
    public static String readToString(String resourcePath) {
        try {
            return new String(Resources.class.getClassLoader().getResourceAsStream(resourcePath).readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

}
