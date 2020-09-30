/**
 * Escreva a descrição da classe Veiculo aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
import java.util.stream.Collectors;
import java.time.LocalDate;

public abstract class Veiculo implements Serializable, TemPosicao,TemFilaDeEspera{
   
    private String matricula;
    private double precoKm;
    private double velocidade;
    private double kmsTotal;
    private int lugares;
    private int fiabilidade;
    private Ponto2D posicao;
    //historico
    private List<Integer> viagens;
    //viagens em fila de espera -> cada int representa um hashcode!
    private List<Integer> emEspera;
   
    public Veiculo( String matricula, double precoKm, double velocidade, double kmsTotal,
                int lugares, int fiabilidade, Ponto2D posicao){
        this.matricula = matricula;
        this.precoKm = precoKm;
        this.velocidade = velocidade;  
        this.kmsTotal = kmsTotal;
        this.lugares = lugares;
        this.fiabilidade = fiabilidade;
        this.posicao = posicao;
        this.viagens = new ArrayList<Integer>();
        this.emEspera = new ArrayList<Integer>();

    }
    public Veiculo()
    {
        this( "", 0.0, 0.0, 0.0, 0, 0, new Ponto2D());
    }
    public Veiculo(Veiculo v)
    {
       this(v.matricula,v.precoKm,v.velocidade,v.kmsTotal,v.lugares,
            v.fiabilidade,v.posicao);
    }

    /**
     * Metodos de instacia (sets,gets,clone,equals,toString)
     */
    
    
    //gets
    public String getMatricula()
    {
        return this.matricula;
    }
    public double getPrecoKm()
    {
        return this.precoKm;
    }
        public double getVelocidade()
    {
        return this.velocidade;
    }
    public double getKmsTotal(){
        return kmsTotal;
    }
    public int getLugares()
    {
        return this.lugares;
    }
    public int getFiabilidade()
    {
        return this.fiabilidade;
    }
    public Ponto2D getPosicao()
    {
        return this.posicao;
    }

    //sets
    public void setPrecoKm(double x)
    {
        this.precoKm = x;
    }    
    public void setVelocidade(double x)
    {
        this.velocidade = x;
    }
    public void setFiabilidade(int x)
    {
        this.fiabilidade = x;
    }
    public void setPosicao(Ponto2D x){
        this.posicao.move(x);
    }
  
 

    public abstract Veiculo clone();
    
    public String toString()
    {   
        StringBuilder sb = new StringBuilder();
        sb.append("Matricula: ");
        sb.append(this.matricula + "\n");
        sb.append("Preço por Km: ");
        sb.append(this.precoKm + " euros\n");
        sb.append("Velocidade: ");
        sb.append(this.velocidade + " Km/h\n");
        sb.append("Kilometros: ");
        sb.append( kmsTotal + " Km\n");
        sb.append("Número de lugares: ");
        sb.append(this.lugares + "\n");
        sb.append("Fiabilidade: ");
        sb.append(fiabilidade + "\n");
        sb.append("Localização: ");
        sb.append(this.posicao.toString() +"\n");
        
        return sb.toString();
    }

    public boolean equals(Object o){
       if( this == o) return true;
       if( o == null || o.getClass() != this.getClass()) return false;

       Veiculo v = (Veiculo) o;
       
       return this.matricula.equals(v.getMatricula()) && this.precoKm==v.getPrecoKm()
              && this.velocidade == v.getVelocidade() && this.lugares == v.getLugares() 
              && this.fiabilidade == v.getFiabilidade() && this.posicao.equals(v.getPosicao());
    }
    public int hashCode(){
        int hash = 7;
        hash = 37*hash + matricula.hashCode();
        return hash;
    }
    
    public List<Integer> getViagens(){ 
        return viagens.stream().collect(Collectors.toList());
    }    
    public void addViagem(int index, DataBase db)
    {
        Travel t = db.getViagem(index);
         
        this.kmsTotal += t.getDistCondutor();
        this.posicao = t.getFim();
        this.viagens.add(index);
    }

    public double lucroTotal(DataBase db){
        if (viagens.isEmpty())
            return 0;
        
        return viagens.stream().map( (Integer i) -> db.getViagem(i))
                               .mapToDouble(Travel::getPrecoReal)
                               .sum();
    }
    public int numDeViagens(){
        return viagens.size();
    }
    
    public List<String> listaViagens(DataBase db){
        List<String> lista = new ArrayList<String>();
        for( int i = 0; i < viagens.size(); i ++)
            lista.add(db.getViagem(i).resumo());
        return lista;
    }
    public List<String> listaViagens(DataBase db, LocalDate inicio, LocalDate fim){
        return viagens.stream().map(i -> db.getViagem(i))
                               .filter( t ->t.getData().isBefore(fim) && t.getData().isAfter(inicio))
                               .map(Travel::resumo)
                               .collect(Collectors.toList());
        
     }
    
    public int getViagem( int x){
        return viagens.get(x);
    }
    
    
    //_------fila-------
    
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
         return emEspera.size();
     }
}