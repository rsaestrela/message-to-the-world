package me.estrela.mttw;

import org.jooq.impl.UpdatableRecordImpl;

public interface GenericRepository<T extends DataTransferObject<R>, R extends UpdatableRecordImpl> {

    long count();

    void save(T t);

    void deleteAll();

}
