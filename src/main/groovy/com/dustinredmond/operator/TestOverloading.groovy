package com.dustinredmond.operator

class TestOverloading {

    static def main(args) {
        def obj1 = new TestObject(5)
        def obj2 = new TestObject(3)
        // calls the overloaded + operator of our TestObject class
        println obj1 + obj2
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
        }

        /*
         * Likewise we overload the - operator
         */
        def minus(someObj) {
            this.val -= someObj.getVal()
        }
    }

}
