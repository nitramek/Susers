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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import fatchilli.susers.AppLifecycleListener;
import fatchilli.susers.SystemException;

public class SusersInputProcessor implements AppLifecycleListener {
    private final List<Command> commands;

    private final BufferedReader reader;
    private final BufferedWriter writer;
    private final ExecutorService executor;

    public SusersInputProcessor(InputStream inputStream, OutputStream outputStream, List<Command> commands) {
        //could be argued all of these should be given via constructor,
        this.reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        this.writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
        this.executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        this.commands = commands;

    }

    private void processLine(String line) {
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
        //could use CompletetableFuture to write only in single dedicated thread but this works just fine
        synchronized (this) {
            try {
                writer.write(msg + "\n");
                writer.flush();
            } catch (IOException e) {
                throw new SystemException("Error writing", e);
            }
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
                executor.submit(() -> processLine(line));
            } while (true);
        } catch (IOException e) {
            throw new SystemException("Error reading input", e);
        }
    }

    @Override
    public void onFinish() {
        try {
            executor.shutdown();
            executor.awaitTermination(200, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            throw new SystemException("Error shutting down executor", e);
        }
    }
}
