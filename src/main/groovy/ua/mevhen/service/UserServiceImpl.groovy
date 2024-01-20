package ua.mevhen.service

import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ua.mevhen.domain.model.Role
import ua.mevhen.domain.model.User
import ua.mevhen.exceptions.UserAlreadyExistsException
import ua.mevhen.exceptions.UserNotFoundException
import ua.mevhen.repository.UserRepository

@Service
class UserServiceImpl implements UserService {

    final UserRepository userRepository

    UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository
    }

    @Override
    @Transactional
    User save(User user) {
        if (ifExists(user.username)) throw new UserAlreadyExistsException("Username: '${user.username}' is taken.")
        user.role = Role.USER.value
        return userRepository.save(user)
    }

    @Override
    User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow { new UserNotFoundException("User: '$username' was not found.") }
    }

    @Override
    @Transactional
    User updateUsername(def id, String username) {
        if (id instanceof String) {
            id = new ObjectId(id)
        }
        def user = findById(id)
        user.username = username
        def updatedUser = userRepository.save(user)
        return updatedUser
    }

    @Override
    @Transactional
    void deleteById(def id) {
        if (id instanceof String) {
            id = new ObjectId(id)
        }
        def user = findById(id)
        userRepository.delete(user)
    }

    @Override
    @Transactional
    void subscribe(String username, def subId) {
        if (subId instanceof String) {
            subId = new ObjectId(subId)
        }
        updateSubscriptions(username, subId) { user, sub -> user.subscribe(sub) }
    }

    @Override
    @Transactional
    void unsubscribe(String username, def subId) {
        if (subId instanceof String) {
            subId = new ObjectId(subId)
        }
        updateSubscriptions(username, subId) { user, sub -> user.unsubscribe(sub) }
    }

    @Override
    boolean ifExists(String username) {
        return userRepository.existsByUsername(username)
    }

    private User findById(ObjectId id) {
        return userRepository.findById(id)
                .orElseThrow { new UserNotFoundException("User with ID: ${id.toString()} was not found.") }
    }

    private void updateSubscriptions(
            String username,
            ObjectId subId,
            Closure<User> subscriptionAction
    ) {
        def user = findByUsername(username)
        def sub = findById(subId)
        subscriptionAction(user, sub)

        [user, sub].each { userRepository.save(it) }
    }

}
