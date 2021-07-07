package fatchilli.susers.cli;

import lombok.Getter;

public class CommandParseException extends RuntimeException {

    @Getter
    private final String commandName;

    CommandParseException(String commandName, String correctExample) {
        super(String.format("Command %s does not match the input, correct example is %s", commandName,
                correctExample));
        this.commandName = commandName;
    }
}
