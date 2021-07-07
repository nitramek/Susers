package fatchilli.susers.cli;

import lombok.Getter;

public class CommandParseException extends RuntimeException {

    @Getter
    private final String commandName;

    CommandParseException(String input, String commandName, String correctExample) {
        super(String.format("Command %s does not match the input: '%s', correct example is: %s", input, commandName,
                correctExample));
        this.commandName = commandName;
    }
}
