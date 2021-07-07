package fatchilli.susers.cli;

public interface Command {

    boolean doesAcceptLine(String line);

    String processLine(String line);
}
