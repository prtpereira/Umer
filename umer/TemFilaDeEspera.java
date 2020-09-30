
/**
 * Write a description of interface TemFilaDeEspera here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public interface TemFilaDeEspera
{
    public void adicionaNaFila( int hashcode );
    
    public boolean temAlguemNaFila();
    
    public int proximoNaFila();
    
    public int numViagensEmEspera(DataBase db);
}
