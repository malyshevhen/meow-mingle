package ua.mevhen.repository

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import ua.mevhen.domain.model.Comment

interface CommentRepository extends MongoRepository<Comment, ObjectId> { }
