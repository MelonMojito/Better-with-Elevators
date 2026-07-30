package betterwithelevators.command;

import betterwithelevators.config.ElevatorConfig;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

/**
 * A block name argument, suggesting every registered block's namespace id.
 */
public class ArgumentTypeBlock implements ArgumentType<String> {

	public static ArgumentTypeBlock block() {
		return new ArgumentTypeBlock();
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
		String remaining = builder.getRemainingLowerCase();
		for (String name : ElevatorConfig.blockNames()) {
			if (name.toLowerCase().contains(remaining)) {
				builder.suggest(name);
			}
		}
		return builder.buildFuture();
	}

	@Override
	public String parse(StringReader reader) throws CommandSyntaxException {
		//read to whitespace by hand: brigadier's unquoted strings reject the ':' and '/' in a namespace id
		int start = reader.getCursor();
		while (reader.canRead() && reader.peek() != ' ') {
			reader.skip();
		}
		return reader.getString().substring(start, reader.getCursor());
	}

	@Override
	public Collection<String> getExamples() {
		return Arrays.asList("minecraft:block/block_steel", "block.steel");
	}

	@Override
	public String toString() {
		return "block()";
	}
}
