# Android Task
We are going to be playing with satellite data. https://tle.ivanstanojevic.me/#/docs

## Goal
Complete and polish the provided project to showcase your everyday development habits: architecture choices, debugging skills, UI craft, tests, and versioning.

## What you must do

1. **Bugfix**
   - This API is 100% public and requests can take an unknown amount of time to return. Can you make the client prepared for that so that it doesn't throw a `SocketTimeoutException`?
   - Somehow one of the lines doesn't appear to be rendering. Can you check what is happening and fix it?
   - It seems that the app is rendering all the satellites, even those that are not currently appearing on screen thus using more resources than required. Can you check what is happening and fix it?

2. **Dependency Injection**  
   Add a DI setup (Hilt) and inject all core objects that are currently constructed manually (API service, repository, view-model, etc.).

3. **Package Structure**  
   Re-organize the code into layers that follow clean-architecture principles.

4. **Details Screen & Navigation**  
   Implement a new screen that shows full details for a selected launch item and wire navigation to reach it from the list. (Use the `/record/{id}` endpoint)

5. **Filters and Sorting**  
   It seems that the `collection` endpoint supports some filtering and sorting options. Can we leverage that and wire it to the UI?

6. **UI Enhancements**  
   Improve the existing bare-bones UI: compose a clearer layout, handle loading / error / empty states, and make the list pleasant to use.

7. **Unit Tests (100 % on one class)**  
   Choose **one** of the following classes — repository, view-model, or API service — and reach full line & branch coverage.

8. **Version Control**  
   Commit early and often. We will look at your commit history to understand how you work. Use clear, atomic messages.

9. **Make it offline first keeping support for filtering and sorting (Bonus)**

## Tech expectations

- Kotlin, Coroutines, Retrofit, Gson, Room, Hilt, androidx.navigation
- Jetpack Compose for UI
- JUnit + MockK tests
- Follow modern Android best practices

## Submission

1. All points will be evaluated on a best effort basis so feel free to bring whatever you get to accomplish
2. Get ready to present the end solution in the next meeting

That’s it — thanks and have fun!
