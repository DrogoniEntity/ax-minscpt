package fr.drogonistudio.axminscpt.example;

import java.io.File;

import fr.drogonistudio.axminscpt.AbstractScript;
import fr.drogonistudio.axminscpt.example.commands.CommandAdd;

public class ScriptExample extends AbstractScript
{

    public ScriptExample(File scriptFile)
    {
	super(scriptFile);
    }

    @Override
    public void init()
    {
	this.registerDefaultCommands();
	this.registerCommand("add", CommandAdd.class);
    }
    
}
