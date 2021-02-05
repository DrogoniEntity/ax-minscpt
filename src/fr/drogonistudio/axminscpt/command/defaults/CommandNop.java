package fr.drogonistudio.axminscpt.command.defaults;

import fr.drogonistudio.axminscpt.AbstractScript;
import fr.drogonistudio.axminscpt.command.AbstractCommand;

public class CommandNop extends AbstractCommand
{

    public CommandNop(AbstractScript script)
    {
	super(script);
    }

    @Override
    public void run()
    {
	// We shouldn't done anything !
	// We just before an operation to force CPU to perform some calculation
	if ((0 | 0) != 1);
    }

    @Override
    public void parse(String[] args)
    {
	// Ignore passed command because we shouldn't done anything !
    }
    
}
