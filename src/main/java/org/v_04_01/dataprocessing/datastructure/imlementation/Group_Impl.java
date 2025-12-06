package org.v_04_01.dataprocessing.datastructure.imlementation;

import org.bson.Document;
import org.v_04_01.dataprocessing.datastructure.Group;
import org.v_04_01.dataprocessing.datastructure.Types;

import java.util.LinkedList;
import java.util.List;

public class Group_Impl implements Group {

    private String name;
    private LinkedList<Types.RIGHT> rights;

    public Group_Impl(Document document) {
        name = document.getString("_id");
        rights = new LinkedList<>();
        List<String> rightsString = document.getList("rights", String.class);
        for (String right : rightsString) {
            rights.add(Types.RIGHT.valueOf(right));
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean hasRight(Types.RIGHT right) {
        return rights.contains(right);
    }

    @Override
    public List<Types.RIGHT> getRights() {
        return rights;
    }
}
