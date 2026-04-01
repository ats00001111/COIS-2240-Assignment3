import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.io.IOException;

public class RentalSystem {
	//private static instance
	private static RentalSystem instance;
	//private constructor
	private RentalSystem() { loadData(); }
	//public accessor method
	public static RentalSystem getInstance()
	{
		if (instance == null)
		{
			instance = new RentalSystem();
		}
		return instance;
	}
    private List<Vehicle> vehicles = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();
    private RentalHistory rentalHistory = new RentalHistory();

    public boolean addVehicle(Vehicle vehicle) {
    	for (Vehicle v : vehicles)
    	{
    		if (v.getLicensePlate().contains(vehicle.getLicensePlate()))
    		{
    		System.out.println("Error: Duplicate license plate");	
    		return false;
    		}
    	}
    	System.out.println("Vehicle added successfully.");
        vehicles.add(vehicle);
        saveVehicle(vehicle);
        return true;
    }

    public boolean addCustomer(Customer customer) {
    	for (Customer c : customers)
    	{
    		if (c.getCustomerId() == customer.getCustomerId())
    		{
    		System.out.println("Error: Duplicate Customer ID");	
    		return false;
    		}
    	}
    	System.out.println("Customer added successfully.");
        customers.add(customer);
        saveCustomer(customer);
        return true;
    }

    public boolean rentVehicle(Vehicle vehicle, Customer customer, LocalDate date, double amount) {
        if (vehicle.getStatus() == Vehicle.VehicleStatus.Available) {
            vehicle.setStatus(Vehicle.VehicleStatus.Rented);
            RentalRecord rented = new RentalRecord(vehicle, customer, date, amount, "RENT");
            rentalHistory.addRecord(rented);
            System.out.println("Vehicle rented to " + customer.getCustomerName());
            saveRecord(rented);
            Path filePath = Paths.get("vehicles.txt");
            try {
                List<String> lines = Files.readAllLines(filePath);
                for (int i = 0; i < lines.size(); i++) {
                    if (lines.get(i).contains(vehicle.getLicensePlate()))
                    {
                    	lines.set(i, lines.get(i).replace("Available", "Rented"));
                    }
                }
                Files.write(filePath, lines);
            } 
            catch(IOException e)
            {
            	e.printStackTrace();
            }
            return true;
        }
        else {
            System.out.println("Vehicle is not available for renting.");
        }
        return false;
    }

    public boolean returnVehicle(Vehicle vehicle, Customer customer, LocalDate date, double extraFees) {
        if (vehicle.getStatus() == Vehicle.VehicleStatus.Rented) {
            vehicle.setStatus(Vehicle.VehicleStatus.Available);
            RentalRecord returned = new RentalRecord(vehicle, customer, date, extraFees, "RETURN");
            rentalHistory.addRecord(returned);
            System.out.println("Vehicle returned by " + customer.getCustomerName());
            saveRecord(returned);
            Path filePath = Paths.get("vehicles.txt");
            try {
                List<String> lines = Files.readAllLines(filePath);
                for (int i = 0; i < lines.size(); i++) {
                    if (lines.get(i).contains(vehicle.getLicensePlate()))
                    {
                    	lines.set(i, lines.get(i).replace("Rented", "Available"));
                    }
                }
                Files.write(filePath, lines);
            } 
            catch(IOException e)
            {
            	e.printStackTrace();
            }
            return true;
        }
        else {
            System.out.println("Vehicle is not rented.");
        }
        return false;
    }    

    public void displayVehicles(Vehicle.VehicleStatus status) {
        // Display appropriate title based on status
        if (status == null) {
            System.out.println("\n=== All Vehicles ===");
        } else {
            System.out.println("\n=== " + status + " Vehicles ===");
        }
        
        // Header with proper column widths
        System.out.printf("|%-16s | %-12s | %-12s | %-12s | %-6s | %-18s |%n", 
            " Type", "Plate", "Make", "Model", "Year", "Status");
        System.out.println("|--------------------------------------------------------------------------------------------|");
    	  
        boolean found = false;
        for (Vehicle vehicle : vehicles) {
            if (status == null || vehicle.getStatus() == status) {
                found = true;
                String vehicleType;
                if (vehicle instanceof Car) {
                    vehicleType = "Car";
                } else if (vehicle instanceof Minibus) {
                    vehicleType = "Minibus";
                } else if (vehicle instanceof PickupTruck) {
                    vehicleType = "Pickup Truck";
                } else {
                    vehicleType = "Unknown";
                }
                System.out.printf("| %-15s | %-12s | %-12s | %-12s | %-6d | %-18s |%n", 
                    vehicleType, vehicle.getLicensePlate(), vehicle.getMake(), vehicle.getModel(), vehicle.getYear(), vehicle.getStatus().toString());
            }
        }
        if (!found) {
            if (status == null) {
                System.out.println("  No Vehicles found.");
            } else {
                System.out.println("  No vehicles with Status: " + status);
            }
        }
        System.out.println();
    }

    public void displayAllCustomers() {
        for (Customer c : customers) {
            System.out.println("  " + c.toString());
        }
    }
    
    public void displayRentalHistory() {
        if (rentalHistory.getRentalHistory().isEmpty()) {
            System.out.println("  No rental history found.");
        } else {
            // Header with proper column widths
            System.out.printf("|%-10s | %-12s | %-20s | %-12s | %-12s |%n", 
                " Type", "Plate", "Customer", "Date", "Amount");
            System.out.println("|-------------------------------------------------------------------------------|");
            
            for (RentalRecord record : rentalHistory.getRentalHistory()) {                
                System.out.printf("| %-9s | %-12s | %-20s | %-12s | $%-11.2f |%n", 
                    record.getRecordType(), 
                    record.getVehicle().getLicensePlate(),
                    record.getCustomer().getCustomerName(),
                    record.getRecordDate().toString(),
                    record.getTotalAmount()
                );
            }
            System.out.println();
        }
    }
    
    public Vehicle findVehicleByPlate(String plate) {
        for (Vehicle v : vehicles) {
            if (v.getLicensePlate().equalsIgnoreCase(plate)) {
                return v;
            }
        }
        return null;
    }
    
    public Customer findCustomerById(int id) {
        for (Customer c : customers)
            if (c.getCustomerId() == id)
                return c;
        return null;
    }
    
    public void saveVehicle(Vehicle vehicle)
    {
    	try (BufferedWriter vehicleWriter = new BufferedWriter(new FileWriter("vehicles.txt", true)))
    	{
			vehicleWriter.write(vehicle.getInfo() + "\n");
			vehicleWriter.close();
		} 
    	catch (IOException e) {
			e.printStackTrace();
		}
    }
    public void saveCustomer(Customer customer)
    {
    	try (BufferedWriter customerWriter = new BufferedWriter(new FileWriter("customers.txt", true))){
			customerWriter.write(customer.toString() + "\n");
			customerWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    public void saveRecord(RentalRecord record)
    {
    	try (BufferedWriter recordWriter = new BufferedWriter(new FileWriter("rental_records.txt", true))){
			recordWriter.write(record.toString() + "\n");
			recordWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    private void loadData()
    {
    	try {
			BufferedReader readVehicles = new BufferedReader(new FileReader("vehicles.txt"));
			String line;
			while((line = readVehicles.readLine()) != null)
			{
				String[] v = line.split("[:|]");
				for(int i=0; i<v.length; i++)
				{
					v[i] = v[i].trim();
				}
				if (v.length > 1)
				{
					if (v.length == 9)
					{
						if (v[8].contains("Yes") || v[8].contains("No"))
						{
							Vehicle temp = new Minibus(v[2],v[3],Integer.parseInt(v[4]),Boolean.parseBoolean(v[8]));
							temp.setLicensePlate(v[1]);
							if (v[5].contains("Rented"))
								temp.setStatus(Vehicle.VehicleStatus.Rented);
							vehicles.add(temp);
						}
						else
						{
						Vehicle temp = new Car(v[2],v[3],Integer.parseInt(v[4]),Integer.parseInt(v[8]));
						temp.setLicensePlate(v[1]);
						if (v[5].contains("Rented"))
							temp.setStatus(Vehicle.VehicleStatus.Rented);
						vehicles.add(temp);
						}
					}
					else
					{
						Vehicle temp = new PickupTruck(v[2],v[3],Integer.parseInt(v[4]),Double.parseDouble(v[8]),Boolean.parseBoolean(v[10]));
						temp.setLicensePlate(v[1]);
						if (v[5].contains("Rented"))
							temp.setStatus(Vehicle.VehicleStatus.Rented);
						vehicles.add(temp);
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	try {
			BufferedReader readCustomers = new BufferedReader(new FileReader("customers.txt"));
			String line;
			while((line = readCustomers.readLine()) != null)
			{
				String[] c = line.split("[:|]");
				for (int i=0; i<c.length; i++)
				{
					c[i] = c[i].trim();
				}
				if (c.length > 1)
				customers.add(new Customer(Integer.parseInt(c[1]),c[3]));
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    		try {
				BufferedReader readRecords = new BufferedReader(new FileReader("rental_records.txt"));
				String line;
				while((line = readRecords.readLine()) != null) 
				{
					String[] r = line.split("[:|]");
					for (int i=0; i<r.length; i++)
					{
						r[i] = r[i].trim();
					}
					if (r.length > 1)
					{
					Vehicle temp = findVehicleByPlate(r[2]);
					Customer TempCustomer = null;
					for(Customer c : customers)
					{
						if (c.getCustomerName().contains(r[4]))
						{
							TempCustomer = findCustomerById(c.getCustomerId());
						}
					}
					rentalHistory.addRecord(new RentalRecord(temp, TempCustomer, LocalDate.parse(r[6]), Double.parseDouble(r[8].replace("$", "")), r[0]));
					}
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    }
}