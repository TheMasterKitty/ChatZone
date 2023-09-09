package themasterkitty.chatzone.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import themasterkitty.chatzone.Main;

import java.util.Objects;

@Mixin(ClientPlayerEntity.class)
public class ChatMessageMixin {
	@Inject(at = @At("HEAD"), method = "sendChatMessage", cancellable = true)
	private void sendChatMessage(String message, @Nullable Text preview, CallbackInfo info) {
		if (!Objects.equals(Main.command, "") && !Main.regularChat) {
			info.cancel();
			assert MinecraftClient.getInstance().player != null;
			MinecraftClient.getInstance().player.sendCommand(Main.command + message);
		}
		else if (Main.regularChat) Main.regularChat = false;
	}
}