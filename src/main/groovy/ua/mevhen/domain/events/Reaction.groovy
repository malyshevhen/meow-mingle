package ua.mevhen.domain.events

record Reaction(String username, String postId, ReactionOperation operation) { }
