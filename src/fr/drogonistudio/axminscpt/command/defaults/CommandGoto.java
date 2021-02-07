package fr.drogonistudio.axminscpt.command.defaults;

import fr.drogonistudio.axminscpt.AbstractScript;
import fr.drogonistudio.axminscpt.command.AbstractCommand;
import fr.drogonistudio.axminscpt.exceptions.MalformedCommandException;

public class CommandGoto extends AbstractCommand
{
    private String label;
    
    public CommandGoto(AbstractScript script)
    {
	super(script);
    }

    @Override
    public void run()
    {
	this.script.moveToLabel(this.label);
    }

    @Override
    public void parse(String[] args)
    {
	if (args.length < 1)
	    throw new MalformedCommandException("Syntax: <label name>");

	if (!this.script.hasLabel(args[0]))
	    throw new MalformedCommandException("label not found");
	this.label = args[0];
    }
    
}
