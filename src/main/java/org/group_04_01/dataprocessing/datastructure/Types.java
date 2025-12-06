package org.group_04_01.dataprocessing.datastructure;

public class Types {
    public Types() {
    }

    public static enum GESCHLECHT {
        MAENNLICH,
        WEIBLICH;

        private GESCHLECHT() {
        }
    }

    public static enum MANDAT {
        DIREKTWAHL,
        LANDESLISTE;

        private MANDAT() {
        }
    }

    public static enum RIGHT {
        LOGOUT,
        CHANGE_PASSWORD,
        CREATE_REPRESENTATIVE,
        DELETE_REPRESENTATIVE,
        EDIT_REPRESENTATIVE,
        ADD_USER,
        DELETE_USER,
        EDIT_USER,
        ADD_GROUP,
        DELETE_GROUP,
        EDIT_GROUP,
        CREATE_SPEECH,
        DELETE_SPEECH,
        EDIT_SPEECH,
        CREATE_SESSION,
        DELETE_SESSION,
        EDIT_SESSION,
        NLP_PROCESSING;

        private RIGHT() {
        }
    }
}
