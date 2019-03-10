package com.finzy.weathernow.utils;

import java.io.IOException;

public class NoConnectivityException extends IOException {

    @Override
    public String getMessage() {
        return "No connectivity exception encountered. Please check data connectivity.";
    }
}
