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
	        "LeaseSpec", "RentalSpec", "MaterialSpec", "ContractSpec",
	        "EquipmentSpec", "InvoiceLine", "Invoice", "Item",
	        "Email", "Company", "Person", "Address", "ZipCode", "State"
	    };

	    try (Connection conn = DatabaseUtils.getConnection()) {
	        conn.setAutoCommit(false);

	        // Use Statement, not PreparedStatement
	        try (Statement stmt = conn.createStatement()) {
	            for (String table : tables) {
	                stmt.executeUpdate("DELETE FROM " + table);
	            }
	            conn.commit();
	        } catch (SQLException ex) {
	            conn.rollback();
	            throw new RuntimeException("Failed to clear database", ex);
	        }
	    } catch (SQLException ex) {
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
		final String INSERT_SQL = "INSERT INTO Person (person_uuid, first_name, last_name, phone) VALUES (?, ?, ?, ?)";
		final String LINK_ADDRESS_SQL = "UPDATE Person p " + "  JOIN Address a ON a.address_uuid = p.person_uuid "
				+ "SET p.address_id = a.address_id " + "WHERE p.person_uuid = ?";

		try (Connection conn = DatabaseUtils.getConnection()) {
			conn.setAutoCommit(false);

			// 1) Insert the person row
			try (PreparedStatement ps = conn.prepareStatement(INSERT_SQL)) {
				ps.setString(1, personUuid.toString());
				ps.setString(2, firstName);
				ps.setString(3, lastName);
				ps.setString(4, phone);
				ps.executeUpdate();
			}

			conn.commit();
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

		if (email == null || email.trim().isEmpty()) {
			return;
		}

		final String FIND_PERSON_SQL = "select person_id from Person where person_uuid = ?";
		final String INSERT_EMAIL_SQL = "insert into Email (email_uuid, person_id, email_address) values (?, ?, ?)";

		try (Connection conn = DatabaseUtils.getConnection()) {
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

			try (PreparedStatement insertPs = conn.prepareStatement(INSERT_EMAIL_SQL)) {
				insertPs.setString(1, UUID.randomUUID().toString());
				insertPs.setInt(2, personId);
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
		final String INSERT_ADDRESS_SQL = "INSERT INTO Address " + "(address_uuid, street, city, state_id, zip_id) "
				+ "VALUES (?, ?, ?, " + "(SELECT state_id FROM State WHERE state_code = ?), "
				+ "(SELECT zip_id   FROM ZipCode WHERE zip_code = ?))";

		final String INSERT_COMPANY_SQL = "INSERT INTO Company "
				+ "(company_uuid, company_name, company_type, primary_contact_id, address_id) "
				+ "VALUES (?, ?, ?, ?, ?)";

		try (Connection conn = DatabaseUtils.getConnection()) {
			conn.setAutoCommit(false);

			Integer addressId = null;
			if (state != null && !state.isBlank() && zip != null && !zip.isBlank()) {
				try (PreparedStatement addrPs = conn.prepareStatement(INSERT_ADDRESS_SQL,
						Statement.RETURN_GENERATED_KEYS)) {
					addrPs.setString(1, UUID.randomUUID().toString());
					addrPs.setString(2, street);
					addrPs.setString(3, city);
					addrPs.setString(4, state);
					addrPs.setString(5, zip);
					int rows = addrPs.executeUpdate();

					if (rows > 0) {
						try (ResultSet keys = addrPs.getGeneratedKeys()) {
							if (keys.next()) {
								addressId = keys.getInt(1);
							}
						}
					}
				} catch (SQLException e) {
					if (state == null || state.isBlank() || zip == null || zip.isBlank()) {
						throw new IllegalArgumentException(
								"Company " + companyUuid + " requires both state and zip code");
					}
				}
			}

			int personId;
			try (PreparedStatement findPs = conn
					.prepareStatement("SELECT person_id FROM Person WHERE person_uuid = ?")) {

				findPs.setString(1, contactUuid.toString());
				try (ResultSet rs = findPs.executeQuery()) {
					if (!rs.next()) {
						throw new RuntimeException("No Person found for UUID " + contactUuid);
					}
					personId = rs.getInt("person_id");
				}
			}

			try (PreparedStatement compPs = conn.prepareStatement(INSERT_COMPANY_SQL)) {
				compPs.setString(1, companyUuid.toString());
				compPs.setString(2, name);
				compPs.setString(3, "Client");
				compPs.setInt(4, personId);
				if (addressId != null) {
					compPs.setInt(5, addressId);
				} else {
					compPs.setNull(5, java.sql.Types.INTEGER);
				}
				compPs.executeUpdate();
			}

			conn.commit();
		} catch (SQLException ex) {
			throw new RuntimeException("Failed to add company " + companyUuid, ex);
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
		final String SQL = "insert into Item (item_uuid, item_type, description, base_cost) values (?, 'Equipment', ?, ?)";

		try (Connection conn = DatabaseUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(SQL)) {
			ps.setString(1, equipmentUuid.toString());
			ps.setString(2, name + " (model " + modelNumber + ")");
			ps.setDouble(3, retailPrice);
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
		final String SQL = "INSERT INTO Item (item_uuid, item_type, description, base_cost) "
				+ "VALUES (?, 'Material', ?, ?)";

		try (Connection conn = DatabaseUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(SQL)) {
			ps.setString(1, materialUuid.toString());
			ps.setString(2, name + " (" + unit + ")");
			ps.setDouble(3, pricePerUnit);
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
		final String SQL = "INSERT INTO Item (item_uuid, item_type, description, base_cost) "
				+ "VALUES (?, 'Contract', ?, 0.00)";

		try (Connection conn = DatabaseUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(SQL)) {
			ps.setString(1, contractUuid.toString());
			ps.setString(2, name + " (servicer " + servicerUuid + ")");
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
		final String FIND_COMPANY_SQL = "SELECT company_id FROM Company WHERE company_uuid = ?";
		final String FIND_PERSON_SQL = "SELECT person_id FROM Person  WHERE person_uuid  = ?";
		final String INSERT_SQL = "INSERT INTO Invoice "
				+ "(invoice_uuid, customer_company_id, salesperson_id, invoice_date) " + "VALUES (?, ?, ?, ?)";

		try (Connection conn = DatabaseUtils.getConnection()) {
			int companyId;
			try (PreparedStatement ps = conn.prepareStatement(FIND_COMPANY_SQL)) {
				ps.setString(1, customerUuid.toString());
				try (ResultSet rs = ps.executeQuery()) {
					if (!rs.next()) {
						throw new RuntimeException("No Company for " + customerUuid);
					}
					companyId = rs.getInt("company_id");
				}
			}

			int personId;
			try (PreparedStatement ps = conn.prepareStatement(FIND_PERSON_SQL)) {
				ps.setString(1, salesPersonUuid.toString());
				try (ResultSet rs = ps.executeQuery()) {
					if (!rs.next())
						throw new RuntimeException("No Person for " + salesPersonUuid);
					personId = rs.getInt("person_id");
				}
			}

			try (PreparedStatement ps = conn.prepareStatement(INSERT_SQL)) {
				ps.setString(1, invoiceUuid.toString());
				ps.setInt(2, companyId);
				ps.setInt(3, personId);
				ps.setDate(4, Date.valueOf(date));
				ps.executeUpdate();
			}

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
		final String SQL = "INSERT INTO InvoiceLine (line_uuid, invoice_id, item_id) " + "VALUES (?, "
				+ "(SELECT invoice_id FROM Invoice WHERE invoice_uuid = ?), "
				+ "(SELECT item_id    FROM Item    WHERE item_uuid    = ?)" + ")";
		try (Connection conn = DatabaseUtils.getConnection(); PreparedStatement ps = conn.prepareStatement(SQL)) {
			ps.setString(1, UUID.randomUUID().toString());
			ps.setString(2, invoiceUuid.toString());
			ps.setString(3, itemUuid.toString());
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
		final String INSERT_LINE_SQL = "INSERT INTO InvoiceLine (line_uuid, invoice_id, item_id) VALUES (?, "
				+ "(SELECT invoice_id FROM Invoice WHERE invoice_uuid = ?), "
				+ "(SELECT item_id    FROM Item    WHERE item_uuid    = ?)" + ")";
		final String INSERT_SPEC_SQL = "INSERT INTO LeaseSpec (line_id, start_date, end_date, lease_rate) VALUES ("
				+ "LAST_INSERT_ID(), ?, ?, (SELECT base_cost FROM Item WHERE item_uuid = ?)" + ")";

		try (Connection conn = DatabaseUtils.getConnection()) {
			conn.setAutoCommit(false);
			try (PreparedStatement linePs = conn.prepareStatement(INSERT_LINE_SQL);
					PreparedStatement specPs = conn.prepareStatement(INSERT_SPEC_SQL)) {

				linePs.setString(1, UUID.randomUUID().toString());
				linePs.setString(2, invoiceUuid.toString());
				linePs.setString(3, itemUuid.toString());
				linePs.executeUpdate();

				specPs.setDate(1, Date.valueOf(start));
				specPs.setDate(2, Date.valueOf(end));
				specPs.setString(3, itemUuid.toString());
				specPs.executeUpdate();

				conn.commit();
			} catch (SQLException e) {
				conn.rollback();
				throw e;
			}
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
		final String INSERT_LINE_SQL = "INSERT INTO InvoiceLine (line_uuid, invoice_id, item_id) VALUES (?, "
				+ "(SELECT invoice_id FROM Invoice WHERE invoice_uuid = ?), "
				+ "(SELECT item_id FROM Item WHERE item_uuid = ?)" + ")";
		final String INSERT_SPEC_SQL = "INSERT INTO RentalSpec (line_id, hours, hourly_rate) VALUES ("
				+ "LAST_INSERT_ID(), ?, (SELECT base_cost FROM Item WHERE item_uuid = ?)" + ")";

		try (Connection conn = DatabaseUtils.getConnection()) {
			conn.setAutoCommit(false);
			try (PreparedStatement linePs = conn.prepareStatement(INSERT_LINE_SQL);
					PreparedStatement specPs = conn.prepareStatement(INSERT_SPEC_SQL)) {

				linePs.setString(1, UUID.randomUUID().toString());
				linePs.setString(2, invoiceUuid.toString());
				linePs.setString(3, itemUuid.toString());
				linePs.executeUpdate();

				specPs.setDouble(1, numberOfHours);
				specPs.setString(2, itemUuid.toString());
				specPs.executeUpdate();

				conn.commit();
			} catch (SQLException e) {
				conn.rollback();
				throw e;
			}
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
		final String INSERT_LINE_SQL = "INSERT INTO InvoiceLine (line_uuid, invoice_id, item_id, quantity) VALUES (?, "
				+ "(SELECT invoice_id FROM Invoice WHERE invoice_uuid = ?), "
				+ "(SELECT item_id    FROM Item    WHERE item_uuid    = ?), ?)";
		final String INSERT_SPEC_SQL = "INSERT INTO MaterialSpec (line_id, unit_price) VALUES ("
				+ "LAST_INSERT_ID(), (SELECT base_cost FROM Item WHERE item_uuid = ?)" + ")";

		try (Connection conn = DatabaseUtils.getConnection()) {
			conn.setAutoCommit(false);
			try (PreparedStatement linePs = conn.prepareStatement(INSERT_LINE_SQL);
					PreparedStatement specPs = conn.prepareStatement(INSERT_SPEC_SQL)) {

				linePs.setString(1, UUID.randomUUID().toString());
				linePs.setString(2, invoiceUuid.toString());
				linePs.setString(3, itemUuid.toString());
				linePs.setInt(4, numberOfUnits);
				linePs.executeUpdate();

				specPs.setString(1, itemUuid.toString());
				specPs.executeUpdate();

				conn.commit();
			} catch (SQLException e) {
				conn.rollback();
				throw e;
			}
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
		final String INSERT_LINE_SQL = "INSERT INTO InvoiceLine (line_uuid, invoice_id, item_id) VALUES (?, "
				+ "(SELECT invoice_id FROM Invoice WHERE invoice_uuid = ?), "
				+ "(SELECT item_id    FROM Item    WHERE item_uuid    = ?)" + ")";

		final String INSERT_SPEC_SQL = "INSERT INTO ContractSpec (line_id, vendor_company_id, contract_terms) VALUES ("
				+ "LAST_INSERT_ID(), " + "(SELECT customer_company_id FROM Invoice WHERE invoice_uuid = ?), " + "?)";

		try (Connection conn = DatabaseUtils.getConnection()) {
			conn.setAutoCommit(false);
			try (PreparedStatement linePs = conn.prepareStatement(INSERT_LINE_SQL);
					PreparedStatement specPs = conn.prepareStatement(INSERT_SPEC_SQL)) {

				linePs.setString(1, UUID.randomUUID().toString());
				linePs.setString(2, invoiceUuid.toString());
				linePs.setString(3, itemUuid.toString());
				linePs.executeUpdate();

				specPs.setString(1, invoiceUuid.toString());
				specPs.setString(2, String.format("$%.2f", amount));
				specPs.executeUpdate();

				conn.commit();
			} catch (SQLException e) {
				conn.rollback();
				throw e;
			}
		} catch (SQLException ex) {
			throw new RuntimeException("Failed to add contract to invoice " + invoiceUuid, ex);
		}
	}
}