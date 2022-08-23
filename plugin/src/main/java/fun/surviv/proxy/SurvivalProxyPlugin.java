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

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import fun.surviv.proxy.commands.MaintenanceCommand;
import fun.surviv.proxy.commands.ReplyCommand;
import fun.surviv.proxy.commands.WhisperCommand;
import fun.surviv.proxy.configuration.JsonConfig;
import fun.surviv.proxy.configuration.defaults.DefaultDatabaseConfig;
import fun.surviv.proxy.configuration.defaults.DefaultPluginConfig;
import fun.surviv.proxy.configuration.defaults.DefaultTablistConfig;
import fun.surviv.proxy.configuration.types.DatabaseConfig;
import fun.surviv.proxy.configuration.types.MaintenanceConfig;
import fun.surviv.proxy.configuration.types.PluginConfig;
import fun.surviv.proxy.configuration.types.TablistConfig;
import fun.surviv.proxy.listener.GlobalChatListener;
import fun.surviv.proxy.listener.PlayerConnectListener;
import fun.surviv.proxy.listener.ProxyPingListener;
import fun.surviv.proxy.listener.TabListListener;
import fun.surviv.proxy.utils.ChatUtil;
import fun.surviv.proxy.utils.Resources;
import lombok.Getter;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * SurvivalProxy; fun.surviv.proxy:SurvivalProxyPlugin
 *
 * @author LuciferMorningstarDev - https://github.com/LuciferMorningstarDev
 * @since 18.08.2022
 */
@Plugin(id = "survival-proxy", name = "SurvivalProxyPlugin", version = "1.0-SNAPSHOT", authors = {"LuciferMorningstarDev"}, url = "https://surviv.fun", dependencies = {@Dependency(id = "luckperms")})
public class SurvivalProxyPlugin {

    @Getter
    private static SurvivalProxyPlugin instance;

    public static String VERSION;
    public static boolean LOADED = false;

    @Getter
    private ProxyServer server;
    @Getter
    private Logger logger;
    @Getter
    private @DataDirectory Path dataDir;
    @Getter
    private File dataFolder;

    @Getter
    private LuckPerms luckPerms;

    @Getter
    private JsonConfig<PluginConfig> pluginConfig;
    @Getter
    private JsonConfig<DatabaseConfig> databaseConfig;
    @Getter
    private JsonConfig<MaintenanceConfig> maintenanceConfig;
    @Getter
    private JsonConfig<TablistConfig> tablistConfig;

    @Inject
    public SurvivalProxyPlugin(ProxyServer server, Logger logger, @DataDirectory Path dataDir) {
        this.server = server;
        this.logger = logger;
        this.dataFolder = dataDir.toFile();
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        onLoad();
    }

    private void onLoad() {
        // throw runtime exception to stop plugin
        if (instance != null) {
            throw new RuntimeException("This plugin can only be loaded once... Please restart the server.");
        }
        instance = this;
    }

    private void onEnable() {
        VERSION = Resources.readToString("version.txt").trim();

        this.loadConfigurations();
        this.loadLuckPerms();
        this.registerCommandsAndListeners();

        ChatUtil.replySenderComponent(this.getServer().getConsoleCommandSource(), Constants.HEADER);
        ChatUtil.replySenderComponent(this.getServer().getConsoleCommandSource(), "&aPlugin Enabled");
        ChatUtil.replySenderComponent(this.getServer().getConsoleCommandSource(), Constants.FOOTER);
    }

    private void onDisable() {
        try {
            this.pluginConfig.save(true);
        } catch (Exception e) {
        }
        try {
            this.databaseConfig.save(true);
        } catch (Exception e) {
        }
        try {
            this.maintenanceConfig.save(true);
        } catch (Exception e) {
        }
        ChatUtil.replySenderComponent(this.getServer().getConsoleCommandSource(), Constants.HEADER);
        ChatUtil.replySenderComponent(this.getServer().getConsoleCommandSource(), "&cPlugin Disabled");
        ChatUtil.replySenderComponent(this.getServer().getConsoleCommandSource(), Constants.FOOTER);
    }

    private void registerCommandsAndListeners() {

        getServer().getEventManager().register(this, new GlobalChatListener(this));
        getServer().getEventManager().register(this, new ProxyPingListener(this));
        getServer().getEventManager().register(this, new PlayerConnectListener(this));
        getServer().getEventManager().register(this, new TabListListener(this));

        getServer().getCommandManager().register(getServer().getCommandManager().metaBuilder("maintenance").aliases("lock", "wartungen").build(), new MaintenanceCommand(this));
        getServer().getCommandManager().register(getServer().getCommandManager().metaBuilder("reply").aliases("lock").build(), new ReplyCommand(this));
        getServer().getCommandManager().register(getServer().getCommandManager().metaBuilder("whisper").aliases("w", "msg", "message").build(), new WhisperCommand(this));

    }

    private void loadLuckPerms() {
        this.luckPerms = LuckPermsProvider.get();
    }

    private void loadConfigurations() {
        if (!this.getDataFolder().exists()) {
            this.getDataFolder().mkdirs();
        }

        // load general pluginConfig
        try {
            File pluginConfigFile = new File(this.getDataFolder(), "config.json");
            this.pluginConfig = new JsonConfig<>(PluginConfig.class, pluginConfigFile);
            this.pluginConfig.setDefault(PluginConfig.class, new DefaultPluginConfig(VERSION + "-do_not_change"));
            this.pluginConfig.load(true);
            this.pluginConfig.save(false);
            if (this.pluginConfig.get().getConfigVersion().split("-")[1] != VERSION) {
                // TODO: rename and replace (or shutdown ??)
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        // define config dir
        File configPath = new File(this.getDataFolder(), Constants.CONFIG_PATH_NAME);
        if (!configPath.exists()) {
            configPath.mkdirs();
        }
        // define data dir
        File dataDor = new File(this.getDataFolder(), Constants.DATA_PATH_NAME);
        if (!dataDor.exists()) {
            dataDor.mkdirs();
        }

        // load general databaseConfig
        try {
            File databaseConfigFile = new File(configPath, "database.json");
            this.databaseConfig = new JsonConfig<>(DatabaseConfig.class, databaseConfigFile);
            this.databaseConfig.setDefault(DatabaseConfig.class, new DefaultDatabaseConfig(VERSION + "-do_not_change"));
            this.databaseConfig.load(true);
            this.databaseConfig.save(false);
            if (this.pluginConfig.get().getConfigVersion().split("-")[1] != VERSION) {
                // TODO: rename and replace (or shutdown ??)
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        // load general maintenanceConfig
        try {
            File maintenanceConfigFile = new File(configPath, "maintenance.json");
            this.maintenanceConfig = new JsonConfig<>(MaintenanceConfig.class, maintenanceConfigFile);
            this.maintenanceConfig.setDefault(MaintenanceConfig.class, new MaintenanceConfig(VERSION + "-do_not_change", false, "&cArbeitem am Server."));
            this.maintenanceConfig.load(true);
            this.maintenanceConfig.save(false);
            if (this.maintenanceConfig.get().getConfigVersion().split("-")[1] != VERSION) {
                // TODO: rename and replace (or shutdown ??)
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        // load general zablistConfig
        try {
            File tablistConfigFile = new File(configPath, "tablist.json");
            this.tablistConfig = new JsonConfig<>(TablistConfig.class, tablistConfigFile);
            this.tablistConfig.setDefault(TablistConfig.class, new DefaultTablistConfig(VERSION + "-do_not_change"));
            this.tablistConfig.load(true);
            this.tablistConfig.save(false);
            if (this.tablistConfig.get().getConfigVersion().split("-")[1] != VERSION) {
                // TODO: rename and replace (or shutdown ??)
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }

    }

    @Subscribe
    public void initProxy(ProxyInitializeEvent event) {
        this.onEnable();
    }

    @Subscribe
    public void onDisable(ProxyShutdownEvent event) {
        this.onDisable();
    }

}
