package monitora.trainingandroid.domain.entity;

import android.graphics.Color;

import com.google.gson.annotations.SerializedName;

/**
 * Entity Status of GitHub API
 *
 * @see <a href="https://status.github.com/api/last-message.json"> Last Status</a>
 * Created by estevao on 09/01/17.
 */

public class Status {
    @SerializedName("status")
    private Type type;
    private String body;
    @SerializedName("created_on")
    private String createdOn;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }


    public enum Type {
        @SerializedName("good")
        GOOD(Color.GREEN, "Good"),

        @SerializedName("minor")
        MINOR(Color.RED, "Minor"),

        @SerializedName("major")
        MAJOR(Color.YELLOW, "Major"),
        NONE(Color.WHITE, "…");

        private final int color;
        private String name;

        Type(int color, String name) {
            this.color = color;
            this.name = name;
        }

        public int getColor() {
            return color;
        }

        public String getName() {
            return name;
        }
    }
}
