package changhyeon.mybudgetcommunity.enums;

public enum IncomeCategory {
    SALARY("급여"),
    BONUS("상여금"),
    INTEREST("이자수입"),
    BUSINESS("사업수입"),
    RENTAL("임대수입"),
    OTHER("기타");

    private final String description;

    IncomeCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
