package com.example.githubsample.ui.home

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.example.base_lib.GlideApp
import com.example.base_lib.ui.BaseFragment
import com.example.githubsample.R
import com.example.githubsample.adapter.GithubRepoLoadStateAdapter
import com.example.githubsample.adapter.GithubReposAdapter
import com.example.githubsample.databinding.FragmentHomeBinding
import com.example.githubsample.ext.showToast
import com.example.githubsample.ui.home.HomeViewModel.Companion.DEFAULT_REPO
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::class.java) {
    private val viewModel: HomeViewModel by viewModel()

    companion object {
        private const val FIRST_OPEN = "FIRST_OPEN"
    }

    private var storage: SharedPreferences? = null

    private val adapter by lazy {
        GithubReposAdapter(GlideApp.with(this), onItemClick = { repo ->
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(repo.htmlUrl))
            startActivity(browserIntent)
        })
    }

    override fun initView() {
        super.initView()
        initAdapter()
        initSwipeToRefresh()
    }

    override fun setupFirst() {
        super.setupFirst()
        checkFirstOpen()
    }

    private fun initAdapter() {
        viewBinding.apply {
            rvRepos.adapter = adapter.withLoadStateHeaderAndFooter(
                header = GithubRepoLoadStateAdapter(adapter),
                footer = GithubRepoLoadStateAdapter(adapter)
            )

            lifecycleScope.launchWhenCreated {
                viewModel.repos.collectLatest {
                    adapter.submitData(it)
                }
            }

            lifecycleScope.launchWhenCreated {
                adapter.loadStateFlow.collect { loadStates ->
                    swipeRefresh.isRefreshing = loadStates.mediator?.refresh is LoadState.Loading

                    val errorState = loadStates.source.append as? LoadState.Error
                        ?: loadStates.append as? LoadState.Error
                    errorState?.let {
                        showToast(it.error.message.toString())
                    }
                }
            }

            lifecycleScope.launchWhenCreated {
                adapter.loadStateFlow
                    .distinctUntilChangedBy { it.refresh }
                    .filter { it.refresh is LoadState.NotLoading }
                    .collectLatest { rvRepos.scrollToPosition(0) }
            }

            lifecycleScope.launchWhenCreated {
                viewModel.profile.observe(viewLifecycleOwner) {
                    it?.let { profile ->
                        viewBinding.apply {
                            tvName.text = profile.name
                            tvBlog.text = profile.blog
                            tvEmail.text = profile.email
                            GlideApp.with(requireActivity()).load(profile.avatarUrl).into(ivAvatar)
                            ivtNumOfRepos.ivIcon.setImageResource(R.drawable.ic_fork)
                            ivtNumOfRepos.tvText.text = profile.publicRepos.toString()
                        }
                    }
                }

                viewModel.totalPage.observe(viewLifecycleOwner) {
                    adapter.setTotalPage(it)
                }
            }
        }
    }

    private fun initSwipeToRefresh() {
        viewBinding.swipeRefresh.setOnRefreshListener { adapter.refresh() }
    }

    private fun checkFirstOpen() {
        storage = requireActivity().getSharedPreferences(FIRST_OPEN, MODE_PRIVATE)
        storage?.apply {
            val isFirstOpen = getBoolean(FIRST_OPEN, true)
            if (isFirstOpen) {
                viewModel.fetchProfile(DEFAULT_REPO)
                edit().putBoolean(FIRST_OPEN, true).apply()
            }
        }
    }
}