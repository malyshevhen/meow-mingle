package ua.mevhen.service.subscription

import groovy.util.logging.Slf4j
import org.springframework.stereotype.Service
import ua.mevhen.domain.events.Subscription
import ua.mevhen.service.UserService

import static ua.mevhen.domain.events.SubscriptionOperation.SUBSCRIBE
import static ua.mevhen.domain.events.SubscriptionOperation.UNSUBSCRIBE

@Service
@Slf4j
class SubscriptionServiceImpl implements SubscriptionService {

    private final UserService userService

    SubscriptionServiceImpl(UserService userService) {
        this.userService = userService
    }

    @Override
    void manageSubscriptions(Subscription subscription) {
        switch (subscription.operation()) {
            case SUBSCRIBE -> {
                log.info("Subscribing user '${ subscription.username }' to '${ subscription.subId }...'")
                userService.subscribe(subscription.username, subscription.subId)
                log.info("Subscribe user '${ subscription.username }' to '${ subscription.subId }'.")
            }
            case UNSUBSCRIBE -> {
                log.info("Unsubscribing user '${ subscription.username }' from '${ subscription.subId }'...")
                userService.unsubscribe(subscription.username, subscription.subId)
                log.info("Unsubscribe user '${ subscription.username }' from '${ subscription.subId }'.")
            }
        }
    }

}
