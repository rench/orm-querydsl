package com.lowang.ormquerydsl.config;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import com.zaxxer.hikari.HikariDataSource;

import io.shardingsphere.api.algorithm.sharding.PreciseShardingValue;
import io.shardingsphere.api.algorithm.sharding.standard.PreciseShardingAlgorithm;
import io.shardingsphere.api.config.ShardingRuleConfiguration;
import io.shardingsphere.api.config.TableRuleConfiguration;
import io.shardingsphere.api.config.strategy.StandardShardingStrategyConfiguration;
import io.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import io.shardingsphere.shardingjdbc.util.DataSourceUtil;

/**
 * sharding jdbc 配置
 *
 * @author wang.ch
 * @date 2018-12-13 11:46:01
 */
@Configuration
public class ShardingConfig {
  private static final Logger LOG = LoggerFactory.getLogger(ShardingConfig.class);
  @Autowired private Environment env;

  @SuppressWarnings("unchecked")
  @Bean
  @Primary
  public DataSource dataSource() {

    // datasouce config
    Map<String, DataSource> dsMap = new HashMap<>(2);
    try {
      JdbcConfig cfg = new JdbcConfig();
      cfg.setUrl(env.getProperty("spring.ds.m0.url"));
      cfg.setJdbcUrl(env.getProperty("spring.ds.m0.url"));
      cfg.setPassword(env.getProperty("spring.ds.m0.password"));
      cfg.setUsername(env.getProperty("spring.ds.m0.username"));
      cfg.setDriverClassName(env.getProperty("spring.ds.m0.driver-class-name"));

      DataSource m0 =
          DataSourceUtil.getDataSource(HikariDataSource.class.getName(), BeanMap.create(cfg));
      dsMap.put("m0", m0);

      cfg = new JdbcConfig();
      cfg.setUrl(env.getProperty("spring.ds.m1.url"));
      cfg.setJdbcUrl(env.getProperty("spring.ds.m1.url"));
      cfg.setPassword(env.getProperty("spring.ds.m1.password"));
      cfg.setUsername(env.getProperty("spring.ds.m1.username"));
      cfg.setDriverClassName(env.getProperty("spring.ds.m1.driver-class-name"));
      DataSource m1 =
          DataSourceUtil.getDataSource(HikariDataSource.class.getName(), BeanMap.create(cfg));
      dsMap.put("m1", m1);
      LOG.debug("datasources init success");
    } catch (Exception e) {
      e.printStackTrace();
    }

    // user table strategy
    StandardShardingStrategyConfiguration tableStrategy =
        new StandardShardingStrategyConfiguration(
            "id",
            new PreciseShardingAlgorithm<String>() {
              @Override
              public String doSharding(
                  Collection<String> availableTargetNames,
                  PreciseShardingValue<String> shardingValue) {
                LOG.info(" user table strategy for : " + shardingValue.getValue());
                return "user";
              }
            });
    // user table database strategy
    StandardShardingStrategyConfiguration dataBaseStrategy =
        new StandardShardingStrategyConfiguration(
            "id",
            new PreciseShardingAlgorithm<String>() {
              @Override
              public String doSharding(
                  Collection<String> availableTargetNames,
                  PreciseShardingValue<String> shardingValue) {
                LOG.info(
                    " user table in database {} strategy for : {} ",
                    availableTargetNames,
                    shardingValue.getValue());
                String database =
                    availableTargetNames
                        .stream()
                        .filter(
                            (x) ->
                                x.equals(
                                    "m"
                                        + new BigInteger(shardingValue.getValue())
                                            .mod(BigInteger.valueOf(availableTargetNames.size()))))
                        .findAny()
                        .get();
                LOG.info(" in {} for {} ", database, shardingValue.getValue());
                return database;
              }
            });
    // user table rule
    TableRuleConfiguration userTable = new TableRuleConfiguration();
    userTable.setLogicTable("user");
    userTable.setActualDataNodes("m${0..1}.user");
    userTable.setDatabaseShardingStrategyConfig(dataBaseStrategy);
    userTable.setTableShardingStrategyConfig(tableStrategy);
    // sharding rule
    ShardingRuleConfiguration shardingRuleConfiguration = new ShardingRuleConfiguration();
    shardingRuleConfiguration.setDefaultDataSourceName("m0");
    shardingRuleConfiguration.getTableRuleConfigs().add(userTable);

    // sharding datasource
    DataSource shardingDataSource = null;
    try {
      shardingDataSource =
          ShardingDataSourceFactory.createDataSource(
              dsMap, shardingRuleConfiguration, new HashMap<>(), new Properties());
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return shardingDataSource;
  }
  // @ConfigurationProperties(prefix="spring.ds.m1")
  public static class JdbcConfig {
    private String url;
    private String jdbcUrl;
    private String username;
    private String password;
    private String driverClassName;
    /** @return the url */
    public String getUrl() {
      return url;
    }
    /** @param url the url to set */
    public void setUrl(String url) {
      this.url = url;
    }

    /** @return the jdbcUrl */
    public String getJdbcUrl() {
      return jdbcUrl;
    }
    /** @param jdbcUrl the jdbcUrl to set */
    public void setJdbcUrl(String jdbcUrl) {
      this.jdbcUrl = jdbcUrl;
    }
    /** @return the username */
    public String getUsername() {
      return username;
    }
    /** @param username the username to set */
    public void setUsername(String username) {
      this.username = username;
    }
    /** @return the password */
    public String getPassword() {
      return password;
    }
    /** @param password the password to set */
    public void setPassword(String password) {
      this.password = password;
    }
    /** @return the driverClassName */
    public String getDriverClassName() {
      return driverClassName;
    }
    /** @param driverClassName the driverClassName to set */
    public void setDriverClassName(String driverClassName) {
      this.driverClassName = driverClassName;
    }
  }
}
