
public class Kernel {
	double[][] matrix;
	String name;

	public Kernel(double[][] matrix) {
		this.name = "user-defined";
		this.matrix = matrix;
	}

	public Kernel(String name, double[][] matrix) {
		this.name = name;
		this.matrix = matrix;
	}

}
