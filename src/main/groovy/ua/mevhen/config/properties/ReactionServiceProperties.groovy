package ua.mevhen.config.properties

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class ReactionServiceProperties {

    @Value('${reaction-task.worker.timeout}')
    private Integer timeout

    @Value('${reaction-task.worker.key-name}')
    private String keyName

    Integer getTimeout() {
        return timeout
    }

    String getKeyName() {
        return keyName
    }

}
