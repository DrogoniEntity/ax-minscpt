package fr.drogonistudio.axminscpt.example;

import java.io.File;

import fr.drogonistudio.axminscpt.AbstractScript;

public class ScriptRunner
{

    public static void main(String args[]) throws Throwable
    {
	AbstractScript script = new ScriptExample(new File("hello.txt"));
	script.init();
	script.run();
    }
}
