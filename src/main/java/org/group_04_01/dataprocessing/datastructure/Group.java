package org.group_04_01.dataprocessing.datastructure;

import java.util.List;

public interface Group {

    String getName();

    boolean hasRight(Types.RIGHT right);

    List<Types.RIGHT> getRights();
}
