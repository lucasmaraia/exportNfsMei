package com.lucas.exportNfsMei.service;

import com.lucas.exportNfsMei.model.RequestCookiesNfse;
import org.springframework.http.ResponseEntity;

public interface ExportNfsService {

    ResponseEntity<?> exportNfs(RequestCookiesNfse requestCookiesNfse);
}
