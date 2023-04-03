package xyz.spoonmap.server.post.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum MealTime {
    아침("아침"), 점심("점심"), 저녁("저녁");

    @Getter
    private final String value;

    MealTime(String value) {
        this.value = value;
    }

    @JsonCreator
    public static MealTime from(String value) {
        for (MealTime mealTime : MealTime.values()) {
            if (mealTime.getValue().equals(value)) {
                return mealTime;
            }
        }
        return null;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
