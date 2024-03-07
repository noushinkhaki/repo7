create table if not exists codepole_order
(
   order_id integer not null,
   user_id integer not null,
   order_date date not null,
   primary key(order_id)
);

create table if not exists item
(
   item_id integer not null,
   order_id integer not null,
   item_name varchar(255) not null,
   unit_price integer not null,
   quantity integer not null,
   primary key(item_id),
   foreign key (order_id) references codepole_order(order_id)
);