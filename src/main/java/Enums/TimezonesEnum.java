package Enums;

public enum TimezonesEnum {
    EST("est"),
    C("c");

    String timezoneCode;
    TimezonesEnum(String timezoneCode) {
        this.timezoneCode = timezoneCode;
    }

    public String getProperName() {
        switch (timezoneCode) {
            case "est":
                return "Eastern";
            case "c":
                return "Central";
            default:
                return "";
        }
    }

    public static boolean exists(String timezoneName) {
        for(TimezonesEnum timezone : TimezonesEnum.values()) {
            if(timezoneName.equals(timezone.timezoneCode)) {
                return true;
            }
        }
        return false;
    }
}
