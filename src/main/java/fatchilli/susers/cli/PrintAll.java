package fatchilli.susers.cli;

import fatchilli.susers.susers.SuserDao;

public class PrintAll implements Command {

    private final SuserDao suserDao;

    public PrintAll(SuserDao suserDao) {
        this.suserDao = suserDao;
    }

    @Override
    public boolean doesAcceptLine(String line) {
        return line.startsWith("PrintAll");
    }

    @Override
    public String processLine(String line) {
        //nothing needed, no args
        return suserDao.getSusers().toString();
    }
}
