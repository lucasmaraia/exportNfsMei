package com.lucas.exportNfsMei.service;

import com.lucas.exportNfsMei.model.Invoice;
import com.lucas.exportNfsMei.model.RequestCookiesNfse;

import java.util.List;

public interface ExportNfsService {

    List<Invoice> exportNfsToExcel(RequestCookiesNfse requestCookiesNfse);
}
