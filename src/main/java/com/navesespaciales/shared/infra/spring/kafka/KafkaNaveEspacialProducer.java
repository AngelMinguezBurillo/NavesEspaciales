package com.navesespaciales.shared.infra.spring.kafka;

import static com.navesespaciales.shared.constantes.Constantes.KAFKA_TOPIC;
import com.navesespaciales.shared.defaults.LogSupport;
import java.util.concurrent.CompletableFuture;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

@Component
public class KafkaNaveEspacialProducer implements LogSupport {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaNaveEspacialProducer(final KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void enviar(final String naveEspacial) {

        CompletableFuture<SendResult<String, String>> completableFuture = kafkaTemplate.send(KAFKA_TOPIC, naveEspacial);
        logInfo("Enviando elemento: {} al broker de KAFKA. Topic: {}", naveEspacial, KAFKA_TOPIC);

        completableFuture.whenComplete((result, ex) -> {
            if (ex == null) {
                logInfo("Kafka. Mensaje enviado correctamente con vlaor {}", result.getProducerRecord().value());
            } else {
                logInfo("Kafka. Ha ocurrido un error al enviar el mensaje {}", result.getProducerRecord().value());
            }
        });
    }

}
