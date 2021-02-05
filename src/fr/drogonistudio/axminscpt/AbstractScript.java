package fr.drogonistudio.axminscpt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.drogonistudio.axminscpt.command.AbstractCommand;
import fr.drogonistudio.axminscpt.command.defaults.*;

public abstract class AbstractScript implements Runnable
{
    private final File scriptFile;
    private final List<String> fileLines;
    private int commandPointer;
    
    private final Map<String, Object> variables;
    
    protected final Map<String, Class<? extends AbstractCommand>> supportedCommands;
    
    public AbstractScript(File scriptFile)
    {
	this.supportedCommands = new HashMap<>();
	this.fileLines = new ArrayList<>();
	this.scriptFile = scriptFile;
	this.variables = new HashMap<>();
    }
    
    /**
     * Here, you will register all of your supported commands
     */
    public abstract void init(); 
    
    protected void registerCommand(String commandName, Class<? extends AbstractCommand> commandClass)
    {
	this.supportedCommands.put(commandName.toUpperCase(), commandClass);
    }
    
    protected final void registerDefaultCommands()
    {
	this.registerCommand("echo", CommandEcho.class);
	this.registerCommand("set", CommandSet.class);
	this.registerCommand("goto", CommandGoto.class);
	this.registerCommand("skip", CommandSkip.class);
	this.registerCommand("compare", CommandCompare.class);
	this.registerCommand("execnexton", CommandExecNextOn.class);
	this.registerCommand("nop", CommandNop.class);
    }
    
    @Override
    public final void run()
    {
	String context = null;
	AbstractCommand commandToRun = null;
	this.commandPointer = -1;
	
	try
	{
	    // Reading file
	    context = "Reading file";
	    String line;
	    BufferedReader reader = new BufferedReader(new FileReader(this.scriptFile));
	    while ((line = reader.readLine()) != null)
	    {
		line = line.trim();
		if (!line.isEmpty() && !line.startsWith("#"))
		    this.fileLines.add(line.trim());
	    }
	    
	    reader.close();
	    
	    // Now, let's execute
	    context = "Executing";
	    this.commandPointer = 0;
	    while (this.commandPointer < this.fileLines.size())
	    {
		commandToRun = this.parse(this.fileLines.get(this.commandPointer));
		commandToRun.run();
		
		++this.commandPointer;
	    }
	}
	catch (Throwable t)
	{
	    System.err.println("Script failed !");
	    System.err.printf("At line %d\n", this.commandPointer);
	    System.err.println("Context: " + context);
	    t.printStackTrace();
	    
	    System.exit(1);
	}
    }
    
    private AbstractCommand parse(String line) throws Throwable
    {
	String opt[] = line.split(" ");
	String commandName = opt[0];
	
	Class<?> commandClass = this.supportedCommands.get(commandName.toUpperCase());
	if (commandClass == null)
	    throw new NullPointerException("Unknown command: " + commandName);
	
	AbstractCommand command = (AbstractCommand) commandClass.getConstructor(AbstractScript.class).newInstance(this);
	
	ArrayList<String> argsList = new ArrayList<>();
	StringBuilder argumentStringBuilder = null;
	for (int i = 1; i < opt.length; i++)
	{
	    if (argumentStringBuilder != null)
	    {
		argumentStringBuilder.append(" " + opt[i]);
		if (argumentStringBuilder.charAt(argumentStringBuilder.length()-1) == '"')
		{
		    argsList.add(argumentStringBuilder.substring(0, argumentStringBuilder.length() - 1));
		    argumentStringBuilder = null;
		}
	    }
	    else
	    {
		if (opt[i].startsWith("\""))
		{
		    argumentStringBuilder = new StringBuilder(opt[i].substring(1));
		    if (argumentStringBuilder.charAt(argumentStringBuilder.length()-1) == '"')
		    {
			argsList.add(argumentStringBuilder.substring(0, argumentStringBuilder.length() - 1));
			argumentStringBuilder = null;
		    }
		}
		else
		{
		    if (opt[i].startsWith("$"))
			argsList.add(this.variables.get(opt[i].substring(1)).toString());
		    else
			argsList.add(opt[i]);
		}
	    }
	}
	
	if (argumentStringBuilder != null)
	    argsList.add(argumentStringBuilder.toString());
	
	command.parse(argsList.toArray(new String[argsList.size()]));
	return command;
    }
    
    public Object getVariable(String key)
    {
	return this.variables.get(key);
    }
    
    public void setVariable(String key, Object value)
    {
	this.variables.put(key, value);
    }
    
    public final int getCurrentCommandPointer()
    {
	return this.commandPointer;
    }
    
    public final void setCommandPointer(int nextPointer)
    {
	this.commandPointer = nextPointer;
    }
    
    public final void moveCommandPointer(int relativeMove)
    {
	this.commandPointer = this.commandPointer + relativeMove;
    }
}
