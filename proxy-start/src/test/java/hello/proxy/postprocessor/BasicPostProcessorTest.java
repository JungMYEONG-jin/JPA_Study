package hello.proxy.postprocessor;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class BasicPostProcessorTest {

    @Test
    void basicConfig() {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(BeanPostProcessorConfig.class);

        // beanA 이름으로 B 객체가 등록
        B b = applicationContext.getBean("beanA", B.class);
        b.helloB();

        // B는 bean으로 등록되지 않음.
        Assertions.assertThrows(NoSuchBeanDefinitionException.class, () -> applicationContext.getBean(A.class));

    }

    @Slf4j
    @Configuration
    static class BeanPostProcessorConfig{
        @Bean(name = "beanA")
        public A a(){
            return new A();
        }

        @Bean
        public PostProcessor helloPostProcessor(){
            return new PostProcessor();
        }
    }

    @Slf4j
    static class A{
        public void helloA(){
            log.info("A");
        }
    }

    @Slf4j
    static class B{
        public void helloB(){
            log.info("B");
        }
    }

    @Slf4j
    static class PostProcessor implements BeanPostProcessor{


        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            log.info("beanName={} bean={}", beanName, bean);
            if (bean instanceof A){
                return new B();
            }
            return bean;
        }
    }
}
