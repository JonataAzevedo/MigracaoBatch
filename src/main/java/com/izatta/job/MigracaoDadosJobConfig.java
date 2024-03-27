package com.izatta.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

public class MigracaoDadosJobConfig {
    @Autowired
    private JobBuilderFactory jobBuilderFactoryr;

    @Bean
    public Job migracaoDadosJob(Step migrarPessoaStep, Step migrarDadosBancariosStep) {
        return jobBuilderFactoryr
                .get("migracaoDadosJob")
                .start(migrarPessoaStep)
                .next(migrarDadosBancariosStep)
                .incrementer(new RunIdIncrementer())
                .build();
    }

}
