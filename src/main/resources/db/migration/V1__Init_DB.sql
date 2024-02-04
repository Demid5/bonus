create table cards (
    balance decimal(38,2) not null,
    id bigint not null auto_increment,
    client_name varchar(255) not null,
    number varchar(255) not null,
    primary key (id))  ;

create table transactions (
    amount decimal(38,2) not null,
    card_id bigint,
    id bigint not null auto_increment,
    timestamp datetime(6) not null,
    status enum ('COMPLETED','CANCELLED') not null,
    type enum ('CREDIT','DEBIT','REFUND') not null,
    primary key (id))      ;

alter table cards
    add constraint cards_fk unique (number)       ;

alter table transactions
    add constraint transactions_cards_fk foreign key (card_id) references cards (id)   ;