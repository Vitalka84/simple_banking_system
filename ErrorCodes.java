package banking;

public enum ErrorCodes {
    e0001("e0001", "Not enough money!"),
    e0002("e0002", "You can't transfer money to the same account!"),
    e0003("e0003", "Probably you made a mistake in the card number. Please try again!"),
    e0004("e0004", "Such a card does not exist."),
    ;

    private String code;
    private String description;

    ErrorCodes(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return new StringBuffer("error{\"code\":\"")
                .append(code)
                .append("\",\"description\":\"")
                .append(description)
                .append("\"}")
                .toString();
    }
}
