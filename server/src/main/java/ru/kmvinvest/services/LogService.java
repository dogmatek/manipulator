package ru.kmvinvest.services;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.kmvinvest.exeptions.MyRuntimeExeption;

import java.util.Collections;
import java.util.Properties;

@Service
@RequiredArgsConstructor
public class LogService {

    private final static String TOPIC_LOGGEDSERVICE = "jd-topic";
    private final static String KEY_LOGGEDSERVICE = "manipulator-server";
    private final static String BOOTSTRAP_SERVER = "kmvinvest.ru:9092";
    final Producer<String, String> producer = createProducer();


    public void add(String log) {
        final ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_LOGGEDSERVICE, KEY_LOGGEDSERVICE, log);
        try {
            RecordMetadata metadata = producer.send(record).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Сохранение в базу данных
        // Возникла проблема единовременного занесения новой записи в базу данных,
        // так как создается один и тот же ID при работе серверной частьи и iot
//        LogEntity logEntity = new LogEntity();
//        logEntity.setKey(KEY_LOGGEDSERVICE);
//        logEntity.setDescription(log);
//        logEntity.setDt(LocalDateTime.now());

//        logRepository.save(logEntity);
    }

    //Добавить лог в кафку/базу данных и вызвать исключение
    @ExceptionHandler(MyRuntimeExeption.class)
    public void addAndExeption(String message) {
        add(message);
        throw new MyRuntimeExeption(message);
    }


    private static Producer<String, String> createProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "KafkaExampleProducer");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return new KafkaProducer<>(props);
    }

    private static Consumer<String, String> createConsumer() {
        final Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "KafkaExampleConsumer");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        final Consumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(TOPIC_LOGGEDSERVICE));
        return consumer;
    }


}
