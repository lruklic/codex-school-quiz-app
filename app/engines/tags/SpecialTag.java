package engines.tags;

import java.awt.Color;

public class SpecialTag {
	
	public SpecialTagType tagType;
	
	public String displayName;
	
	public Color color;
	
	public String year;
	
	public String level;
	
	public String season;

	public SpecialTag(SpecialTagType tagType, String displayName, Color color, String year, String level, String season) {
		this.tagType = tagType;
		this.displayName = displayName;
		this.color = color;
		this.year = year;
		this.level = level;
		this.season = season;
	}

	

}
