package app.dao;

import java.util.List;

public interface IDAO <T,I> {
    T createMovie(T t);
    List<T> getAll();
    T getById(I id);
    T update(T t);
    boolean delete(I id);

}
