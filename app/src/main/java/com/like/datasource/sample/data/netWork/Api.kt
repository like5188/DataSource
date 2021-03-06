package com.like.datasource.sample.data.netWork

import com.like.datasource.sample.data.model.*
import retrofit2.http.GET
import retrofit2.http.Path

interface Api {

    @GET("/banner/json")
    suspend fun getBanner(): ResultModel<List<BannerInfo.BannerEntity>?>

    @GET("/article/top/json")
    suspend fun getTopArticle(): ResultModel<List<TopArticleEntity>?>

    @GET("/article/list/{page}/json")
    suspend fun getArticle(@Path("page") page: Int): ResultModel<PagingModel<ArticleEntity>?>
}