package EveEvents;

public class EventCreator {
    String creatorNickname;
    String creatorId;

    public EventCreator(String creatorNickname, String creatorId) {
        this.creatorNickname = creatorNickname;
        this.creatorId = creatorId;
    }

    public String getCreatorNickname() {
        return creatorNickname;
    }

    public String getCreatorId() {
        return creatorId;
    }
}
