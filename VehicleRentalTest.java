import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class VehicleRentalTest
{
	@Test
	public void testLicensePlate()
	{
		Vehicle v1 = new Car("","",0,0);
		Vehicle v2 = new Car("","",0,0);
		Vehicle v3 = new Car("","",0,0);
		Vehicle v4 = new Car("","",0,0);
		Vehicle v5 = new Car("","",0,0);
		Vehicle v6 = new Car("","",0,0);
		Vehicle v7 = new Car("","",0,0);
		List<Vehicle> testingVehicles = Arrays.asList(v1,v2,v3,v4,v5,v6,v7);
		List<String> licensePlate = Arrays.asList("AAA100","ABC567","ZZZ999","",null,"AAA1000","ZZZ99");
		for (int i=0; i<testingVehicles.size(); i++)
		{
			try
			{
				testingVehicles.get(i).setLicensePlate(licensePlate.get(i));
			} catch(IllegalArgumentException e)
			{
				assertEquals("Invalid license plate", e.getMessage());
			}
		}
	}
	@Test
	public void testRentandReturnVehicle()
	{
		Vehicle v1 = new Car("","",0,0);
		v1.setLicensePlate("ABC123");
		Customer c1 = new Customer(0,"");
		Assertions.assertEquals(Vehicle.VehicleStatus.Available, v1.getStatus());
		RentalSystem rentalSystem = RentalSystem.getInstance();
		assertTrue(rentalSystem.rentVehicle(v1, c1, LocalDate.now(), 0));
		Assertions.assertEquals(Vehicle.VehicleStatus.Rented, v1.getStatus());
		assertFalse(rentalSystem.rentVehicle(v1, c1, LocalDate.now(), 0));
		assertTrue(rentalSystem.returnVehicle(v1, c1, LocalDate.now(), 0));
		Assertions.assertEquals(Vehicle.VehicleStatus.Available, v1.getStatus());
		assertFalse(rentalSystem.returnVehicle(v1, c1, LocalDate.now(), 0));
	}
}