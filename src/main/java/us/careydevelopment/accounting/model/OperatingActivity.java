package us.careydevelopment.accounting.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class OperatingActivity {

    private OperatingActivityType type;
    private String name;
    private Long value;

    public OperatingActivityType getType() {
        return type;
    }

    public void setType(OperatingActivityType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

}
