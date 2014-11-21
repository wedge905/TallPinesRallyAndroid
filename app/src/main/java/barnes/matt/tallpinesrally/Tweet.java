package barnes.matt.tallpinesrally;


public class Tweet {

    private String text;
    private String created_at;
    private String id;
    private Tweet retweeted_status;

    private String datecreated;
    private String InReplyToStatusId;
    private String InReplyToUserId;
    private String InReplyToScreenName;
    private TwitterUser User;

    public String getDateCreated() {
        return created_at;
    }
    public String getId() {
        return id;
    }
    public String getInReplyToScreenName() {
        return InReplyToScreenName;
    }
    public String getInReplyToStatusId() {
        return InReplyToStatusId;
    }
    public String getInReplyToUserId() {
        return InReplyToUserId;
    }
    public String getText() {
        if (retweeted_status != null) {
            text = retweeted_status.getText();
            retweeted_status = null;
        }

        return text;
    }
    public void setDateCreated(String dateCreated) {
        created_at = dateCreated;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setInReplyToScreenName(String inReplyToScreenName) {
        InReplyToScreenName = inReplyToScreenName;
    }

    public void setInReplyToStatusId(String inReplyToStatusId) {
        InReplyToStatusId = inReplyToStatusId;
    }

    public void setInReplyToUserId(String inReplyToUserId) {
        InReplyToUserId = inReplyToUserId;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setUser(TwitterUser user) {
        User = user;
    }

    public TwitterUser getUser() {
        return User;
    }

    @Override
    public String toString(){
        return getText();
    }
}