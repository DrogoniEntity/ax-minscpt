package fr.drogonistudio.axminscpt.command;

import fr.drogonistudio.axminscpt.AbstractScript;

public abstract class AbstractCommand implements Runnable
{
    protected final AbstractScript script;
    
    public AbstractCommand(AbstractScript script)
    {
	this.script = script;
    }
    
    public abstract void parse(String args[]);
    
}
