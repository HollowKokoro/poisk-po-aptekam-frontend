import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.consumer.KafkaConsumer
import java.time.Duration
import java.util.*
import kotlin.collections.ArrayList

class Consumer {

    private val _result = MutableStateFlow<List<String>>(emptyList())
    val result : StateFlow<List<String>> = _result.asStateFlow()

    fun run() {
        val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default)
        coroutineScope.launch {
            val localResult: MutableList<String> = mutableListOf()
            val props = Properties()
            withContext(Dispatchers.IO) {
                props.load(this.javaClass.classLoader.getResourceAsStream("consumer.properties"))
            }
            val consumer: KafkaConsumer<String, String> = KafkaConsumer(props)
            consumer.subscribe(listOf("result"))
            for (i in (1..2)) {
                var records: ConsumerRecords<String, String> = consumer.poll(Duration.ofSeconds(100))
                while (records.isEmpty) {
                    withContext(Dispatchers.IO) {
                        Thread.sleep(Duration.ofSeconds(1L).toMillis())
                    }
                    records = consumer.poll(Duration.ofSeconds(1))
                }
                records.firstOrNull()?.value()?.let { result ->
                    synchronized(localResult) {
                        localResult.add(result)
                    }
                }
            }
            consumer.close()
            _result.value = ArrayList(localResult)

//            (1..5).forEach {
//                delay(1000)
//                localResult.add(it.toString())
//                println("localResult:$localResult")
//                _result.value = ArrayList(localResult)
//                println("_result.value:${_result.value}")
//            }
        }
    }
}
