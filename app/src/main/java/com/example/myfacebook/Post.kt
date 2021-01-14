package com.example.myfacebook

import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

abstract class Post(val otype: PostType) {
}

enum class PostType {
    STATUS,
    IMAGE,
    CREATE,
    USERS,
    PROFILE,
    PROGRESS,
    EMPTY
}

abstract class PostData(val owner: String? = Firebase.auth.uid)


data class StatusData(
    val message: String,
    val name: String,
    val dpPath: String,
    val time: Timestamp
) : PostData()

data class ImageData(
    val filePath: String,
    val name: String,
    val dpPath: String,
    val time: Timestamp
) : PostData()

data class CreateData(val filePath: String) : PostData()
data class ProfileData(
    val name: String,
    val filePath: String,
    val uid: String,
    val following: Boolean
) : PostData()

data class UserData(val name: String, val filePath: String, val uid: String) : PostData()

data class EmptyData(val type: EmptyDataType) : PostData()

enum class EmptyDataType {
    WALL,
    PROFILE,
    USERS
}

data class Status(val data: StatusData) : Post(PostType.STATUS)


data class Image(val data: ImageData) : Post(PostType.IMAGE)
data class Create(val data: CreateData) : Post(PostType.CREATE)
data class Users(val data: UserData) : Post(PostType.USERS)
data class Profile(val data: ProfileData) : Post(PostType.PROFILE)
class Progress : Post(PostType.PROGRESS)
data class Empty(val data: EmptyData) : Post(PostType.EMPTY)