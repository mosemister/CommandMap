package org.commandmap;

import com.google.inject.Inject;
import org.apache.logging.log4j.Logger;
import org.commandmap.commands.add.AddMappingsCommand;
import org.commandmap.commands.aliasmap.AliasMappedCommand;
import org.commandmap.config.CommandConfig;
import org.commandmap.detail.AliasCommandDetail;
import org.commandmap.detail.CommandDetail;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.ConstructPluginEvent;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.builtin.jvm.Plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Plugin("command_config")
public class CommandMappingPlugin {

	private @NotNull CommandConfig commandConfig;
	private final @NotNull PluginContainer container;
	private final @NotNull Logger logger;

	private static @NotNull CommandMappingPlugin plugin;

	@Inject
	public CommandMappingPlugin(@NotNull PluginContainer container, @NotNull Logger logger) {
		plugin = this;
		this.container = container;
		this.logger = logger;
	}

	public @NotNull Logger getLogger() {
		return this.logger;
	}

	@Listener
	public void pluginStarting(ConstructPluginEvent event) {
		commandConfig = new CommandConfig(new File(this.getPluginFolder(), "config.conf"));
	}

	@Listener
	public void commandParameterRegister(RegisterCommandEvent<Command.Parameterized> event) {
		event.register(this.container, AddMappingsCommand.create(), "alias");
		Collection<CommandDetail> details = commandConfig.buildDetails();
		this.logger.info("Found '" + details.size() + "' mapped commands");
		details.forEach(commandDetail -> {
			if (commandDetail instanceof AliasCommandDetail) {
				AliasCommandDetail aliasDetail = (AliasCommandDetail) commandDetail;
				AliasMappedCommand command = new AliasMappedCommand(aliasDetail);
				List<String> alias = new ArrayList<>(Arrays.asList(aliasDetail.getAliases()));
				String original = alias.get(0);
				alias.remove(0);
				event.register(this.container, command.buildCommand(), original, alias.toArray(new String[0]));
			}
		});

	}

	@Listener
	public void commandRawRegister(RegisterCommandEvent<Command.Raw> event) {

	}

	private File getPluginFolder() {
		return Sponge.configManager().pluginConfig(this.container).directory().toFile();
	}

	public CommandConfig getConfig() {
		return this.commandConfig;
	}

	public static @NotNull CommandMappingPlugin getPlugin() {
		return plugin;
	}
}
