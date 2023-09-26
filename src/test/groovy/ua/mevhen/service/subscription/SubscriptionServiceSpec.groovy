package ua.mevhen.service.subscription

import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import ua.mevhen.domain.events.Subscription
import ua.mevhen.service.AbstractIntegrationSpec
import ua.mevhen.service.UserService

import static ua.mevhen.domain.events.SubscriptionOperation.SUBSCRIBE
import static ua.mevhen.domain.events.SubscriptionOperation.UNSUBSCRIBE

class SubscriptionServiceSpec extends AbstractIntegrationSpec {

    @Autowired
    SubscriptionService subscriptionService

    @SpringBean
    UserService userService = Mock(UserService)

    def "test manageSubscriptions with SUBSCRIBE operation"() {
        given:
        def subscription = new Subscription(username: 'John', subId: '123', operation: SUBSCRIBE)

        when:
        subscriptionService.manageSubscriptions(subscription)

        then:
        1 * userService.subscribe('John', '123')
        0 * userService.unsubscribe(_, _)
    }

    def "test manageSubscriptions with UNSUBSCRIBE operation"() {
        given:
        def subscription = new Subscription(username: 'Jane', subId: '456', operation: UNSUBSCRIBE)

        when:
        subscriptionService.manageSubscriptions(subscription)

        then:
        0 * userService.subscribe(_, _)
        1 * userService.unsubscribe('Jane', '456')
    }

}
