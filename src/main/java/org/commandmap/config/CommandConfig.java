package org.commandmap.config;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.commandmap.CommandMappingPlugin;
import org.commandmap.detail.AliasCommandDetail;
import org.commandmap.detail.CommandDetail;
import org.commandmap.detail.DetailType;
import org.commandmap.detail.MappedCommandDetail;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.serialize.SerializationException;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CommandConfig {

	private final @NotNull File file;
	private final @NotNull HoconConfigurationLoader loader;
	private final @NotNull CommentedConfigurationNode root;

	public CommandConfig(@NotNull File file) {
		this.file = file;
		this.loader = HoconConfigurationLoader.builder().file(file).build();
		CommentedConfigurationNode node;
		try {
			node = this.loader.load();
		} catch (ConfigurateException e) {
			e.printStackTrace();
			node = this.loader.createNode();
		}
		this.root = node;
	}

	public void save() throws IOException {
		if (!this.file.exists()) {
			this.file.getParentFile().mkdirs();
			this.file.createNewFile();
		}
		this.loader.save(this.root);
	}

	public void addDetail(@NotNull AliasCommandDetail detail) throws SerializationException {
		CommentedConfigurationNode node = root.node("commands");
		int amount = 0;
		while (true) {
			if (node.node(detail.getCommand() + "-" + amount).isNull()) {
				break;
			}
			amount++;
		}
		String keyName = detail.getCommand();
		if (amount != 0) {
			keyName = keyName + "-" + amount;
		}
		node = node.node(keyName);
		node.node("type").set(DetailType.ALIAS.name());
		node.node("command").set(detail.getCommand());
		node.node("plugin").set(detail.getOwningPlugin());
		node.node("alias").set(Arrays.asList(detail.getAliases()));
	}

	public Collection<CommandDetail> buildDetails() {
		return root.node("commands").childrenMap().values().parallelStream().map(commandNode -> {
					String type = commandNode.node("type").getString();
					if (type == null) {
						CommandMappingPlugin.getPlugin()
								.getLogger()
								.warn("Could not find type of command in " + commandNode.key());
						return null;
					}
					if (type.equalsIgnoreCase(DetailType.ALIAS.name())) {
						return buildAliasDetailsFrom(commandNode);
					}
					if (type.equalsIgnoreCase(DetailType.MAP.name())) {
						return buildMappedDetailsFrom(commandNode);
					}
					CommandMappingPlugin.getPlugin().getLogger().warn(
							"Invalid type of command in " + commandNode.key() + " (type of '" + type + "' is "
									+ "invalid)");
					return null;
				}).filter(Objects::nonNull)
				.collect(Collectors.toList());
	}

	private MappedCommandDetail buildMappedDetailsFrom(ConfigurationNode node) {
		throw new RuntimeException("Mapped not ready");
	}

	private AliasCommandDetail buildAliasDetailsFrom(ConfigurationNode node) {
		String command = node.node("command").getString();
		if (command == null) {
			CommandMappingPlugin.getPlugin().getLogger().warn("failed to read 'command' from '" + node.key() + "'");
			return null;
		}
		String plugin = node.node("plugin").getString();
		if (plugin == null) {
			CommandMappingPlugin.getPlugin().getLogger().warn("failed to read 'plugin' from '" + node.key() + "'");
			return null;
		}

		String[] array;
		try {
			@Nullable List<String> list = node.node("alias").getList(String.class);
			if (list == null) {
				CommandMappingPlugin.getPlugin().getLogger().warn("failed to read 'alias' from '" + node.key() + "'");
				return null;
			}
			array = list.toArray(new String[0]);
		} catch (SerializationException e) {
			e.printStackTrace();
			return null;
		}
		return new AliasCommandDetail(plugin, command, array);
	}
}
