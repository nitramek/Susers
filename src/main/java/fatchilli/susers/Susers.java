package fatchilli.susers;

import java.util.List;

import fatchilli.susers.cli.Add;
import fatchilli.susers.cli.DeleteAll;
import fatchilli.susers.cli.PrintAll;
import fatchilli.susers.cli.SusersInputProcessor;
import fatchilli.susers.susers.SuserDaoImpl;

public class Susers {

    public static void main(String[] args) {
        final DatabaseSetup databaseSetup = new DatabaseSetup();

        final SuserDaoImpl dao = new SuserDaoImpl(databaseSetup);
        final List<AppLifecycleListener> databaseSetups = List.of(databaseSetup,
                new SusersInputProcessor(System.in, System.out,
                        List.of(new Add(dao), new PrintAll(dao), new DeleteAll(dao))));

        for (AppLifecycleListener setup : databaseSetups) {
            setup.onStart();
        }

        for (AppLifecycleListener setup : databaseSetups) {
            setup.onFinish();
        }
    }
}
