package com.leapwise.banking.util;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
@AllArgsConstructor
@Profile("!test")
@Log4j2
public class ApplicationStartup implements ApplicationRunner {

    private final TransactionFileGenerator fileGenerator;
    private final TransactionImporter transactionImporter;
    private final ApplicationContext applicationContext;
    private final Environment environment;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        DataSource dataSource = applicationContext.getBean(DataSource.class);
        executeDataSql(dataSource);

        fileGenerator.generateTransactionsFile("transactions.txt", 100000);
        transactionImporter.importTransactions();
    }

    private void executeDataSql(DataSource dataSource) {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator(new ClassPathResource("util/data.sql"));
        populator.execute(dataSource);
    }
}

