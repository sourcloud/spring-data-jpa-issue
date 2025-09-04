package eu.sourcloud.demo.example.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface SomeRepository : JpaRepository<SomeEntity, Long>, JpaSpecificationExecutor<SomeEntity> {

    fun deleteByName(name: String)

    fun deleteByNameStartsWith(prefix: String)

    fun deleteByNameStartsWithViaCustomSpecification(prefix: String) {
        delete { root, _, criteriaBuilder ->
            criteriaBuilder.like(root.get("name"), "$prefix%")
        }
    }

}