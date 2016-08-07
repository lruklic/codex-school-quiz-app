package converter;

public class ListDetails {
	
	private String listSymbol = "•";
	
	private float listIndentation = 20;
	
	/**
	 * Constructor for ListDetails.
	 */
	public ListDetails() {	
	}
	
	/**
	 * Copy constructor.
	 * @param details
	 */
	public ListDetails(ListDetails details) {
		this.listIndentation = details.listIndentation;
		this.listSymbol = details.listSymbol;
	}
	
	public String getListSymbol() {
		return listSymbol;
	}

	public float getListIndentation() {
		return listIndentation;
	}
	
	public void increaseSymbol() {
		if (listSymbol.equals("•")) {
			listSymbol = "-";
		}
	}
	
}
