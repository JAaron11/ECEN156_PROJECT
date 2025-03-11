package com.vgb;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.Test;

/**
 * JUnit test suite for VGB invoice system.
 */
public class InvoiceTests {

	public static final double TOLERANCE = 0.001;


	/**
	 * Tests the subtotal, tax total and grand total values of an invoice in
	 * the VGB system.
	 */
	@Test
	public void testInvoice01() {

		Equipment equip = new Equipment(UUID.randomUUID(), 'E', "Dump Truck", "DT730", 120455);
		
		Lease lease = new Lease(UUID.randomUUID(), 'L', "Dump Truck", "DT730", 120455,
				LocalDate.of(2024, 1, 1), LocalDate.of(2026, 6, 1));
		
		Rental rental = new Rental(UUID.randomUUID(), 'R', "Dump Truck", "DT730", 120455, 25.0);
		
		// Creates 3 instances of invoices
		Invoice invoice = new Invoice();
		invoice.addItems(equip);
		invoice.addItems(lease);
		invoice.addItems(rental);
		
		// Calculates and compares the values to the expected values

		double expectedSubtotal = 120455.00 + 87420.63 + 3011.38; 	// = 210948.72
		double expectedTaxTotal = 6323.74 + 1500.00 + 132.05;		// = 7955.42
		double expectedGrandTotal = expectedSubtotal + expectedTaxTotal; // = 218904.14

		double actualSubtotal = invoice.getSubtotal();
        double actualTaxTotal = invoice.getTaxTotal();
        double actualGrandTotal = invoice.getGrandTotal();

		//Uses assertEquals with the TOLERANCE to compare:
		assertEquals(expectedSubtotal, actualSubtotal, TOLERANCE);
		assertEquals(expectedTaxTotal, actualTaxTotal, TOLERANCE);
		assertEquals(expectedGrandTotal, actualGrandTotal, TOLERANCE);
	
		String s = invoice.toString();
		assertTrue(s.contains("Dump Truck"));
	}

	/**
	 * Tests the subtotal, tax total and grand total values of an invoice in
	 * the VGB system.
	 */
	@Test
	public void testInvoice02() {

		Material material = new Material(UUID.randomUUID(), 'M', "Nails", "Box", 9.99);
		material.setQuantity(31);
		
		Contract contract = new Contract(UUID.randomUUID(), 'C', "foundation pour", "0673a09a-5cc1-4269-88f2-e665c2f3f33c");
		contract.setContractAmount(10500.00);
		
		Invoice invoice = new Invoice();
		invoice.addItems(material);
		invoice.addItems(contract);
		
		// Calculates and compares the values to the expected values.
        double expectedSubtotal = 309.69 + 10500.00; // = 10809.69
        double expectedTaxTotal = 22.14 + 0;           // = 22.14
        double expectedGrandTotal = expectedSubtotal + expectedTaxTotal; // = 10831.83

        // Retrieves actual values.
        double actualSubtotal = invoice.getSubtotal();
        double actualTaxTotal = invoice.getTaxTotal();
        double actualGrandTotal = invoice.getGrandTotal();

        assertEquals(expectedSubtotal, actualSubtotal, TOLERANCE);
        assertEquals(expectedTaxTotal, actualTaxTotal, TOLERANCE);
        assertEquals(expectedGrandTotal, actualGrandTotal, TOLERANCE);

        String s = invoice.toString();
        assertTrue(s.contains("Nails"));
        assertTrue(s.contains("foundation pour"));
	}



}
