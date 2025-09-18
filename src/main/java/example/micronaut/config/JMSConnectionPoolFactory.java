package example.micronaut.config;

import io.micronaut.context.annotation.Factory;
import io.micronaut.jms.pool.JMSConnectionPool;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import jakarta.jms.ConnectionFactory;
import org.apache.activemq.ActiveMQConnectionFactory;

@Factory
public class JMSConnectionPoolFactory {

    // Raw ConnectionFactory bean with unique name
    @Singleton
    @Named("rawActiveMqConnectionFactory")
    public ConnectionFactory rawConnectionFactory() {
        return new ActiveMQConnectionFactory("tcp://localhost:61616");
    }

    // Pooled ConnectionFactory bean with the expected name
    @Singleton
    @Named("activeMqConnectionFactory")
    public JMSConnectionPool jmsConnectionPool(@Named("rawActiveMqConnectionFactory") ConnectionFactory connectionFactory) {
        int maxConnections = 10;
        int maxSessionsPerConnection = 500;
        return new JMSConnectionPool(connectionFactory, maxConnections, maxSessionsPerConnection);
    }
}
