package vizardalpha.songsofspirit.util;

import snake2d.util.sets.LIST;

import java.util.ArrayList;
import java.util.List;

public class Mapper {
    public static <T> List<T> toJavaList(LIST<T> gameLIST) {
        List<T> races = new ArrayList<>();

        for (T race : gameLIST) {
            races.add(race);
        }

        return races;
    }
}
