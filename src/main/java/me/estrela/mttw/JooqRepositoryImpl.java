package me.estrela.mttw;

import org.jooq.DSLContext;
import org.jooq.impl.TableImpl;
import org.jooq.impl.UpdatableRecordImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public abstract class JooqRepositoryImpl<T extends DataTransferObject<R>, R extends UpdatableRecordImpl<R>> {

    @Autowired
    private DSLContext dsl;

    private final TableImpl<R> table;

    public JooqRepositoryImpl(TableImpl<R> table) {
        this.table = table;
    }

    @Transactional
    public long count() {
        return dsl.fetchCount(table);
    }

    @Transactional
    public void save(T t) {
        R record = t.toRecord(dsl);
        record.store();
    }

    @Transactional
    public void deleteAll() {
        dsl.truncate(table).execute();
    }

}
