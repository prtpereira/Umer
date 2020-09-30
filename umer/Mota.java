
/**
 * Write a description of class Moto here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Mota extends Veiculo
{
    // instance variables 
    //nada
    private static final int NMaxPessoas = 1;
    private static final int fiabilidadeDefault = 80;
    
    public Mota(){
        super("",0,0,0,NMaxPessoas,0,new Ponto2D());
    }
    public Mota( String matricula, double precoKm,double velocidade, Ponto2D localizacao){
        super(matricula,precoKm,velocidade,0,NMaxPessoas,fiabilidadeDefault,localizacao);
    }
    public Mota( Mota l){
        super( (Veiculo) l) ;
    }
    
    
    public Mota clone(){
        return new Mota( this);
    }
}
