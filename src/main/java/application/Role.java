package application;

public enum Role {
    CREATOR("creator"),
    MODERATOR("moderator"),
    ADMINISTRATOR("administrator");

    private String value;

    Role(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
