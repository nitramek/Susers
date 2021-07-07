package fatchilli.susers.cli;

import fatchilli.susers.susers.SuserDaoImpl;

public class PrintAll implements Command {

    private final SuserDaoImpl suserDao;

    public PrintAll(SuserDaoImpl suserDao) {
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
