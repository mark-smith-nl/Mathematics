package nl.smith.mathematics.utility;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;

@Configuration
@ComponentScan({ "nl.smith.mathematics.functions", "nl.smith.mathematics.services" })
public class ApplicationConfiguration {

	@Bean(autowire = Autowire.BY_TYPE)
	public DriverManagerDataSource driverManagerDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();

		dataSource.setDriverClassName("org.hsqldb.jdbcDriver");
		dataSource.setUrl("jdbc:hsqldb:file:" + ApplicationProperties.getApplicationProperties().getProperty("databasePath"));
		dataSource.setUsername("dba");
		dataSource.setPassword("pwd");

		return dataSource;
	}

	@Bean(autowire = Autowire.BY_TYPE)
	public LocalSessionFactoryBean localSessionFactoryBean() {
		LocalSessionFactoryBean localSessionFactoryBean = new LocalSessionFactoryBean();
		System.out.println(localSessionFactoryBean);
		localSessionFactoryBean.setPackagesToScan(new String[] { "nl.smith.mathematics.persist.domain" });
		Properties hibernateProperties = getHibernateProperties();
		localSessionFactoryBean.setHibernateProperties(hibernateProperties);
		Properties hibernateProperties2 = localSessionFactoryBean.getHibernateProperties();
		System.out.println(hibernateProperties2.getProperty("hibernate.hbm2ddl.auto"));
		System.out.println(hibernateProperties);
		System.out.println(hibernateProperties2);
		return localSessionFactoryBean;
	}

	private static Properties getHibernateProperties() {
		Properties hibernateProperties = new Properties();

		hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
		hibernateProperties.setProperty("hibernate.show_sql", "validate");
		hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "update");
		System.out.println(hibernateProperties.getProperty("hibernate.hbm2ddl.auto"));
		return hibernateProperties;
	}

}
