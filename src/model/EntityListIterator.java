package model;

import java.util.Iterator;
import java.util.Set;

public class EntityListIterator<E extends Entity> implements Iterator<E> {

    private Integer[] indicies;
    private Object[] entities;
    private EntityList<?> entityList;
    private int curIndex = 0;

    public EntityListIterator(Object[] entities, Set<Integer> indicies, EntityList<?> entityList) {
        this.entities = entities;
        this.indicies = indicies.toArray(new Integer[indicies.size()]);
        this.entityList = entityList;
    }

    public boolean hasNext() {
        return indicies.length != curIndex;
    }

    @SuppressWarnings("unchecked")
    public E next() {
        Object temp = entities[indicies[curIndex]];
        curIndex++;
        return (E) temp;
    }

    public void remove() {
        if (curIndex >= 1) {
            entityList.remove(indicies[curIndex - 1]);
        }
    }
}