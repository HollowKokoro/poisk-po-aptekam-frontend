import kotlinx.coroutines.*
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import java.util.*

class Producer(drugName: String) {
    init {
        val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default)
        coroutineScope.launch {
            val props = Properties()
            withContext(Dispatchers.IO) {
                props.load(this.javaClass.classLoader.getResourceAsStream("producer.properties"))
            }
            val producerRecord: ProducerRecord<String, String> = ProducerRecord("drugName", "drugName", drugName)
            val kafkaProducer: KafkaProducer<String, String> = KafkaProducer(props)
            kafkaProducer.send(producerRecord)
            kafkaProducer.close()
        }
    }
}
