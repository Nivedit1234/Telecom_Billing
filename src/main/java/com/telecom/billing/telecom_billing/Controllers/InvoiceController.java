package com.telecom.billing.telecom_billing.Controllers;


import com.telecom.billing.telecom_billing.Models.Invoice;
import com.telecom.billing.telecom_billing.Services.InvoiceService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {

    private final InvoiceService service;

    public InvoiceController(InvoiceService service) {
        this.service = service;
    }

    @PostMapping("generateInvoice/{customerId}")
    public Invoice generateInvoice(@PathVariable Long customerId,
                                   @RequestParam String month) {

        YearMonth ym = YearMonth.parse(month.trim());  // remove unwanted spaces/newlines
        LocalDate billingDate = ym.atDay(1);

        return service.generateMonthlyInvoice(customerId, ym);
    }


    @GetMapping("/{customerId}")
    public List<Invoice> getInvoices(@PathVariable Long customerId) {
        return service.getInvoicesForCustomer(customerId);
    }
}
