package utils;

public enum CommonUtilities {
	cu;

	
	private CommonUtilities() {

	}

	public void printIfPossible(String string, boolean canPrint) {
		if (canPrint) {
			System.out.println(string);
		}
	}
}
