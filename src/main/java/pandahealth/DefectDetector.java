package pandahealth;

import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction.Context;

import org.apache.flink.util.Collector;

import pandahealth.Measurement;
import java.util.Map;

public class DefectDetector extends KeyedProcessFunction<String, Measurement, Measurement> {

    private static final Map<String, Double> EXPECTED_WEIGHTS = Map.of(
        "type-one", 1.0,
        "type-two", 2.0,
        "type-five", 5.0,
        "type-ten", 10.0
    );
    private static final double MAXIMUM_DEVIATION = 0.05;

    @Override
    public void processElement(
            Measurement measurement,
            Context context,
            Collector<Measurement> collector) throws Exception {

                String itemType = measurement.getType();
                double weight = measurement.getWeight();
                double expectedWeight = EXPECTED_WEIGHTS.get(itemType);

                if(Math.abs(weight - expectedWeight) / expectedWeight > MAXIMUM_DEVIATION) {
                    collector.collect(measurement);
                }
    }
}

