package fr.drogonistudio.axminscpt.exceptions;


public class MalformedCommandException extends RuntimeException
{
    private static final long serialVersionUID = 5083046825088398982L;
    
    public MalformedCommandException(String message)
    {
	super(message);
    }
}
