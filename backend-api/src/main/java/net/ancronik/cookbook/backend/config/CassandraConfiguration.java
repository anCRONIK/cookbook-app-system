package net.ancronik.cookbook.backend.config;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.data.UdtValue;
import com.datastax.oss.driver.api.core.type.UserDefinedType;
import com.datastax.oss.driver.api.core.type.codec.TypeCodec;
import com.datastax.oss.driver.api.core.type.codec.registry.CodecRegistry;
import com.datastax.oss.driver.api.core.type.codec.registry.MutableCodecRegistry;
import net.ancronik.cookbook.backend.data.codec.IngredientCodec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.core.convert.CassandraCustomConversions;
import org.springframework.data.cassandra.core.mapping.CassandraMappingContext;
import org.springframework.data.cassandra.core.mapping.SimpleTupleTypeFactory;
import org.springframework.data.cassandra.core.mapping.SimpleUserTypeResolver;
import org.springframework.data.cassandra.core.mapping.UserTypeResolver;
import org.springframework.data.convert.CustomConversions;

/**
 * Configuration for Cassandra database.
 *
 * @author Nikola Presecki
 */
@Configuration
public class CassandraConfiguration extends AbstractCassandraConfiguration {

    @Value("${spring.data.cassandra.keyspace-name}")
    private String keySpace;

    @Value("${spring.data.cassandra.contact-points}")
    private String contactPoints;

    @Value("${spring.data.cassandra.port}")
    private int port;

    @Override
    public String getKeyspaceName() {
        return keySpace;
    }

    @Override
    public String getContactPoints() {
        return contactPoints;
    }

    @Override
    public int getPort() {
        return port;
    }


    @Override
    public CassandraMappingContext cassandraMapping() throws ClassNotFoundException {
        CqlSession cqlSession = this.getRequiredSession();
        UserTypeResolver userTypeResolver = new SimpleUserTypeResolver(cqlSession, CqlIdentifier.fromCql(this.getKeyspaceName()));
        CassandraMappingContext mappingContext = new CassandraMappingContext(userTypeResolver, SimpleTupleTypeFactory.DEFAULT);
        CustomConversions customConversions = (CustomConversions) this.requireBeanOfType(CassandraCustomConversions.class);
        this.getBeanClassLoader().ifPresent(mappingContext::setBeanClassLoader);
        mappingContext.setCodecRegistry(cqlSession.getContext().getCodecRegistry());
        mappingContext.setCustomConversions(customConversions);
        mappingContext.setInitialEntitySet(this.getInitialEntitySet());
        mappingContext.setSimpleTypeHolder(customConversions.getSimpleTypeHolder());
        addCustomCodecs(cqlSession);
        return mappingContext;
    }

    private void addCustomCodecs(CqlSession cqlSession) {
        CodecRegistry codecRegistry = cqlSession.getContext().getCodecRegistry();

        UserDefinedType ingredientUdt = cqlSession.getMetadata()
                .getKeyspace(getKeyspaceName())
                .flatMap(keyspaceMetadata -> keyspaceMetadata.getUserDefinedType("ingredient")).orElseThrow();

        TypeCodec<UdtValue> innerCodec = codecRegistry.codecFor(ingredientUdt);
        ((MutableCodecRegistry) codecRegistry).register(new IngredientCodec(innerCodec));
    }
}