package ua.mevhen.repository

import org.bson.types.ObjectId
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository
import ua.mevhen.domain.model.Comment

interface CommentRepository extends MongoRepository<Comment, ObjectId> {

    @Query(value = "{post.id: ?0}")
    Page<Comment> findByPostId(ObjectId postId, Pageable pageable)

}
