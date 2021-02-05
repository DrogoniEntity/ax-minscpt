package fr.drogonistudio.axminscpt.example.commands;

import fr.drogonistudio.axminscpt.AbstractScript;
import fr.drogonistudio.axminscpt.command.AbstractCommand;
import fr.drogonistudio.axminscpt.exceptions.MalformedCommandException;
import fr.drogonistudio.axminscpt.utils.StringNumberTool;

public class CommandAdd extends AbstractCommand
{
    private String resultLocation;
    
    private Double operand1;
    private Double operand2;
    
    public CommandAdd(AbstractScript script)
    {
	super(script);
    }

    @Override
    public void run()
    {
	System.out.println("Dest == " + this.resultLocation);
	Double result = this.operand1 + this.operand2;
	this.script.setVariable(this.resultLocation, result);
    }

    @Override
    public void parse(String[] args)
    {
	if (args.length < 3)
	    throw new MalformedCommandException("Syntax: <result location> <operand 1> <operand 2>");
	
	this.resultLocation = args[0];
	
	Number op1 = StringNumberTool.stringToNumber(args[1]);
	Number op2 = StringNumberTool.stringToNumber(args[2]);
	
	if (op1 == null || op2 == null)
	    throw new MalformedCommandException("Operand 1 and 2 must be number");
	
	this.operand1 = op1.doubleValue();
	this.operand2 = op2.doubleValue();
    }
    
}
