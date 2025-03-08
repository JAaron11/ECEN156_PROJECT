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

		//1. Create test instances 3 different types of invoice items
		//   You may reuse the instances from your Entity test suites
		Equipment equip = new Equipment(UUID.randomUUID(), 'E', "Backhoe 3000", "BH30X2", 95125);
		
		Lease lease = new Lease(UUID.randomUUID(), 'L', "Backhoe 3000", "BH30X2", 95125,
				LocalDate.of(2024, 1, 1), LocalDate.of(2026, 6, 1));
		
		Rental rental = new Rental(UUID.randomUUID(), 'R', "Backhoe 3000", "BH30X2", 95125, 25.0);
		
		//2. Create an instance of your invoice and add these 3 items to it
		Invoice invoice = new Invoice();
		invoice.addItems(equip);
		invoice.addItems(lease);
		invoice.addItems(rental);
		
		//3. Calculate and compare the values to the expected values.
		//data values

		double expectedSubtotal = 95125.00 + 69037.29 + 2378.13; 	// = 166540.42
		double expectedTaxTotal = 4994.06 + 1500.00 + 104.16;		// = 6598.22
		double expectedGrandTotal = expectedSubtotal + expectedTaxTotal; // = 173138.64

		//Call your invoice's methods to get these values
		double actualSubtotal = invoice.getSubtotal();
        double actualTaxTotal = invoice.getTaxTotal();
        double actualGrandTotal = invoice.getGrandTotal();

		//Use assertEquals with the TOLERANCE to compare:
		assertEquals(expectedSubtotal, actualSubtotal, TOLERANCE);
		assertEquals(expectedTaxTotal, actualTaxTotal, TOLERANCE);
		assertEquals(expectedGrandTotal, actualGrandTotal, TOLERANCE);
		
		// ensure that the string representation contains necessary elements
		String s = invoice.toString();
		assertTrue(s.contains("Backhoe 3000"));
	}

	/**
	 * Tests the subtotal, tax total and grand total values of an invoice in
	 * the VGB system.
	 */
	@Test
	public void testInvoice02() {
		//1. Create test instances the other 2 types of invoice items
		//   You may reuse the instances from your Entity test suites
		Material material = new Material(UUID.randomUUID(), 'M', "Nails", "Box", 9.99);
		material.setQuantity(31);
		
		Contract contract = new Contract(UUID.randomUUID(), 'C', "foundation pour", "0673a09a-5cc1-4269-88f2-e665c2f3f33c");
		contract.setContractAmount(10500.00);
		
		//2. Create an instance of your invoice and add these 2 items to it
		Invoice invoice = new Invoice();
		invoice.addItems(material);
		invoice.addItems(contract);
		
		//3. Calculate and compare the values to the expected values.
        double expectedSubtotal = 309.69 + 10500.00; // = 10809.69
        double expectedTaxTotal = 22.14 + 0;           // = 22.14
        double expectedGrandTotal = expectedSubtotal + expectedTaxTotal; // = 10831.83

        // Retrieve actual values.
        double actualSubtotal = invoice.getSubtotal();
        double actualTaxTotal = invoice.getTaxTotal();
        double actualGrandTotal = invoice.getGrandTotal();

        // Assert with tolerance.
        assertEquals(expectedSubtotal, actualSubtotal, TOLERANCE);
        assertEquals(expectedTaxTotal, actualTaxTotal, TOLERANCE);
        assertEquals(expectedGrandTotal, actualGrandTotal, TOLERANCE);

        String s = invoice.toString();
        assertTrue(s.contains("Nails"));
        assertTrue(s.contains("foundation pour"));
	}



}
