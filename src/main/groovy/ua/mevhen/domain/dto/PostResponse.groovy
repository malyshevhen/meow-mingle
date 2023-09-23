package ua.mevhen.domain.dto

import java.time.LocalDate

record PostResponse(
    String id,
    String authorId,
    String content,
    Set<UserInfo> likes,
    Set<CommentResponse> comments,
    LocalDate created,
    LocalDate updated
) { }