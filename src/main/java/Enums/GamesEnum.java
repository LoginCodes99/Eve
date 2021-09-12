package Enums;

public enum GamesEnum {
    VALORANT("valorant"),
    GTAV("gtav");

    String gameName;
    GamesEnum(String gameName) {
        this.gameName = gameName;
    }

    public String getProperName() {
        switch (gameName) {
            case "valorant":
                return "Valorant";
            case "gtav":
                return "GTA V";
            default:
                return "";
        }
    }

    public static boolean exists(String gameName) {
        for(GamesEnum game : GamesEnum.values()) {
            if(gameName.equals(game.gameName)) {
                return true;
            }
        }
        return false;
    }

    public static GamesEnum fromString(String gameName) {
        switch (gameName) {
            case "valorant":
                return VALORANT;
            case "gtav":
                return GTAV;
            default:
                return null;
        }
    }
}
