package ua.mevhen.service.subscription

import ua.mevhen.domain.events.Subscription

interface SubscriptionService {

    void manageSubscriptions(Subscription subscription)

}