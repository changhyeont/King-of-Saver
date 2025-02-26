package changhyeon.mybudgetcommunity.enums;

public enum ExpenseCategory {
    TRANSPORT("교통비"),
    UTILITIES("공과금"),
    PERSONAL("품위유지"),
    HOBBY("취미"),
    FOOD("식비"),
    DINING("식비(외식)"),
    EDUCATION("학업"),
    DATE("데이트"),
    OTHER("기타");

    private final String displayName;

    ExpenseCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
