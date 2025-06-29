package uz.shop.model;

import lombok.Data;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Data
public abstract class BaseModel {
    private UUID id;
    private String createdDate;
    private UUID createdBy;
    private boolean active;
    public BaseModel() {
        this.id = UUID.randomUUID();
        this.createdDate = String.valueOf(Instant.now());
        this.active = true;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BaseModel base = (BaseModel) o;
        return active == base.active && Objects.equals(id, base.id) && Objects.equals(createdDate, base.createdDate) && Objects.equals(createdBy, base.createdBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdDate, createdBy, active);
    }
}
