package com.dustinredmond.operator

class TestOverloading {

    static def main(args) {
        def obj1 = new TestObject(5)
        def obj2 = new TestObject(3)
        // calls the overloaded + operator of our TestObject class
        obj1 + obj2
        obj1 - obj2
    }

    static class TestObject {
        def val
        TestObject(int val) {
            this.val = val
        }
        int getVal() {
            return val
        }
        /*
         * Here we overload the + operator
         */
        def plus(someObj) {
            this.val += someObj.getVal()
            println "Adding ${someObj.getVal()} to ${this.val}"
        }

        /*
         * Likewise we overload the - operator
         */
        def minus(someObj) {
            this.val -= someObj.getVal()
            println "Subtracting ${someObj.getVal()} from ${this.val}"
        }

        // overload * (multiplication)
        def multiply(someObj) {
            this.val *= someObj.getVal()
            println "Multiplying ${someObj.getVal()} by ${this.val}"
        }

        // overload / (division)
        def div(someObj) {
            this.val /= someObj.getVal()
            println "Dividing ${someObj.getVal()} by ${this.val}"
        }

        // overload %
        def mod(someObj) {
            println "Overloaded %"
        }

        // overload **
        def power(someObj) {
            println "Overloaded **"
        }

        // overload |
        def or(someObj) {
            
        }

        // overload &
        def and(someObj) {}

        // overload ^
        def xor(someObj) {}

        // overload as (eg. thisObj as String)
        def asType(someObj) {}

        // overload obj()
        def call() {}

        // overload obj[?]
        def getAt(someIndex) {}

        // overload obj[?] = someVal
        def putAt(someIndex, someVal) {}

        // overload in (e.g. someObj in someOtherObj)
        def isCase(someObj) {}

        // overload <<
        def leftShift(someObj) {}

        // overload >>
        def rightShift(someObj) {}

        // overload ++
        def next() {}

        // overload --
        def previous() {}

        // overload +someObj
        def positive() {}

        // overload -someObj
        def negative() {}

        // overload ~
        def bitwiseNegate() {}
    }

}
