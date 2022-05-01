package ru.erudyt.online.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.persistence.EntityManagerFactory
import javax.sql.DataSource

@Configuration
@EnableJpaRepositories(
    entityManagerFactoryRef = "apiEntityManagerFactory",
    transactionManagerRef = "apiTransactionManager",
    basePackages = ["ru.erudyt.online.repository.api"]
)
@EnableTransactionManagement
@EnableConfigurationProperties(DialectSettings::class)
class ApiDatasourceConfig @Autowired constructor(
    private val dialectSettings: DialectSettings,
) {

    companion object {
        private const val ENTITY_PACKAGE = "ru.erudyt.online.entity.api"
    }

    @Bean("apiDataSource")
    @ConfigurationProperties(prefix = "api.datasource")
    fun apiDataSource(): DataSource {
        return DataSourceBuilder.create().build()
    }

    @Bean("apiEntityManagerFactory")
    fun apiEntityManagerFactory(
        @Qualifier("apiDataSource") dataSource: DataSource,
    ): LocalContainerEntityManagerFactoryBean {
        return LocalContainerEntityManagerFactoryBean().apply {
            setDataSource(dataSource)
            setPackagesToScan(ENTITY_PACKAGE)
            persistenceUnitName = "api"
            jpaVendorAdapter = HibernateJpaVendorAdapter()
            setJpaPropertyMap(
                mapOf(
                    "hibernate.dialect" to dialectSettings.apiDialect,
                    "hibernate.hbm2ddl.auto" to "update",
                )
            )
        }
    }

    @Bean("apiTransactionManager")
    fun apiTransactionManager(
        @Qualifier("apiEntityManagerFactory") apiEntityManagerFactory: EntityManagerFactory,
    ): PlatformTransactionManager {
        return JpaTransactionManager(apiEntityManagerFactory)
    }
}