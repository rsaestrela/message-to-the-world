CREATE TABLE public.MESSAGE (
    ID varchar(255) NOT NULL,
    EVENT_ID varchar(255) NOT NULL,
    TEXT varchar(255) NOT NULL,
    AUTHOR varchar(32) NOT NULL,
    UP_VOTES integer default 0,
    DOWN_VOTES integer default 0,
    PUBLISHED_DATE timestamp without time zone,
    PRESENTED_DATE timestamp without time zone,
CONSTRAINT message_pkey PRIMARY KEY (ID)
)