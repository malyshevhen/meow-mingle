package ua.mevhen.domain.events

record Comment(String username, String postId, String content, CommentOperation operation) {

}