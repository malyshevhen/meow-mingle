package ua.mevhen.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import ua.mevhen.dto.PostRequest
import ua.mevhen.dto.PostResponse
import ua.mevhen.dto.UserInfo
import ua.mevhen.dto.UserRegistration
import ua.mevhen.service.subscription.SubscriptionService

class FeedServiceIntegrationSpec extends AbstractIntegrationSpec {

    @Autowired
    FeedService feedService

    @Autowired
    UserService userService

    @Autowired
    PostService postService

    @Autowired
    SubscriptionService subscriptionService

    def users = new ArrayList<UserInfo>()
    def posts = new ArrayList<PostResponse>()

    def setup() {
        users = (1..2).collect {
            return userService.save(
                new UserRegistration(
                    username: "User$it",
                    email: "email$it@mail.com," +
                        " Passw@rd$it"))
        }

        (1..5).each {
            users.each { userInfo ->
                def postRequest = new PostRequest(content: "test content: $it from ${ userInfo.username }")
                posts.add(postService.save(userInfo.username, postRequest))
            }
        }
    }

    def "test retrieving feed"() {
        given:
        def pageable = PageRequest.of(0, 10)

        when:
        userService.subscribe(users[0].username, users[1].id)
        def feed = feedService.getFeed(users[0].username, pageable)

        then:
        feed.totalElements == 5
        feed.totalPages == 1
    }

}
