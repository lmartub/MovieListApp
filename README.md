# MovieListApp
Test app to view movie information

## Description
The application shows a list of movies and when the end of the scroll is reached, it is requested more information and the list is expanded.

<img src="https://github.com/lmartub/MovieListApp/blob/master/Screenshots/MainView_Portrait.png" data-canonical-src="https://github.com/lmartub/MovieListApp/blob/master/Screenshots/MainView_Portrait.png" height="300" />

When the user selects a movie the app expands its information on another screen. It shows user's evaluation, release date and a complete description.

<img src="https://github.com/lmartub/MovieListApp/blob/master/Screenshots/Detail_View.png" data-canonical-src="https://github.com/lmartub/MovieListApp/blob/master/Screenshots/Detail_View.png" height="300" />

## Tools
For the data source of the movies I used the API of TheMoviesDB as required.
For API requests I used the Retrofit library as required.
To show the list of movies I used the RecyclerView component because it allows me to have infinity scroll.

### Improvements
* Translation. Translate the app and shows information according to user language, even with in API movie. Now the application is getting information only in spanish.
* Improve pagination for the infinity scroll, keep the data needed to show the list.
* Add option to mark and filter by favorites's movies.
