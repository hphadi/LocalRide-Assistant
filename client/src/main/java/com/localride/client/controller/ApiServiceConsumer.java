package com.localride.client.controller;

import com.localride.client.service.ApiService;

/**
 * Interface for controllers that need access to the ApiService.
 * This allows ApiService to be passed to dynamically loaded FXML controllers.
 */
public interface ApiServiceConsumer {
    void setApiService(ApiService apiService);
}