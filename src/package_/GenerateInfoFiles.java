package package_; 
   /*
    * IMPORTS:
    * java.io.: To handle input/output operations, such as reading and writing files.
    * java.util.: To use collections and other utilities, comInner Classes
    */
import java.io.*;
import java.util.*;

public class GenerateInfoFiles {

	/*
	 * Inner Classes Salesman
	 * 
	 * Attributes: documentType: Type of salesman document. documentNumber: Number
	 * of the document. firstName: First name of the seller. lastName: Last name of
	 * the seller. sales: List of sales made by the seller. Methods: Constructor:
	 * Initializes a Salesman with the data provided and an empty list of sales.
	 * getTotalSales: Calculates the total sales of the seller based on the prices
	 * of the products. toString: Returns the full name of the seller.
	 * 
	 */
	static class Salesman {
		String documentType;
		long documentNumber;
		String firstName;
		String lastName;

		public Salesman(String documentType, long documentNumber, String firstName, String lastName) {
			this.documentType = documentType;
			this.documentNumber = documentNumber;
			this.firstName = firstName;
			this.lastName = lastName;
		}
	}

	/*
	 * Sale Attributes: productId: ID of the product sold. quantity: Quantity sold
	 * of the product. Constructor: Initializes a Sale with the product ID and the
	 * quantity sold.
	 */
	static class Sale {
		int productId;
		int quantity;

		public Sale(int productId, int quantity) {
			this.productId = productId;
//            this.quantity = quantity;
		}
	}

	/*
	 * generateSalesFile method Purpose: To create a text file with the
	 * salesperson's information and sales. Process: Uses BufferedWriter to write to
	 * a file. Writes the line with the salesman's document type and number. Writes
	 * each sale on a separate line in the format IDProduct;Quantity. Exception
	 * Handling: Throws IOException if an error occurs while writing the file.
	 * 
	 */
	public static void generateSalesFile(Salesman salesman, List<Sale> sales, String filename) throws IOException {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
			// Enter the seller's information
			writer.write(salesman.documentType + ";" + salesman.documentNumber);
			writer.newLine();

			// Write sales
			for (Sale sale : sales) {
				writer.write(sale.productId + ";" + sale.quantity);
				writer.newLine();
			}
		}
		System.out.println("Archivo de ventas generado: " + filename);
	}

	/*
	 * generateSalesmenInfoFile method Purpose: Create a text file with the
	 * information of all salesmen. Process: Uses BufferedWriter to write to a file.
	 * Writes each salesman in one line in the format
	 * DocumentType;DocumentNumber;FirstName;LastName.
	 * 
	 */
	public static void generateSalesmenInfoFile(List<Salesman> salesmen, String filename) throws IOException {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
			for (Salesman salesman : salesmen) {
				writer.write(salesman.documentType + ";" + salesman.documentNumber + ";" + salesman.firstName + ";"
						+ salesman.lastName);
				writer.newLine();
			}
		}
		System.out.println("Archivo de información de vendedores generado: " + filename);
	}

	/*
	 * Method generateProductsFile Purpose: Create a text file with the information
	 * of all products. Process: Uses BufferedWriter to write to a file. Writes each
	 * product in one line in the format IDProduct;ProductName;ProductName;Price,
	 * with a fixed price of 0.0 for simplification.
	 */
	public static void generateProductsFile(Map<Integer, String> products, String filename) throws IOException {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
			for (Map.Entry<Integer, String> entry : products.entrySet()) {
				writer.write(entry.getKey() + ";" + entry.getValue() + ";0.0"); // Price 0.0 for simplification
				writer.newLine();
			}
		}
		System.out.println("Archivo de información de productos generado: " + filename);
	}

	/*
	 * Main method 
	 * Purpose: Execute the code to generate the files. Process: Create
	 * Vendors: Creates a list of vendors with sample information. Create Sales:
	 * Creates a list of sales for a specific salesperson. 
	 * Generate Files: Calls generateSalesFile to create the sales file for the first salesperson.
	 * Call generateSalesmenInfoFile to create the salesmen information file. 
	 * Calls generateProductsFile to create the products information file. Exception
	 * Handling: Catches and displays errors if they occur during file generation.
	 * 
	 */
	public static void main(String[] args) {
		try {
			// Create vendor information
			List<Salesman> salesmen = Arrays.asList(new Salesman("CC", 1055075L, "AMILCAR", "BOSCAN"),
					new Salesman("CC", 1122334L, "GILBERTO", "SANTAROSA"),
					new Salesman("CC", 2233445L, "ANTONIO", "BANDERAS"));

			// Create sales from a salesperson
			List<Sale> sales = Arrays.asList(new Sale(1, 10), new Sale(2, 5), new Sale(3, 7));
			generateSalesFile(salesmen.get(0), sales, "1055075_sales_1.txt"); // Sales file for the first salesperson
			generateSalesFile(salesmen.get(1), sales, "1122334_sales_1.txt"); // Sales file for the second salesperson
			generateSalesFile(salesmen.get(2), sales, "2233445_sales_1.txt"); // Sales file for the third salesperson

			// Create vendor information file
			generateSalesmenInfoFile(salesmen, "salesmen_info.txt");

			// Create product information file
			Map<Integer, String> products = Map.of(1, "Product1", 2, "Product2", 3, "Product3");
			generateProductsFile(products, "products_info.txt");

			System.out.println("Archivos generados correctamente.");
		} catch (IOException e) {
			System.err.println("Error generando archivos: " + e.getMessage());
		}
	}
}
