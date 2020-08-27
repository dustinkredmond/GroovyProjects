package com.dustinredmond.json

import groovy.json.JsonBuilder

class JsonGeneration {

    static void main(args) {
        def builder = new JsonBuilder()

        builder.employees {
            employee {
                firstname 'John'
                lastname 'Smith'
                responsibilities(
                        first: "Math",
                        second: "English",
                        third: "History"
                )
            }
        }

        assert builder.toString().equals("{\"employees\":{\"employee\":{\"firstname\":\"John\",\"lastname\":\"Smith\"," +
                "\"responsibilities\":{\"first\":\"Math\",\"second\":\"English\",\"third\":\"History\"}}}}")
    }
}
