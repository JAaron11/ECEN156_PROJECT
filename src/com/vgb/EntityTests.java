package com.vgb;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.vgb.Lease;

import java.time.LocalDate;
import java.util.UUID;

/**
 * JUnit test suite for VGB invoice system.
 */
public class EntityTests {

	public static final double TOLERANCE = 0.001;

	/**
	 * Creates an instance of a piece of equipment and tests if
	 * its cost and tax calculations are correct.
	 *
	 * TODO: finish implementation
	 */
	@Test
	public void testEquipment() {

		//data values
		UUID uuid = UUID.randomUUID();;
		String name = "Backhoe 3000";
		String model = "BH30X2";
		double cost = 95125.00;

		//1. TODO: Create an instance of equipment with the data values
		Equipment equip = new Equipment(uuid, 'E', name, model, (int) cost);

		//2. Establish the expected cost and tax (rounded to nearest cent)
		double expectedCost = cost;
		double expectedTax = Math.round(cost * 0.0525 * 100.0) / 100.0;

		//3. TODO: Invoke methods to determine the cost/tax:
		double actualCost = equip.getCost(); // For example, a getter that returns cost.
		double actualTax = equip.calculateTax(); // For example, computes cost * 0.0525.

		//4. Use assertEquals with the TOLERANCE to compare:
		assertEquals(expectedCost, actualCost, TOLERANCE);
		assertEquals(expectedTax, actualTax, TOLERANCE);
		
		// ensure that the string representation contains necessary elements
		String s = equip.toString();
		assertTrue(s.contains("Backhoe 3000"));
		assertTrue(s.contains("BH30X2"));
		// Format the cost to two decimal places and check its string representation.
		assertTrue(s.contains(String.format("%.2f", cost)));

	}

	@Test
	public void testLease() {
		UUID uuid = UUID.randomUUID();
		String name = "Backhoe 3000";
		String model = "BH30X2";
		int cost = 95125;
		LocalDate startDate = LocalDate.of(2024,  1, 1);
		LocalDate endDate = LocalDate.of(2026, 6, 1);
		
		// Creates a Lease instance.
		Lease lease = new Lease(uuid, 'L', name, model, cost, startDate, endDate);
		
		double expectedTotalCost = 70537.29;
		double actualTotalCost = lease.calculateTotalCost();
		assertEquals(expectedTotalCost, actualTotalCost, TOLERANCE);
		
		String s = lease.toString();
		assertTrue(s.contains("2024-01-01"));
		assertTrue(s.contains("2026-06-01"));
	}

	@Test
	public void testRental() {
		UUID uuid = UUID.randomUUID();
		String name = "Backhoe 3000";
		String model = "BH30X2";
		int cost = 95125;
		double hours = 25.0;
		
		// Create a Rental instance.
		Rental rental = new Rental(uuid, 'R', name, model, cost, hours);
		
		double expectedTotalCost = 2482.29;
        double actualTotalCost = rental.calculateTotalCost();
        assertEquals(expectedTotalCost, actualTotalCost, TOLERANCE);
        
        // Verify that the string representation includes key details.
        String s = rental.toString();
        assertTrue(s.contains("Backhoe 3000"));
        assertTrue(s.contains("BH30X2"));
    }


	@Test
	public void testMaterial() {
		UUID uuid = UUID.randomUUID();
        String name = "Nails";
        String packaging = "Box";
        double unitPrice = 9.99;
        int quantity = 31;
        
        // Create a Material instance.
        Material material = new Material(uuid, 'M', name, packaging, unitPrice);
        material.setQuantity(quantity);
        
        double expectedTotalCost = 331.83;
        double actualTotalCost = material.calculateTotalCost();
        assertEquals(expectedTotalCost, actualTotalCost, TOLERANCE);
        
        // Verify that the string representation contains key details.
        String s = material.toString();
        assertTrue(s.contains("Nails"));
        assertTrue(s.contains("Box"));
        assertTrue(s.contains(String.format("%.2f", unitPrice)));
    }


	@Test
	public void testContract() {
		UUID uuid = UUID.randomUUID();
        String name = "foundation pour";
        String subcontractorUUID = "0673a09a-5cc1-4269-88f2-e665c2f3f33c";
        double contractAmount = 10500.00;
        
        // Create a Contract instance.
        Contract contract = new Contract(uuid, 'C', name, subcontractorUUID);
        // Set the contract amount via a setter method.
        contract.setContractAmount(contractAmount);
        
        double expectedTotalCost = 10500.00;
        double actualTotalCost = contract.calculateTotalCost();
        assertEquals(expectedTotalCost, actualTotalCost, TOLERANCE);
        
        // Check that the string representation contains the subcontractor's UUID and description.
        String s = contract.toString();
        assertTrue(s.contains("foundation pour"));
        assertTrue(s.contains(subcontractorUUID));
    }
}