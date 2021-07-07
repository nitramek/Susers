package fatchilli.susers.susers;

import java.io.Serializable;

import lombok.NonNull;
import lombok.Value;

@Value(staticConstructor = "of")
public class Suser implements Serializable {

    long userId;

    @NonNull
    String guid;
    @NonNull
    String username;

}
