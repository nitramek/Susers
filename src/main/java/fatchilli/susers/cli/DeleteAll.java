package fatchilli.susers.cli;

import fatchilli.susers.susers.SuserDaoImpl;

public class DeleteAll implements Command {

    public static final String COMMAND_NAME = "DeleteAll";
    private final SuserDaoImpl suserDao;

    public DeleteAll(SuserDaoImpl suserDao) {
        this.suserDao = suserDao;
    }

    @Override
    public boolean doesAcceptLine(String line) {
        return line.startsWith(COMMAND_NAME);
    }

    @Override
    public String processLine(String line) {
        suserDao.deleteSusers();
        return "All users were deleted";
    }
}
