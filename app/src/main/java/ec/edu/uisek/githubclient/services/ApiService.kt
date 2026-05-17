package ec.edu.uisek.githubclient.services

import ec.edu.uisek.githubclient.models.Repository
import ec.edu.uisek.githubclient.models.RepositoryPayload
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.Response
import retrofit2.http.Query

interface ApiService {
    @GET(value = "user/repos")
    suspend fun getRespository (
        @Query(value = "sort") sort: String = "created",
        @Query(value = "direction") direction: String = "desc",
        @Query(value = "affiliation") affiliation: String = "owner",
        @Query(value = "t") t: String = "${System.currentTimeMillis()}"
    ): List <Repository>

    @POST (value = "user/repos")
    suspend fun createRepository(
        @Body payload: RepositoryPayload
    ): Repository

    @PATCH("repos/{owner}/{repo}")
    suspend fun updateRepository(
        @Path("owner") owner: String,
        @Path("repo") repoName: String,
        @Body payload: RepositoryPayload
    ): Repository

    @DELETE("repos/{owner}/{repo}")
    suspend fun deleteRepository(
        @Path("owner") owner: String,
        @Path("repo") repoName: String
    ): Response<Void>
}