package fr.drogonistudio.axminscpt.command.defaults;

import fr.drogonistudio.axminscpt.AbstractScript;
import fr.drogonistudio.axminscpt.command.AbstractCommand;

public class CommandEcho extends AbstractCommand
{
    private String arguments[];
    public CommandEcho(AbstractScript script)
    {
	super(script);
    }

    @Override
    public void run()
    {
	for (int i = 0; i < this.arguments.length; i++)
	{
	    System.out.print(this.arguments[i]);
	    if (i < (this.arguments.length - 1))
		System.out.print(' ');
	}
	System.out.print('\n');
    }

    @Override
    public void parse(String[] args)
    {
	this.arguments = args;
    }
    
    
}
