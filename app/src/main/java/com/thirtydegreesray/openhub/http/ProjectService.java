

package com.thirtydegreesray.openhub.http;

import android.support.annotation.NonNull;

import com.thirtydegreesray.openhub.mvp.model.Card;
import com.thirtydegreesray.openhub.mvp.model.Project;
import com.thirtydegreesray.openhub.mvp.model.ProjectColumn;

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
    @NonNull @GET("projects/{projectId}")
    Observable<Response<Project>> getProjectInfo(
            @Header("forceNetWork") boolean forceNetWork,
            @Path("projectId") int id
    );

    @Headers("Accept: application/vnd.github.inertia-preview+json")
    @NonNull @GET("projects/{projectId}/columns")
    Observable<Response<ArrayList<ProjectColumn>>> getProjectColumns(
            @Header("forceNetWork") boolean forceNetWork,
            @Path("projectId") int id
    );

    @Headers("Accept: application/vnd.github.inertia-preview+json")
    @NonNull @GET("projects/columns/{columnId}/cards")
    Observable<Response<ArrayList<Card>>> getColumnCards(
            @Header("forceNetWork") boolean forceNetWork,
            @Path("columnId") int columnId,
            @Query("archived_state") String archivedState
    );

    @Headers("Accept: application/vnd.github.inertia-preview+json")
    @NonNull @GET("repos/{owner}/{repo}/projects")
    Observable<Response<ArrayList<Project>>> getRepoProjects(
            @Header("forceNetWork") boolean forceNetWork,
            @Path("owner") String owner,
            @Path("repo") String repo,
            @Query("page") int page
    );
}
