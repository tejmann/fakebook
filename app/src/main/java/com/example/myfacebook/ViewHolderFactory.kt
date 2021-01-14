package com.example.myfacebook

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class ViewHolderFactory<T : Post> {
    abstract fun <V : BaseViewHolder<T>> getViewHolder(
        type: PostType,
        parent: ViewGroup,
        clickListener: ClickListener
    ): V

    fun getView(parent: ViewGroup, layoutRes: Int): View =
        LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)

}

class PostViewHolderFactory : ViewHolderFactory<Post>() {
    override fun <V : BaseViewHolder<Post>> getViewHolder(
        type: PostType,
        parent: ViewGroup,
        clickListener: ClickListener
    ): V =
        when (type) {
            PostType.STATUS -> StatusViewHolder(
                getView(parent, R.layout.status_view_holder),
                clickListener
            )
            PostType.IMAGE -> ImageViewHolder(
                getView(parent, R.layout.image_view_holder),
                clickListener
            )
            PostType.CREATE -> CreatePostViewHolder(
                getView(parent, R.layout.create_post),
                clickListener
            )
            PostType.USERS -> UsersViewHolder(
                getView(parent, R.layout.users_view_holder),
                clickListener
            )
            PostType.PROFILE -> ProfileViewHolder(
                getView(parent, R.layout.profile_view_holder),
                clickListener
            )
            PostType.PROGRESS -> ProgressViewHolder(
                getView(parent, R.layout.loading_view_holder),
                clickListener
            )
            PostType.EMPTY -> EmptyViewHolder(
                getView(parent, R.layout.empty_view_holder),
                clickListener
            )
            else -> CreatePostViewHolder(getView(parent, R.layout.create_post), clickListener)
        } as V
}