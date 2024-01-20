package pandahealth;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.formats.json.JsonDeserializationSchema;
import org.apache.flink.formats.json.JsonSerializationSchema;

import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;

import pandahealth.Measurement;
import pandahealth.DefectDetector;

public class DefectDetectJob {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        KafkaSource<Measurement> source = KafkaSource.<Measurement>builder()
                                        .setBootstrapServers("localhost:19092")
                                        .setTopics("produced-items")
                                        .setStartingOffsets(OffsetsInitializer.earliest())
                                        .setValueOnlyDeserializer(new JsonDeserializationSchema<Measurement>(Measurement.class))
                                        .build();

        DataStream<Measurement> measurements = env.fromSource(source, WatermarkStrategy.noWatermarks(), "Kafka Source");

    
        
        DataStream<Measurement> alerts = measurements
            .keyBy(Measurement::getID)
            .process(new DefectDetector())
            .name("defect-detector");

        KafkaSink<Measurement> sink = KafkaSink.<Measurement>builder()
                                    .setBootstrapServers("localhost:19092")
                                    .setRecordSerializer(KafkaRecordSerializationSchema.builder()
                                        .setTopic("defective-items")
                                        .setValueSerializationSchema(new JsonSerializationSchema<Measurement>())
                                        .build()
                                    )
                                    .build();
    
        alerts
            .sinkTo(sink);

        env.execute("Defect Detection");
    }
}
