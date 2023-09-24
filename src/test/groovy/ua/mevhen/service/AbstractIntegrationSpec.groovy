package ua.mevhen.service


import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.spock.Testcontainers
import spock.lang.Shared
import spock.lang.Specification

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE
import static org.testcontainers.utility.DockerImageName.parse

@SpringBootTest(webEnvironment = NONE)
@Testcontainers
abstract class AbstractIntegrationSpec extends Specification {

    @Shared
    static final def MONGO = new MongoDBContainer(parse('mongo:6.0')).withExposedPorts(27017)

    static {
        MONGO.start()
    }

    @DynamicPropertySource
    static void setMongoDbProperties(DynamicPropertyRegistry registry) {
        registry.add('spring.data.mongodb.uri', MONGO::getReplicaSetUrl)
    }

}
