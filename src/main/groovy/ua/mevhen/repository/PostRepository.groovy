package ua.mevhen.repository

import org.bson.types.ObjectId
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import ua.mevhen.domain.model.Post

interface PostRepository extends MongoRepository<Post, ObjectId> {

    Page<Post> findAllByAuthorIdIn(Collection<ObjectId> id, Pageable pageable)

}