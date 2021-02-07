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
    private final Map<String, Integer> labels;
    
    protected final Map<String, Class<? extends AbstractCommand>> supportedCommands;
    
    public AbstractScript(File scriptFile)
    {
	this.supportedCommands = new HashMap<>();
	this.fileLines = new ArrayList<>();
	this.scriptFile = scriptFile;
	this.variables = new HashMap<>();
	this.labels = new HashMap<>();
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
	
	try
	{
	    // Reading file
	    context = "Reading file";
	    String line;
	    BufferedReader reader = new BufferedReader(new FileReader(this.scriptFile));
	    this.commandPointer = 0;
	    
	    while ((line = reader.readLine()) != null)
	    {
		line = line.trim();
		
		// Define a label if the line begin with ":"
		if (line.startsWith(":"))
		{
		    String labelName = line.substring(1);
		    
		    // We will keep only the first word
		    int firstSpaceIndex = labelName.indexOf(' ');
		    if (firstSpaceIndex != -1)
			labelName = labelName.substring(0, firstSpaceIndex - 1);
		    
		    // The label point to "pointer - 1" because, when we move to the line,
		    // pointer will be incremented to "+1" after the action
		    this.labels.put(labelName, (this.commandPointer - 1));
		}
		
		// Register a command if it's not a dummy line (empty line or comment)
		else if (!line.isEmpty() && !line.startsWith("#"))
		{
		    this.fileLines.add(line.trim());
		    ++this.commandPointer;
		}
	    }
	    
	    reader.close();
	    
	    // Now, let's execute
	    this.commandPointer = 0;
	    while (this.commandPointer < this.fileLines.size())
	    {
		context = "Parsing";
		commandToRun = this.parse(this.fileLines.get(this.commandPointer));
		
		context = "Executing";
		commandToRun.run();
		
		++this.commandPointer;
	    }
	}
	catch (Throwable t)
	{
	    System.err.println("Script failed !");
	    System.err.printf("Pointer: %d\n", this.commandPointer);
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
	if (nextPointer < -1)
	    nextPointer = -1;
	
	this.commandPointer = nextPointer;
    }
    
    public final void moveCommandPointer(int relativeMove)
    {
	this.setCommandPointer(this.commandPointer + relativeMove);
    }
    
    public final boolean hasLabel(String labelName)
    {
	return this.labels.containsKey(labelName);
    }
    
    public final void moveToLabel(String labelName)
    {
	// Checking if a label already exists
	if (!this.labels.containsKey(labelName))
	    throw new NullPointerException("label not found");
	
	// If it's true, move to label's pointer
	this.commandPointer = this.labels.get(labelName);
    }
}
