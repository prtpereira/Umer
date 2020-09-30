
/**
 * Write a description of class Carrinha here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Carrinha extends Veiculo
{
    // instance variables 
    //nada
    private static final int NMaxPessoas = 8;
    private static final int fiabilidadeDefault = 40;
    public Carrinha(){
        super("",0,0,0,NMaxPessoas,0,new Ponto2D());
    }
    public Carrinha( String matricula, double precoKm, double velocidade, Ponto2D localizacao){
        super(matricula,precoKm,velocidade,0,NMaxPessoas,fiabilidadeDefault,localizacao);
    }
    public Carrinha( Carrinha l){
         super( (Veiculo) l) ;
    }
 
    
    public Carrinha clone(){
        return new Carrinha(this);
    }

}
