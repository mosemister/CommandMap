package org.commandmap.detail.argument;

import org.spongepowered.api.command.CommandCompletion;
import org.spongepowered.api.command.parameter.CommandContext;

import java.util.*;

public interface ServerBoundArgument extends DetailArgument {

	Collection<CommandCompletion> getSuggestions(CommandContext context, String currentInput);

	Optional<Comparator<CommandCompletion>> getSorting();

	default List<CommandCompletion> getSuggestionsSorted(CommandContext context, String currentInput) {
		Optional<Comparator<CommandCompletion>> opSorting = this.getSorting();
		Collection<CommandCompletion> unsortedResults = getSuggestions(context, currentInput);
		if (opSorting.isPresent()) {
			List<CommandCompletion> linked = new LinkedList<>(unsortedResults);
			linked.sort(opSorting.get());
			return linked;
		}
		if (unsortedResults instanceof List) {
			return (List<CommandCompletion>) unsortedResults;
		}
		List<CommandCompletion> results = new LinkedList<>(unsortedResults);
		results.sort(Comparator.comparing(CommandCompletion::completion));
		return results;
	}
}
