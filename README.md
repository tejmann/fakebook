# fakebook
fakebook is a copy of the Facebook android app which I made to showcase my android skills and improve on my back-end skills. The app has a Wall and Profile tab same as the Facebook app. </br>
Share updates and photos, engage with friends, and stay connected.
Current features include:
* Connect with friends and family and meet new people on your social media network
* Set status updates to help relay what’s going on in your world
* Share photos of your favorite memories.


![login](https://github.com/tejmann/fakebook/blob/master/fb_signin_new.gif)![wall](https://github.com/tejmann/fakebook/blob/master/fb_wall_new.gif)
![search](https://github.com/tejmann/fakebook/blob/master/fb_search_new.gif)

In this app you’ll find : 
- The [Model-View-ViewModel (MVVM) Pattern](https://medium.com/upday-devs/android-architecture-patterns-part-3-model-view-viewmodel-e7eeee76b73b).
- Kotlin [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) for background operations.
- [Koin](https://insert-koin.io/) used for Dependency Injection.
- A single activity architecture.
- Reactive UIs using [LiveData](https://developer.android.com/topic/libraries/architecture/livedata).
- A custom **RecyclerView Adapter** to work with any viewholder. 
- **Firebase** to store users and their data.
- **Cloud Functions** for Firebase is used for backend trigger operations. 

## How to get it running and gameplay:
Clone the GitHub repository and build the project using Android Studio. This project is for Android API 21+. 
Once the app is installed, signup using your email and follow your friends. 
You can view the latest posts of the users you follow on your Wall. 
All the posts you post can be viewed in your profile tab.

## Features to come:
The following are in the works or currently planned:
- Ability to like, comment and share a post.
- Setting up FCM push notifications. 
- Chat messaging.

