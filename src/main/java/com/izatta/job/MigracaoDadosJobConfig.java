package com.izatta.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;


@EnableBatchProcessing
@Configuration
public class MigracaoDadosJobConfig {
    @Autowired
    private JobBuilderFactory jobBuilderFactoryr;

    @Bean
    public Job migracaoDadosJob(@Qualifier("migrarPessoaStep") Step migrarPessoaStep,
                                @Qualifier("migrarDadosBancariosStep") Step migrarDadosBancariosStep) {
        return jobBuilderFactoryr
                .get("migracaoDadosJob")
                .start(startStepsParalelos(migrarPessoaStep, migrarDadosBancariosStep))
                .end()
                .incrementer(new RunIdIncrementer())
                .build();
    }

    private Flow startStepsParalelos(Step migrarPessoaStep, Step migrarDadosBancariosStep) {
        Flow migrarDadosBancariosFlow = new FlowBuilder<Flow>("migrarDadosBancariosFlow")
                .start(migrarDadosBancariosStep).build();

        return new FlowBuilder<Flow>("stepsParalelos")
                .start(migrarPessoaStep).split(new SimpleAsyncTaskExecutor())
                .add(migrarDadosBancariosFlow)
                .build();
    }

}
