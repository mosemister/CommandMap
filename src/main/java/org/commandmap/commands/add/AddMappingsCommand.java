package org.commandmap.commands.add;

import net.kyori.adventure.text.Component;
import org.commandmap.CommandMappingPlugin;
import org.commandmap.config.CommandConfig;
import org.commandmap.detail.AliasCommandDetail;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.CommandCompletion;
import org.spongepowered.api.command.CommandExecutor;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.plugin.PluginContainer;

import java.io.IOException;
import java.util.Collection;

public class AddMappingsCommand {

	private static Parameter.Value<String> command;
	private static Parameter.Value<PluginContainer> plugin;
	private static Parameter.Value<String> alias;

	public static class AddAliasExecutor implements CommandExecutor {

		@Override
		public CommandResult execute(CommandContext context) {
			String command = context.requireOne(AddMappingsCommand.command);
			PluginContainer plugin = context.requireOne(AddMappingsCommand.plugin);
			Collection<? extends String> aliases = context.all(AddMappingsCommand.alias);
			AliasCommandDetail detail =
					new AliasCommandDetail(plugin.metadata().id(), command, aliases.toArray(new String[0]));
			CommandConfig config = CommandMappingPlugin.getPlugin().getConfig();
			try {
				config.addDetail(detail);
				config.save();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			return CommandResult.success();
		}
	}

	public static Command.Parameterized create() {
		plugin = Parameter.plugin().key("plugin").build();
		command = Parameter.string().key("command").completer((context, currentInput) -> Sponge.server()
				.commandManager()
				.knownMappings()
				.parallelStream()
				.map(map -> CommandCompletion.of(map.primaryAlias(),
						Component.text(map.plugin().map(plugin -> plugin.metadata().id() + ":").orElse("")
								+ map.primaryAlias())))
				.toList()).build();
		alias = Parameter.remainingJoinedStrings().key("alias").build();

		Command.Parameterized addAlias = Command
				.builder()
				.addParameter(plugin)
				.addParameter(command)
				.addParameter(alias)
				.executor(new AddAliasExecutor())
				.build();
		return addAlias;
	}

}
