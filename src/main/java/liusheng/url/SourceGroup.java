package liusheng.url;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 年: 2020  月: 04 日: 14 小时: 09 分钟: 07
 * 用户名: LiuSheng
 */

public class SourceGroup<T> implements Source<T> {
    private List<Source<T>> sources = new ArrayList<>(8);

    @Override
    public T get() {
        for (Source<T> source : sources) {
            T obj = source.get();
            if (Objects.nonNull(obj)) {
                return obj;
            }
        }
        return null;
    }

    public void addSource(Source<T> source) {
        sources.add(source);
    }
}
