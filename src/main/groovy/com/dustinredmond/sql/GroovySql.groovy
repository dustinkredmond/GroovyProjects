package com.dustinredmond.sql

import groovy.sql.Sql

class GroovySql {

    static void main(String[] args) {
        createATable()
        doASelectStatement()

    }

    static def createATable() {
        def sql = getSql()
        def createDdl = "create table testing (id(11) integer not null);"
        sql.execute(createDdl)
        sql.close()
    }

    static def doASelectStatement() {
        def sql = getSql()
        sql.eachRow("select id from testing") {
            print it[0] // prints the id
        }
        sql.close()
    }

    private static Sql getSql() {
        return Sql.newInstance("jdbc:mysql://localhost:3306/TESTDB",
        "aUser",
        "theirPassword",
        "com.mysql.jdbc.Driver")
    }
}
