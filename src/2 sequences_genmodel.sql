CREATE SEQUENCE BUS_SEQ
    INCREMENT BY 1
    START WITH 0
    MINVALUE 0  -- This will ensure start at 1!
    MAXVALUE 99
    NOCYCLE
    NOCACHE
    ORDER;
CREATE SEQUENCE DRIVE_SEQ
    INCREMENT BY 1
    START WITH 0
    MINVALUE 0  -- This will ensure start at 1!
    MAXVALUE 99
    NOCYCLE
    NOCACHE
    ORDER;
CREATE SEQUENCE SERVICES_BOOK_POSITION_SEQ
    INCREMENT BY 1
    START WITH 0
    MINVALUE 0  -- This will ensure start at 1!
    MAXVALUE 99
    NOCYCLE
    NOCACHE
    ORDER;
CREATE SEQUENCE COURSE_SEQ
INCREMENT BY 1
    START WITH 0
    MINVALUE 0  -- This will ensure start at 1!
    MAXVALUE 99
    NOCYCLE
    NOCACHE
    ORDER;