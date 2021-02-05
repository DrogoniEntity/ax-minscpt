package fr.drogonistudio.axminscpt.command.defaults;

import fr.drogonistudio.axminscpt.AbstractScript;
import fr.drogonistudio.axminscpt.command.AbstractCommand;
import fr.drogonistudio.axminscpt.exceptions.MalformedCommandException;

public class CommandSkip extends AbstractCommand
{
    private int relativePosition;
    
    public CommandSkip(AbstractScript script)
    {
	super(script);
    }

    @Override
    public void run()
    {
	this.script.moveCommandPointer(this.relativePosition);
    }

    @Override
    public void parse(String[] args)
    {
	if (args.length < 1)
	    throw new MalformedCommandException("Syntax: <absolute position>");
	try
	{
	    this.relativePosition = Integer.parseInt(args[0]);
	}
	catch (NumberFormatException ex)
	{
	    throw new MalformedCommandException("Argument must be an integer");
	}
    }    
}
