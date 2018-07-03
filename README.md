## Navigation

### Installation

[Read this article](https://developer.android.com/topic/libraries/architecture/navigation/navigation-implementing) about how to implement Navigation inside Android Application

***Please Note*** that you need [the Beta build of Android Studio](https://developer.android.com/studio/preview/) which supports Navigation Editor to use this project.


### How it works

Navigation Architecture Component is a tool created by Google to make it easier for developers to work on Android projects, it works with the following rules:
- Only one Activity lives in the Application, all the other screens are `Fragments`
- Main Activity includes the navigation component, every fragment and actions (parameters) are configured in the res/navigation/nav_graph.xml file (open with Navigation Editor on Android Studio v3.3).
- xml elements are available for navigation actions:
  - `action`: Navigate to a new Fragment, contains an `id` and a `destination`
  - `argument`: Argument to pass to a new Fragment, containts a `name` and a `defaultValue`

***Please*** refer to the [documentation](https://developer.android.com/topic/libraries/architecture/navigation/navigation-implementing).

You may also want to check [this video](https://www.youtube.com/watch?v=GOpeBbfyb6s) for insights on how to create new fragments, link fragments together (navigate between each other), everything that we need to know is explained in this video.

Test Fragment is only here for navigation demonstration, it will be deleted very soon.