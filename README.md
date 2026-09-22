# uwa-social-android-assessment
A simple social feed implementation built as part of the UWA Social Android technical assessment.

## App Demo

[▶ Watch Screen Recording](https://github.com/user-attachments/assets/0509eb7e-8578-4659-ac43-ae06188444a0
)

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

Likes use optimistic UI updates to provide immediate feedback.
If the server operation fails, the local state is reverted.

## Testing

Unit tests cover:
- Successful post retrieval
- API failure handling
- Like functionality

## Technical Decisions

I prioritized a simple, maintainable architecture that can
scale to additional social features without introducing
unnecessary complexity.
