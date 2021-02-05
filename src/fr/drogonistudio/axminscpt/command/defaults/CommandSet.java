package fr.drogonistudio.axminscpt.command.defaults;

import fr.drogonistudio.axminscpt.AbstractScript;
import fr.drogonistudio.axminscpt.command.AbstractCommand;
import fr.drogonistudio.axminscpt.exceptions.MalformedCommandException;
import fr.drogonistudio.axminscpt.utils.StringNumberTool;

public class CommandSet extends AbstractCommand
{
    private String key;
    private Object value;
    
    public CommandSet(AbstractScript script)
    {
	super(script);
    }
    
    @Override
    public void run()
    {
	this.script.setVariable(this.key, this.value);
    }
    
    @Override
    public void parse(String[] args)
    {
	if (args.length < 2)
	    throw new MalformedCommandException("Command syntax: <variable name> <value>");
	
	this.key = args[0];
	
	// Is a number
	Number valNumber = StringNumberTool.stringToNumber(args[1]);
	if (valNumber != null)
	    this.value = valNumber;
	else
	    this.value = args[1];
    }
    
}
