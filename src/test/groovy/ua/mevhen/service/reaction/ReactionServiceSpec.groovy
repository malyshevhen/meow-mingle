package ua.mevhen.service.reaction

import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import ua.mevhen.domain.events.Reaction
import ua.mevhen.service.AbstractIntegrationSpec
import ua.mevhen.service.PostService

import static ua.mevhen.domain.events.ReactionOperation.LIKE
import static ua.mevhen.domain.events.ReactionOperation.UNLIKE

class ReactionServiceSpec extends AbstractIntegrationSpec {

    @Autowired
    ReactionService reactionService

    @SpringBean
    PostService postService = Mock(PostService)

    def "test manageReaction with LIKE operation"() {
        given:
        def reaction = new Reaction(username: 'John', postId: '12345', operation: LIKE)

        when:
        reactionService.manageReaction(reaction)

        then:
        1 * postService.addLike('John', '12345')
        0 * postService.removeLike(_, _)
    }

    def "test manageReaction with UNLIKE operation"() {
        given:
        def reaction = new Reaction(username: 'John', postId: '12345', operation: UNLIKE)

        when:
        reactionService.manageReaction(reaction)

        then:
        1 * postService.removeLike('John', '12345')
        0 * postService.addLike(_, _)
    }

}
