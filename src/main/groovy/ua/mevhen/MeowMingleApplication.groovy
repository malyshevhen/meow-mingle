package ua.mevhen

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@SpringBootApplication
@EnableMongoRepositories
class MeowMingleApplication {

    static void main(String[] args) {
        SpringApplication.run(MeowMingleApplication, args)
    }

}
