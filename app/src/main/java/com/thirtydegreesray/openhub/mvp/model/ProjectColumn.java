

package com.thirtydegreesray.openhub.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Tratcher on 2020/05/16
 */

public class ProjectColumn implements Parcelable {

    private String url;
    @SerializedName("project_url") private String projectUrl;
    @SerializedName("cards_url") private String cardsUrl;
    private String name;
    private int id;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public int getId() { return id; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeString(this.projectUrl);
        dest.writeString(this.cardsUrl);
        dest.writeString(this.name);
        dest.writeInt(this.id);
    }

    public ProjectColumn() {
    }

    protected ProjectColumn(Parcel in) {
        this.url = in.readString();
        this.projectUrl = in.readString();
        this.cardsUrl = in.readString();
        this.name = in.readString();
        this.id = in.readInt();
    }

    public static final Creator<ProjectColumn> CREATOR = new Creator<ProjectColumn>() {
        @Override
        public ProjectColumn createFromParcel(Parcel in) {
            return new ProjectColumn(in);
        }

        @Override
        public ProjectColumn[] newArray(int size) {
            return new ProjectColumn[size];
        }
    };
}
