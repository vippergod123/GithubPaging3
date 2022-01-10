package com.example.githubsample.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.base_lib.GlideRequests
import com.example.domain.entity.GithubRepo
import com.example.githubsample.R
import com.example.githubsample.databinding.ItemRepoBinding
import com.example.githubsample.model.GithubRepoListItem

class GithubReposAdapter(
    private val glide: GlideRequests,
    private val onItemClick: ((repo: GithubRepo) -> Unit)? = null
) :
    PagingDataAdapter<GithubRepoListItem, GithubReposAdapter.GithubRepoViewHolder>(REPO_COMP) {
    inner class GithubRepoViewHolder(
        private val binding: ItemRepoBinding,
        private val glide: GlideRequests,
        private val onItemClick: ((repo: GithubRepo) -> Unit)? = null
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: GithubRepoListItem?) {
            binding.apply {
                containerRepo.isVisible = false
                containerSeparator.isVisible = false
                containerLoading.isVisible = false
                when (item) {
                    null -> {
                        containerLoading.isVisible = true
                    }
                    is GithubRepoListItem.Separator -> {
                        containerSeparator.isVisible = true
                        tvSeparator.text = item.atPage.toString()
                    }
                    is GithubRepoListItem.Item -> {
                        val repo = item.repo
                        containerRepo.isVisible = true
                        binding.root.setOnClickListener {
                            onItemClick?.invoke(repo)
                        }
                        tvName.text = "${repo.name}"
                        tvDescription.text = repo.description
                        ivtFolk.ivIcon.setImageResource(R.drawable.ic_fork)
                        ivtFolk.tvText.text = repo.forksCount?.toString()

                        ivtStar.ivIcon.setImageResource(R.drawable.ic_star)
                        ivtStar.tvText.text = repo.stargazersCount?.toString()

                        ivtWatchers.ivIcon.setImageResource(R.drawable.ic_eye)
                        ivtWatchers.tvText.text = repo.watchersCount?.toString()

//                        repo.owner?.let { owner ->
//                            glide.load(owner.avatarUrl)
//                                .placeholder(R.drawable.img_repo_placeholder).into(ivAvatar)
//                        } ?: glide.clear(ivAvatar)

                    }
                }
            }

        }
    }

    companion object {
        val REPO_COMP = object : DiffUtil.ItemCallback<GithubRepoListItem>() {
            override fun areContentsTheSame(
                oldItem: GithubRepoListItem,
                newItem: GithubRepoListItem
            ): Boolean =
                oldItem == newItem

            override fun areItemsTheSame(
                oldItem: GithubRepoListItem,
                newItem: GithubRepoListItem
            ): Boolean =
                oldItem.id == newItem.id
        }
    }

    override fun onBindViewHolder(holder: GithubRepoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GithubRepoViewHolder {
        return GithubRepoViewHolder(
            ItemRepoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            glide,
            onItemClick
        )
    }
}