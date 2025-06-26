package uz.shop.model;

import lombok.Data;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Data
public abstract class Base {
    private UUID id;
    private String createdDate;
    private UUID createdBy;
    private boolean active;



    public Base() {
        this.id = UUID.randomUUID();
        LocalDateTime vaqt = LocalDateTime.now(ZoneId.of("Asia/Tashkent"));
        this.createdDate = vaqt.format(DateTimeFormatter.ofPattern(" EEEE yyyy-MM-dd HH:mm:ss"));
        this.active = true;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Base base = (Base) o;
        return active == base.active && Objects.equals(id, base.id) && Objects.equals(createdDate, base.createdDate) && Objects.equals(createdBy, base.createdBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdDate, createdBy, active);
    }
}
