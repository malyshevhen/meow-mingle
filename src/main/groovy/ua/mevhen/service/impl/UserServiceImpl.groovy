package ua.mevhen.service.impl

import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ua.mevhen.domain.dto.UserInfo
import ua.mevhen.domain.dto.UserRegistration
import ua.mevhen.domain.model.Role
import ua.mevhen.domain.model.User
import ua.mevhen.exceptions.UserAlreadyExistsException
import ua.mevhen.exceptions.UserNotFoundException
import ua.mevhen.mapper.UserMapper
import ua.mevhen.repository.UserRepository
import ua.mevhen.service.UserService

@Service
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
            throw new UserAlreadyExistsException("Username: '$username' is taken.")
        }
        def userToSave = userMapper.toUser(regForm)
        userToSave.role = Role.USER.value
        def savedUser = userRepository.save(userToSave)
        return userMapper.toUserInfo(savedUser)
    }

    @Override
    User findByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow { -> new UserNotFoundException("User: '$username' was not found.") }
    }

    @Override
    @Transactional
    UserInfo updateUsername(String id, String username) {
        def user = findById(id)
        user.username = username
        def updatedUser = userRepository.save(user)
        return userMapper.toUserInfo(updatedUser)
    }

    @Override
    @Transactional
    void deleteById(String id) {
        def user = findById(id)
        userRepository.delete(user)
    }

    @Override
    @Transactional
    List<UserInfo> subscribe(String username, String subId) {
        return updateSubscriptions(username, subId) { user, sub -> user.subscribe(sub) }
    }

    @Override
    @Transactional
    List<UserInfo> unsubscribe(String username, String subId) {
        return updateSubscriptions(username, subId) { user, sub -> user.unsubscribe(sub) }
    }

    private List<UserInfo> updateSubscriptions(
        String username,
        String subId,
        Closure<User> subscriptionAction
    ) {
        def user = findByUsername(username)
        def sub = findById(subId)

        subscriptionAction(user, sub)

        def updatedUsers = [user, sub].collect {userRepository.save(it)}

        return updatedUsers.collect { userMapper.toUserInfo(it) }
    }

    private User findById(String id) {
        userRepository.findById(new ObjectId(id))
            .orElseThrow { -> new UserNotFoundException("User with id: $id was not found.") }
    }

    private boolean ifExists(String username) {
        userRepository.existsByUsername(username)
    }

}
