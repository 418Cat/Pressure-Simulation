package mainPkg;

public class Main {

	public static void main(String[] args) {
		
		Simulation sim = new Simulation(250, 250, 1, new Window(500, 500, 250, 250));
		sim.simulate(true);

	}

}
