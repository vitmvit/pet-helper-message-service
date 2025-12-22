package by.vitikova.discovery.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StringUtils {

    public boolean isEmpty(CharSequence line) {
        return line == null || line.isEmpty();
    }
}