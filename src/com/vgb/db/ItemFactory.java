package com.vgb.db;

import com.vgb.*;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Factory for turning a row’s fields into the right Item subclass.
 */
public class ItemFactory {

	/**
	 * Overload for catalog‑only items (no invoice‑line spec).
	 */
	public static Item create(String type, UUID id, String desc, int qty, double baseCost) throws Exception {
		// delegate to the full method with all spec fields = null
		return create(type, id, desc, qty, baseCost, null, // unitPrice
				null, // vendorCompanyId
				null, // hours
				null, // hourlyRate
				null, // startDate
				null, // endDate
				null, // leaseRate
				null, // dailyRate
				null // equipmentSize
		);
	}

	/**
	 * Full method that handles all the invoice‑line spec parameters.
	 */
	public static Item create(String type, UUID id, String desc, int qty, double baseCost, Double unitPrice,
			Integer vendorCompanyId, Double hours, Double hourlyRate, java.sql.Date startDate, java.sql.Date endDate,
			Double leaseRate, Double dailyRate, String equipmentSize) throws Exception {
		switch (type) {
		case "Material": {
	        char typeChar = 'M'; 
	        String name        = desc;
	        String unit        = "each";
	        double quantityVal = qty;
	        double unitCostVal = (unitPrice != null ? unitPrice : baseCost);
	        return new Material(id, typeChar, name, unit, quantityVal, unitCostVal);
	        }
		case "Contract":
			Companies vendor = CompanyDAO.loadById(vendorCompanyId);
			return new Contract(id, 'C', desc, vendor.getCompanyUuid().toString(), baseCost);
		case "Rental": {
			char typeChar = 'R';
			String model = (equipmentSize != null ? equipmentSize : desc);
			double rentalCost = (hourlyRate != null ? hourlyRate : baseCost);
			double hoursVal = (hours != null ? hours : 0.0);
			return new Rental(id, typeChar, desc, model, rentalCost, hoursVal);
			}
		case "Lease": {
			char typeChar = 'L';
			String name = desc;
			String model = desc;
			double leasePriceVal = (leaseRate != null ? leaseRate : baseCost);
			LocalDate start = 	(startDate != null ? startDate.toLocalDate() : null);
			LocalDate end = 	(endDate != null ? endDate.toLocalDate() : null);
			return new Lease(id, typeChar, name, model, leasePriceVal, start, end);
			}
		case "Equipment": {
			char typeChar = 'E';
			String name = desc;
			String model = (equipmentSize != null ? equipmentSize : desc);
			double equipmentPriceVal = (dailyRate != null ? dailyRate : baseCost);
			return new Equipment(id, typeChar, name, model, equipmentPriceVal);
			}
		default:
			throw new IllegalArgumentException("Unknown item type: " + type);
		}
	}
}