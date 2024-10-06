package package_;

/*
 * IMPORTS:
 * java.io.: To handle input/output operations, such as reading and writing files.
 * java.util.: To use collections and other utilities, comInner Classes
 */
import java.io.*;
import java.util.*;

public class Main {

	/*
	 * Inner Classes Salesman
	 * 
	 * Attributes: documentType: Type of salesman document. documentNumber: Number
	 * of the document. firstName: First name of the seller. lastName: Last name of
	 * the seller.
	 */

	static class Salesman {
		String documentType;
		long documentNumber;
		String firstName;
		String lastName;
		List<Sale> sales;

		public Salesman(String documentType, long documentNumber, String firstName, String lastName) {
			this.documentType = documentType;
			this.documentNumber = documentNumber;
			this.firstName = firstName;
			this.lastName = lastName;
			this.sales = new ArrayList<>();
		}

		/*
		 * getTotalSales: Calculates the total sales of the seller based on the prices
		 * of the products.
		 */
		public double getTotalSales(Map<Integer, Product> products) {
			double total = 0;
			for (Sale sale : sales) {
				Product product = products.get(sale.productId);
				if (product != null) {
					total += Math.abs(sale.quantity) * Math.abs(product.price);
				}
			}
			return total;
		}

		// toString: Returns the full name of the seller.
		@Override
		public String toString() {
			return firstName + " " + lastName;
		}
	}

	/*
	 * sales: List of sales made by the seller. Methods: Constructor: Initializes a
	 * Salesman with the data provided and an empty list of sales.
	 */

	static class Sale {
		int productId;
		int quantity;

		public Sale(int productId, int quantity) {
			this.productId = productId;
			this.quantity = quantity;
		}
	}

	/*
	 * File Processing Methods loadSalesmenInfo Purpose: Read a txt file with
	 * information about sellers and load it into a list of Salesman objects.
	 * Process: Reads the file line by line. Splits each line into parts using the ;
	 * delimiter. Creates a new Salesman object with the data for each line and adds
	 * it to the list.
	 */
	public static List<Salesman> loadSalesmenInfo(String filename) throws IOException {
		List<Salesman> salesmen = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(";");
				String documentType = parts[0];
				long documentNumber = Long.parseLong(parts[1]);
				String firstName = parts[2];
				String lastName = parts[3];
				salesmen.add(new Salesman(documentType, documentNumber, firstName, lastName));
			}
		}
		return salesmen;
	}

	private static Map<Integer, Product> loadProducts(String filename) throws IOException {
		Map<Integer, Product> products = new HashMap<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
			String line;
			while ((line = reader.readLine()) != null) {
				// Removes possible quotation marks and blank spaces
				line = line.replace("\"", "").trim();
				String[] parts = line.split(";");

				if (parts.length == 3) {
					try {
						int productId = Integer.parseInt(parts[0].trim());
						String productName = parts[1].trim();
						double price = Double.parseDouble(parts[2].trim());
						products.put(productId, new Product(productId, productName, price));
					} catch (NumberFormatException e) {
						System.err.println("Error de formato en la línea: " + line);
					}
				} else {
					System.err.println("Formato incorrecto en la línea: " + line);
				}
			}
		}
		return products;
	}

	/*
	 * loadProductPrices Purpose: Read a txt file with product prices and load them
	 * into a map. Process: Read the file line by line. Clean up any quotation marks
	 * and whitespace. Split each line into parts using the ; delimiter. Extract the
	 * product ID, name and price, and save them to the productPrices map. Handle
	 * any formatting errors.
	 */

	static class Product {
		int id;
		String name;
		double price;

		public Product(int id, String name, double price) {
			this.id = id;
			this.name = name;
			this.price = price;
		}
	}

	/*
	 * The loadSales method loads sales files for each salesman in the salesmen
	 * list, searching for sequential files in a specified directory. For each
	 * salesman, it constructs a base name using the document number and an index,
	 * and then attempts to open sales files with names such as
	 * documentNumber_sales_1.txt, documentNumber_sales_2.txt, etc. If the file
	 * exists, it reads line by line, omitting the first line containing the
	 * document type and number. Each remaining line represents a sale, separated by
	 * semicolon, where the product ID and quantity sold are extracted and added to
	 * the sales list of the vendor. If a file does not exist, the process stops for
	 * that salesperson. Also, if no file is found, a warning is issued.
	 * 
	 */

	public static void loadSales(String directory, List<Salesman> salesmen) throws IOException {
		for (Salesman salesman : salesmen) {
			String baseFilename = directory + "/" + salesman.documentNumber + "_sales";
			int fileIndex = 1;

			while (true) {
				String filename = baseFilename + "_" + fileIndex + ".txt";
				File file = new File(filename);

				if (file.exists()) {
					System.out.println("Procesando archivo de ventas: " + filename);
					try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
						String line;
						reader.readLine(); 
						while ((line = reader.readLine()) != null) {
							String[] parts = line.split(";");
							if (parts.length >= 2) {
								try {
									int productId = Integer.parseInt(parts[0].trim());
									int quantity = Math.abs(Integer.parseInt(parts[1].trim())); 
									salesman.sales.add(new Sale(productId, quantity));
								} catch (NumberFormatException e) {
									System.err.println("Formato incorrecto en la línea: " + line);
								}
							}
						}
					}
					fileIndex++;
				} else {
					break;
				}
			}

			if (fileIndex == 1) {
				System.err.println("No se encontraron archivos de ventas para el vendedor: " + salesman.documentNumber);
			}
		}
	}

	/*
	 * createSalesReportFile method Purpose: Create a CSV file with a sales report
	 * by salesperson. Process: Sort the list of salespeople by total sales (highest
	 * to lowest). Write the salesperson name and their total sales to the CSV file.
	 * Display a success message.
	 * 
	 */
	public static void createSalesReportFile(List<Salesman> salesmen, Map<Integer, Product> products, String filename)
			throws IOException {
		salesmen.sort((s1, s2) -> Double.compare(s2.getTotalSales(products), s1.getTotalSales(products)));

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
			for (Salesman salesman : salesmen) {
				double totalSales = salesman.getTotalSales(products);
				writer.write(salesman + ";" + totalSales);
				writer.newLine();
			}
		}
		System.out.println("Archivo de reporte de ventas generado: " + filename);
	}

	/*
	 * createProductReportFile method Purpose: Create a CSV file with a report of
	 * products sold. Process: Calculates the total quantity sold for each product.
	 * Sorts the products by quantity sold (largest to smallest). Writes the product
	 * name, price, and quantity to the CSV file. Displays a success message.
	 * 
	 */
	public static void createProductReportFile(Map<Integer, Product> products, List<Salesman> salesmen, String filename)
			throws IOException {
		Map<Integer, Integer> productQuantities = new HashMap<>();

		// Sum of the quantities sold for each product
		for (Salesman salesman : salesmen) {
			for (Sale sale : salesman.sales) {
				int productId = sale.productId;
				int quantity = sale.quantity;
				productQuantities.put(productId, productQuantities.getOrDefault(productId, 0) + quantity);
			}
		}

		// Sort the products by quantity sold (from highest to lowest)
		List<Map.Entry<Integer, Integer>> sortedProducts = new ArrayList<>(productQuantities.entrySet());
		sortedProducts.sort((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()));

		// Write the report to a CSV file
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
			for (Map.Entry<Integer, Integer> entry : sortedProducts) {
				int productId = entry.getKey();
				int quantity = entry.getValue();
				Product product = products.get(productId);
				if (product != null) {
					writer.write(product.name + ";" + product.price + ";" + quantity);
					writer.newLine();
				} else {
					System.err.println("Producto no encontrado para el ID: " + productId);
				}
			}
		}
		System.out.println("Archivo de reporte de productos generado: " + filename);
	}

	/*
	 * Main Upload vendor information from a text file Upload product information
	 * from a text file Upload sales from corresponding files for each salesperson
	 * Creates a CSV file with a sales report for each salesperson Creates a CSV
	 * file with a report of products sold Prints a message indicating that the
	 * reports were generated correctly Handles possible input/output exceptions and
	 * displays an error message
	 */
	public static void main(String[] args) {
		try {
			List<Salesman> salesmen = loadSalesmenInfo("salesmen_info.txt");
			Map<Integer, Product> products = loadProducts("products_info.txt");

			loadSales(".", salesmen);
			createSalesReportFile(salesmen, products, "sales_report.csv");
			createProductReportFile(products, salesmen, "product_report.csv");
			System.out.println("Reportes generados correctamente.");
		} catch (IOException e) {
			System.err.println("Error procesando archivos: " + e.getMessage());
		}
	}

}
