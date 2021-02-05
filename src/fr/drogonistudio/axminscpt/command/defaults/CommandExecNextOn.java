package fr.drogonistudio.axminscpt.command.defaults;

import fr.drogonistudio.axminscpt.AbstractScript;
import fr.drogonistudio.axminscpt.command.AbstractCommand;
import fr.drogonistudio.axminscpt.exceptions.MalformedCommandException;

public class CommandExecNextOn extends AbstractCommand
{   
    private boolean comparaisonResult;
    
    public CommandExecNextOn(AbstractScript script)
    {
	super(script);
    }

    @Override
    public void run()
    {
	if (!this.comparaisonResult)
	    this.script.moveCommandPointer(1);
    }
    
    @Override
    public void parse(String[] args)
    {
	if (args.length < 2)
	    throw new MalformedCommandException("Syntax: <variable to test> <excpeted value>");
	
	Object value = this.script.getVariable(args[0]);
	if (value instanceof String)
	    this.comparaisonResult = ((String) value).equals(args[1].toString());
	else
	{
	    try
	    {
		this.comparaisonResult = (Double.compare(Double.parseDouble(value.toString()), Double.parseDouble(args[1])) == 0);
	    }
	    catch (NumberFormatException ex)
	    {
		throw new MalformedCommandException("Excepted number");
	    }
	}
    }
    
}
