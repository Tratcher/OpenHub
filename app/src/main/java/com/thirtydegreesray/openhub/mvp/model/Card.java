

package com.thirtydegreesray.openhub.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Tratcher on 2020/05/16
 */

public class Card implements Parcelable {

    private String url;
    private int id;
    private String note;
    private Boolean archived;
    @SerializedName("column_url") private String columnUrl;
    @SerializedName("content_url") private String contentUrl;
    @SerializedName("project_url") private String projectUrl;

    private Issue issue;

    public String getUrl() {
        return url;
    }

    public String getContentUrl() { return contentUrl; }

    public String getNote() {
        return note;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getId() { return id; }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    public Issue getIssue() { return issue; }

    public enum CardType {
        Unknown,
        Note,
        // ExternalIssue, // A note that's actually a url to an issue elsewhere.
        Issue,
        // PullRequest,
    }

    public CardType getCardType()
    {
        if (contentUrl == null && note == null)
        {
            return CardType.Unknown;
        }

        if (note != null)
        {
            return CardType.Note;
        }

        // "https://api.github.com/repos/{owner}/{repo}/issues/{issueNumber}"
        if (contentUrl.startsWith("https://api.github.com/") && contentUrl.contains("/issues/"))
        {
            return CardType.Issue;
        }

        return CardType.Unknown;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeInt(this.id);
        dest.writeString(this.note);
        dest.writeByte(this.archived ? (byte)1 : 0);
        dest.writeString(this.columnUrl);
        dest.writeString(this.contentUrl);
        dest.writeString(this.projectUrl);
    }

    public Card() {
    }

    protected Card(Parcel in) {
        this.url = in.readString();
        this.id = in.readInt();
        this.note = in.readString();
        this.archived = in.readByte() != 0;
        this.columnUrl = in.readString();
        this.contentUrl = in.readString();
        this.projectUrl = in.readString();
    }

    public static final Creator<Card> CREATOR = new Creator<Card>() {
        @Override
        public Card createFromParcel(Parcel in) {
            return new Card(in);
        }

        @Override
        public Card[] newArray(int size) {
            return new Card[size];
        }
    };
}
