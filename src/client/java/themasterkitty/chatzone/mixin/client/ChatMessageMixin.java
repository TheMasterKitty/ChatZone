package themasterkitty.chatzone.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import themasterkitty.chatzone.Main;

import java.util.Objects;

@Mixin(ChatScreen.class)
public abstract class ChatMessageMixin {
	@Shadow public abstract String normalize(String chatText);

	@Inject(at = @At("HEAD"), method = "sendMessage", cancellable = true)
	private void sendChatMessage(String dirtyMessage, boolean _addToHistory, CallbackInfoReturnable<Boolean> ci) {
		String message = normalize(dirtyMessage);

		if (message.startsWith("/")) return;
		if (!Objects.equals(Main.command, "") && !Main.regularChat) {
			ci.setReturnValue(true);
			assert MinecraftClient.getInstance().player != null;
			if (Main.command.startsWith(("/")))
				MinecraftClient.getInstance().player.networkHandler.sendCommand(Main.command.substring(1) + message);
			else {
				Main.regularChat = true;
				MinecraftClient.getInstance().player.networkHandler.sendChatMessage(message);
			}
		}
		else if (Main.regularChat) Main.regularChat = false;
	}
}