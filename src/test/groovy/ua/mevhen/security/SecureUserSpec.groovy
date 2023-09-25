package ua.mevhen.security

import org.bson.types.ObjectId
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import spock.lang.Specification
import ua.mevhen.domain.model.User

class SecureUserSpec extends Specification {

    def "test SecureUser implementation"() {
        given:
        def user = new User(
            id: new ObjectId(),
            username: "testUser",
            password: "testPassword",
            role: "ROLE_USER"
        )
        def secureUser = new SecureUser(user: user)

        expect:
        secureUser instanceof UserDetails
        secureUser.username == "testUser"
        secureUser.password == "testPassword"
        secureUser.authorities == [new SimpleGrantedAuthority("ROLE_USER")]
        secureUser.isAccountNonExpired() == true
        secureUser.isAccountNonLocked() == true
        secureUser.isCredentialsNonExpired() == true
        secureUser.isEnabled() == true
    }
}
