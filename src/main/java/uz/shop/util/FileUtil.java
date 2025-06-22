package uz.shop.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.io.File;
import java.util.List;

public class FileUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    public static <T> void write(File file, List<T> list){
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, list);
    }

    @SneakyThrows
    public static <T> List<T> read(File file, Class<T> clazz){
        return objectMapper.readValue(file, objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
    }
}
