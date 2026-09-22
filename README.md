# uwa-social-android-assessment
A simple social feed implementation built as part of the UWA Social Android technical assessment.

## App Demo

[▶ Watch Screen Recording](https://github.com/user-attachments/assets/0509eb7e-8578-4659-ac43-ae06188444a0
)

## APK
Download the APK here: https://drive.google.com/file/d/1zRNoSAU67MrqmDSfu9lNSP4nDgBj8Z5T/view?usp=sharing

## Tech Stack

- Kotlin
- Jetpack Compose
- MVVM
- Hilt
- Retrofit
- Paging 3
- Room
- Coil
- Coroutines / Flow
- JUnit

## Architecture

The application follows MVVM with a Repository pattern.

UI
↓
ViewModel
↓
Repository
↓
Remote / Local data sources

## Pagination

Paging 3 is used to load posts incrementally rather than
loading the entire feed at once.

## Offline Support

Posts retrieved from the API are persisted locally using Room.
When network connectivity is unavailable, cached posts can still
be displayed.

## Image Caching

Coil is used for profile and post media loading with memory
and disk caching enabled.

## Error Handling

The UI handles:
- Loading
- Empty
- Error
- Offline
- Pagination errors

## Like Interaction

Liking a post needs to feel instant and survive Paging's internal diffing, so FeedViewModel keeps an in-memory map of pending like overrides that's combined with the paged stream (combine(pagedPosts, likeOverrides)) and applied on top of whatever page data comes through. The same toggle is persisted to Room in the background via PostRepository.toggleLike so it survives process death and offline sessions.

## Testing

Unit tests cover:
- PostRepositoryTest — verifies the network/cache fallback behaviour
- FeedViewModelTest — verifies the like-toggle overrides the paged data and calls through to the repository (asSnapshot() from paging-testing is used to materialize PagingData in tests, since PagingData.map is lazy)

## Technical Decisions

I prioritized a simple, maintainable architecture that can
scale to additional social features without introducing
unnecessary complexity.

## Running the appp
From Android Studio:
Clone the repo: git clone git@github.com:RotimiDev/uwa-social-android-assessment.git

1. File → Open, select the cloned uwa-social-android-assessment folder
2. Let Gradle sync (first sync pulls dependencies, may take a minute)
3. Pick an emulator or a connected physical device from the device dropdown
4. Click Run (▶) — this installs and launches the app

## Running the unit test
From the command line:
./gradlew testDebugUnitTest
