package xyz.spoonmap.server.util;


import static org.jeasy.random.FieldPredicates.named;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.randomizers.range.IntegerRangeRandomizer;
import org.jeasy.random.randomizers.range.LongRangeRandomizer;

public class RandomEntityGenerator {

    public static <T> T create(Class<T> type) {

        while (true) {
            try {
                EasyRandomParameters parameters = new EasyRandomParameters()
                    .excludeField(named("id"))
                    .stringLengthRange(1, 60)
                    .randomize(Long.class, new LongRangeRandomizer(1L, 9999L))
                    .randomize(Integer.class, new IntegerRangeRandomizer(1, 9999));

                return new EasyRandom(parameters).nextObject(type);
            } catch (Exception ignore) {

            }
        }

    }

    public static <T> T createWithId(Class<T> type) {

        while (true) {
            try {
                EasyRandomParameters parameters = new EasyRandomParameters()
                    .stringLengthRange(1, 60)
                    .randomize(Long.class, new LongRangeRandomizer(1L, 9999L))
                    .randomize(Integer.class, new IntegerRangeRandomizer(1, 9999));

                return new EasyRandom(parameters).nextObject(type);
            } catch (Exception ignore) {

            }
        }
    }

}
