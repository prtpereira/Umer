
/**
 * Write a description of class Driver here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.Collection;
import java.util.Iterator;
import java.text.DecimalFormat;
import java.math.RoundingMode;
import java.time.LocalDate;

public class Driver extends User implements TemFilaDeEspera
{
    private double grauCumprimento;
    private double classificaçao;
    private double kmsTotal;
    private boolean disponivel;
    private String carroPessoal;  
    //Lista de espera
    private List<Integer> emEspera;
    //construtor vazio
    public Driver(){
        this.grauCumprimento = 0;
        this.classificaçao = 0;
        this.kmsTotal = 0;
        this.disponivel = false;
        this.carroPessoal = "";
    }
    //construtor que passa todas as variaveis
    public Driver(String email,String nome, String password, String morada,LocalDate data,
                  boolean disponivel,String carro){
        super(email,nome,password,morada,data);
        this.grauCumprimento = 100;
        this.classificaçao = 0;
        this.kmsTotal = 0;
        this.disponivel = disponivel;
        this.carroPessoal = carro;
        this.emEspera = new ArrayList<Integer>();              
    }
    //construtor que recebe um driver
    public Driver( Driver d){
        super((User) d);
        this.grauCumprimento = d.getGrauCumprimento();
        this.classificaçao = d.getClassificaçao();
        this.kmsTotal = d.getKmsTotal();
        this.disponivel = d.getDisponivel();
        this.carroPessoal = d.getCarroPessoal();
        this.emEspera = d.getEmEspera();
    }
    //sets e gets
    public List<Integer> getEmEspera(){
        return this.emEspera.stream().collect(Collectors.toList());
    }
    public double getClassificaçao(){      
        return classificaçao;
    }
    public double getGrauCumprimento(){
        return grauCumprimento;
    }
    public double getKmsTotal(){
        return kmsTotal;
    }
    public String getCarroPessoal(){ return carroPessoal;}
    
    public boolean getDisponivel(){  return disponivel; }
  
    public void setClassificaçao(double classi){
        this.classificaçao = classi;
    }
    public void setGrauCumprimento(double cump){
        this.grauCumprimento = cump;
    }
    public void setKmsTotal(double x){
        this.kmsTotal = x;
    }
    public void setVeiculo(String a){
        this.carroPessoal = a;
    }
    public void setDisponivel(boolean x){
        this.disponivel = x;
    }
       
    //metodos Obrigatorios
    
    public boolean equals(Object o){
        if( o == this ) return true;
        if( o == null || o.getClass() != this.getClass()) return false;
        Driver d = (Driver) o;
        
        return super.equals(o) && grauCumprimento == d.getGrauCumprimento() && 
               classificaçao == d.getClassificaçao() &&kmsTotal == d.getKmsTotal() && 
               disponivel == d.getDisponivel() && carroPessoal.equals( d.getCarroPessoal() );
    }
   
    
    public Driver clone(){   
        return new Driver(this);
    }
    
   
    public String toString(){
        StringBuilder sb = new StringBuilder();
        DecimalFormat df = new DecimalFormat("##.##");
        df.setRoundingMode(RoundingMode.DOWN);
        
        sb.append( super.toString());
        
        sb.append("\nGrau de cumprimento: ");
        sb.append(df.format(this.grauCumprimento) + "\n");
        sb.append("Classificação: " );
        sb.append(df.format(this.classificaçao) + "\n");
        sb.append("Kms totais percorridos: ");
        sb.append(df.format(this.kmsTotal) + "\n");
        sb.append("Carro: ");
        sb.append(this.carroPessoal);
     
        return sb.toString();
    }
   
    //outros metodos
   
    
    public boolean estaDisponivel(){
        return this.disponivel;
    }
    
    public void fazOrçamento(Travel travel, DataBase db){
        double kms = travel.getDistCondutor();
        Veiculo veiculo = db.getVeiculo( this.getVeiculo() );
        
        travel.setTempoEstimado( (kms * 60)/veiculo.getVelocidade() );
        travel.setPrecoEstimado( kms * veiculo.getPrecoKm() );
    }
    public void addViagem( int index, DataBase db){
        super.addViagem(index);
        Travel t = db.getViagem(index);
        int n = numDeViagens();
        //atualiza grauDeCumprimento
        if( t.getTempoEstimado() >= t.getTempoReal())
            grauCumprimento = (( n*grauCumprimento) +1)/(n+1);
        else
            grauCumprimento = (grauCumprimento*n)/(n+1);
           
        kmsTotal += t.getDistCondutor();
    }
    
    public String getVeiculo(){
        return carroPessoal;
    }
      
    public double lucro( DataBase db){
        return getViagens().stream().map( (Integer i) -> db.getViagem(i))
                                    .mapToDouble(Travel::getPrecoReal)
                                    .sum();
    }
    
    public Set<Travel> getViagensComOsVeiculos( Set<String> matriculas, DataBase db){
        return getViagens().stream().map( (Integer i) -> db.getViagem(i))
                                    .filter( (Travel t) -> matriculas.contains(t.getCarro()))
                                    .collect( Collectors.toSet());
    }
    
    // faz uma viagem que esta na sua lista de espera ou na do seu veiculo!
    public void fazViagemEmEspera(DataBase db) throws ViagemNaoExistenteException{
        Veiculo carro = db.getVeiculo( this.getVeiculo());
        int hashcode;
        //ve qual a primeira viagem que tem que fazer
        if( !temAlguemNaFila()){
            if(!carro.temAlguemNaFila() )
                throw new ViagemNaoExistenteException();
            else
                hashcode = carro.proximoNaFila();
        }
        else
            hashcode = proximoNaFila();
        //finaliza
        Travel.finalizaViagem( hashcode, this, carro, db);
    }
    
    
        //--------------------FILAS DE ESPERA----------------------------
    
     public void adicionaNaFila( int hashcode ){
         emEspera.add(hashcode);
     }
     public boolean temAlguemNaFila(){
         return !emEspera.isEmpty();
        }
     public int proximoNaFila(){
        return emEspera.remove(0);
     }
     public int numViagensEmEspera(DataBase db){
         return emEspera.size() + db.getVeiculo(this.carroPessoal).numViagensEmEspera(db);
     }
}