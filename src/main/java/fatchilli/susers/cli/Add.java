package fatchilli.susers.cli;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fatchilli.susers.susers.Suser;
import fatchilli.susers.susers.SuserAlreadyExistsException;
import fatchilli.susers.susers.SuserDao;

public class Add implements Command {

    public static final String COMMAND_NAME = "Add";

    public static final Pattern ADD_PATTERN = Pattern.compile("Add \\((\\d+),\\s*\"([^\"]*)\",\\s*\"([^\"]*)\"\\)");
    private final SuserDao suserDao;

    public Add(SuserDao suserDao) {
        this.suserDao = suserDao;
    }

    @Override
    public boolean doesAcceptLine(String line) {
        return line.startsWith(COMMAND_NAME);
    }

    @Override
    public String processLine(String line) {
        final Matcher matcher = ADD_PATTERN.matcher(line);
        if (!matcher.matches()) {
            throw new CommandParseException(COMMAND_NAME, "Add (1, \"a1\", \"Robert\")");
        }

        final String firstArg = matcher.group(1);
        final String secondArg = matcher.group(2);
        final String thirdArg = matcher.group(3);
        try {
            suserDao.add(Suser.of(Long.parseLong(firstArg), secondArg, thirdArg));
        } catch (SuserAlreadyExistsException e) {
            return e.getMessage();
        }
        return "User was added";
    }
}
