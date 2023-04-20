package xyz.spoonmap.server.util;

import java.util.List;
import java.util.stream.IntStream;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.randomizers.range.IntegerRangeRandomizer;
import org.jeasy.random.randomizers.range.LongRangeRandomizer;

public class RandomEntityGenerator {

    public static <T> T create(Class<T> type) {
        return new EasyRandom(getParamWithId()).nextObject(type);
    }

    public static <T> List<T> create(Class<T> type, int count) {
        return IntStream.range(0, count)
                        .mapToObj(i -> create(type))
                        .toList();
    }

    public static <T> T createWithId(Class<T> type) {
        return new EasyRandom(getParamWithId()).nextObject(type);
    }

    private static EasyRandomParameters getParamWithId() {
        return new EasyRandomParameters()
            .stringLengthRange(1, 60)
            .randomize(Long.class, new LongRangeRandomizer(1L, 9999L))
            .randomize(Integer.class, new IntegerRangeRandomizer(1, 9999));
    }

    private static EasyRandomParameters getParam() {
        return new EasyRandomParameters()
            .stringLengthRange(1, 60)
            .randomize(Long.class, new LongRangeRandomizer(1L, 9999L))
            .randomize(Integer.class, new IntegerRangeRandomizer(1, 9999));
    }

}
