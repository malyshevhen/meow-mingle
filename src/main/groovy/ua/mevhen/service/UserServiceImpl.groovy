package ua.mevhen.service

import groovy.util.logging.Slf4j
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ua.mevhen.domain.model.Role
import ua.mevhen.domain.model.User
import ua.mevhen.dto.UserInfo
import ua.mevhen.dto.UserRegistration
import ua.mevhen.exceptions.UserAlreadyExistsException
import ua.mevhen.exceptions.UserNotFoundException
import ua.mevhen.mapper.UserMapper
import ua.mevhen.repository.UserRepository

@Service
@Slf4j
class UserServiceImpl implements UserService {

    private final UserRepository userRepository
    private final UserMapper userMapper

    UserServiceImpl(
        UserRepository userRepository,
        UserMapper userMapper
    ) {
        this.userRepository = userRepository
        this.userMapper = userMapper
    }

    @Override
    @Transactional
    UserInfo save(UserRegistration regForm) {
        def username = regForm.username
        if (ifExists(username)) {
            final message = "Username: '$username' is taken."
            log.error(message)
            throw new UserAlreadyExistsException(message)
        }
        def userToSave = userMapper.toUser(regForm)
        userToSave.role = Role.USER.value
        def savedUser = userRepository.save(userToSave)
        log.info("Saved user with ID: ${ savedUser.id }")
        return userMapper.toUserInfo(savedUser)
    }

    @Override
    User findByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow { ->
                final message = "User: '$username' was not found."
                log.error(message)
                new UserNotFoundException(message)
            }
    }

    @Override
    @Transactional
    UserInfo updateUsername(String usernameToUpdate, String username) {
        def user = findByUsername(usernameToUpdate)
        user.username = username
        def updatedUser = userRepository.save(user)
        log.info("Updated username for user with ID: ${ updatedUser.id } to: $username")
        return userMapper.toUserInfo(updatedUser)
    }

    @Override
    @Transactional
    void deleteById(String id) {
        def user = findById(id)
        userRepository.delete(user)
        log.info("Deleted user with ID: $id")
    }

    @Override
    @Transactional
    void deleteByUsername(String username) {
        def user = userRepository.findByUsername(username)
            .orElseThrow { new UserNotFoundException("User $username not found") }
        userRepository.delete(user)
        log.info("Deleted user: $username")
    }

    @Override
    @Transactional
    void subscribe(String username, String subId) {
        log.info("Process subscription of User: $username to user with ID: $subId")
        updateSubscriptions(username, subId) { user, sub -> user.subscribe(sub) }
        log.info("Subscription of User: $username to user with ID: $subId completed.")
    }

    @Override
    @Transactional
    void unsubscribe(String username, String subId) {
        log.info("Process unsubscription of User: $username from user with ID: $subId")
        updateSubscriptions(username, subId) { user, sub -> user.unsubscribe(sub) }
        log.info("Unsubscription of User: $username from user with ID: $subId completed.")
    }

    @Override
    boolean ifExists(String username) {
        userRepository.existsByUsername(username)
    }

    private void updateSubscriptions(
        String username,
        String subId,
        Closure<User> subscriptionAction
    ) {
        def user = findByUsername(username)
        def sub = findById(subId)

        subscriptionAction(user, sub)

        [user, sub].each { userRepository.save(it) }
    }

    private User findById(String id) {
        userRepository.findById(new ObjectId(id))
            .orElseThrow { ->
                final message = "User with ID: $id was not found."
                log.error(message)
                new UserNotFoundException(message)
            }
    }

}
