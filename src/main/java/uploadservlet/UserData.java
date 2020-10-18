package uploadservlet;

public class UserData {
    private static final long serialVersionUID = 205242440643911308L;
    private String UserName,Comment,FilePath;

    public String getUserName() {
        return UserName;
    }

    public String getComment() {
        return Comment;
    }

    public String getFilePath() {
        return FilePath;
    }

    public UserData(String userName, String comment, String filePath) {
        UserName = userName;
        Comment = comment;
        FilePath = filePath;
    }



}
