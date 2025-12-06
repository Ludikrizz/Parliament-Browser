package org.group_04_01.dataprocessing.datastructure.imlementation;

import org.bson.Document;
import org.group_04_01.dataprocessing.datastructure.User;

public class User_Impl implements User {

    private String username;
    private String group;

    public User_Impl(Document document) {
        username = document.getString("_id");
        group = document.getString("group");
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getGroup() {
        return group;
    }
}
