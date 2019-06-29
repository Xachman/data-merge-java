# Database Merge


## How it works

This project compares rows on a relational database and inserts and updates data on the first provided database with the data on the second provided database.  A list of actions either to insert or update and compiled. Then these actions will be committed on the first provided database.

## Options

~~~
usage: database-merge
    --actions                       Print out all actions that would be
                                    taken
    --config <arg>                  Yaml config file
 -d1,--database-1-name <arg>        First database name
 -d1h,--database-1-host <arg>       First database hostname or ip of host
 -d1p,--database-1-password <arg>   First database password
 -d1u,--database-1-username <arg>   First database username
 -d2,--database-2-name <arg>        Second database name
 -d2h,--database-2-host <arg>       Second database hostname or ip of host
 -d2p,--database-2-password <arg>   Second database password
 -d2u,--database-2-username <arg>   Second database username
 -t,--type <arg>                    Type of connection ex: mysql
    --table <arg>                   Table to be merged

~~~


## Config

Database Merge tries to find relations using primary and foreign keys.  However when these are not provided you can supply a config file with the config option.

Example:
~~~ yaml
tables:
  - name: postsmeta
    relationships:
      - parent:
          column: id
          name: posts
        column: post_id
  - name: users_meta
    relationships:
      - parent:
          column: id
          name: users
        column: user_id
  - name: category_v_posts
    relationships:
      - parent:
          column: id
          name: posts
        column: post_id
      - parent:
          column: cat_id
          name: categories
        column: category_id
~~~
