package net.ancronik.cookbook.backend;

import net.ancronik.cookbook.backend.config.CacheConfig;
import net.ancronik.cookbook.backend.data.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration;
import org.springframework.boot.autoconfigure.data.cassandra.CassandraDataAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@EnableAutoConfiguration(exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class,
        CassandraAutoConfiguration.class,
        CassandraDataAutoConfiguration.class
})
@ComponentScan(
        basePackages = {"net.ancronik.cookbook.backend.domain"}
)
@Import(CacheConfig.class)
public class TestConfigurationForUnitTesting {

    @MockBean
    AuthorRepository authorRepository;

    @MockBean
    RecipeRepository recipeRepository;

    @MockBean
    RecipeCommentRepository recipeCommentRepository;

    @MockBean
    RecipeCategoryRepository recipeCategoryRepository;

    @MockBean
    MeasurementUnitRepository measurementUnitRepository;

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
