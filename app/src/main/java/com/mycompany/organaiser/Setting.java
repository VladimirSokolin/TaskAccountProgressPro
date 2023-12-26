package com.mycompany.organaiser;

public class Setting {


    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String VALUE = "value";
    public static final String ID = "id";
    public static final String NAME_TABLE = "settings";
    public static final String QUERY_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + NAME_TABLE + " ("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+ TITLE + " TEXT," + DESCRIPTION + " TEXT," + VALUE + " INTEGER);";
    public static final String QUERY_DELETE_TABLE = "DROP TABLE IF EXISTS " + NAME_TABLE + ";";

    long id;
    String title;
    String description;
    int value;
    public Setting(){
    }
    public Setting(String title, int value) {
        this.title = title;
        this.value = value;
    }
    public Setting(String title, String description, int value) {
        this.title = title;
        this.description = description;
        this.value = value;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static String insertIfNotExist(Setting setting){
        return "INSERT OR IGNORE INTO " + NAME_TABLE + "(" + TITLE + ", " + DESCRIPTION + ", " + VALUE + ") VALUES ('"+ setting.title +"', '" + setting.description +"', "+ setting.value +")";
    }
}
