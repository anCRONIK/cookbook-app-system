package net.ancronik.cookbook.backend.authentication.application.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * Configuration for database specific things (such as additional datasources).
 *
 * @author Nikola Presecki
 */
@Configuration
public class DatabaseConfig {

    @Bean
    @ConfigurationProperties("oauth2.datasource")
    public DataSourceProperties oauth2DatasourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("oauth2.datasource.configuration")
    public DataSource oauth2DataSource(@Value("${oauth2.datasource.hikari.schema}") String schema) {
        DataSourceProperties props = oauth2DatasourceProperties();
        HikariDataSource ds = props.initializeDataSourceBuilder()
                .type(HikariDataSource.class).build();
        ds.setSchema(schema); //FIXME figure out why this is needed
        return ds;
    }

    @Bean
    public JdbcTemplate oauth2JdbcTemplate(@Qualifier("oauth2DataSource") DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }

}
