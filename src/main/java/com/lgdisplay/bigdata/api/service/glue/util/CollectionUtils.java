package com.lgdisplay.bigdata.api.service.glue.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Collection Utility.
 */
public class CollectionUtils {

    public static Collection toCollection(Object... objects) {
        return org.springframework.util.CollectionUtils.arrayToList(objects);
    }

    public static List<String> toList(String... objects) {
        List list = new ArrayList();
        for (String object : objects) {
            list.add(object);
        }
        return list;
    }

}
