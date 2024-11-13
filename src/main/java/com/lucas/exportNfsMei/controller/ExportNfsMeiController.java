package com.lucas.exportNfsMei.controller;

import com.lucas.exportNfsMei.model.RequestCookiesNfse;
import com.lucas.exportNfsMei.service.ExportNfsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exportNfs")
public class ExportNfsMeiController {

    @Autowired
    private ExportNfsService exportNfsService;

    @PostMapping
    public ResponseEntity<?> exportNfs(@RequestBody RequestCookiesNfse requestCookiesNfse){
        return  ResponseEntity.ok(exportNfsService.exportNfsToExcel(requestCookiesNfse));
    }
}
