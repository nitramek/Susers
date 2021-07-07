package fatchilli.susers.susers;

import lombok.Getter;

public class SuserAlreadyExistsException extends RuntimeException {

    @Getter
    private final long suserId;

    SuserAlreadyExistsException(long suserId) {
        super(String.format("Suser with id %d already exists", suserId));
        this.suserId = suserId;
    }
}
