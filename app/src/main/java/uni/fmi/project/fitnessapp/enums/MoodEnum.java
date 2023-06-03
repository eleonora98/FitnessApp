package uni.fmi.project.fitnessapp.enums;

import uni.fmi.project.fitnessapp.R;
import uni.fmi.project.fitnessapp.entity.Mood;

public enum MoodEnum {

    HAPPY(1, "Happy", R.drawable.happy2),
    NERVOUS(2, "Nervous", R.drawable.medium),
    SAD(3, "Sad", R.drawable.sad);

    private int id;
    private String name;
    private int resId;
    MoodEnum(int id, String name, int resId){
        this.id = id;
        this.name = name;
        this.resId = resId;
    }

    private static MoodEnum getByCode(int id) {
        for (MoodEnum moodEnum : values()) {
            if (id == moodEnum.id) {
                return moodEnum;
            }
        }
        return HAPPY;
    }

    public static String getStringMood(int id) {
        MoodEnum moodEnum = getByCode(id);
        switch (moodEnum) {
            case HAPPY:
                return HAPPY.getName();
            case NERVOUS:
                return NERVOUS.getName();
            case SAD:
                return SAD.getName();
        }
        return HAPPY.getName();
    }

    public static int getDrawableByMood(int id) {
        MoodEnum moodEnum = getByCode(id);
        switch (moodEnum) {
            case HAPPY:
                return HAPPY.getResId();
            case NERVOUS:
                return NERVOUS.getResId();
            case SAD:
                return SAD.getResId();
        }
        return HAPPY.getResId();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }
}
