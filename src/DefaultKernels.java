
public class DefaultKernels {
	static Kernel getKernel(String kerName) throws KernelException {
		if (kerName.equalsIgnoreCase("SimpleBlur3")) {
			double[][] ker = new double[3][3];
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					ker[i][j] = 0.111;
				}
			}
			return new Kernel(kerName, ker);
		} else if (kerName.equalsIgnoreCase("SimpleBlur5")) {
			double[][] ker = new double[5][5];
			for (int i = 0; i < 5; i++) {
				for (int j = 0; j < 5; j++) {
					ker[i][j] = 0.04;
				}
			}
			return new Kernel(kerName, ker);
		} else if (kerName.equalsIgnoreCase("GaussianBlur3")) {
			double[][] ker = { { 1, 2, 1 }, { 2, 4, 2 }, { 1, 2, 1 } };
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					ker[i][j] /= 16;
				}
			}
			return new Kernel(kerName, ker);
		} else if (kerName.equalsIgnoreCase("GaussianBlur5")) {
			double[][] ker = { { 1, 4, 7, 4, 1 }, { 4, 16, 26, 16, 4 }, { 7, 26, 41, 26, 7 }, { 4, 16, 26, 16, 4 },
					{ 1, 4, 7, 4, 1 } };
			for (int i = 0; i < 5; i++) {
				for (int j = 0; j < 5; j++) {
					ker[i][j] /= 273;
				}
			}
			return new Kernel(kerName, ker);
		} else if (kerName.equalsIgnoreCase("UnsharpMask5")) {
			double[][] ker = { { 1, 4, 6, 4, 1 }, { 4, 16, 24, 16, 4 }, { 6, 24, -476, 24, 6 }, { 4, 16, 24, 16, 4 },
					{ 1, 4, 6, 4, 1 } };
			for (int i = 0; i < 5; i++) {
				for (int j = 0; j < 5; j++) {
					ker[i][j] = -ker[i][j] / 256;
				}
			}
			return new Kernel(kerName, ker);
		} else if (kerName.equalsIgnoreCase("SimpleSharpen3")) {
			double[][] ker = { { -1, -1, -1 }, { -1, 9, -1 }, { -1, -1, -1 } };
			return new Kernel(kerName, ker);
		} else if (kerName.equalsIgnoreCase("SimpleSharpen5")) {
			double[][] ker = { { -1, -1, -1, -1, -1 }, { -1, -1, 25, -1, -1 }, { -1, -1, -1, -1, -1 },
					{ -1, -1, -1, -1, -1 }, { -1, -1, -1, -1, -1 } };
			return new Kernel(kerName, ker);
		} else if (kerName.equalsIgnoreCase("UnsharpMask3")) {
			double[][] ker = { { 0, -1, 0 }, { -1, 5, -1 }, { 0, -1, 0 } };
			return new Kernel(kerName, ker);
		} else if (kerName.equalsIgnoreCase("SobelX3")) {
			double[][] ker = { { -1, 0, 1 }, { -2, 0, 2 }, { -1, 0, 1 } };
			return new Kernel(kerName, ker);
		} else if (kerName.equalsIgnoreCase("SobelY3")) {
			double[][] ker = { { -1, -2, -1 }, { 0, 0, 0 }, { 1, 2, 1 } };
			return new Kernel(kerName, ker);
		} else if (kerName.equalsIgnoreCase("PrewittX5")) {
			double[][] ker = { { 2, 2, 2, 2, 2 }, { 1, 1, 1, 1, 1 }, { 0, 0, 0, 0, 0 }, { -1, -1, -1, -1, -1 },
					{ -2, -2, -2, -2, -2 } };
			return new Kernel(kerName, ker);
		} else if (kerName.equalsIgnoreCase("PrewittY5")) {
			double[][] ker = { { 2, 1, 0, -1, -2 }, { 2, 1, 0, -1, -2 }, { 2, 1, 0, -1, -2 }, { 2, 1, 0, -1, -2 },
					{ 2, 1, 0, -1, -2 } };
			return new Kernel(kerName, ker);
		} else if (kerName.equalsIgnoreCase("Emboss3")) {
			double[][] ker = { { -2, -1, 0 }, { -1, 1, 1 }, { 0, 1, 2 } };
			return new Kernel(kerName, ker);
		} else if (kerName.equalsIgnoreCase("DiagonalLaplacian3")) {
			double[][] ker = { { -1, -1, -1 }, { -1, 8, -1 }, { -1, -1, -1 } };
			return new Kernel(kerName, ker);
		} else if (kerName.equalsIgnoreCase("SimpleLaplacian3")) {
			double[][] ker = { { 0, -1, 0 }, { -1, 4, -1 }, { 0, -1, 0 } };
			return new Kernel(kerName, ker);
		} else if (kerName.equalsIgnoreCase("GaussianLaplacian5")) {
			double[][] ker = { { 0, 0, -1, 0, 0 }, { 0, -1, -2, -1, 0 }, { -1, -2, 16, -2, -1 }, { 0, -1, -2, -1, 0 },
					{ 0, 0, -1, 0, 0 } };
			return new Kernel(kerName, ker);
		} else {
			throw new KernelException("Kernel " + kerName + " is not defined.");
		}
	}
}

@SuppressWarnings("serial")
class KernelException extends Exception {
	String msg;

	public KernelException(String msg) {
		this.msg = msg;
	}

	@Override
	public String getMessage() {
		return this.msg;
	}

}