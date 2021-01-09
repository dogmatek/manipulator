package ru.kmvinvest;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Collections;
import java.util.Properties;


public class Application {

    private final static String TOPIC = "jd-topic";
    private final static String BOOTSTRAP_SERVER = "kmvinvest.ru:9092";

    public static void main(String[] args) {
        final Producer<String, String> producer = createProducer();
        try {
            producer.send(new ProducerRecord<>(TOPIC, "scanner-kafka", BOOTSTRAP_SERVER)).get();
            Thread.sleep(3000L);
            producer.send(new ProducerRecord<>(TOPIC, "scanner-kafka", TOPIC)).get();
        } catch (Exception e) {
            e.printStackTrace();
        }


        Thread consumerThread = new Thread(() -> {
            final Consumer<String, String> consumer = createConsumer();
            while (true) {
                final ConsumerRecords<String, String> consumerRecords = consumer.poll(1000);

                if (consumerRecords.count() == 0) {
                    continue;
                }
                consumerRecords.forEach(record -> {
                    System.out.printf("Consumer Record:(%s, %s, %d, %d)\n", record.key(), record.value(), record.partition(), record.offset());
                });
                consumer.commitAsync();
            }
        });
        consumerThread.start();
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
        consumer.subscribe(Collections.singletonList(TOPIC));
        return consumer;
    }


}