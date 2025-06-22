package uz.shop.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.File;
import java.util.List;

public class FileUtil<T> {
    private static final ObjectMapper objectMapper =new ObjectMapper();

    @SneakyThrows
    public static <T> void write(File file, List<T> t){
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(file,t);
    }

    @SneakyThrows
    public static <T> List<T> read(File file){
        return objectMapper.readValue( file, new TypeReference<>() {});
    }
}
