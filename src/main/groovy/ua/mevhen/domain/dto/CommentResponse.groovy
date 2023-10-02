package ua.mevhen.domain.dto

import java.time.LocalDate

record CommentResponse(
    String id,
    String authorId,
    String content,
    LocalDate created,
    LocalDate updated
) { }