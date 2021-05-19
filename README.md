# AX-MinSCPT
A customizable script engine.

*Do you want to create your own assembly language ? This tool is made for you !*

No seriously, this tool is a utility made to create some custom script languages. You can create your own instruction set and create some generic scripts useful to perform some classic operations (like project creation, compilation, etc.).

The line formatting is generic (like assembly language) and you cannot modifying it. There is an example of a custom script with its own language :
```
echo Hello World !

set a 1
set b 2
add c $a $b
echo "Value of C is" $c
```
And the following script will print on screen :
```
Hello World !
Value of C is 3
```

Variables are represented with an name and when you need to call them, you prefix variable name with `$` (e.g: `$my_var`). Variables can be text or numbers.

Currently, this platform is in beta statement and maybe code interpretation can be wrong.

## Getting started

First of all, you need to clone this repository (`git clone` bla bla bla...). And then, you can begin to code your own language.

You will need to extends these two classes :

* `fr.drogonistudio.axminscpt.AbstractScript` : Your script engine
* `fr.drogonistudio.axminscpt.AbstractCommand` : A command

**But how does it work ?** I will describe these two classes !

### The command class.

A command class extend `fr.drogonistudio.axminscpt.AbstractCommand` and contains two majors methods :
* `public void run()` : This method is invoked when the command is fully compiled.
* `public void parse(String[])` : This method is invoke when a command is created and user pass arguments.

When you extend `AbstractCommand`, you must keep default constructor (`public AbstractCommand(AbstractScript)`).

During command creation, if something went wrong (like invalid arguments), you can throw an malformed exception (`fr.drogonistudio.axminscpt.exceptions.MalformedCommandException`).

Some commands are already implemented :
* `echo` - Print something on console
* `set` - Set an arbitrary value to a variable
* `skip` - Skip `x` lines
* `goto` - Jump to specified line
* `compare` - Compare 2 values and store the result to an specified variable
* `execnexton` - Compare 2 values and if these values mismatches, the next line will be skip
* `nop` - No operation (a stupid instruction is made to force CPU to perform calculation)

### The script class.

A script class extend `fr.drogonistudio.axminscpt.AbstractScript` and must implement the following method :

```java
public void init();
```

Here, you will register all of your commands to support with `protected void registerCommand(String, Command<? extends AbstractCommand>)`.

If you want to directly register already implemented commands quoted before, invoke `protected void registerDefaultCommands()`.

### An example of these implementation.

```java
public class MyAwesomeCommand extends AbstractCommand
{
    private String him;
    
    public MyAwesomeCommand(AbstractScript script)
    {
    	super(script);
    }
    
    @Override
    public void run()
    {
    	System.out.println(this.him + " is awesome !");
    }
    
    @Override
    public void parse(String args[])
    {
    	if (args.length < 1)
        	throw new MalformedCommandException("Synxtax: <awesome people>");
       	this.him = args[0];
    }
}

public class MyScript extends AbstractScript
{
    public AbstractScript(File scriptFile)
    {
    	super(scriptFile);
    }
    
    @Override
    public void init()
    {
    	this.registerDefaultCommands();
        this.registerCommand("is_awesome", MyAwesomeCommand.class);
    }
}

public class Runner
{
    public static void main(String args[])
    {
    	MyScript script = new MyScript(new File("my script.txt"));
        script.init();
        script.run();
    }
}
```
