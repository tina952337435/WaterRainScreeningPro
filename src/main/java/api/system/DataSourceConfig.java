package api.system;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {
    @Primary
    @Bean(name="swzzmxsqSource")
    @ConfigurationProperties(prefix = "spring.datasource.swzzmxsq")
    public DataSource swzzmxsqSource(){
        return DataSourceBuilder.create().build();
    }

    @Bean(name="swzzzjkSource")
    @ConfigurationProperties(prefix = "spring.datasource.swzzzjk")
    public DataSource swzzzjkSource(){
        return DataSourceBuilder.create().build();
    }
}
