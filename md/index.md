[javadoc](doc/javadoc)

[部署文档](doc/1.html)

[接口文档](doc/3.html)

数据库
```
create table devices
(
    uuid        int auto_increment
        primary key,
    password    varchar(32)   default '0'                   not null,
    type        int                                         not null,
    description varchar(128)  default ''                    not null,
    local       varchar(64)   default ''                    not null,
    reserves    varchar(1024) default '{"set":[]}'          not null,
    control     varchar(32)   default '{}'                  not null,
    state       varchar(128)                                null,
    last        timestamp     default '2000-01-01 00:00:00' not null
);

create index devices_local_index
    on devices (local);

create index devices_type_index
    on devices (type);
    
-------------------------

create table users
(
    uuid     int auto_increment
        primary key,
    type     int           default 0            not null,
    account  varchar(32)                        not null,
    password varchar(32)                        not null,
    name     varchar(16)                        null,
    reserves varchar(1024) default '{"set":[]}' not null,
    image    varchar(128)                       null,
    constraint users_account_uindex
        unique (account)
);

create index users_type_index
    on users (type);

```

