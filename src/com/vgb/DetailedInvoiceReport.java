package com.vgb;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

public class DetailedInvoiceReport {
    /**
     * Prints detailed information for each invoice.
     * @param invoices List of Invoice objects.
     * @param companies Map of company UUID (as String) to Company object.
     * @param persons Map of person UUID (as String) to Person object.
     */
    public void printReport(List<Invoice> invoices, Map<String, Companies> companies, Map<String, Person> persons) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (Invoice inv : invoices) {
            System.out.println("Invoice# " + inv.getInvoiceId());
            System.out.println("Date     " + sdf.format(inv.getDate()));
            System.out.println("Customer:");
            // Look up company info using the customer UUID stored in the invoice.
            String companyUUID = inv.getCustomer();
            Companies comp = companies.get(companyUUID);
            if (comp != null) {
                System.out.println(comp.getName() + " (" + comp.getCompanyUuid() + ")");
                // Look up the contact person for the company.
                Person contact = persons.get(comp.getContactUuid().toString());
                if (contact != null) {
                    System.out.println(contact.getLastName() + ", " + contact.getFirstName() + " (" + contact.getUuid() + ")");
                    System.out.println("\t[" + String.join(", ", contact.getEmails()) + "]");
                }
                System.out.println();
                System.out.println("\t" + comp.getAddress().getStreet());
                System.out.println("\t" + comp.getAddress().getCity() + " " + comp.getAddress().getState() + " " + comp.getAddress().getZip());
            } else {
                System.out.println(companyUUID);
            }
            
            System.out.println("Sales Person:");
            // Look up the sales person info using the sales person UUID stored in the invoice.
            String salesPersonUUID = inv.getSalesPerson();
            Person salesPerson = persons.get(salesPersonUUID);
            if (salesPerson != null) {
                System.out.println(salesPerson.getLastName() + ", " + salesPerson.getFirstName() 
                        + " (" + salesPerson.getUuid() + ")");
                System.out.println("\t[" + String.join(", ", salesPerson.getEmails()) + "]");
            } else {
                System.out.println(salesPersonUUID);
            }

            List<Item> items = inv.getItems();
            System.out.printf("Items (%d)                                                            Tax       Total%n", items.size());
            System.out.println("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-                          -=-=-=-=-=- -=-=-=-=-=-");
            for (Item item : items) {
                // Each concrete Item should override toString() to format its details.
                System.out.println(item.toString());
                System.out.printf("                                                              $%10.2f $%10.2f%n", item.getTaxTotal(), item.getSubTotal());
            }
            System.out.println("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-                          -=-=-=-=-=- -=-=-=-=-=-");
            System.out.printf("                                                   Subtotals $%10.2f $%10.2f%n",
                    inv.getTaxTotal(), inv.getGrandTotal() - inv.getTaxTotal());
            System.out.printf("                                                 Grand Total             $%10.2f%n",
                    inv.getGrandTotal());
            System.out.println();
        }
    }
}