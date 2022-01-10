package com.example.githubsample.ui.home

import androidx.lifecycle.*
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import com.example.base_lib.config.RequestConfig
import com.example.base_lib.ui.BaseViewModel
import com.example.domain.usecase.*
import com.example.githubsample.model.GithubRepoListItem
import com.example.githubsample.model.UpdateProfile
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.math.max

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModel(
    private val getGithubRepos: GetGithubRepos,
    private val savedStateHandle: SavedStateHandle,
    private val getProfile: GetGithubProfile,
    private val fetchProfile: FetchGithubProfile,
    private val getTotalGithubRepoPages: GetTotalGithubRepoPages,
) : BaseViewModel() {

    companion object {
        const val KEY_REPO = "KEY_REPO"
        const val DEFAULT_REPO = "google"
    }

    init {
        if (!savedStateHandle.contains(KEY_REPO)) {
            savedStateHandle.set(KEY_REPO, DEFAULT_REPO)
        }
    }

    private val _updateProfile = MutableLiveData(UpdateProfile(false, DEFAULT_REPO))
    val profile = _updateProfile.switchMap { updateProfile ->
        if (updateProfile.isForceUpdate) {
            viewModelScope.launch {
                val param = FetchGithubProfileParam(repoName = updateProfile.name)
                fetchProfile.invoke(param)
            }
        }
        getProfile.invoke()
    }

    val totalPage = getTotalGithubRepoPages.invoke()

    val repos = savedStateHandle.getLiveData<String>(KEY_REPO)
        .asFlow()
        .flatMapLatest {
            val param = GetGithubReposParam(RequestConfig.DEFAULT_PAGE_SIZE, repoName = it)
            getGithubRepos.invoke(param)
        }
        .map { pagingData ->
            pagingData.map { repo -> GithubRepoListItem.Item(repo) }
                .insertSeparators { before: GithubRepoListItem?, after: GithubRepoListItem? ->
                    if (before == null && after == null) {
                        null
                    } else if (after == null) {
                        null
                    } else if (before == null) {
                        GithubRepoListItem.Separator(after.id, after.atPage)
                    } else if (before.atPage != after.atPage) {
                        GithubRepoListItem.Separator(after.id, after.atPage)
                    } else {
                        null
                    }
                }
//                .insertHeaderItem(TerminalSeparatorType.SOURCE_COMPLETE,GithubRepoListItem.Header(100,100))
        }
        .cachedIn(viewModelScope)

    fun fetchProfile(name: String) {
        _updateProfile.value = UpdateProfile(true, name)
    }

}