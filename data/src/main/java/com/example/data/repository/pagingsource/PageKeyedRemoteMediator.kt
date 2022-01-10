package com.example.data.repository.pagingsource

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.data.api.wrapApiCall
import com.example.data.source.GithubRepoEndpoint
import com.example.data.source.local.GithubRepoDao
import com.example.data.source.local.GithubRepoDatabase
import com.example.data.source.local.RemoteKeyDao
import com.example.domain.ResponseHandler
import com.example.domain.entity.GithubRepo
import com.example.domain.entity.RemoteKeys
import com.example.domain.exception.AppException
import com.example.domain.usecase.GetGithubReposParam
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

const val DEFAULT_PAGE_INDEX = 1

@OptIn(ExperimentalPagingApi::class)
class PageKeyedRemoteMediator(
    private val db: GithubRepoDatabase,
    private val githubRepoEndpoint: GithubRepoEndpoint,
    private val param: GetGithubReposParam,
) : RemoteMediator<Int, GithubRepo>() {
    private val githubRepoDao: GithubRepoDao = db.getGithubRepoDao()
    private val remoteKeyDao: RemoteKeyDao = db.getRemoteKeysDao()
    override suspend fun initialize(): InitializeAction {
        return if (githubRepoDao.getRepoSize() == 0) {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        } else {
            InitializeAction.SKIP_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, GithubRepo>
    ): MediatorResult {
        return try {

            val page = when (val pageKeyData = getKeyPageData(loadType, state)) {
                is MediatorResult.Success -> {
                    return pageKeyData
                }
                else -> {
                    pageKeyData as Int
                }
            }
//            Log.d("DUYTS", "Before calling api $page")
            val response = wrapApiCall {
                githubRepoEndpoint.fetchRepos(
                    page = page,
                    pageSize = state.config.pageSize,
                    repoName = param.repoName
                )
            }
//            Log.d("DUYTS", "API request success ${response}")
            when (response) {
                is ResponseHandler.Success -> {
                    val items: MutableList<GithubRepo> = response.data.toMutableList()
                    val isEndOfList = items.isEmpty()
                    items.map { it.atPage = page }
                    val prevKey = if (page == DEFAULT_PAGE_INDEX) null else page - 1
                    val nextKey = if (isEndOfList) null else page + 1
                    val keys = items.map { RemoteKeys(it.id, prevKey, nextKey) }
                    db.withTransaction {
                        if (loadType == LoadType.REFRESH) {
                            githubRepoDao.clearRepos()
                            remoteKeyDao.clearRemoteKeys()
                        }
                        githubRepoDao.insertAll(items)
                        remoteKeyDao.insert(keys)

//                        Log.d("DUYTS", "Saved data to db ${items}")
                        Timber.tag("DUYTS")
                            .d("Page: $page - Size: ${items.size} - loadType: $loadType - item in db: ${githubRepoDao.getRepoSize()}  - nextKey: $nextKey")
                    }
                    MediatorResult.Success(endOfPaginationReached = isEndOfList)
                }
                is ResponseHandler.Failure -> {
//                    Log.d("DUYTS", "Failed in response: $response")
                    MediatorResult.Error(response.error)
                }
                else -> MediatorResult.Error(AppException.Unknown)
            }

        } catch (ex: IOException) {
//            Log.d("DUYTS", "Failed in catch: $ex")
            return MediatorResult.Error(ex)
        } catch (ex: HttpException) {
//            Log.d("DUYTS", "Failed in catch: $ex")
            return MediatorResult.Error(ex)
        }
    }

    private suspend fun getKeyPageData(
        loadType: LoadType,
        state: PagingState<Int, GithubRepo>
    ): Any {
        return when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: DEFAULT_PAGE_INDEX
            }
            LoadType.APPEND -> {
                val remoteKeys = getLastRemoteKey(state)
                val nextKey = remoteKeys?.nextKey
                return nextKey ?: MediatorResult.Success(endOfPaginationReached = false)
            }
            LoadType.PREPEND -> {
                val remoteKeys = getFirstRemoteKey(state)
                val prevKey = remoteKeys?.prevKey ?: return MediatorResult.Success(
                    endOfPaginationReached = false
                )
                prevKey
            }
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, GithubRepo>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                remoteKeyDao.remoteKeysRepoID(repoId)
            }
        }
    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, GithubRepo>): RemoteKeys? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { repo -> remoteKeyDao.remoteKeysRepoID(repo.id) }
    }

    private suspend fun getFirstRemoteKey(state: PagingState<Int, GithubRepo>): RemoteKeys? {
        return state.pages
            .firstOrNull { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { repo -> remoteKeyDao.remoteKeysRepoID(repo.id) }
    }
}