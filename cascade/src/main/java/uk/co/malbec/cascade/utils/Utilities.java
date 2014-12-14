package uk.co.malbec.cascade.utils;


import java.util.ArrayList;
import java.util.List;

public class Utilities {


    public static <S> S find(Iterable<S> items, Call<Boolean, S> predicate){
        for (S item: items){
            if (predicate.call(item)){
                return item;
            }
        }
        return null;
    }

    public static <S> S find(S[] items, Call<Boolean, S> predicate){
        for (S item: items){
            if (predicate.call(item)){
                return item;
            }
        }
        return null;
    }

    public static <S> List<S> reduce(List<S> items, Call<Boolean, S> predicate){
        List<S> result = new ArrayList<S>();
        for (S item: items){
            if (predicate.call(item)){
                result.add(item);
            }
        }
        return result;
    }

    public interface Call<T,S>{
        public T call(S s);
    }
}
