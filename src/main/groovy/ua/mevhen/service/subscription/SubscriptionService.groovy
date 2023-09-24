package ua.mevhen.service.subscription

import ua.mevhen.domain.dto.Subscription

interface SubscriptionService {

    void manageSubscriptions(Subscription subscription)

}