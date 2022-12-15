import org.bson.Document;

import java.io.Serializable;
import java.lang.invoke.StringConcatFactory;

public class User {
    private String user_id;
    private String user_name;
    private String subject;
    private String text_1;
    private String text_2;
    private String text_3;
    private String text_4;
    private String text_5;
    private String text_6;
    private String text_7;
    private String text_8;
    private String text_9;
    private String text_10;
    private String passwd;
    private String nickname;
    private String count;
    private String target;
    private String backc;
    private String fontc;
    private String bordc;
    private String linec;

    public User(String user_id, String user_name, String subject,
                String text_1, String text_2, String text_3, String text_4, String text_5,
                String text_6, String text_7, String text_8, String text_9, String text_10) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.subject = subject;
        this.text_1 = text_1;
        this.text_2 = text_2;
        this.text_3 = text_3;
        this.text_4 = text_4;
        this.text_5 = text_5;
        this.text_6 = text_6;
        this.text_7 = text_7;
        this.text_8 = text_8;
        this.text_9 = text_9;
        this.text_10 = text_10;
    }

    public User(String[] data) {
        this.subject = data[0];
        this.user_name = data[1];
        this.user_id = data[2];
        this.text_1 = data[3];
        this.text_2 = data[4];
        this.text_3 = data[5];
        this.text_4 = data[6];
        this.text_5 = data[7];
        this.text_6 = data[8];
        this.text_7 = data[9];
        this.text_8 = data[10];
        this.text_9 = data[11];
        this.text_10 = data[12];
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id='" + user_id + '\'' +
                ", user_name='" + user_name + '\'' +
                ", subject='" + subject + '\'' +
                ", text_1='" + text_1 + '\'' +
                ", text_2='" + text_2 + '\'' +
                ", text_3='" + text_3 + '\'' +
                ", text_4='" + text_4 + '\'' +
                ", text_5='" + text_5 + '\'' +
                ", text_6='" + text_6 + '\'' +
                ", text_7='" + text_7 + '\'' +
                ", text_8='" + text_8 + '\'' +
                ", text_9='" + text_9 + '\'' +
                ", text_10='" + text_10 + '\'' +
                ", passwd='" + passwd + '\'' +
                ", nickname='" + nickname + '\'' +
                ", count='" + count + '\'' +
                ", target='" + target + '\'' +
                ", backc='" + backc + '\'' +
                ", fontc='" + fontc + '\'' +
                ", bordc='" + bordc + '\'' +
                ", linec='" + linec + '\'' +
                '}';
    }
}
