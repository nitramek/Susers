package fatchilli.susers.susers;

import java.util.List;

public interface SuserDao {
    void add(Suser suser) throws SuserAlreadyExistsException;

    void deleteSusers();

    List<Suser> getSusers();
}
