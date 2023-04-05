package xyz.spoonmap.server.post.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;
import java.util.Objects;

public enum MealTime {
    아침("아침"), 점심("점심"), 저녁("저녁");

    private final String value;

    MealTime(String value) {
        this.value = value;
    }

    @JsonCreator
    public static MealTime from(String value) {
        return Arrays.stream(MealTime.values())
                .filter(mealTime -> Objects.equals(mealTime.getValue(), value))
                .findFirst()
                .orElse(MealTime.저녁);
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
