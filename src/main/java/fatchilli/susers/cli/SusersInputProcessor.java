package fatchilli.susers.cli;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import fatchilli.susers.AppLifecycleListener;
import fatchilli.susers.SystemException;

public class SusersInputProcessor implements AppLifecycleListener {
    private final List<Command> commands;

    private final BufferedReader reader;
    private final BufferedWriter writer;

    public SusersInputProcessor(InputStream inputStream, OutputStream outputStream, List<Command> commands) {
        this.reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        this.writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
        this.commands = commands;
    }

    private void processLine(String line) throws IOException {
        final Optional<Command> foundCommand = commands.stream()
                .filter(command -> command.doesAcceptLine(line))
                .findFirst();
        try {
            foundCommand.ifPresentOrElse(command -> writeLine(command.processLine(line)),
                    () -> writeLine("No command was found for current input"));
        } catch (CommandParseException e) {
            writeLine(e.getMessage());
        }
    }

    private void writeLine(String msg) {
        try {
            writer.write(msg + "\n");
        } catch (IOException e) {
            throw new SystemException("Error writing", e);
        }
    }

    @Override
    public void onStart() {
        try {
            do {
                String line = reader.readLine();
                if (line == null) {
                    return;
                }
                processLine(line);
                writer.flush();
            } while (true);
        } catch (IOException e) {
            throw new SystemException("Error reading input", e);
        }
    }

    @Override
    public void onFinish() {

    }
}
