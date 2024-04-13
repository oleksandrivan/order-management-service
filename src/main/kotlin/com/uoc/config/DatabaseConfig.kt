package com.uoc.config

import com.mysql.cj.jdbc.MysqlDataSource
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Property
import io.micronaut.context.annotation.Value
import javax.sql.DataSource

@Factory
class DatabaseConfig(
    private val configReader: ConfigReader
) {

    @Bean
    fun dataSource(): DataSource {
        val dataSource = MysqlDataSource()
        dataSource.setURL(System.getProperty("DATABASE_URL"))
        dataSource.user = System.getProperty("DATABASE_USERNAME")
        dataSource.password = System.getProperty("DATABASE_PASSWORD")
        return dataSource
    }
}