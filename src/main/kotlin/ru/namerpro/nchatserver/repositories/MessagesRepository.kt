package ru.namerpro.nchatserver.repositories

import org.apache.kafka.clients.admin.AdminClient
import org.apache.kafka.clients.admin.AdminClientConfig
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.time.Duration
import java.util.*
import java.util.concurrent.TimeUnit

@Repository
class MessagesRepository @Autowired constructor(
    private val clientRepository: ClientRepository
) {

    // client id + chat id -> reader with partner
    private val readChannelsHolder: HashMap<Long, HashMap<Long, KafkaConsumer<String, String>>> = hashMapOf()

    fun createChat(
        id: Long,
        chatId: Long,
        topic: String
    ) {
        val data = readChannelsHolder.getOrPut(id) { hashMapOf() }
        data[chatId] = KafkaConsumer(
            mapOf(
                Pair(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS),
                Pair(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID_CONFIG),
                Pair(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, AUTO_OFFSET_RESET)
            ),
            StringDeserializer(),
            StringDeserializer()
        )
        data[chatId]?.assign(listOf(TopicPartition(topic, 0)))
    }

    fun sendMessage(
        id: Long,
        element: Pair<Long, String>
    ) {
        val client = clientRepository.retrieve(id) ?: return
        val partnerId = client.chatIdToPartner[element.first] ?: return
        val topic = "topic_${id}_${partnerId}_${element.first}"

        KAFKA_PRODUCER.send(ProducerRecord(topic, element.second))
    }

    fun receiveMessages(
        id: Long,
        chatId: Long
    ): List<String> {
        val data = readChannelsHolder[id] ?: return emptyList()
        val reader = data[chatId] ?: return emptyList()

        return reader.poll(POLL_DATA_AWAIT_TIME).map {
            it.value()
        }.toList()
    }

    fun deleteChat(
        id: Long,
        chatId: Long
    ) {
        if (readChannelsHolder[id]?.containsKey(chatId) == true) {
            readChannelsHolder[id]?.get(chatId)?.close()
            readChannelsHolder[id]?.remove(chatId)
        }
    }

    fun createTopics(
        topics: List<NewTopic>
    ) {
        ADMIN_CLIENT.createTopics(topics).all()[MAXIMUM_TIME_FOR_TOPICS_TO_CREATE_SEC, TimeUnit.SECONDS]
    }

    fun removeTopics(
        topics: List<String>
    ) {
        ADMIN_CLIENT.deleteTopics(topics).all()[MAXIMUM_TIME_FOR_TOPICS_TO_CREATE_SEC, TimeUnit.SECONDS]
    }

    fun hasLinkageWith(
        clientId: Long,
        chatId: Long
    ): Boolean = readChannelsHolder[clientId]?.containsKey(chatId) ?: false

    companion object {
        private val POLL_DATA_AWAIT_TIME = Duration.ofMillis(300)
        private const val MAXIMUM_TIME_FOR_TOPICS_TO_CREATE_SEC = 5L

        private const val BOOTSTRAP_SERVERS = "localhost:9092"
        private const val GROUP_ID_CONFIG = "server_group_id"
        private const val AUTO_OFFSET_RESET = "earliest"

        private val ADMIN_CLIENT = AdminClient.create(mapOf(
            Pair(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS)
        ))

        private val KAFKA_PRODUCER = KafkaProducer(mapOf(
                Pair(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS),
                Pair(ProducerConfig.CLIENT_ID_CONFIG, UUID.randomUUID().toString())
            ),
            StringSerializer(),
            StringSerializer()
        )

        const val MESSAGE_CODE = 0
        const val EXIT_CODE = 1
    }

}