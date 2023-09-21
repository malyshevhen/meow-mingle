package ua.mevhen.service

import org.springframework.boot.test.context.SpringBootTest
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.spock.Testcontainers
import org.testcontainers.utility.DockerImageName
import spock.lang.Specification

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE

@SpringBootTest(webEnvironment = NONE)
@Testcontainers
class AbstractIntegrationSpec extends Specification {

    private static MongoDBContainer mongo

    static {
        mongo = new MongoDBContainer(DockerImageName.parse('mongo:6.0'))
            .withExposedPorts(27017)
        mongo.start()
        System.setProperty('spring.data.mongo.host', mongo.getHost())
        System.setProperty('spring.data.mongo.port', mongo.getMappedPort(27017).toString())
    }

    def cleanupSpec() {
        mongo.stop()
    }

}
