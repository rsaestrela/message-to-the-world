package me.estrela.mttw;

import org.jooq.DSLContext;

public interface DataTransferObject<R> {

    R toRecord(DSLContext dsl);

}
