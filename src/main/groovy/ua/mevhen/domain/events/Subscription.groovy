package ua.mevhen.domain.events

record Subscription(String username, String subId, SubscriptionOperation operation) { }