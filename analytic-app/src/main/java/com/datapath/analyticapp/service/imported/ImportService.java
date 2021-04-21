package com.datapath.analyticapp.service.imported;

import java.time.LocalDateTime;

public interface ImportService {
    void update();

    LocalDateTime getLastModified();
}
