package api.system;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = {"api.data.swzzzjk"},sqlSessionFactoryRef = "swzzzjkSqlSessionFactory")
public class swzzzjkConfig {
    @Autowired
    @Qualifier("swzzzjkSource")
    private DataSource dataSource;
    @Bean
    public SqlSessionFactory swzzzjkSqlSessionFactory() throws Exception{
        SqlSessionFactoryBean sqlSessionFactoryBean=new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);

        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/swzzzjk/**/*.xml"));

        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    public SqlSessionTemplate swzzzjkSqlSession() throws Exception{
        SqlSessionTemplate sqlSessionTemplate=new SqlSessionTemplate(swzzzjkSqlSessionFactory());
        return sqlSessionTemplate;
    }
}
