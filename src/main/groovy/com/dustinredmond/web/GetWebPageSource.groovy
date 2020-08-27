package com.dustinredmond.web

class GetWebPageSource {

    static String get(String url) {
        // Yes, that's really all there is to it
        return url.toURL().getText()
    }

}
