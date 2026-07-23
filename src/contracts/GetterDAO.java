package contracts;

import java.util.List;

public interface GetterDAO<K, E> {
    E findById(K key);
    List<E> findAll();
}
