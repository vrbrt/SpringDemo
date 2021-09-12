package pl.vrbrt.spring.springBatch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
@Slf4j
public class BatchConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final ProductRepository productRepository;


    @Bean
    public ItemWriter<Product> writer(){
        return productRepository::saveAll;
    }

    @Bean
    public FlatFileItemReader<Product> reader() {
        return new FlatFileItemReaderBuilder<Product>()
                .name("personItemReader")
                .resource(new ClassPathResource("products.csv"))
                .linesToSkip(1)
                .delimited()
                .names("name", "manufacturerCode", "price", "unit")
                .fieldSetMapper(new BeanWrapperFieldSetMapper<Product>() {{
                    setTargetType(Product.class);
                }})
                .build();
    }


    @Bean
    public Job importProductsJob(Step step1, JobExecutionListener listener){
        return jobBuilderFactory.get("importProductJob")
                .listener(listener)
                .incrementer(new RunIdIncrementer())
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    public Step step1(FlatFileItemReader<Product> reader, ItemWriter<Product> writer) {
        return stepBuilderFactory.get("step1")
                .<Product,Product>chunk(2)
                .reader(reader)
                .writer(writer)
                .build();
    }

    @Bean
    public JobExecutionListener executionListener(){
        return new JobExecutionListener() {
            @Override
            public void beforeJob(JobExecution jobExecution) {
                log.info("Products before batch");
                productRepository.findAll()
                        .forEach(p -> log.info("Product: {}",p));
            }

            @Override
            public void afterJob(JobExecution jobExecution) {
                log.info("Products after batch");
                productRepository.findAll()
                        .forEach(p -> log.info("Product: {}",p));
            }
        };
    }

}