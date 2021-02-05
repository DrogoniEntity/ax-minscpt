package fr.drogonistudio.axminscpt.utils;

import java.util.regex.Pattern;

public final class StringNumberTool
{
    private StringNumberTool()
    {
    }
    
    private static final Pattern NUM_PATTERN = Pattern.compile("-?\\d+(\\.\\d+)?");
    
    public static Number stringToNumber(String s)
    {
	if (NUM_PATTERN.matcher(s).matches())
	{
	    // Is floating number
	    if (s.contains("."))
		return Float.parseFloat(s);
	    // However, it's a integer
	    else
		return Integer.parseInt(s);
	}
	
	return null;
    }
}
