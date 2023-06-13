package io.lsdconsulting.lsd.distributed.postgres.config

import com.fasterxml.jackson.databind.ObjectMapper
import io.lsdconsulting.lsd.distributed.postgres.repository.InterceptedDocumentPostgresRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
open class LibraryConfig {

    @Bean
    @ConditionalOnExpression("#{'\${lsd.dist.connectionString:}'.startsWith('jdbc:postgresql://')}")
    open fun interceptedDocumentRepositoryFromConnectionString(
        @Value("\${lsd.dist.connectionString}") dbConnectionString: String,
        objectMapper: ObjectMapper,
        @Value("\${lsd.dist.db.failOnConnectionError:#{true}}") failOnConnectionError: Boolean,
    ) = InterceptedDocumentPostgresRepository(dbConnectionString, objectMapper, failOnConnectionError)

    @Bean
    @ConditionalOnBean(value = [DataSource::class])
    @ConditionalOnMissingBean(value = [InterceptedDocumentPostgresRepository::class])
    open fun interceptedDocumentRepositoryFromDataSource(
        dataSource: DataSource,
        objectMapper: ObjectMapper,
        @Value("\${lsd.dist.db.failOnConnectionError:#{true}}") failOnConnectionError: Boolean,
    ): InterceptedDocumentPostgresRepository {
        return InterceptedDocumentPostgresRepository(dataSource, objectMapper)
    }
}
