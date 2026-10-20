package com.example.faketags.mixin;

import com.example.faketags.FakeTagsClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Map;

@Mixin(ChatHud.class)
public class ChatHudMixin {

    @ModifyVariable(method = "addMessage(Lnet/minecraft/text/Text;)V", at = @At("HEAD"), argsOnly = true)
    private Text modifyChatMessage(Text message) {
        if (message == null) return null;

        String rawString = message.getString();

        for (Map.Entry<String, Text> entry : FakeTagsClient.CUSTOM_TAGS.entrySet()) {
            String targetPlayer = entry.getKey();

            if (rawString.toLowerCase().contains(targetPlayer)) {
                MutableText customPrefix = entry.getValue().copy();
                return customPrefix.append(message);
            }
        }
        return message;
    }
}