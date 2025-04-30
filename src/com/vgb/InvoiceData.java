package com.vgb;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.UUID;

import com.vgb.db.DatabaseUtils;

/**
 * This is a collection of utility methods that define a general API for
 * interacting with the database supporting this application.
 *
 */
public class InvoiceData {

	/**
	 * Removes all records from all tables in the database.
	 */
	public static void clearDatabase() {
	    String[] tables = {
	        // subtype specs
	        "LeaseSpec",
	        "RentalSpec",
	        "MaterialSpec",
	        "ContractSpec",
	        "EquipmentSpec",

	        // join table
	        "InvoiceLine",

	        // header
	        "Invoice",

	        // catalog
	        "Item",

	        // email
	        "Email",

	        // company before person (because Person → Company.primary_contact_id)
	        "Company",

	        // person
	        "Person",

	        // addresses and lookups
	        "Address",
	        "ZipCode",
	        "State"
	    };

		try (Connection conn = DatabaseUtils.getConnection()) {
	        conn.setAutoCommit(false);
	        try (Statement stmt = conn.createStatement()) {
	            for (String table : tables) {
	                stmt.executeUpdate("delete from " + table);
	            }
	            conn.commit();
	        } catch (SQLException ex) {
	            // rollback if any delete fails
	            try {
	                conn.rollback();
	            } catch (SQLException rbEx) {
	                // log or wrap rollback failure too
	                throw new RuntimeException("Failed to rollback after clearDatabase error", rbEx);
	            }
	            throw new RuntimeException("Failed to clear database", ex);
	        }
	    } catch (SQLException ex) {
	        // catches getConnection() and setAutoCommit() failures
	        throw new RuntimeException("Failed to clear database", ex);
	    }
	}

	/**
	 * Method to add a person record to the database with the provided data.
	 *
	 * @param personUuid
	 * @param firstName
	 * @param lastName
	 * @param phone
	 */
	public static void addPerson(UUID personUuid, String firstName, String lastName, String phone) {
		final String SQL = "insert into Person (person_uuid, first_name, last_name, phone) values (?, ?, ?, ?)";

		try (Connection conn = DatabaseUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(SQL)) {
			ps.setString(1, personUuid.toString());
			ps.setString(2, firstName);
			ps.setString(3, lastName);
			ps.setString(4, phone);
			ps.executeUpdate();
		} catch (SQLException ex) {
			throw new RuntimeException("Failed to add person: " + personUuid, ex);
		}
	}

	/**
	 * Adds an email record corresponding person record corresponding to the
	 * provided <code>personUuid</code>
	 *
	 * @param personUuid
	 * @param email
	 */
	public static void addEmail(UUID personUuid, String email) {
		final String FIND_PERSON_SQL =
		        "select person_id from Person where person_uuid = ?";
		    final String INSERT_EMAIL_SQL =
		        "insert into Email (email_uuid, person_id, email_address) values (?, ?, ?)";

		    try (Connection conn = DatabaseUtils.getConnection()) {
		        // 1) lookup the surrogate PK
		        int personId;
		        try (PreparedStatement findPs = conn.prepareStatement(FIND_PERSON_SQL)) {
		            findPs.setString(1, personUuid.toString());
		            try (ResultSet rs = findPs.executeQuery()) {
		                if (!rs.next()) {
		                    throw new RuntimeException("No Person found for UUID " + personUuid);
		                }
		                personId = rs.getInt("person_id");
		            }
		        }

		        // 2) insert email with generated UUID
		        try (PreparedStatement insertPs = conn.prepareStatement(INSERT_EMAIL_SQL)) {
		            insertPs.setString(1, UUID.randomUUID().toString());
		            insertPs.setInt   (2, personId);
		            insertPs.setString(3, email);
		            insertPs.executeUpdate();
		        }

		    } catch (SQLException ex) {
		        throw new RuntimeException("Failed to add email for: " + personUuid, ex);
		    }
		}

	/**
	 * Adds a company record to the database with the primary contact person
	 * identified by the given code.
	 *
	 * @param companyUuid
	 * @param name
	 * @param contactUuid
	 * @param street
	 * @param city
	 * @param state
	 * @param zip
	 */
	public static void addCompany(UUID companyUuid, UUID contactUuid, String name, String street, String city,
			String state, String zip) {
		final String SQL = "insert into Company (company_uuid, contact_uuid, name, street, city, state, zip) values (?, ?, ?, ?, ?, ?, ?)";

		try (Connection conn = DatabaseUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(SQL)) {
			ps.setString(1, companyUuid.toString());
			ps.setString(2, contactUuid.toString());
			ps.setString(3, name);
			ps.setString(4, street);
			ps.setString(5, city);
			ps.setString(6, state);
			ps.setString(7, zip);
			ps.executeUpdate();
		} catch (SQLException ex) {
			throw new RuntimeException("Failed to add company: " + companyUuid, ex);
		}
	}

	/**
	 * Adds an equipment record to the database of the given values.
	 *
	 * @param equipmentUuid
	 * @param name
	 * @param modelNumber
	 * @param retailPrice
	 */
	public static void addEquipment(UUID equipmentUuid, String name, String modelNumber, double retailPrice) {
		final String SQL = "insert into Equipment (equipment_uuid, name, model_number, retail_price) values (?, ?, ?, ?)";

		try (Connection conn = DatabaseUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(SQL)) {
			ps.setString(1, equipmentUuid.toString());
			ps.setString(2, name);
			ps.setString(3, modelNumber);
			ps.setDouble(4, retailPrice);
			ps.executeUpdate();
		} catch (SQLException ex) {
			throw new RuntimeException("Failed to add equipment " + equipmentUuid, ex);
		}
	}

	/**
	 * Adds an material record to the database of the given values.
	 *
	 * @param materialUuid
	 * @param name
	 * @param unit
	 * @param pricePerUnit
	 */
	public static void addMaterial(UUID materialUuid, String name, String unit, double pricePerUnit) {
		final String SQL = "insert into Material (material_uuid, name, unit, price_per_unit) values (?, ?, ?, ?)";

		try (Connection conn = DatabaseUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(SQL)) {
			ps.setString(1, materialUuid.toString());
			ps.setString(2, name);
			ps.setString(3, unit);
			ps.setDouble(4, pricePerUnit);
			ps.executeUpdate();
		} catch (SQLException ex) {
			throw new RuntimeException("Failed to add material " + materialUuid, ex);
		}
	}

	/**
	 * Adds an contract record to the database of the given values.
	 *
	 * @param contractUuid
	 * @param name
	 * @param unit
	 * @param pricePerUnit
	 */
	public static void addContract(UUID contractUuid, String name, UUID servicerUuid) {
		final String SQL = "insert into Contract (contract_uuid, name, servicer_uuid) values (?, ?, ?)";

		try (Connection conn = DatabaseUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(SQL)) {
			ps.setString(1, contractUuid.toString());
			ps.setString(2, name);
			ps.setString(3, servicerUuid.toString());
			ps.executeUpdate();
		} catch (SQLException ex) {
			throw new RuntimeException("Failed to add contract " + contractUuid, ex);
		}
	}

	/**
	 * Adds an Invoice record to the database with the given data.
	 *
	 * @param invoiceUuid
	 * @param customerUuid
	 * @param salesPersonUuid
	 * @param date
	 */
	public static void addInvoice(UUID invoiceUuid, UUID customerUuid, UUID salesPersonUuid, LocalDate date) {
		final String SQL = "insert into Invoice (invoice_uuid, customer_uuid, salesperson_uuid, invoice_date) values (?, ?, ?, ?)";

		try (Connection conn = DatabaseUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(SQL)) {
			ps.setString(1, invoiceUuid.toString());
			ps.setString(2, customerUuid.toString());
			ps.setString(3, salesPersonUuid.toString());
			ps.setDate(4, Date.valueOf(date));
			ps.executeUpdate();
		} catch (SQLException ex) {
			throw new RuntimeException("Failed to add invoice " + invoiceUuid, ex);
		}

	}

	/**
	 * Adds an Equipment purchase record to the given invoice.
	 *
	 * @param invoiceUuid
	 * @param itemUuid
	 */
	public static void addEquipmentPurchaseToInvoice(UUID invoiceUuid, UUID itemUuid) {
		final String SQL = "insert into EquipmentPurchase (invoice_uuid, equipment_uuid) values (?, ?)";

		try (Connection conn = DatabaseUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(SQL)) {
			ps.setString(1, invoiceUuid.toString());
			ps.setString(2, itemUuid.toString());
			ps.executeUpdate();
		} catch (SQLException ex) {
			throw new RuntimeException("Failed to add equipment purchase to invoice " + invoiceUuid, ex);
		}
	}

	/**
	 * Adds an Equipment lease record to the given invoice.
	 *
	 * @param invoiceUuid
	 * @param itemUuid
	 * @param start
	 * @param end
	 */
	public static void addEquipmentLeaseToInvoice(UUID invoiceUuid, UUID itemUuid, LocalDate start, LocalDate end) {
		final String SQL = "insert into EquipmentLease (invoice_uuid, equipment_uuid, lease_start, lease_end) values (?, ?, ?, ?)";

		try (Connection conn = DatabaseUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(SQL)) {
			ps.setString(1, invoiceUuid.toString());
			ps.setString(2, itemUuid.toString());
			ps.setDate(3, Date.valueOf(start));
			ps.setDate(4, Date.valueOf(end));
			ps.executeUpdate();
		} catch (SQLException ex) {
			throw new RuntimeException("Failed to add equipment lease to invoice " + invoiceUuid, ex);
		}
	}

	/**
	 * Adds an Equipment rental record to the given invoice.
	 *
	 * @param invoiceUuid
	 * @param itemUuid
	 * @param numberOfHours
	 */
	public static void addEquipmentRentalToInvoice(UUID invoiceUuid, UUID itemUuid, double numberOfHours) {
		final String SQL = "insert into EquipmentRental (invoice_uuid, equipment_uuid, hours_rented) values (?, ?, ?)";

		try (Connection conn = DatabaseUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(SQL)) {
			ps.setString(1, invoiceUuid.toString());
			ps.setString(2, itemUuid.toString());
			ps.setDouble(3, numberOfHours);
			ps.executeUpdate();
		} catch (SQLException ex) {
			throw new RuntimeException("Failed to add equipment rental to invoice " + invoiceUuid, ex);
		}
	}

	/**
	 * Adds a material record to the given invoice.
	 *
	 * @param invoiceUuid
	 * @param itemUuid
	 * @param numberOfUnits
	 */
	public static void addMaterialToInvoice(UUID invoiceUuid, UUID itemUuid, int numberOfUnits) {
		final String SQL = "insert into MaterialPurchase (invoice_uuid, material_uuid, quantity) values (?, ?, ?)";

		try (Connection conn = DatabaseUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(SQL)) {
			ps.setString(1, invoiceUuid.toString());
			ps.setString(2, itemUuid.toString());
			ps.setInt(3, numberOfUnits);
			ps.executeUpdate();
		} catch (SQLException ex) {
			throw new RuntimeException("Failed to add material to invoice " + invoiceUuid, ex);
		}
	}

	/**
	 * Adds a contract record to the given invoice.
	 *
	 * @param invoiceUuid
	 * @param itemUuid
	 * @param amount
	 */
	public static void addContractToInvoice(UUID invoiceUuid, UUID itemUuid, double amount) {
		final String SQL =
	            "insert into ContractPurchase (invoice_uuid, contract_uuid, amount) values (?, ?, ?)";

	        try (Connection conn = DatabaseUtils.getConnection();
	             PreparedStatement ps = conn.prepareStatement(SQL)) {
	            ps.setString(1, invoiceUuid.toString());
	            ps.setString(2, itemUuid.toString());
	            ps.setDouble(3, amount);
	            ps.executeUpdate();
	        } catch (SQLException ex) {
	            throw new RuntimeException("Failed to add contract to invoice " + invoiceUuid, ex);
	        }

	}

}
