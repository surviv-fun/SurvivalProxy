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

import com.velocitypowered.api.command.CommandSource;
import fun.surviv.proxy.Constants;
import fun.surviv.proxy.serialization.ComponentSerializer;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.kyori.adventure.text.Component;

import java.util.Collection;

/**
 * SurvivalProxy; fun.surviv.proxy.utils:ChatUtil
 *
 * @author LuciferMorningstarDev - https://github.com/LuciferMorningstarDev
 * @since 18.08.2022
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatUtil {

    /**
     * @param message
     * @return component
     */
    public static Component toColoredComponent(String message) {
        return ComponentSerializer.etAndHEX.deserialize(message);
    }

    /**
     * Translate placeholders of placeholder api ( if not enabled placeholders will not be translated )
     *
     * @param source
     * @param message
     * @return messageReplacement
     */
    public static String replacePlaceholders(CommandSource source, String message) {
        return message; // TODO: replace placeholders
    }

    /**
     * Reply with a message to a sender
     *
     * @param source
     * @param message
     * @return success
     */
    public static boolean reply(CommandSource source, String message) {
        return replySenderComponent(source, message);
    }

    public static boolean replyError(CommandSource source) {
        return reply(source, "&cError! &7Details in console...");
    }

    /**
     * Reply with a message to a sender
     *
     * @param source
     * @param message
     * @return success
     */
    public static boolean replySenderComponent(CommandSource source, String message) {
        source.sendMessage(toColoredComponent("").append(Constants.CHAT_PREFIX_COMPONENT).append(toColoredComponent(message)));
        return true;
    }

    /**
     * Reply with a message to a sender replyUnPrefixedSenderComponent
     *
     * @param source
     * @param message
     * @return success
     */
    public static boolean replyUnPrefixedSenderComponent(CommandSource source, String message) {
        source.sendMessage(toColoredComponent(message));
        return true;
    }

    /**
     * Reply with a message to a sender replyUnPrefixedSenderComponent
     *
     * @param source
     * @param message
     * @return success
     */
    public static boolean replyUnPrefixed(CommandSource source, String message) {
        return replyUnPrefixedSenderComponent(source, message);
    }

    /**
     * Reply with a component message to a sender
     *
     * @param source
     * @param message
     * @return success
     */
    public static boolean replySender(CommandSource source, Component message) {
        source.sendMessage(message);
        return true;
    }

    public static void copyPartialMatches(String input, Collection<String> available, Collection<String> toAppend) {
        for (String string : available) {
            if (string.toLowerCase().startsWith(input.toLowerCase())) {
                toAppend.add(string);
            }
        }
    }

}
