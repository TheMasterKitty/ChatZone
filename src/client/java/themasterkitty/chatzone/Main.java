package themasterkitty.chatzone;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class Main implements ClientModInitializer {
	public static String command = "";
	public static boolean regularChat = false;
    private LiteralArgumentBuilder<FabricClientCommandSource> literally(String name) {
        return literal(name)
                .then(literal("set")
                        .then(argument("cmd", StringArgumentType.greedyString())
                                .executes(ctx -> {
                                    command = StringArgumentType.getString(ctx, "cmd") + " ";
                                    assert MinecraftClient.getInstance().player != null;
                                    MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of("§aChatzone successfully set to " + command + "(" + (command.startsWith("/") ? "command prefix" : "message prefix") + ")"));

                                    return 1;
                                }))
                ).then(literal("send")
                        .then(argument("msg", StringArgumentType.greedyString())
                                .executes(ctx -> {
                                    String message = StringArgumentType.getString(ctx, "msg");
                                    regularChat = true;
                                    assert MinecraftClient.getInstance().player != null;
                                    MinecraftClient.getInstance().player.networkHandler.sendChatMessage(message);

                                    return 1;
                                }))
                        .executes(ctx -> {
                            assert MinecraftClient.getInstance().player != null;
                            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of("§c/cz send <msg>§6 - Sends a command to regular chat (without disabling the command)"));

                            return 1;
                        })
                ).then(literal("clear")
                        .executes(ctx -> {
                            command = "";
                            assert MinecraftClient.getInstance().player != null;
                            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of("§6The current chatzone was cleared."));

                            return 1;
                        })
                ).then(literal("get")
                        .executes(ctx -> {
                            assert MinecraftClient.getInstance().player != null;
                            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of("§6Your current chatzone is " + (command.isEmpty() ? "unset." : command)));

                            return 1;
                        })
                ).executes(ctx -> {
                    assert MinecraftClient.getInstance().player != null;
                    MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of("§c/cz set <cmd>§6 - Sets the command to run when sending a chat message (example: '/cz set /msg user')"));
                    MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of("§c/cz send <msg>§6 - Sends the message normally (without disabling the chatzone)"));
                    MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.of("§c/cz clear§6 - Clears the chatzone"));
                    return 1;
                });
    }
	@Override
	public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, ignored) -> {
            dispatcher.register(literally("cz"));
            dispatcher.register(literally("chatzone"));
        });
	}
}