package eu.sourcloud.demo.example

import eu.sourcloud.demo.example.domain.SomeEntity
import eu.sourcloud.demo.example.domain.SomeRepository
import io.kotest.assertions.throwables.shouldNotThrowAny
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.support.TransactionTemplate

@SpringBootTest(
    properties = [
        "spring.jpa.properties.hibernate.show_sql=true",
        "spring.jpa.properties.hibernate.format_sql=false",
        "logging.level.org.hibernate.SQL=DEBUG",
        "logging.level.org.hibernate.orm.jdbc.bind=trace",
    ]
)
class KotlinDataIssueApplicationTests {

    @Autowired
    lateinit var someRepository: SomeRepository

    @Autowired
    lateinit var transactionManager: PlatformTransactionManager

    @BeforeEach
    fun setup() {
        someRepository.deleteAll()
    }

    @Test
    fun `Deleting a single entity via repository method should not throw any exception`() {

        val tx = TransactionTemplate(transactionManager)

        val entityName = "Example"

        tx.execute {
            someRepository.save(SomeEntity(name = entityName))
        }

        // okay
        shouldNotThrowAny {
            tx.execute {
                someRepository.deleteByName(entityName)
            }
        }
    }

    @Test
    fun `Deleting multiple entities via repository method should not throw any exception`() {

        val tx = TransactionTemplate(transactionManager)

        tx.execute {
            someRepository.saveAll(listOf(
                SomeEntity(name = "someName"),
                SomeEntity(name = "someOtherName"),
                SomeEntity(name = "aSomewhatMeaningfulName"))
            )
        }

        // okay
        shouldNotThrowAny {
            tx.execute {
                someRepository.deleteByNameStartsWith("some")
            }
        }
    }

    @Test
    fun `Deleting multiple entities via repository method using specification underneath should not throw any exception`() {

        val tx = TransactionTemplate(transactionManager)

        tx.execute {
            someRepository.saveAll(listOf(
                SomeEntity(name = "someName"),
                SomeEntity(name = "someOtherName"),
                SomeEntity(name = "aSomewhatMeaningfulName"))
            )
        }

        // EmptyResultDataAccessException, works with Spring Boot 3.5.5
        shouldNotThrowAny {
            tx.execute {
                someRepository.deleteByNameStartsWithViaCustomSpecification("some")
            }
        }

    }

}
