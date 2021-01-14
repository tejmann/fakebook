package com.example.myfacebook

import com.google.firebase.Timestamp
import org.koin.android.ext.koin.ERROR_MSG

fun HashMap<*, *>.toPost(): Post? {
    val postType = this["otype"] as? String
    return when (postType) {
        "IMAGE" -> {
            val imageData = this["data"] as? HashMap<*, *>
            val url = imageData?.get("url") as? String
            val userDp = imageData?.get("dpPath") as? String
            val userName = imageData?.get("name") as? String
            val seconds = ((this["time"] as? HashMap<*, *>)?.get("_seconds") as? Int)?.toLong()
            val nanoseconds = (this["time"] as? HashMap<*, *>)?.get("_nanoseconds") as? Int
            val timestamp = seconds?.let { nanoseconds?.let { it1 -> Timestamp(it, it1) } }
            url?.let {
                userDp?.let { it1 ->
                    userName?.let { it2 ->
                        timestamp?.let { it3 ->
                            ImageData(
                                it, it2, it1,
                                it3
                            )
                        }
                    }
                }
            }
                ?.let { Image(it) }
        }
        "STATUS" -> {
            val statusData = this["data"] as? HashMap<*, *>
            val message = statusData?.get("message") as? String
            val userDp = statusData?.get("dpPath") as? String
            val userName = statusData?.get("name") as? String
            val seconds = ((this["time"] as? HashMap<*, *>)?.get("_seconds") as? Int)?.toLong()
            val nanoseconds = (this["time"] as? HashMap<*, *>)?.get("_nanoseconds") as? Int
            val timestamp = seconds?.let { nanoseconds?.let { it1 -> Timestamp(it, it1) } }
            message?.let {
                userName?.let { it1 ->
                    userDp?.let { it2 ->
                        timestamp?.let { it3 ->
                            StatusData(
                                it,
                                it1,
                                it2,
                                it3
                            )
                        }
                    }
                }
            }?.let { Status(it) }
        }
        "CREATE" -> {
            val createData = this["data"] as? HashMap<*, *>
            val dpPath = createData?.get("dpPath") as? String
            dpPath?.let { CreateData(it) }?.let { Create(it) }
        }
        "USER" -> {
            val userData = this["data"] as? HashMap<*, *>
            val userDp = userData?.get("dpPath") as? String
            val userName = userData?.get("name") as? String
            val uid = this["id"] as? String
            userDp?.let { userName?.let { it1 -> uid?.let { it2 -> UserData(it1, it, it2) } } }
                ?.let { Users(it) }
        }
        "PROFILE" -> {
            val userData = this["data"] as? HashMap<*, *>
            val userDp = userData?.get("dpPath") as? String
            val userName = userData?.get("name") as? String
            val uid = this["id"] as? String
            val following = (userData?.get("following") as? Boolean)

            userDp?.let {
                userName?.let { it1 ->
                    uid?.let { it2 ->
                        following?.let { it3 ->
                            ProfileData(
                                it1, it, it2,
                                it3
                            )
                        }
                    }
                }
            }
                ?.let { Profile(it) }
        }
        "EMPTY" -> {
            val type = this["etype"] as? String
            val eType = when (type) {
                "WALL" -> EmptyDataType.WALL
                "PROFILE" -> EmptyDataType.PROFILE
                "USERS" -> EmptyDataType.USERS
                else -> null
            }
            eType?.let { EmptyData(it) }?.let { Empty(it) }
        }
        else -> {
            null
        }
    }
}
