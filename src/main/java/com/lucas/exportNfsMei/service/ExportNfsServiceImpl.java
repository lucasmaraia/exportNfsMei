package com.lucas.exportNfsMei.service;

import com.lucas.exportNfsMei.model.Invoice;
import com.lucas.exportNfsMei.model.RequestCookiesNfse;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExportNfsServiceImpl implements ExportNfsService {

    private static final String BASE_URL = "https://www.nfse.gov.br/EmissorNacional/Notas/Emitidas";

    public ResponseEntity<?> exportNfs(RequestCookiesNfse requestCookiesNfse){

        Map<String, String> cookies = getCookies(requestCookiesNfse);

        if(!getInvoices(cookies).isEmpty()){
            return ResponseEntity.ok(getInvoices(cookies));
        }

        return new ResponseEntity<>("Notas Fiscais não encontradas por gentileza atualize o cookies",
                HttpStatus.BAD_REQUEST);

    }

    private static boolean hasNextPage(Document doc) {
        Element nextPage = doc.select("ul.pagination li:not(.disabled) a[data-original-title='Próxima']").first();

        return nextPage != null;
    }

    private List<Invoice> getInvoices( Map<String, String> cookies){
        List<Invoice> invoices = new ArrayList<>();

        int pageNumber = 1;
        boolean hasMorePages = true;

        while (hasMorePages) {
            String pageUrl = BASE_URL + "?pg=" + pageNumber;

            Connection connection = Jsoup.connect(pageUrl)
                    .cookies(cookies)
                    .method(Connection.Method.GET);

            Document doc;
            try {
                doc = connection.get();
            } catch (IOException e) {
               return invoices;
            }
            Element table = doc.select("table.table-striped").first();

            if (table != null) {

                Elements rows = table.select("tbody tr");


                for (Element row : rows) {
                    Elements cols = row.select("td");

                    String date = cols.get(0).text();
                    String issuedTo = cols.get(1).text();
                    String city = cols.get(2).text();
                    String value = cols.get(3).text();
                    String tax = cols.get(5).text();

                    Element situationElement = cols.get(4).selectFirst("img");
                    String situation = "approved";

                    if (situationElement != null) {
                        String src = situationElement.attr("src");
                        if (src.contains("cancelada")) situation = "canceled";
                    }

                    Invoice invoice = Invoice.builder()
                            .date(date)
                            .issuedTo(issuedTo)
                            .city(city)
                            .value(value)
                            .situation(situation)
                            .tax(tax)
                            .build();

                    invoices.add(invoice);
                }

                hasMorePages = hasNextPage(doc);
            } else {
                hasMorePages = false;
            }
            pageNumber++;
        }

        return invoices;
    }

    private Map<String, String> getCookies(RequestCookiesNfse requestCookiesNfse) {
        Map<String, String> cookies = new HashMap<>();
        cookies.put("ARRAffinity", requestCookiesNfse.arraffinity());
        cookies.put("ASP.NET_SessionId", requestCookiesNfse.sessionId());
        cookies.put("Emissor", requestCookiesNfse.emissor());
        cookies.put("__RequestVerificationToken_L0VtaXNzb3JOYWNpb25hbA2", requestCookiesNfse.requestVerificationToken());
        return cookies;
    }
}
