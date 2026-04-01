import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

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
}