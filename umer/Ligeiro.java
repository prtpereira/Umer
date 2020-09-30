
/**
 * Write a description of class Ligeiro here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Ligeiro extends Veiculo
{
    // instance variables 
    //nada
    private static final int NMaxPessoas = 4;
    private static final int fiabilidadeDefault = 60;
    public Ligeiro(){
        super("",0,0,0,NMaxPessoas,0,new Ponto2D());
    }
    public Ligeiro( String matricula, double precoKm ,double velocidade, Ponto2D localizacao){
        super(matricula,precoKm,velocidade,0,NMaxPessoas,fiabilidadeDefault,localizacao);
    }
    public Ligeiro( Ligeiro l){
        super( (Veiculo) l) ;
    }
    
    public Ligeiro clone(){
        return new Ligeiro( this);
    }
    
}
