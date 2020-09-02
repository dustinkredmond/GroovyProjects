package com.dustinredmond.sql

import groovy.sql.Sql

class GroovySql {

    static void main(String[] args) {
        createATable()
        doAnInsertStatement()
        doAnUpdateStatement()
        doASelectStatement()

    }

    static def createATable() {
        def sql = getSql()
        def createDdl = "create table testing (id(11) integer not null);"
        sql.execute(createDdl)
        sql.close()
    }

    static def doAnInsertStatement() {
        def params = [1]
        def sql = getSql()
        sql.executeInsert('insert into testing (id) values (?)', params)
    }

    static def doASelectStatement() {
        def sql = getSql()
        sql.eachRow("select id from testing") {
            print it[0] // prints the id
        }
        sql.close()
    }

    static def doAnUpdateStatement() {
        def sql = getSql()
        def params = ["John", 1]
        sql.executeUpdate("update testing set first_name = ? where id = ?", params)
    }

    private static Sql getSql() {
        return Sql.newInstance("jdbc:mysql://localhost:3306/TESTDB",
        "aUser",
        "theirPassword",
        "com.mysql.jdbc.Driver")
    }
}
