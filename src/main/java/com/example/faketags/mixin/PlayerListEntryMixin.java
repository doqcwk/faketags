package com.example.faketags.mixin;

import com.example.faketags.FakeTagsClient;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerListEntry.class)
public abstract class PlayerListEntryMixin {

    @Shadow public abstract GameProfile getProfile();

    @Inject(method = "getDisplayName", at = @At("RETURN"), cancellable = true)
    private void modifyTabDisplayName(CallbackInfoReturnable<Text> cir) {
        String name = getProfile().getName();
        String lowerName = name.toLowerCase();

        if (FakeTagsClient.CUSTOM_TAGS.containsKey(lowerName)) {
            Text existingDisplayName = cir.getReturnValue();
            Text prefix = FakeTagsClient.CUSTOM_TAGS.get(lowerName);

            MutableText newDisplayName = prefix.copy();
            if (existingDisplayName != null) {
                newDisplayName.append(existingDisplayName);
            } else {
                newDisplayName.append(Text.literal(name));
            }

            cir.setReturnValue(newDisplayName);
        }
    }
}