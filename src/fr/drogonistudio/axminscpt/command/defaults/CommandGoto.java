package fr.drogonistudio.axminscpt.command.defaults;

import fr.drogonistudio.axminscpt.AbstractScript;
import fr.drogonistudio.axminscpt.command.AbstractCommand;
import fr.drogonistudio.axminscpt.exceptions.MalformedCommandException;

public class CommandGoto extends AbstractCommand
{
    private int destination;
    
    public CommandGoto(AbstractScript script)
    {
	super(script);
    }

    @Override
    public void run()
    {
	this.script.setCommandPointer(this.destination);
    }

    @Override
    public void parse(String[] args)
    {
	if (args.length < 1)
	    throw new MalformedCommandException("Syntax: <absolute position>");
	try
	{
	    this.destination = Integer.parseInt(args[0]);
	}
	catch (NumberFormatException ex)
	{
	    throw new MalformedCommandException("Argument must be an integer");
	}
    }
    
}
