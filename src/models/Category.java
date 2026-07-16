package models;

public class Category extends BaseModel {
    private String name;
    private String description;
    private boolean active;
    private boolean requiresSize;

    public Category() {
    }

    public Category(Long id, String name, String description, boolean active, boolean requiresSize) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.active = active;
        this.requiresSize = requiresSize;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isRequiresSize() {
        return requiresSize;
    }

    public void setRequiresSize(boolean requiresSize) {
        this.requiresSize = requiresSize;
    }
}
