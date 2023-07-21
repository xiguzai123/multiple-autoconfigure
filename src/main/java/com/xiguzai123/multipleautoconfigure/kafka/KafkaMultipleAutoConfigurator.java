package com.xiguzai123.multipleautoconfigure.kafka;

import lombok.SneakyThrows;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.kafka.*;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.*;
import org.springframework.kafka.listener.adapter.RecordFilterStrategy;
import org.springframework.kafka.support.converter.BatchMessageConverter;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.kafka.transaction.KafkaAwareTransactionManager;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author chenyanhong
 * @since 2022-10-19 16:53
 */
public class KafkaMultipleAutoConfigurator {

    @SneakyThrows
    public static ConcurrentKafkaListenerContainerFactory<?, ?> kafkaListenerContainerFactory(KafkaProperties properties,
                                                                                              ObjectProvider<RecordMessageConverter> messageConverter,
                                                                                              ObjectProvider<RecordFilterStrategy<Object, Object>> recordFilterStrategy,
                                                                                              ObjectProvider<BatchMessageConverter> batchMessageConverter,
                                                                                              ObjectProvider<KafkaTemplate<Object, Object>> kafkaTemplate,
                                                                                              ObjectProvider<KafkaAwareTransactionManager<Object, Object>> kafkaTransactionManager,
                                                                                              ObjectProvider<ConsumerAwareRebalanceListener> rebalanceListener,
                                                                                              ObjectProvider<ErrorHandler> errorHandler,
                                                                                              ObjectProvider<BatchErrorHandler> batchErrorHandler,
                                                                                              ObjectProvider<CommonErrorHandler> commonErrorHandler,
                                                                                              ObjectProvider<AfterRollbackProcessor<Object, Object>> afterRollbackProcessor,
                                                                                              ObjectProvider<RecordInterceptor<Object, Object>> recordInterceptor,
                                                                                              ConcurrentKafkaListenerContainerFactoryConfigurer configurer,
                                                                                              ObjectProvider<ConsumerFactory<Object, Object>> kafkaConsumerFactory) {
        final Class<?> clazz = Class.forName("org.springframework.boot.autoconfigure.kafka.KafkaAnnotationDrivenConfiguration");
        final Constructor<?> constructor = ReflectionUtils.accessibleConstructor(clazz, KafkaProperties.class, ObjectProvider.class, ObjectProvider.class, ObjectProvider.class, ObjectProvider.class, ObjectProvider.class, ObjectProvider.class, ObjectProvider.class, ObjectProvider.class, ObjectProvider.class, ObjectProvider.class);
        constructor.setAccessible(true);
        final Object instance = constructor.newInstance(properties, messageConverter, recordFilterStrategy,rebalanceListener, batchMessageConverter, kafkaTemplate, kafkaTransactionManager, errorHandler, batchErrorHandler, commonErrorHandler, afterRollbackProcessor, recordInterceptor);
        final Method[] methods = ReflectionUtils.getDeclaredMethods(clazz);
        final Method kafkaListenerContainerFactory = Arrays.stream(methods).filter(method -> "kafkaListenerContainerFactory".equals(method.getName())).findFirst().orElse(null);
        kafkaListenerContainerFactory.setAccessible(true);
        return (ConcurrentKafkaListenerContainerFactory<?, ?>) kafkaListenerContainerFactory.invoke(instance, configurer, kafkaConsumerFactory);
    }

    public static ConsumerFactory<?, ?> kafkaConsumerFactory(KafkaProperties properties,
                                                             ObjectProvider<DefaultKafkaConsumerFactoryCustomizer> customizers) {
        final KafkaAutoConfiguration kafkaAutoConfiguration = new KafkaAutoConfiguration(properties);
        return kafkaAutoConfiguration.kafkaConsumerFactory(customizers);
    }

    public static ProducerFactory<?, ?> kafkaProducerFactory(KafkaProperties properties,
                                                             ObjectProvider<DefaultKafkaProducerFactoryCustomizer> customizers) {
        final KafkaAutoConfiguration kafkaAutoConfiguration = new KafkaAutoConfiguration(properties);
        return kafkaAutoConfiguration.kafkaProducerFactory(customizers);
    }
}
