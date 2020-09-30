import java.util.Random;


/**
 * Escreva a descrição da classe Travel aqui.
 * 
 * @author (seu nome) 
 * @version (número de versão ou data)
 */
import java.text.DecimalFormat;
import java.math.RoundingMode;
import java.io.Serializable;
import java.time.LocalDate;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.File;


public class Travel implements Serializable
{
    
    // variáveis de instância - substitua o exemplo abaixo pelo seu próprio
    private Ponto2D inicio;
    private Ponto2D fim;
    
    private LocalDate data;
    
    private String cliente;  //mail
    private String condutor; //mail
    private String carro;  //matricula
    
    private double precoEstimado;
    private double precoReal;
    
    private double tempoEstimado;
    private double tempoReal;
    
    private double distCondutor;
    private double distCliente;
    
    private int nota; //nota dada pelo cliente 0-100 || -1 significa que nao lhe foi atribuida nota
    
    /**
     * Construtor para objetos da classe Travel
     */
    public Travel()
   {
       this(new Ponto2D() ,new Ponto2D(), "", "", "",0.0 ,0.0, 0.0, 0.0, 0.0,0.0);
   }

    public Travel(Ponto2D inicio, Ponto2D fim, String cliente, String condutor,String carro    , 
                  double precoEstimado, double precoReal, double distanciaCondutor, double distanciaCliente,
                  double tempoE, double tempoR){
        this.inicio = inicio;
        this.fim = fim;
        
        //esta data vai ser quando o motorista fizer a viagem e nao a que ficou aqui definida
        this.data = LocalDate.now();
        
        this.cliente = cliente;
        this.condutor = condutor;
        this.carro = carro;
        
        this.precoEstimado = precoEstimado;
        this.precoReal = precoReal;
        
        this.distCondutor = distanciaCondutor;
        this.distCliente = distanciaCliente;
        
        this.tempoEstimado = tempoE;
        this.tempoReal = tempoR;
        
        this.nota = -1;
   }
   

   public Travel(Travel t){
        this.inicio = t.getInicio();
        this.fim = t.getFim();
        
        this.data = t.getData();
        
        this.cliente = t.getCliente();
        this.condutor = t.getCondutor();
        this.carro = t.getCarro();
        
        this.precoEstimado = t.getPrecoEstimado();
        this.precoReal = t.getPrecoReal();
        
        this.distCondutor = t.getDistCondutor();
        this.distCliente = t.getDistCliente();
        
        this.tempoEstimado = t.getTempoEstimado();
        this.tempoReal = t.getTempoReal();
        
        this.nota = t.getNota();
   }


    
   public Ponto2D getInicio(){
       return inicio.clone();
    }
   public Ponto2D getFim(){
       return fim.clone();
    }
   public LocalDate getData(){
       return data;
   }
   public String getCliente(){
        return this.cliente;
    }
   public String getCarro(){
        return this.carro;
   }
   public String getCondutor(){
        return this.condutor;
    }
   public double getPrecoEstimado(){
        return this.precoEstimado;
    }
   public double getPrecoReal(){
        return this.precoReal;
    }
   public double getDistCondutor(){
        return this.distCondutor;
   }
   public double getDistCliente(){
        return this.distCliente;
   }
   public double getTempoEstimado(){
        return this.tempoEstimado;
   }
   public double getTempoReal(){
        return this.tempoReal;
   }
   public int getNota(){
        return this.nota;
   }
    public void setData( LocalDate d){
        this.data = d;
    }
    public void setCliente( String c){
        cliente = c;
    }
    public void setCondutor( String d){
        condutor = d;
    }
    public void setCarro( String v){
        carro = v;
    }
    public void setPrecoReal( double p){
        precoReal = p;
    }
    public void setPrecoEstimado(double p){
        precoEstimado = p;
    }
    public void setDistCondutor( double d){
        distCondutor = d;
    }
    public void setDistCliente( double d){
        distCliente = d;
    }    
    public void setTempoEstimado ( double t){
        tempoEstimado = t;
    }
    public void setTempoReal( double t){
        tempoReal = t;
    }
    public void setNota( int n){
        nota = n;
    }
    
    public boolean equals(Object o)
    {
        if ( o == this) 
            return true;
        if ( o==null || o.getClass()!=this.getClass()) 
            return false;
            
        Travel t= (Travel) o;
        
        return inicio.equals(t.getInicio()) && fim.equals(t.getFim()) &&
               cliente.equals(t.getCliente()) && condutor.equals(t.getCondutor()) && carro.equals(t.getCarro()) &&
               precoEstimado==t.getPrecoEstimado() && precoReal==t.getPrecoReal() && 
               distCondutor==t.getDistCondutor() && distCliente==getDistCliente() && 
               tempoEstimado==t.getTempoEstimado() && tempoReal==t.getTempoReal() &&
               nota==t.getNota() && data.equals( t.getData());
      
    }
    public Travel clone(){
        return new Travel(this);
    }
    public String toString(){
        DecimalFormat df = new DecimalFormat("##.##");
        df.setRoundingMode(RoundingMode.DOWN);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        
        StringBuilder sb = new StringBuilder();
        sb.append("Viagem:\n");
        sb.append("Posição inicial: ");
        sb.append(inicio.toString()+"\n");
        sb.append("Posição final: ");
        sb.append(fim.toString()+"\n");
        sb.append("Data da viagem: ");
        sb.append(data.toString() + "\n");
        sb.append("Cliente: ");
        sb.append(cliente +"\n");
        sb.append("Condutor: ");
        sb.append(condutor  + "\n");
        sb.append("Veiculo: ");
        sb.append(carro + "\n");
        sb.append("Preço estimado/real: ");
        sb.append(df.format(precoEstimado) +"/" + df.format(precoReal) + " euros\n");
        sb.append("Tempo estimado/real: ");
        sb.append(df.format(tempoEstimado) + "/" + df.format(tempoReal) + " minutos\n");
        sb.append("Distancia percorrida pelo condutor/cliente: ");
        sb.append(df.format(distCondutor) +"/" + df.format(distCliente) + "kms\n");
        if(nota > 0){
            sb.append("Avalição do cliente: ");
            sb.append(nota  + "\n");
        }
        return sb.toString();
    }
    
   public String resumo(){
       StringBuilder sb = new StringBuilder();
       SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
       
       sb.append("De ");
       sb.append(inicio.toString());
       sb.append(" ate ");
       sb.append(fim.toString());
       sb.append(" no dia ");
       sb.append(data.toString());
       
       return sb.toString();
   }
  
    
   public static void fazViagem( Client c, Ponto2D x, DataBase db) throws CondutorMaisProximoException{
       Driver d = db.DriverMaisPerto(c.getPosicao());
       Veiculo v = db.getVeiculo( d.getVeiculo());
       fazViagem(c,d,v,x,db);
    }
   
   public static void fazViagem( Client c, Veiculo v, Ponto2D x, DataBase db) throws CarroNaoExistenteException{
       //procura um taxiste que possa seguir com o carro
       Driver d = db.getDriver(v);
       fazViagem(c,d,v,x,db); 
   }
    
   public static void fazViagem( Client c, Driver d, Ponto2D x, DataBase db){
       Veiculo v = db.getVeiculo( d.getVeiculo());
       fazViagem(c,d,v,x,db);
   }
        // procura um carro que este possa conduzir
   /**
    * Metodo para fazer uma viagem.
    * Este metodo altera os objetos intervenientes (cliente,condutor e carro) atraves
    * do metodo addViagem que as suas classes tem0
    * 
    * @param  cliente   Client que solicitou a viagem.
    * @param  condutor  Driver que vai efetuar a viagem.
    * @param  carro     Veiculo que vai ser usado para fazer a viagem.
    * @param   x        Ponto2D para onde o cliente deseja ir.
    */     
   public static void fazViagem( Client cliente, Driver condutor, Veiculo carro, Ponto2D x, DataBase db ){
        //inicializa
        double dist1 = cliente.getPosicao().distance(x);
        double dist2 = carro.getPosicao().distance( cliente.getPosicao()) + dist1;
        
        Travel viagem = new Travel(cliente.getPosicao(), x,
                                   cliente.getEmail(), condutor.getEmail(), carro.getMatricula(),
                                   0.0, 0.0,      // preço estimado/real
                                   dist2, dist1,  //distanciaCondutor /distaciaCliente
                                   0.0, 0.0);     //tempo Estimado/real

            //pede orçamento - 
        condutor.fazOrçamento(viagem,db);   
        
            //altera orçamento
        alteraViagemRandom(viagem, db);
        
            // faz addViagem nos 3 intrevenientes
        int index = db.add(viagem);
        
        //preenche a data exata que fez a viagem!!!
        viagem.setData( LocalDate.now() );
        
        cliente.addViagem( index, db );
        condutor.addViagem( index, db);
        carro.addViagem(index,db);
    }
    
    
    /**
     * Metodo que recebe uma viagem com o tempo e custo estimado e atraves de certos valores calcula 
     * o tempo e custo real.
     * 
     * @param t   Viagem que cai alterar.
     */  
    private static void alteraViagemRandom(Travel t, DataBase db){
        Driver condutor = (Driver) db.getUser( t.getCondutor() );
        Veiculo carro = db.getVeiculo( condutor.getVeiculo() );
        
       
        t.tempoReal = (t.tempoEstimado * geraPercRandom()) *0.75 +
                      (2 - condutor.getGrauCumprimento()/100) *0.10 +
                      (2 - carro.getFiabilidade()/100) *0.15;
        
        if ( t.tempoReal - t.tempoEstimado > 0.25 * t.tempoEstimado)
            t.precoReal = t.precoEstimado;
        else
            t.precoReal = (t.precoEstimado * t.tempoReal)/ t.tempoEstimado;
         
    }
    
    /**
     * @return um numero random entre 0.75 e 1.25.
     */
    private static double geraPercRandom(){
        double x;
        do{
            Random rn = new Random();
            x = rn.nextInt() % 51;
        }while( x <= 0);
        return x/100 + 0.75;
    }
    
   //----------- LISTA DE ESPERRA ----------------
   
   
   public static void addParaAFila(Client cliente, TemFilaDeEspera obj, Ponto2D destino,DataBase db){
       double dist1 = cliente.getPosicao().distance(destino);
       Travel viagem = new Travel(cliente.getPosicao(), destino,
                                   cliente.getEmail(), "", "",
                                   0.0, 0.0,      // preço estimado/real
                                   0,dist1,       //distanciaCondutor /distaciaCliente
                                   0.0, 0.0);
       //cliente.adicionaNaFila(viagem.hashCode());
       obj.adicionaNaFila( viagem.hashCode());
       db.addListaDeEspera(viagem);
    }
   public static void finalizaViagem(int hashcode, Driver condutor, Veiculo carro, DataBase db){
       Travel viagem = db.getERemoveViageEmEspera(hashcode);
       double dist2 = carro.getPosicao().distance( viagem.getInicio()) + viagem.getDistCliente();
       
       viagem.setCondutor( condutor.getEmail());
       viagem.setCarro(carro.getMatricula());
       viagem.setDistCondutor(dist2);
       Client cliente = (Client) db.getUser( viagem.getCliente());
       
            //pede orçamento 
        condutor.fazOrçamento(viagem,db);   
        
            //altera orçamento
        alteraViagemRandom(viagem, db);
        
            // faz addViagem nos 3 intrevenientes
        int index = db.add(viagem);
        
        //preenche a data exata que fez a viagem!!!
        viagem.setData( LocalDate.now() );
        
        cliente.addViagem( index, db );
        condutor.addViagem( index, db);
        carro.addViagem(index,db);
    }
   //dos obj escolhe qual deles tem a viagem mais antiga e retorna essa viagem
    /*
     public static int proximaViagem( TemFilaDeEspera a, TemFilaDeEspera b){
       int hashcode; 
       if( !a.temAlguemNaFila()){
            if(!b.temAlguemNaFila() )
                throw new ViagemNaoExistenteException();
            else
               hashcode = b.proximoNaFila();
        }
        else{
            if( !b.temAlguemNaFila())
                hashcode = a.proximoNaFila();
            else{
                int viagemA = a.proximoNaFila();
                int viagemB = b.proximoNaFila();
                if( emEspera.get(viagemA).getData().is
            }
    
        }
     */
}
   
  