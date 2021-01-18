#### 最新版本

模块|DataSource
---|---
最新版本|[![Download](https://jitpack.io/v/like5188/DataSource.svg)](https://jitpack.io/#like5188/DataSource)

## 功能介绍
1、该项目是基于 kotlin + coroutines + androidx 开发的数据源。

2、封装了数据操作、数据获取、多数据源合并等功能。

3、包括2种数据源：分页（包含或者不包含数据库）、不分页（包含或者不包含数据库）。

4、返回的数据：
```java
    data class Result<ResultType>(
        // 结果报告
        val resultReportFlow: Flow<ResultReport<ResultType>>,
        // 初始化操作
        val initial: () -> Unit,
        // 刷新操作
        val refresh: () -> Unit,
        // 失败重试操作
        val retry: () -> Unit,
        // 往后加载更多，不分页时不用设置
        val loadAfter: (() -> Unit)? = null,
        // 往前加载更多，不分页时不用设置
        val loadBefore: (() -> Unit)? = null
    )
```

## 使用方法：

1、引用

在Project的gradle中加入：
```groovy
    allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }
```
在Module的gradle中加入：
```groovy
    dependencies {
        // coroutines
        implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-core:版本号'
        implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:版本号'

        implementation 'com.github.like5188:DataSource:版本号'
    }
```

2、使用
```java
    1、不分页，创建数据源继承自

    [com.like.datasource.notPaging.NotPagingDataSource]、[com.like.datasource.notPaging.NotPagingDbDataSource]

    ，然后通过 result() 方法获取 [com.like.datasource.Result]。

    2、分页，创建数据源继承自

    [com.like.datasource.paging.byData.DataKeyedPagingDataSource]、[com.like.datasource.paging.byData.DataKeyedPagingDbDataSource]

    或者 [com.like.datasource.paging.byPageNo.PageNoKeyedPagingDataSource]、[com.like.datasource.paging.byPageNo.PageNoKeyedPagingDbDataSource]

    ，然后通过 result() 方法获取 [com.like.datasource.Result]。

    3、如果有多个数据源，可以使用 [com.like.datasource.util.MultiDataSourceHelper] 中的方法进行数据源的组合。
```