package EveEventManager;

public class GamePlayer {
    private String creatorNickname;
    private String creatorName;
    private String creatorId;
    private String creatorGuildId;

    public GamePlayer(String creatorNickname, String creatorName, String creatorId, String creatorGuildId) {
        this.creatorNickname = creatorNickname;
        this.creatorName = creatorName;
        this.creatorId = creatorId;
        this.creatorGuildId = creatorGuildId;
    }

    public String getCreatorNickname() { return this.creatorNickname; }
    public String getCreatorName() { return this.creatorName; }
    public String getCreatorId() { return this.creatorId; }
    public String getCreatorGuildId() { return  this.creatorGuildId; }
}
