package api.tools;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lk
 * @Date 2020-07-10 19:26
 * @Description
 */
public class ListUtils {


    /**
     * 根据条件对List进行过滤
     * @param list
     * @param strategy
     * @param <T>
     * @return
     */
    public static <T> List<T> filter(List<T> list, ListFilterStrategy<T> strategy) {
        List<T> rList = new ArrayList<>();
        if(!CommonUtills.collectionIsNull(list)){
            for (int i = 0; i < list.size(); i++) {
                T t =  list.get(i);
                if (strategy.contain(t)){
                    rList.add(t);
                }
            }
        }
        ((ArrayList<T>) rList).trimToSize();//释放申请的多余的空间
        return rList;
    }


    public static interface ListFilterStrategy<T> {
        public boolean contain(T t);
    }
}
