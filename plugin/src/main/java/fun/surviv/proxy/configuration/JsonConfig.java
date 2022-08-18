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

package fun.surviv.proxy.configuration;

import java.io.File;
import java.io.IOException;

/**
 * SurvivalProxy; fun.surviv.proxy.configuration:JsonConfig
 *
 * @author LuciferMorningstarDev - https://github.com/LuciferMorningstarDev
 * @since 18.08.2022
 */
public class JsonConfig<T> {

    private final File dataFile;

    private Class<T> configurationClazz;

    private T configuration;

    /**
     * JsonConfig which can hold plain Java Objects which are GSON serializable ( needs to have Full Args Constructor AND Getter and Setter for each field )
     *
     * @param clazz    GSON serializable class
     * @param dataFile the configuration file
     */
    public JsonConfig(Class<T> clazz, File dataFile) {
        this.dataFile = dataFile;
        this.configurationClazz = clazz;
    }

    public void setDefault(Class clazz, Object defaultConfig) {
        this.configurationClazz = clazz;
        this.configuration = (T) defaultConfig;
    }

    public void load() throws IOException {
        if (dataFile.exists()) {
            configuration = ConfigLoader.loadConfig(configurationClazz, dataFile);
        }
    }

    public void load(boolean overrideDefault) throws IOException {
        if (!overrideDefault && configuration != null) {
            return;
        }
        load();
    }

    public void save() throws IOException {
        ConfigLoader.saveConfig(configuration, dataFile);
    }

    public void save(boolean overwrite) throws IOException {
        if (!overwrite && dataFile.exists()) {
            return;
        }
        save();
    }

    public T get() {
        return configuration;
    }

}
