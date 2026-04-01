public abstract class Vehicle {
    private String licensePlate;
    private String make;
    private String model;
    private int year;
    private VehicleStatus status;

    public enum VehicleStatus { Available, Held, Rented, UnderMaintenance, OutOfService }

    public Vehicle(String make, String model, int year) {
    	if (make == null || make.isEmpty())
    		this.make = null;
    	else
    		this.make = Capitalize(make);
    	
    	if (model == null || model.isEmpty())
    		this.model = null;
    	else
    		this.model = Capitalize(model);
    	
        this.year = year;
        this.status = VehicleStatus.Available;
        this.licensePlate = null;
    }

    public Vehicle() {
        this(null, null, 0);
    }

    public void setLicensePlate(String plate) {
        if (isValidPlate(plate) == false)
        	throw new IllegalArgumentException("Invalid license plate");
        this.licensePlate = plate;
    }

    public void setStatus(VehicleStatus status) {
    	this.status = status;
    }

    public String getLicensePlate() { return licensePlate; }

    public String getMake() { return make; }

    public String getModel() { return model;}

    public int getYear() { return year; }

    public VehicleStatus getStatus() { return status; }

    public String getInfo() {
        return "| " + licensePlate + " | " + make + " | " + model + " | " + year + " | " + status + " |";
    }
    
    public String Capitalize(String text)
    {
    	return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }
    
    private boolean isValidPlate(String plate)
    {
    	if (plate != null)
    	{
    	char[] p = plate.toCharArray();
    	if (p.length==6 && Character.isLetter(p[0]) && Character.isLetter(p[1]) && Character.isLetter(p[2])
    			&& Character.isDigit(p[3]) && Character.isDigit(p[4]) && Character.isDigit(p[5]))
    		return true;
    	}
    	return false;
    }
}