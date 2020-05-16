

package com.thirtydegreesray.openhub.http;

import android.support.annotation.NonNull;

import com.thirtydegreesray.openhub.mvp.model.Project;

import java.util.ArrayList;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Tratcher on 2020/05/16
 */

public interface ProjectService {

    @Headers("Accept: application/vnd.github.inertia-preview+json")
    @NonNull @GET("repos/{owner}/{repo}/projects")
    Observable<Response<ArrayList<Project>>> getRepoProjects(
            @Header("forceNetWork") boolean forceNetWork,
            @Path("owner") String owner,
            @Path("repo") String repo,
            @Query("page") int page
    );
}
