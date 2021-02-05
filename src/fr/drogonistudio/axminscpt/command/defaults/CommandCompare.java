package fr.drogonistudio.axminscpt.command.defaults;

import fr.drogonistudio.axminscpt.AbstractScript;
import fr.drogonistudio.axminscpt.command.AbstractCommand;
import fr.drogonistudio.axminscpt.exceptions.MalformedCommandException;
import fr.drogonistudio.axminscpt.utils.StringNumberTool;

public class CommandCompare extends AbstractCommand
{
    private String resultLocation;
    private Number operand1;
    private Number operand2;
    
    public CommandCompare(AbstractScript script)
    {
	super(script);
    }

    @Override
    public void run()
    {
	int cmpResult = Double.compare(Double.parseDouble(this.operand1.toString()), Double.parseDouble(this.operand2.toString()));
	// If equals: cmpResult = 0
	// Else if op1 < op2: cmpResult = -1
	// Else: cmpResult = 1
	
	this.script.setVariable(this.resultLocation, cmpResult);
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
	
	this.operand1 = op1;
	this.operand2 = op2;
    }
    
}
