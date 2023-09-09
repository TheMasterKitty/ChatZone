package themasterkitty.chatzone;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import java.io.*;
import java.util.Objects;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;
public class Main implements ClientModInitializer {
	public static String command = "";
	String offsave = "";
	public static boolean regularChat = false;
	final File data = new File("chatzone.txt");
	@Override
	public void onInitializeClient() {
		if (data.exists()) {
			StringBuilder builder = new StringBuilder();
			try (BufferedReader reader = new BufferedReader(new FileReader(data))) {
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			command = builder.toString();
		}
		else {
			try {
				data.createNewFile();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		ClientCommandRegistrationCallback.EVENT.register((dispatcher, ignored) -> dispatcher.register(literal("chatzone")
				.then(argument("command", StringArgumentType.greedyString())
					.executes(context -> {
						command = StringArgumentType.getString(context, "command") + " ";
						assert MinecraftClient.getInstance().player != null;
						MinecraftClient.getInstance().player.sendMessage(Text.literal("§aCommand successfully set to /" + command));
						try (FileWriter writer = new FileWriter(data)) {
							writer.write(command);
						} catch (IOException e) {
							e.printStackTrace();
						}

						return 1;
					})
				).then(literal("off")
					.executes(context -> {
						offsave = command;
						command = "";
						assert MinecraftClient.getInstance().player != null;
						MinecraftClient.getInstance().player.sendMessage(Text.literal("§aCommand successfully cleared"));
						try (FileWriter writer = new FileWriter(data)) {
							writer.write(command);
						} catch (IOException e) {
							e.printStackTrace();
						}

						return 1;
					})
				).then(literal("on")
					.executes(context -> {
						assert MinecraftClient.getInstance().player != null;
						if (!Objects.equals(offsave, "")) {
							command = offsave;
							offsave = "";
							MinecraftClient.getInstance().player.sendMessage(Text.literal("§aCommand successfully set to /" + command));
							try (FileWriter writer = new FileWriter(data)) {
								writer.write(command);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						else MinecraftClient.getInstance().player.sendMessage(Text.literal("§cYou don't have a previous save of /chatzone off."));

						return 1;
					})
				).then(literal("send")
					.then(argument("message", StringArgumentType.greedyString())
						.executes(context -> {
							String message = StringArgumentType.getString(context, "message");
							regularChat = true;
							assert MinecraftClient.getInstance().player != null;
							MinecraftClient.getInstance().player.sendChatMessage(message, Text.literal(message));

							return 1;
						})
					.executes(context -> {
						assert MinecraftClient.getInstance().player != null;
						MinecraftClient.getInstance().player.sendMessage(Text.literal("§c/chatzone send <message>§6 - Sends a command to regular chat (without disabling the command)"));

						return 1;
					}))
				).then(literal("get")
					.executes(context -> {
						assert MinecraftClient.getInstance().player != null;
						MinecraftClient.getInstance().player.sendMessage(Text.literal("§6Your current ChatZone is /" + command));

						return 1;
					})
				).executes(context -> {
					assert MinecraftClient.getInstance().player != null;
					MinecraftClient.getInstance().player.sendMessage(Text.literal("§c/chatzone <command>§6 - Sets the command to run when sending a chat message (example: '/chatzone msg TheMasterKitty')"));
					MinecraftClient.getInstance().player.sendMessage(Text.literal("§c/chatzone off§6 - Clears the command to run"));
					MinecraftClient.getInstance().player.sendMessage(Text.literal("§c/chatzone on§6 - Sets the command to the last command used in /chatzone off"));
					MinecraftClient.getInstance().player.sendMessage(Text.literal("§c/chatzone send <message>§6 - Sends a command to regular chat (without disabling the command)"));

					return 1;
				})));
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, ignored) -> dispatcher.register(literal("cz")
				.then(argument("command", StringArgumentType.greedyString())
						.executes(context -> {
							command = StringArgumentType.getString(context, "command") + " ";
							assert MinecraftClient.getInstance().player != null;
							MinecraftClient.getInstance().player.sendMessage(Text.literal("§aCommand successfully set to " + command));
							try (FileWriter writer = new FileWriter(data)) {
								writer.write(command);
							} catch (IOException e) {
								e.printStackTrace();
							}

							return 1;
						})
				).then(literal("off")
						.executes(context -> {
							offsave = command;
							command = "";
							assert MinecraftClient.getInstance().player != null;
							MinecraftClient.getInstance().player.sendMessage(Text.literal("§aCommand successfully cleared"));
							try (FileWriter writer = new FileWriter(data)) {
								writer.write(command);
							} catch (IOException e) {
								e.printStackTrace();
							}

							return 1;
						})
				).then(literal("on")
						.executes(context -> {
							assert MinecraftClient.getInstance().player != null;
							if (!Objects.equals(offsave, "")) {
								command = offsave;
								offsave = "";
								MinecraftClient.getInstance().player.sendMessage(Text.literal("§aCommand successfully set to " + command));
								try (FileWriter writer = new FileWriter(data)) {
									writer.write(command);
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
							else MinecraftClient.getInstance().player.sendMessage(Text.literal("§cYou don't have a previous save of /chatzone off."));

							return 1;
						})
				).then(literal("send")
						.then(argument("message", StringArgumentType.greedyString())
								.executes(context -> {
									String message = StringArgumentType.getString(context, "message");
									regularChat = true;
									assert MinecraftClient.getInstance().player != null;
									MinecraftClient.getInstance().player.sendChatMessage(message, Text.literal(message));

									return 1;
								}))
						.executes(context -> {
							assert MinecraftClient.getInstance().player != null;
							MinecraftClient.getInstance().player.sendMessage(Text.literal("§c/chatzone send <message>§6 - Sends a command to regular chat (without disabling the command)"));

							return 1;
						})
				).then(literal("get")
					.executes(context -> {
						assert MinecraftClient.getInstance().player != null;
						MinecraftClient.getInstance().player.sendMessage(Text.literal("§6Your current ChatZone is /" + command));

						return 1;
				})).executes(context -> {
					assert MinecraftClient.getInstance().player != null;
					MinecraftClient.getInstance().player.sendMessage(Text.literal("§c/chatzone <command>§6 - Sets the command to run when sending a chat message (example: '/chatzone msg TheMasterKitty')"));
					MinecraftClient.getInstance().player.sendMessage(Text.literal("§c/chatzone off§6 - Clears the command to run"));
					MinecraftClient.getInstance().player.sendMessage(Text.literal("§c/chatzone on§6 - Sets the command to the last command used in /chatzone off"));
					MinecraftClient.getInstance().player.sendMessage(Text.literal("§c/chatzone send <message>§6 - Sends a command to regular chat (without disabling the command)"));

					return 1;
				})));
	}
}