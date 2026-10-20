package com.example.faketags;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.text.Text;
import java.util.HashMap;
import java.util.Map;

public class FakeTagsClient implements ClientModInitializer {
    public static final Map<String, Text> CUSTOM_TAGS = new HashMap<>();

    @Override
    public void onInitializeClient() {
        setTag("xars13", "§c§l[KURUCU] ");
        
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("faketag")
                .then(ClientCommandManager.argument("player", StringArgumentType.string())
                .then(ClientCommandManager.argument("tag", StringArgumentType.greedyString())
                .executes(context -> {
                    String player = StringArgumentType.getString(context, "player");
                    String tagText = StringArgumentType.getString(context, "tag").replace("&", "§");
                    setTag(player, tagText + " ");
                    context.getSource().sendFeedback(Text.literal("§a[FakeTags] Tag eklendi!"), false);
                    return 1;
                }))));
        });
    }

    public static void setTag(String playerName, String rawFormattedTag) {
        CUSTOM_TAGS.put(playerName.toLowerCase(), Text.literal(rawFormattedTag));
    }
}