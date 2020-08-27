package com.dustinredmond.time

import groovy.time.TimeCategory

class TimeCategoryUsages {

    static void main(args) {
        Date date = new Date()
        // Use TimeCategory with a closure to enable easy date calc.
        use(TimeCategory) {
            println 2.days.from.today
            println 30.seconds.from.now
            println 30.days.from.now
            println 30.days.ago
            println 3.minutes.ago
            println date + 1.month
            println date + 6.months
            println date - 2.weeks
            date = 1.year.from.now
        }
        println date
    }
}
