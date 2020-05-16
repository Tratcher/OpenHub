

package com.thirtydegreesray.openhub.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Tratcher on 2020/05/16
 */

public class Project implements Parcelable {

    private String url;
    @SerializedName("html_url") private String htmlUrl;
    @SerializedName("owner_url") private String ownerUrl;
    @SerializedName("columns_url") private String columnsUrl;
    private String name;
    private String body;
    private int id;
    private String state;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public String getOwnerUrl() {
        return ownerUrl;
    }

    public void setOwnerUrl(String ownerUrl) {
        this.ownerUrl = ownerUrl;
    }

    public String getColumnsUrl() {
        return columnsUrl;
    }

    public void setColumnsUrl(String columnsUrl) {
        this.columnsUrl = columnsUrl;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return body;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeString(this.htmlUrl);
        dest.writeString(this.ownerUrl);
        dest.writeString(this.columnsUrl);
        dest.writeString(this.name);
        dest.writeString(this.body);
        dest.writeInt(this.id);
        dest.writeString(this.state);
    }

    public Project() {
    }

    protected Project(Parcel in) {
        this.url = in.readString();
        this.htmlUrl = in.readString();
        this.ownerUrl = in.readString();
        this.columnsUrl = in.readString();
        this.name = in.readString();
        this.body = in.readString();
        this.id = in.readInt();
        this.state = in.readString();
    }

    public static final Creator<Project> CREATOR = new Creator<Project>() {
        @Override
        public Project createFromParcel(Parcel in) {
            return new Project(in);
        }

        @Override
        public Project[] newArray(int size) {
            return new Project[size];
        }
    };

}
