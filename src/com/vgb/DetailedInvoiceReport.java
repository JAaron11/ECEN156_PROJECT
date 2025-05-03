package com.vgb;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * Prints a detailed invoice report, falling back to the company address
 * when the contact person has no address of their own.
 */
public class DetailedInvoiceReport {
    private static final SimpleDateFormat DATE_FMT = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Prints detailed information for each invoice.
     */
    public void printReport(
        List<Invoice> invoices,
        Map<String, Companies> companies,
        Map<String, Person> persons
    ) {
        for (Invoice inv : invoices) {
            // 1) Print the entire invoice in one shot:
            System.out.println(inv.toString());
            System.out.println();  // blank line before details

            // 2) Print company/customer details:
            printCompanyDetails(inv.getCustomer(), companies, persons);
            System.out.println();  // blank line

            // 3) Print salesperson details:
            printSalesPersonDetails(inv.getSalesPerson(), persons);
            System.out.println("\n" + "=".repeat(80) + "\n");
        }
    }

    private void printCompanyDetails(
        String companyUuid,
        Map<String, Companies> companies,
        Map<String, Person> persons
    ) {
        System.out.println("Customer Details:");
        Companies comp = companies.get(companyUuid);
        if (comp == null) {
            System.out.println("  Unknown company UUID: " + companyUuid);
            return;
        }

        System.out.printf("  %s (%s)%n",
            comp.getName(),
            comp.getCompanyUuid());

        // Contact person (may be null)
        Person contact = null;
        if (comp.getContactUuid() != null) {
            contact = persons.get(comp.getContactUuid().toString());
        }
        if (contact != null) {
            System.out.printf("    Contact: %s, %s (%s)%n",
                contact.getLastName(),
                contact.getFirstName(),
                contact.getUuid());
            System.out.printf("      [%s]%n",
                String.join(", ", contact.getEmails()));
        }

        // Pick contact’s address if they have one, otherwise company’s
        Address addr = (contact != null && contact.getAddress() != null)
            ? contact.getAddress()
            : comp.getAddress();

        if (addr != null) {
            System.out.printf("      %s%n", addr.getStreet());
            System.out.printf("      %s %s %s%n",
                addr.getCity(), addr.getState(), addr.getZip());
        } else {
            System.out.println("      (no address on file)");
        }
    }

    private void printSalesPersonDetails(
        String salesPersonUuid,
        Map<String, Person> persons
    ) {
        System.out.println("Sales Person:");
        Person sp = persons.get(salesPersonUuid);
        if (sp == null) {
            System.out.println("  Unknown salesperson UUID: " + salesPersonUuid);
            return;
        }

        System.out.printf("  %s, %s (%s)%n",
            sp.getLastName(),
            sp.getFirstName(),
            sp.getUuid());

        System.out.printf("    [%s]%n",
            String.join(", ", sp.getEmails()));
    }
}
