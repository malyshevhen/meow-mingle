## Twitter-like Social Media API
### Technology Stack
- Code written in Groovy
- Spring Boot framework
- MongoDB database
- Reddis as queue for asynchronous operations
- Gradle build system
- Testing with Spock
- Docker for containerization
### Project Description
This project aims to create a Twitter-like API where users can perform various actions including user registration, login, logout, posting tweets, viewing their timeline, viewing other users' timelines, commenting on tweets, liking/unliking tweets, and following/unfollowing other users.

### API Endpoints
- Create User
- Edit User
- Delete User
- Create Tweet
- Edit Tweet
- Delete Tweet
- Like/Unlike a Tweet
- Follow another User (Following a user will show their tweets in your timeline)
- Unfollow another User
- Comment on a Tweet
- Get User's Timeline (including likes and comments)
- Get Other User's Timeline
- Get Comments for a Tweet
##### You can test all endpoints with [OpenAPI-UI](http://localhost:8080/swagger-ui/index.html)

### How to Use
- User Registration: Users can create an account with the system.
- User Management: Users can edit their profile information and delete their account.
- Tweeting: Users can create, edit, and delete their tweets.
- Interactions: Users can like/unlike tweets, follow/unfollow other users, and comment on tweets.
- Timeline: Users can view their own timeline (tweets from users they follow) and the timeline of other users.
- Comments: Users can view comments on tweets.
### Testing
All functionality in this API is thoroughly tested using Spock tests. You can run the tests to verify the correctness of the implementation.

### Deployment
You just need to download `docker-compose-full-image.yml` an run:
```sh
	docker compose up
```
Image is hosted in Github regidtry.
