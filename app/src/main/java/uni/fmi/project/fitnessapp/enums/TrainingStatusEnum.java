package uni.fmi.project.fitnessapp.enums;

public enum TrainingStatusEnum {

    NEW(1, "New"),
    IN_PROGRESS(2, "In progress"),
    DONE(3, "Done");

    private int id;
    private String name;
    TrainingStatusEnum(int id, String name){
        this.id = id;
        this.name = name;
    }
    private static TrainingStatusEnum getByCode(int id) {
        for (TrainingStatusEnum trainingStatus : values()) {
            if (id == trainingStatus.id) {
                return trainingStatus;
            }
        }
        return NEW;
    }

    public static String getStatusById(int id) {
        TrainingStatusEnum trainingStatusEnum = getByCode(id);
        switch (trainingStatusEnum) {
            case NEW:
                return NEW.getName();
            case IN_PROGRESS:
                return IN_PROGRESS.getName();
            case DONE:
                return DONE.getName();
        }
        return NEW.getName();
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
}
