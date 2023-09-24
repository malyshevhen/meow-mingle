package ua.mevhen.service

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.spock.Testcontainers
import spock.lang.Shared
import spock.lang.Specification

import static org.testcontainers.utility.DockerImageName.parse

@SpringBootTest
@Testcontainers
abstract class AbstractIntegrationSpec extends Specification {

    @Shared
    static final def MONGO = new MongoDBContainer(parse('mongo:6.0')).withExposedPorts(27017)

    @Shared
    static final def REDIS = new GenericContainer(parse('redis:7.2.1')).withExposedPorts(6379)

    static {
        MONGO.start()
        REDIS.start()
    }

    @DynamicPropertySource
    static void setMongoDbProperties(DynamicPropertyRegistry registry) {
        registry.add('spring.data.mongodb.uri', MONGO::getReplicaSetUrl)
        registry.add('spring.data.redis.host', REDIS::getHost)
        registry.add('spring.data.redis.port', REDIS::getMappedPort(6379)::toString)
    }

}
