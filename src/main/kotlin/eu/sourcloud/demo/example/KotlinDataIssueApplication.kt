package eu.sourcloud.demo.example

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KotlinDataIssueApplication

fun main(args: Array<String>) {
	runApplication<KotlinDataIssueApplication>(*args)
}
